package io.github.bensku.env;

import java.nio.file.Path;

import net.sandius.rembulan.StateContext;
import net.sandius.rembulan.Table;
import net.sandius.rembulan.env.RuntimeEnvironments;
import net.sandius.rembulan.lib.StandardLibrary;

/**
 * Just gives the scripts everything we can give.
 *
 */
public class SimpleEnvGenerator implements EnvGenerator {
    
    public SimpleEnvGenerator() {
        
    }
    
    @Override
    public Table create(StateContext context, Path path) {
        Table env = StandardLibrary.in(RuntimeEnvironments.system()).installInto(context);
        
        return env;
    }
}
