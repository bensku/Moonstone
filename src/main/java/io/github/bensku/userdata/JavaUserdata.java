package io.github.bensku.userdata;

import java.util.List;
import java.util.Map;

import io.github.bensku.call.CachingResolver;
import io.github.bensku.call.FieldResolver;
import io.github.bensku.call.NotResolvableException;
import net.sandius.rembulan.ByteString;
import net.sandius.rembulan.LuaRuntimeException;
import net.sandius.rembulan.Metatables;
import net.sandius.rembulan.Table;
import net.sandius.rembulan.impl.ImmutableTable;
import net.sandius.rembulan.impl.NonsuspendableFunctionException;
import net.sandius.rembulan.runtime.AbstractFunction2;
import net.sandius.rembulan.runtime.AbstractFunction3;
import net.sandius.rembulan.runtime.ExecutionContext;
import net.sandius.rembulan.runtime.ResolvedControlThrowable;

/**
 * Implements Lua __index and and __newindex for Java objects as field and
 * method access.
 *
 */
public class JavaUserdata {
    
    /**
     * Metatable to apply for userdata.
     */
    private Table metatable = new ImmutableTable.Builder()
            .add(Metatables.MT_INDEX, new Index())
            .add(Metatables.MT_NEWINDEX, new NewIndex())
            .build();
    
    /**
     * Field resolvers for all classes that need them.
     */
    private Map<Class<?>,FieldResolver> fieldResolvers;
    private List<Class<? extends FieldResolver>> resolverOrder;
    
    public JavaUserdata(List<Class<? extends FieldResolver>> order) {
        this.resolverOrder = order;
    }
    
    public Table getMetatable() {
        return metatable;
    }
    
    private FieldResolver getResolver(Class<?> c) {
        FieldResolver resolver = fieldResolvers.get(c);
        if (resolver == null) {
            resolver = new CachingResolver(resolverOrder);
            fieldResolvers.put(c, resolver);
        }
        return resolver;
    }
    
    /**
     * Transforms a Lua object to closest similar one in Java, if needed
     * and possible. May return the object that was given as parameter.
     * @param obj Object from Lua.
     * @return Object to use with Java code.
     */
    protected Object luaToJava(Object obj) {
        if (obj instanceof ByteString) { // Passing ByteString to Java method would be... unwise
            obj = ((ByteString) obj).toRawString();
        }
        return obj;
    }
    
    public class Index extends AbstractFunction2 {

        @Override
        public void resume(ExecutionContext context, Object suspendedState) throws ResolvedControlThrowable {
            throw new NonsuspendableFunctionException();
        }

        @Override
        public void invoke(ExecutionContext context, Object obj, Object field) throws ResolvedControlThrowable {
            field = luaToJava(field);
            
            if (!(field instanceof String)) {
                throw new LuaRuntimeException("Java fields must have string names");
            }
            
            try {
                getResolver(obj.getClass()).get(obj, (String) field);
            } catch (NotResolvableException e) {
                throw new LuaRuntimeException(e);
            }
        }
        
    }
    
    public class NewIndex extends AbstractFunction3 {

        @Override
        public void resume(ExecutionContext context, Object suspendedState) throws ResolvedControlThrowable {
            throw new NonsuspendableFunctionException();
        }

        @Override
        public void invoke(ExecutionContext context, Object obj, Object field, Object value)
                throws ResolvedControlThrowable {
            field = luaToJava(field);
            value = luaToJava(value);
            
            if (!(field instanceof String)) {
                throw new LuaRuntimeException("Java fields must have string names");
            }
            try {
                getResolver(obj.getClass()).set(obj, (String) field, value);
            } catch (NotResolvableException e) {
                throw new LuaRuntimeException(e);
            }
        }
        
    }

}
