package io.github.bensku.call;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;


import net.sandius.rembulan.ByteString;

/**
 * Resolver which uses getters and setters. Uses caching.
 *
 */
public class BeanResolver implements FieldResolver {
    
    private Class<?> c;
    
    private MethodHandles.Lookup lookup;
    
    /**
     * Getter method handle.
     */
    private MethodHandle getter;
    
    /**
     * Setter method handle.
     */
    private MethodHandle setter;

    @Override
    public Object get(Object obj, String field) throws NotResolvableException {
        if (getter == null) {
            try {
                getter = getterMethod(field);
            } catch (IllegalAccessException e) {
                throw new NotResolvableException("Getter is not public");
            }
            if (getter == null){
                throw new NotResolvableException("No getter found");
            }
        }
        
        try {
            return getter.invoke(obj);
        } catch (Throwable e) {
            throw new NotResolvableException("Getter threw an exception");
        }
    }
    
    private MethodHandle getterMethod(String field) throws IllegalAccessException {
        String mName = "get" + field;
        Method[] methods = c.getDeclaredMethods();
        for (Method m : methods) {
            if (mName.equalsIgnoreCase(m.getName())) {
                return lookup.unreflect(m);
            }
        }
        
        return null;
    }

    @Override
    public void set(Object obj, String field, Object value)
            throws NotResolvableException {
        if (setter == null) {
            try {
                setter = setterMethod(field);
            } catch (IllegalAccessException e) {
                throw new NotResolvableException("Setter is not public");
            }
            if (setter == null){
                throw new NotResolvableException("No setter found");
            }
        }
        
        try {
            setter.invoke(obj, value);
        } catch (Throwable e) {
            throw new NotResolvableException("Setter threw an exception");
        }
    }
    
    private MethodHandle setterMethod(String field) throws IllegalAccessException {
        String mName = "set" + field;
        Method[] methods = c.getDeclaredMethods();
        for (Method m : methods) {
            if (mName.equalsIgnoreCase(m.getName())) {
                return lookup.unreflect(m);
            }
        }
        
        return null;
    }
    
}
