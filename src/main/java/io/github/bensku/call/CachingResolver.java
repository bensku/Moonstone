package io.github.bensku.call;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CachingResolver implements FieldResolver {
    
    private Map<String, FieldResolver> resolvers;
    private List<Class<? extends FieldResolver>> resolverOrder;
    
    public CachingResolver(List<Class<? extends FieldResolver>> order) {
        this.resolvers = new HashMap<>();
        this.resolverOrder = order;
    }
    
    @Override
    public Object get(Object obj, String field) throws NotResolvableException {
        FieldResolver resolver = resolvers.get(field);
        if (resolver == null) { // Get the resolver...
            for (Class<? extends FieldResolver> c: resolverOrder) {
                FieldResolver r;
                try {
                    r = c.newInstance();
                } catch (InstantiationException | IllegalAccessException e1) {
                    e1.printStackTrace();
                    continue; // Just go on
                }
                try {
                    r.get(obj, field);
                    resolver = r; // If we got here, no exception was thrown
                    break;
                } catch (NotResolvableException e) { // This resolver didn't work...
                    continue;
                }
            }
            if (resolver == null) { // Still null? Allright, start providing nils to Lua
                resolver = new NullResolver();
            }
            resolvers.put(field, resolver);
        }
        
        return resolver.get(obj, field);
    }

    @Override
    public void set(Object obj, String field, Object value) throws NotResolvableException {
        FieldResolver resolver = resolvers.get(field);
        if (resolver == null) { // Get the resolver...
            for (Class<? extends FieldResolver> c: resolverOrder) {
                FieldResolver r;
                try {
                    r = c.newInstance();
                } catch (InstantiationException | IllegalAccessException e1) {
                    e1.printStackTrace();
                    continue; // Just go on
                }
                try {
                    r.set(obj, field, value);
                    resolver = r; // If we got here, no exception was thrown
                    break;
                } catch (NotResolvableException e) { // This resolver didn't work...
                    continue;
                }
            }
            if (resolver == null) { // Still null? Allright, start providing nils to Lua
                throw new NotResolvableException("Cannot set a value where only null is acceptable!");
            }
            resolvers.put(field, resolver);
            return;
        }
        
        resolver.set(obj, field, value);
    }

}
