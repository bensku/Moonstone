package io.github.bensku.userdata;

import net.sandius.rembulan.Metatables;
import net.sandius.rembulan.Table;
import net.sandius.rembulan.Userdata;
import net.sandius.rembulan.impl.ImmutableTable;
import net.sandius.rembulan.impl.NonsuspendableFunctionException;
import net.sandius.rembulan.runtime.AbstractFunction2;
import net.sandius.rembulan.runtime.AbstractFunction3;
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
        throw new UnsupportedOperationException();
    }

    @Override
    public Object setUserValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Table getMetatable() {
        return metatable;
    }

    @Override
    public Table setMetatable(Table mt) {
        throw new UnsupportedOperationException();
    }
    
    public static class Index extends AbstractFunction2 {

        @Override
        public void resume(ExecutionContext context, Object suspendedState) throws ResolvedControlThrowable {
            throw new NonsuspendableFunctionException();
        }

        @Override
        public void invoke(ExecutionContext context, Object obj, Object field) throws ResolvedControlThrowable {
            // TODO Auto-generated method stub
            
        }
        
    }
    
    public static class NewIndex extends AbstractFunction3 {

        @Override
        public void resume(ExecutionContext context, Object suspendedState) throws ResolvedControlThrowable {
            throw new NonsuspendableFunctionException();
        }

        @Override
        public void invoke(ExecutionContext context, Object obj, Object field, Object value)
                throws ResolvedControlThrowable {
            // TODO Auto-generated method stub
            
        }
        
    }

}
