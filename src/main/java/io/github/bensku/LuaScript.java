package io.github.bensku;

import java.util.Objects;

import org.spongepowered.api.event.EventManager;

import io.github.bensku.listener.EventListeners;
import net.sandius.rembulan.StateContext;
import net.sandius.rembulan.Table;
import net.sandius.rembulan.exec.CallException;
import net.sandius.rembulan.exec.CallPausedException;
import net.sandius.rembulan.exec.DirectCallExecutor;
import net.sandius.rembulan.runtime.LuaFunction;

public class LuaScript {
    
    private Moonstone moon;
    private EventListeners eventListeners;
    
    private DirectCallExecutor callExecutor;
    private StateContext stateContext;
    
    private Table env;
    private LuaFunction main;
    
    public LuaScript(Moonstone moon, EventManager eventManager, DirectCallExecutor callExecutor, StateContext stateContext) {
        this.moon = moon;
        this.eventListeners = new EventListeners(moon, eventManager);
        this.callExecutor = callExecutor;
        this.stateContext = stateContext;
    }
    
    public StateContext getContext() {
        return stateContext;
    }

    public Table getEnv() {
        return env;
    }

    public void setEnv(Table env) {
        this.env = env;
    }

    public LuaFunction getMain() {
        return main;
    }

    public void setMain(LuaFunction main) {
        this.main = main;
    }
    
    /**
     * (Re)loads this lua script. First, all event listeners are unregistered.
     * Then, main function is ran (and new ones are registered).
     * @throws InterruptedException If loading is interrupted.
     * @throws CallPausedException If script was paused, which shouldn't ever happen.
     * @throws CallException If something is wrong with function.
     */
    public void load() throws CallException, CallPausedException, InterruptedException {
        // Check for few possible null values
        Objects.requireNonNull(env, "Lua script cannot run without environment!");
        Objects.requireNonNull(main, "Lua script main function must be set before loading!");
        
        eventListeners.unregisterAll();
        callExecutor.call(stateContext, main);
    }
    
    public void unload() {
        eventListeners.unregisterAll();
    }
}
