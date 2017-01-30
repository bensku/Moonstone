package io.github.bensku;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.EventManager;
import org.spongepowered.api.scheduler.SpongeExecutorService;

import io.github.bensku.env.EnvGenerator;
import io.github.bensku.loader.ScriptLoader;
import net.sandius.rembulan.Variable;
import net.sandius.rembulan.exec.CallException;
import net.sandius.rembulan.exec.CallPausedException;
import net.sandius.rembulan.exec.DirectCallExecutor;
import net.sandius.rembulan.impl.StateContexts;
import net.sandius.rembulan.runtime.LuaFunction;

/**
 * Manages all file-based scripts. Usually singleton.
 *
 */
public class ScriptManager {
    
    private Moonstone moon;
    private EventManager eventManager;
    private SpongeExecutorService executor;
    
    private ScriptLoader scriptLoader;
    private EnvGenerator envGenerator;
    
    private Map<Path, LuaScript> scripts;
    
    public ScriptManager(Moonstone moon, EventManager eventManager, ScriptLoader scriptLoader, EnvGenerator envGenerator) {
        this.moon = moon;
        this.eventManager = eventManager;
        this.executor = Sponge.getScheduler().createSyncExecutor(moon);
        this.scriptLoader = scriptLoader;
        this.envGenerator = envGenerator;
        this.scripts = new ConcurrentHashMap<>();
    }
    
    /**
     * Tries to load a script at given path asynchronously. If this fails, error
     * is printed.
     * @param path Path to script.
     * @param name Name of script.
     */
    public void loadScript(Path path, String name) {
        LuaScript script = scripts.get(path);
        if (script == null) {
            script = createScript();
            scripts.put(path, script);
        }
        final LuaScript fScript = script;
        
        script.setEnv(envGenerator.create(script.getContext(), path));
        
        scriptLoader.loadScript(path, name, new Variable(script.getEnv()), new Callback<LuaFunction>() {
            
            @Override
            public void onException(Exception e) {
                // TODO add better logging later
                e.printStackTrace();
            }
            
            @Override
            public void onComplete(LuaFunction func) {
                fScript.setMain(func);
                executor.schedule(new Runnable() {
                    
                    @Override
                    public void run() {
                        try {
                            fScript.load();
                        } catch (CallException e) { // TODO better error reporting
                            e.printStackTrace();
                        } catch (CallPausedException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        
                    }
                }, 0, TimeUnit.SECONDS);
            }

        });
    }
    
    public void unload(Path path) {
        LuaScript script = scripts.get(path);
        if (script == null)
            return;
        script.unload();
        scripts.remove(path);
    }
    
    public void unloadAll() {
        for (LuaScript script : scripts.values()) {
            script.unload(); // Do unloading before...
        }
        scripts.clear(); // ... throwing everything to GC!
    }
    
    private LuaScript createScript() {
        return new LuaScript(moon, eventManager, DirectCallExecutor.newExecutor(), StateContexts.newDefaultInstance());
    }
}
