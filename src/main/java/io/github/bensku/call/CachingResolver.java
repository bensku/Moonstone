package io.github.bensku.call;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CachingResolver implements FieldResolver {
    
    private Map<String, FieldResolver> resolvers;
    private List<FieldResolver> resolverOrder;
    
    public CachingResolver() {
        resolvers = new HashMap<>();
    }
    
    @Override
    public Object get(Object obj, String field) {
        FieldResolver resolver = resolvers.get(field);
        Object resolved = null;
        if (resolver == null) { // Get the resolver...
            for (FieldResolver r : resolverOrder) {
                try {
                    resolved = r.get(obj, field);
                    resolver = r; // If we got here, no exception was thrown
                } catch (NotResolvableException e) { // This resolver didn't work...
                    continue;
                }
            }
            if (resolver == null) { // Still null? Allright, start providing nils to Lua
                resolver = new NullResolver();
            }
        }
        
        return null;
    }

    @Override
    public void set(Object obj, String field, Object value) {
        // TODO Auto-generated method stub
        
    }

}
