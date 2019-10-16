package market.henry.auth.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import market.henry.auth.enums.ResponseCode;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Slf4j
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response implements Serializable {

  private String statusCode;
  private String statusMessage;
  private Object data;

  public Response(Integer statusCode, String statusMessage, Object data) {
    this.statusCode = (statusCode==202 ? "00" :String.valueOf(statusCode));
    this.statusMessage = statusMessage;
    this.data = data;
  }
  public Response(String statusCode, String statusMessage, Object data) {
    this.statusCode = statusCode;
    this.statusMessage = statusMessage;
    this.data = data;
  }
  public static ResponseEntity<?> setUpResponse(Integer httpCode,String statusMessage,String replace, Object  obj){
    Response responseMessage = new Response(httpCode,statusMessage.replace("{}",replace), obj);
    return ResponseEntity.status(httpCode).body(responseMessage);
  }
  public static ResponseEntity setUpResponse(ResponseCode resonseCode, String replace, Object  obj){
    Response responseMessage = new Response(resonseCode.getCode(), resonseCode.getValue().replace("{}",replace), obj);
    //log.info("Final response {}",responseMessage);
    return ResponseEntity.status(resonseCode.getCode()).body(responseMessage);
  }
  public static Response setUpResponse(ResponseCode resonseCode, String replace){
    return new Response(resonseCode.getCode(), resonseCode.getValue().replace("{}",replace), null);

  }

}