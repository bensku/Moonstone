package io.github.bensku.listener;

import java.util.HashSet;
import java.util.Set;

import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.EventManager;

import io.github.bensku.Moonstone;

/**
 * Represents event listeners for a single script.
 * Remember to call {@link #unregisterAll()} before discarding!
 * 
 */
public class EventListeners {
    
    private Moonstone moon;
    private EventManager eventManager;
    private Set<LuaEventListener> listeners;
    
    /**
     * Initializes event listeners tracking.
     * @param moon The plugin.
     * @param eventManager Sponge event manager.
     */
    public EventListeners(Moonstone moon, EventManager eventManager) {
        this.moon = moon;
        this.eventManager = eventManager;
        this.listeners = new HashSet<>();
    }
    
    /**
     * Registers single Lua event listener.
     * @param event Event to listen.
     * @param listener Listener instance.
     */
    public void registerListener(Class<Event> event, LuaEventListener listener) {
        eventManager.registerListener(moon, event, listener);
        listeners.add(listener);
    }
    
    /**
     * Unregisters all event listeners from this object.
     */
    public void unregisterAll() {
        for (LuaEventListener listener : listeners) {
            eventManager.unregisterListeners(listener);
        }
    }
}
