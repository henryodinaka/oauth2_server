package market.henry.auth.services.redis;

import lombok.extern.slf4j.Slf4j;
import market.henry.auth.utils.JsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService{
    private static final String KEY = "oauth2:server";
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private RedisRepository redisRepository;

    public <T> boolean saveRecordToRedis(T obj, String key,long sessionTimeOut) {
        if (key == null){
            log.error("Form number is NULL");
            return false;
        }
        String json = JsonBuilder.generateJson(obj);

        redisRepository.add(json,key,sessionTimeOut);
        redisTemplate.expire(KEY, sessionTimeOut, TimeUnit.MINUTES);
        return true;
    }

    public <T> T  getRecordFromRedis(String key,Class<T> clazz) {
        Object obj = redisRepository.findObj(key);
        if (obj != null){
            System.out.println("the retrieved object "+obj);
            T fromJson = JsonBuilder.otherObj(obj.toString(),clazz);
            return fromJson;
        }
        return null;
    }


    public <T> void  deleteRecordFromRedis(Long userId){
        redisRepository.delete( String.valueOf(userId));

    }
}
