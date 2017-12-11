package io.github.bensku.call;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import io.github.bensku.userdata.JavaMethod;

/**
 * Resolves methods from Java objects.
 *
 */
public class MethodResolver implements FieldResolver {
    
    private Class<?> c;
    private MethodHandles.Lookup lookup;
    private JavaMethod method;
    
    public MethodResolver(Class<?> c, MethodHandles.Lookup lookup) {
        this.c = c;
        this.lookup = lookup;
    }

    @Override
    public Object get(Object obj, String field) throws NotResolvableException {
        // First argument is useless for us
        if (method == null) {
            Method m = findMethod(field);
            if (m == null) {
                throw new NotResolvableException("method does not exist");
            }
            
            try {
                method = new JavaMethod(lookup.unreflect(m));
            } catch (IllegalAccessException e) {
                throw new NotResolvableException("illegal method access");
            }
        }
        
        return method;
    }
    
    private Method findMethod(String name) {
        for (Method m : c.getMethods()) {
            if (m.getName().equals(name)) {
                return m;
            }
        }
        
        return null;
    }

    @Override
    public void set(Object obj, String field, Object value) throws NotResolvableException {
        throw new NotResolvableException("cannot override Java method");
    }

}
