package io.github.bensku.userdata;

import java.lang.invoke.MethodHandle;

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
    
    /**
     * Used to call the method.
     */
    private MethodHandle handle;
    
    public JavaMethod(MethodHandle handle) {
        this.handle = handle;
    }
    
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
            Object result;
            try {
                // If it is instance method, first argument must be instance
                // Conveniently, this is exactly how Lua treats instance methods internally
                result = handle.invoke(args);
            } catch (Throwable e) {
                throw new RuntimeException(e); // TODO better error handling
            }
            context.getReturnBuffer().setTo(result); // Java methods can only return single value
        }
        
    }
}
