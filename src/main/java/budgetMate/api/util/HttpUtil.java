package budgetMate.api.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HttpUtil {
    /**
     * <h2>Public</h2>
     * @param list {List<Object>}
     * @return {ResponseEntity<List<Object>>}
     * <p>Handle get.</p>
     */
    public <T> ResponseEntity<List<T>> handleGet(List<T> list){
        return list == null
                ? ResponseEntity.noContent().headers(this.buildBaseHeaders()).build()
                : ResponseEntity.ok().headers(this.buildBaseHeaders()).body(list);
    }

    /**
     * <h2>Public</h2>
     * @param object {Object}
     * @return {ResponseEntity<Object>}
     * <p>Handle get.</p>
     */
    public <T> ResponseEntity<T> handleGet(T object){
        return object == null
                ? ResponseEntity.noContent().headers(this.buildBaseHeaders()).build()
                : ResponseEntity.ok().headers(this.buildBaseHeaders()).body(object);
    }

    /**
     * <h2>Public</h2>
     * @param object {Object}
     * @return {ResponseEntity<Object>}
     * <p>Handle get.</p>
     */
    public <T> ResponseEntity<T> handleGet(T object, HttpHeaders customHeaders){
        return object == null
                ? ResponseEntity.noContent().headers(this.buildHeaders(customHeaders)).build()
                : ResponseEntity.ok().headers(this.buildHeaders(customHeaders)).body(object);
    }

    /**
     * <h2>Public</h2>
     * @param object {Object}
     * @return {ResponseEntity<Object>}
     * <p>Handle add.</p>
     */
    public <T> ResponseEntity<T> handleAdd(T object) {
        return object == null
                ? ResponseEntity.noContent().headers(this.buildBaseHeaders()).build()
                : ResponseEntity.status(HttpStatus.CREATED).headers(this.buildBaseHeaders()).body(object);
    }

    /**
     * <h2>Public</h2>
     * @param object {Object}
     * @return {ResponseEntity<Object>}
     * <p>Handle update.</p>
     */
    public <T> ResponseEntity<T> handleUpdate(T object) {
        return object == null
                ? ResponseEntity.noContent().headers(this.buildBaseHeaders()).build()
                : ResponseEntity.ok().headers(this.buildBaseHeaders()).body(object);
    }

    /**
     * <h2>Public</h2>
     * @param object {Object}
     * @return {ResponseEntity<Object>}
     * <p>Handle delete.</p>
     */
    public ResponseEntity<Void> handleDelete(Object object){
        return object == null
                ? ResponseEntity.notFound().headers(this.buildBaseHeaders()).build()
                : ResponseEntity.noContent().headers(this.buildBaseHeaders()).build();
    }

    /**
     * <h2>Private</h2>
     * @return {HttpHeaders}
     * <p>Build headers.</p>
     */
    private HttpHeaders buildHeaders(HttpHeaders customHeaders){
        final HttpHeaders headers = this.buildBaseHeaders();
        headers.addAll(customHeaders);

        return headers;
    }

    /**
     * <h2>Private</h2>
     * @return {HttpHeaders}
     * <p>Build base headers.</p>
     */
    private HttpHeaders buildBaseHeaders(){
        final HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        return headers;
    }
}
