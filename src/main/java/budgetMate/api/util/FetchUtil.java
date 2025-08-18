package budgetMate.api.util;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FetchUtil {
    /**
     * <h2>Fetch resource</h2>
     * @param object {Optional<T>}
     * @param resourceName {resourceName}
     * @return {T}
     * <p>Wrapper for repository methods that return resource optionally.</p>
     */
    public <T> T fetchResource(Optional<T> object, String resourceName){
        return object
                .orElseThrow(() -> new IllegalStateException(String.format("%s not found!", resourceName)));
    }
}
