package io.github.bensku.env;

import java.nio.file.Path;

import net.sandius.rembulan.StateContext;
import net.sandius.rembulan.Table;

/**
 * Generates environments for Lua scripts.
 *
 */
public interface EnvGenerator {
    
    /**
     * Creates environment for a script.
     * @param context Script context.
     * @param path Path of the script.
     * @return Environment table (upvalue).
     */
    Table create(StateContext context, Path path);
}
