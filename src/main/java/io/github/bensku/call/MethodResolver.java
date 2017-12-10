package io.github.bensku.call;

import java.util.Map;

import io.github.bensku.userdata.JavaMethod;

/**
 * Resolves methods from Java objects.
 *
 */
public class MethodResolver implements FieldResolver {
    
    private Map<String,JavaMethod> methods;

    @Override
    public Object get(Object obj, String field) throws NotResolvableException {
        return null;
    }

    @Override
    public void set(Object obj, String field, Object value) throws NotResolvableException {
        throw new NotResolvableException("cannot override Java method");
    }

}
