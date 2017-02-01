package io.github.bensku.call;

/**
 * Resolves Lua fields to Java method calls/field access.
 *
 */
public interface FieldResolver {
    
    /**
     * Gets a "field" from object.
     * @param obj Target object.
     * @param field Field name.
     * @return Data. May be null.
     */
    Object get(Object obj, String field) throws NotResolvableException;
    
    /**
     * Sets a "field" to object.
     * @param obj Target object.
     * @param field Field name.
     * @param value New value, may be null.
     */
    void set(Object obj, String field, Object value) throws NotResolvableException;
}
