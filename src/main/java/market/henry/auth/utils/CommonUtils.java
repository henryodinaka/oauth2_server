package market.henry.auth.utils;

import lombok.extern.slf4j.Slf4j;
import market.henry.auth.enums.ResponseCode;
import market.henry.auth.exceptions.AuthServerException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Henry on 16/07/2019.
 */
@Slf4j
public final class CommonUtils {
    
    private CommonUtils() {}

    public static DateParse dateFormat(String startDate, String endDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime sDate = null;
        LocalDateTime eDate = null;
        if (!Validation.validData(startDate)|| !Validation.validData(endDate ==null))return null;
        sDate = LocalDate.parse(startDate, formatter).atStartOfDay();
        eDate = LocalDate.parse(endDate,formatter).atStartOfDay().plusDays(1).minusSeconds(1);
        return new DateParse(sDate,eDate);
    }
    public static String  dateToString(LocalDate localDate) throws AuthServerException {
        try {
            if (!Validation.validData(localDate)) return null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return formatter.format(localDate);
        }catch (Exception e){
            log.error("Date parse error",e);
            throw new AuthServerException(ResponseCode.BAD_REQUEST.getCode(),e.getMessage());
        }
    }
    public static LocalDate  dateFormat(String startDate) throws AuthServerException {
        try {
            if (!Validation.validData(startDate)) return null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            return LocalDate.parse(startDate,formatter);
        }catch (Exception e){
            log.error("Date parse error",e);
            e.printStackTrace();
            throw new AuthServerException(ResponseCode.BAD_REQUEST.getCode(),e.getMessage());
        }
    }
    public static void main(String[] args) {
        /*DateParse dateParse = dateFormat("2019-08-04", "2019-08-10");

        System.out.println("The Start date time "+dateParse.getStartDate());
        System.out.println("The End date time "+dateParse.getEndDate());*/
        try {
            String dateToString = dateToString(LocalDate.now());
            System.out.println("To String: "+dateToString);
            LocalDate localDate = dateFormat(dateToString);
            System.out.println("To date: "+localDate);
        } catch (AuthServerException e) {
            e.printStackTrace();
        }
    }
}
