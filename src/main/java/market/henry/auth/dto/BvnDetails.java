package market.henry.auth.dto;

import lombok.Data;
import market.henry.auth.exceptions.AuthServerException;
import market.henry.auth.model.User;
import market.henry.auth.utils.CommonUtils;

@Data
public class BvnDetails {
    private String title;
    private String firstName;
    private String surname;
    private String gender;
    private String phoneNumber;
    private String  dob;

    public BvnDetails(User user) throws AuthServerException {
        this.title = user.getTitle();
        this.firstName = user.getFirstName();
        this.surname = user.getLastName();
        this.gender = user.getGender();
        this.phoneNumber = user.getPhoneNumber();
        this.dob = CommonUtils.dateToString(user.getDob());
    }
}
