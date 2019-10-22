package market.henry.auth.dto;

import lombok.Data;

@Data
public class AccountCreationRequest {
    boolean update;
    private String bvn;
    private String title;
    private String firstName;
    private String middleName;
    private String surname;
    private String phoneNumber;
    private String  dob;
    private String gender;
    private String address;
    private String country;
    private String state;
    private String nextOfKinName;
    private String nextOfKinPhoneNumber;
    private String email;
    private String idType;
    private String idCardNumber;
    private String branch;
    private String annualIncome;
    private String  initialAmount;
    private String accountTier;

    private String accountType;
    private String passportPhoto;
    private String regulatoryId;
    private String utilityBill;
    private String referenceForm;
    private String signature;
    private String proofOfAddress;
}
