package market.henry.auth.enums;

public enum ResponseCode {
    IN_PROGRESS(102,"Processing your Request"),
    SUCCESS(202,"Successful request"),
    AWAITING_AUTHORIZATION(206,"{} successful and awaiting authorization"),
    CREATED(201,"{} successful"),
    PROCEED(202,"Proceed to save; {}"),
    ITEM_NOT_FOUND(404,"Sorry we can't find {} data on our system"),
    ITEM_FOUND(302,"{} already exist; "),
    NOT_AUTHORIZED(401,"!Oops seems something went wrong...try again later"),
    REQUEST_TIMEOUT(408,"Session timeout.....please try again"),
    BAD_REQUEST(400,"Invalid request: {}"),
    UNKNOWN(500,"An error occurred...please try again later"),
    NOT_IMPLEMENTED(501,"Not Implemented...please try again later"),
    UNAVAILABLE(503,"{} service is currently unavailable at the moment ...please try again later");
    String value;
    Integer code;

    ResponseCode(Integer code, String value) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public Integer getCode() {
        return code;
    }
}
