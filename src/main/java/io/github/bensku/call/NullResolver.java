package io.github.bensku.call;

public class NullResolver implements FieldResolver {
    
    public NullResolver() {
    }
    
    @Override
    public Object get(Object obj, String field) throws NotResolvableException {
        return null;
    }

    @Override
    public void set(Object obj, String field, Object value)
            throws NotResolvableException {
        throw new UnsupportedOperationException("Cannot set to null field.");
    }

}
