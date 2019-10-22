package market.henry.auth.model;

import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Entity
public class Account {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
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

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "accountDocument")
    private AccountDocument accountDocument;
}
