package io.github.bensku;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.plugin.Plugin;

import io.github.bensku.env.SimpleEnvGenerator;
import io.github.bensku.loader.AsyncScriptLoader;
import net.sandius.rembulan.compiler.CompilerChunkLoader;

/**
 * Moonstone, Lua scripting for Sponge.
 *
 */
@Plugin(id = "moonstone", name = "Moonstone", version = "0.1-SNAPSHOT")
public class Moonstone {
    
    private ScriptManager scriptManager;
    
    @Listener
    public void onLoad(GameInitializationEvent event) {
        initScriptManager();
    }
    
    public void initScriptManager() {
        scriptManager = new ScriptManager(this, Sponge.getEventManager(), new AsyncScriptLoader(10, CompilerChunkLoader.of("moonstone")), new SimpleEnvGenerator());
    }
}
