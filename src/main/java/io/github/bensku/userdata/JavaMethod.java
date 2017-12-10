package io.github.bensku.userdata;

import net.sandius.rembulan.Metatables;
import net.sandius.rembulan.Table;
import net.sandius.rembulan.Userdata;
import net.sandius.rembulan.impl.ImmutableTable;
import net.sandius.rembulan.impl.NonsuspendableFunctionException;
import net.sandius.rembulan.runtime.AbstractFunctionAnyArg;
import net.sandius.rembulan.runtime.ExecutionContext;
import net.sandius.rembulan.runtime.ResolvedControlThrowable;

public class JavaMethod extends Userdata {
    
    private Table metatable = new ImmutableTable.Builder()
            .add(Metatables.MT_CALL, new Call())
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
    
    public class Call extends AbstractFunctionAnyArg {

        @Override
        public void resume(ExecutionContext context, Object suspendedState) throws ResolvedControlThrowable {
            throw new NonsuspendableFunctionException();
        }

        @Override
        public void invoke(ExecutionContext context, Object[] args) throws ResolvedControlThrowable {
            Object funcName = args[0];
        }
        
    }
}
