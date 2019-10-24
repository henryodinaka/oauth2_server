package market.henry.auth.utils;

import market.henry.auth.dto.AccountCheckRequest;
import market.henry.auth.dto.LoanRequest;
import market.henry.auth.dto.SecretRequest;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.regex.Pattern;

public class Validation {

    public static String validateAccountCheckRequest(AccountCheckRequest accountCheckRequest){
        if (accountCheckRequest == null) return "Invalid request";
        if (!validData(accountCheckRequest.getFirstName())) return "Invalid first name";
        if (!validData(accountCheckRequest.getLastName())) return "Invalid last name";
        return null;
    }
    public static String validateLoanRequest(LoanRequest loanRequest){
        if (loanRequest == null) return "Invalid request";
        if (!validLength(loanRequest.getAccountNumber(),10)) return "Account number must be 10 digits";
        if (!validData(loanRequest.getApplicationNumber())) return "Invalid application number";
        if (!validNumberLength(loanRequest.getInterestRate(),1,3)) return "Interest rate cannot be more than 3 digits";
        if (!validNumberLength(loanRequest.getAmount(),1,15)) return "amount can not be more than 15 digits";
        if (!validData(loanRequest.getProductCode())) return "Invalid product code";
        if (!validNumberLength(loanRequest.getTenor(),1,3)) return "Tenor can not be between 1 and 3 digist";
        if (!validData(loanRequest.getProductCode())) return "Invalid product code";
        return null;
    }
    public static String validateSecretRequest(SecretRequest secretRequest, String channelCode){
        if (secretRequest == null) return "Invalid request";
        if (!validNumberLength(secretRequest.getPhoneNumber(),4,15)) return "phone number must be between 4 and 11 digits";
        if ("01".equalsIgnoreCase(channelCode) && !validNumberLength(secretRequest.getSecret(),6)) return "token must be 6 digits";
        if ("02".equalsIgnoreCase(channelCode)||"03".equalsIgnoreCase(channelCode) && !validNumberLength(secretRequest.getSecret(),4)) return "pin must be 4 digits";
        return null;
    }
    public static boolean validData(Object obj){
        if (obj ==null|| String.valueOf(obj).isEmpty()) {
            return false;
        }
        if (obj.equals("null")) {
            return false;
        }
        if (obj instanceof String){
            String s = ((String)obj).trim();
            if (s.length()<1) return false;
        }

        return true;
    }

    public static boolean validName(String name) {

        if (name.length() < 2) {
            return false;
        }

        if (name.length() > 255) {
            return false;
        }

        //pure alphabets
        if (!(Pattern.compile("[a-zA-Z]+").matcher(name).matches())) {
            return false;
        }

        return true;
    }

    public static boolean validLength(String string, int min, int max) {
        if (!validData(string)) return false;
        if (string.length() < min) {
            return false;
        }

        if (max != 0) {
            if (string.length() > max) {
                return false;
            }
        }

        return true;
    }
    public static boolean validNumberLength(String numbers,int min, int max) {

        //pure number
        if (!validNumber(numbers)) {
            return false;
        }
        if (!validLength(numbers,min,max))
            return false;

        return true;
    }
    public static boolean validNumberLength(String numbers,int digits) {
        if (numbers ==null)
            return false;

        //pure number
        if (!validNumber(numbers)) {
            return false;
        }

        if (!validLength(numbers,digits))
            return false;

        return true;
    }
    public static boolean validNumber(String numbers) {
        //pure number
        if (!(Pattern.compile("[0-9]+").matcher(numbers).matches())) {
            return false;
        }
        return true;
    }
    public static boolean validLength(String string, int digits) {

        if (string.length() < digits) {
            return false;
        }

        if (digits != 0) {
            if (string.length() > digits) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {

    }
}
