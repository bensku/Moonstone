package io.github.bensku.loader;

import java.nio.file.Path;

import io.github.bensku.Callback;
import net.sandius.rembulan.Variable;
import net.sandius.rembulan.runtime.LuaFunction;

/**
 * Loads Lua scripts from files or from strings. May work asynchronously.
 *
 */
public interface ScriptLoader {
    
    /**
     * Loads a Lua script directly from String.
     * @param code Lua code.
     * @param name Name of code block provided.
     * @param env Environment (upvalue).
     * @param callback Callback for completion.
     * @return Future for function.
     */
    void loadScript(String code, String name, Variable env, Callback<LuaFunction> callback);
    
    /**
     * Loads a Lua script directly from a file.
     * The file should be text file, encoded in UTF-8.
     * @param path Path to script.
     * @param name Name of script.
     * @param env Environment (upvalue).
     * @param callback Callback for completion.
     * @return Future for function.
     */
    void loadScript(Path path, String name, Variable env, Callback<LuaFunction> callback);
    
}
