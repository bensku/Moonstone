package io.github.bensku.listener;

import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.EventListener;

import net.sandius.rembulan.StateContext;
import net.sandius.rembulan.exec.DirectCallExecutor;
import net.sandius.rembulan.runtime.LuaFunction;

/**
 * Listens to single event, then redirects it to given Lua function.
 *
 */
public class LuaEventListener implements EventListener<Event> {
    
    private LuaFunction handler;
    private StateContext state;
    
    public LuaEventListener(StateContext state, LuaFunction handler) {
        this.handler = handler;
        this.state = state;
    }
    
    @Override
    public void handle(Event event) throws Exception {
        // Just redirect the event to Lua
        DirectCallExecutor.newExecutor().call(state, handler, event);
    }

}
