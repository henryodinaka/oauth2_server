package market.henry.auth.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import market.henry.auth.exceptions.AuthServerException;
import market.henry.auth.model.Account;
import market.henry.auth.model.User;
import market.henry.auth.utils.CommonUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class BvnDetails {
    private String bvn;
    private String title;
    private String firstName;
    private String surname;
    private String gender;
    private String phoneNumber;
    private String email;
    private String  dob;
    private String accountNumber;

    public BvnDetails(User user) throws AuthServerException {
        this.bvn = user.getBvn();
        this.title = user.getTitle();
        this.firstName = user.getFirstName();
        this.surname = user.getLastName();
        this.gender = user.getGender();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
        this.dob = CommonUtils.dateToString(user.getDob());
        this.accountNumber = user.getAccountNumber();
    }

    public BvnDetails(Account account) throws AuthServerException {
        this.bvn = account.getBvn();
        this.title = account.getTitle();
        this.firstName = account.getFirstName();
        this.surname = account.getSurname();
        this.gender = account.getGender();
        this.phoneNumber = account.getPhoneNumber();
        this.email = account.getEmail();
        this.dob = CommonUtils.dateToString(account.getDob());
    }
}
