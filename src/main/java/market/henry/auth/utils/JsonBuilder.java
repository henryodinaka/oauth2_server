package market.henry.auth.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class JsonBuilder {
    public JsonBuilder() {
    }

    public static String generateJson(Object object) {

        ObjectMapper mapper = new ObjectMapper();
        String s = null;
        try {
            s = mapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error("Error occured while generating from Json ",e);
            return null;
        }
        return s;
    }

    public static<T> T otherObj(String jsonObj,Class<T> clazz){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(jsonObj, clazz);
        } catch (IOException e) {
            log.error("Error occurred while casting json to object",e);
            return null;
        }

    }
}