package io.github.bensku.userdata;

import net.sandius.rembulan.Metatables;
import net.sandius.rembulan.Table;
import net.sandius.rembulan.Userdata;
import net.sandius.rembulan.impl.ImmutableTable;
import net.sandius.rembulan.impl.NonsuspendableFunctionException;
import net.sandius.rembulan.runtime.AbstractFunctionAnyArg;
import net.sandius.rembulan.runtime.ExecutionContext;
import net.sandius.rembulan.runtime.LuaFunction;
import net.sandius.rembulan.runtime.ResolvedControlThrowable;

/**
 * Redirects Lua __index and and __call to Java object.
 *
 */
public class JavaUserdata extends Userdata {
    
    private Table metatable = new ImmutableTable.Builder()
            .add(Metatables.MT_INDEX, new Index())
            .add(Metatables.MT_NEWINDEX, new NewIndex())
            .build();

    @Override
    public Object getUserValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object setUserValue(Object value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Table getMetatable() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Table setMetatable(Table mt) {
        // TODO Auto-generated method stub
        return null;
    }
    
    public static class Index extends AbstractFunctionAnyArg {

        @Override
        public void resume(ExecutionContext context, Object suspendedState)
                throws ResolvedControlThrowable {
            throw new NonsuspendableFunctionException();
        }

        @Override
        public void invoke(ExecutionContext context, Object[] args)
                throws ResolvedControlThrowable {
            // TODO Auto-generated method stub
            
        }
        
    }
    
    public static class NewIndex extends AbstractFunctionAnyArg {

        @Override
        public void resume(ExecutionContext context, Object suspendedState)
                throws ResolvedControlThrowable {
            throw new NonsuspendableFunctionException();
        }

        @Override
        public void invoke(ExecutionContext context, Object[] args)
                throws ResolvedControlThrowable {
            // TODO Auto-generated method stub
            
        }
        
    }

}
