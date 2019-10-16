package market.henry.auth.services.redis;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public interface RedisRepository<T> {

    /**
     * Return all object saved in redis
     */
    Map<T, T> findAll(String sessionId);

    /**
     * Add key-value pair to Redis.
     */
    void add(T obj, Object otherKey, long sessionTimeOut);

    /**
     * Delete a key-value pair in Redis.
     */
    void delete(Object otherKey);
    /**
     * Delete a key-value pair in Redis.
     */
    void delete(String sessionId);

    /**
     * find an object
     */
    T findObj(Object otherKey);

}
