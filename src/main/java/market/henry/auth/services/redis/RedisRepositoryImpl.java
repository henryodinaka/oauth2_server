package market.henry.auth.services.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Repository
@Slf4j
public class RedisRepositoryImpl implements RedisRepository<String> {
    private static final String store = "oauth2:server";

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private HashOperations hashOperations;


//    @Autowired
//    public RedisRepositoryImpl(RedisTemplate<String, Object> redisTemplate){
//        this.redisTemplate = redisTemplate;
//    }

    @PostConstruct
    private void init(){
        hashOperations = redisTemplate.opsForHash();
    }

    public void add(final String token,Object key,long sessionTimeOut) {
        hashOperations.put(store+key,key, token);
        redisTemplate.expire(store+key, sessionTimeOut, TimeUnit.MINUTES);
    }

    public void delete(Object key) {
        hashOperations.delete(store+key,key);
    }

    public void delete(final String sessionId) {
        hashOperations.delete(store +sessionId);
    }

    public String findObj( Object key){
        return (String) hashOperations.get(store+key,key);
    }
    
    public Map<String, String> findAll(String sessionId){

        return hashOperations.entries(store +sessionId);
    }

}
