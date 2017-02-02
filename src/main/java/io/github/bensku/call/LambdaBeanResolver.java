package io.github.bensku.call;

import java.lang.reflect.Method;

import com.hervian.lambda.Lambda;
import com.hervian.lambda.LambdaFactory;

import net.sandius.rembulan.ByteString;

/**
 * Resolver which uses getters and setters. Uses caching.
 *
 */
public class LambdaBeanResolver implements FieldResolver {
    
    /**
     * Getter method lambda.
     */
    private Lambda getter;
    
    /**
     * Setter method lambda.
     */
    private Lambda setter;
    
    /**
     * Class for primitive type or null if object.
     */
    private Class<?> primitiveType;

    @Override
    public Object get(Object obj, String field) throws NotResolvableException {
        if (getter == null) {
            Method method = getterMethod(obj, field);
            if (method == null){
                throw new NotResolvableException("No getter found!");
            }
            
            Class<?> returnType = method.getReturnType();
            primitiveType = !returnType.isPrimitive() ? null : returnType;
            
            try {
                getter = LambdaFactory.create(method);
            } catch (Throwable e) {
                throw new NotResolvableException("Could not initialize getter accessor.");
            }
        }
        
        if (primitiveType.equals(boolean.class)) {
            return getter.invoke_for_boolean();
        } else if (primitiveType.equals(char.class)) {
            throw new UnsupportedOperationException("char getter is not yet supported");
            //return getter.invoke_for_char();
        } else if (primitiveType.equals(byte.class)) {
            return getter.invoke_for_byte();
        } else if (primitiveType.equals(short.class)) {
            return getter.invoke_for_short();
        } else if (primitiveType.equals(int.class)) {
            return getter.invoke_for_int();
        } else if (primitiveType.equals(float.class)) {
            return getter.invoke_for_float();
        } else if (primitiveType.equals(long.class)) {
            return getter.invoke_for_long();
        } else if (primitiveType.equals(double.class)) {
            return getter.invoke_for_double();
        } else {
            return getter.invoke_for_Object();
        }
    }
    
    private Method getterMethod(Object obj, String field) {
        String mName = "get" + field;
        Class<?> c = obj.getClass();
        Method[] methods = c.getDeclaredMethods();
        for (Method m : methods) {
            if (mName.equalsIgnoreCase(m.getName())) {
                return m;
            }
        }
        
        return null;
    }

    @Override
    public void set(Object obj, String field, Object value)
            throws NotResolvableException {
        if (setter == null) {
            Method method = setterMethod(obj, field);
            if (method == null){
                throw new NotResolvableException("No setter found!");
            }
            
            Class<?> returnType = method.getParameterTypes()[0];
            primitiveType = !returnType.isPrimitive() ? null : returnType;
            
            try {
                setter = LambdaFactory.create(method);
            } catch (Throwable e) {
                throw new NotResolvableException("Could not initialize lambda accessor.");
            }
        }
        
        if (primitiveType.equals(boolean.class)) {
            setter.invoke_for_boolean(((Boolean) value).booleanValue());
        } else if (primitiveType.equals(char.class)) {
            throw new UnsupportedOperationException("char is not yet supported");
            //setter.invoke_for_char();s
        } else if (primitiveType.equals(byte.class)) {
            setter.invoke_for_byte(((Number) value).byteValue());
        } else if (primitiveType.equals(short.class)) {
            setter.invoke_for_short(((Number) value).shortValue());
        } else if (primitiveType.equals(int.class)) {
            setter.invoke_for_int(((Number) value).intValue());
        } else if (primitiveType.equals(float.class)) {
            setter.invoke_for_float(((Number) value).floatValue());
        } else if (primitiveType.equals(long.class)) {
            setter.invoke_for_long(((Number) value).longValue());
        } else if (primitiveType.equals(double.class)) {
            setter.invoke_for_double(((Number) value).doubleValue());
        } else {
            Object str = value instanceof ByteString ? ((ByteString) value).toRawString() : value;
            setter.invoke_for_Object(str);
        }
    }
    
    private Method setterMethod(Object obj, String field) { // TODO handle overloading on Java side...
        String mName = "set" + field;
        Class<?> c = obj.getClass();
        Method[] methods = c.getDeclaredMethods();
        for (Method m : methods) {
            if (mName.equalsIgnoreCase(m.getName())) {
                return m;
            }
        }
        
        return null;
    }
    
}
