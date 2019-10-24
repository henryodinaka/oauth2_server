package market.henry.auth.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name = "Users")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String firstName;
    private String lastName;
    private String gender;
    private String bvn;
    private String phoneNumber;
    private String email;
    private LocalDate dob;
    private String accountNumber;
    private BigDecimal accountBalance;

    @JsonIgnore
    @JsonIgnoreProperties(ignoreUnknown = true)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "UserRoles", joinColumns = @JoinColumn(name = "UserId", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "RoleId", referencedColumnName = "id"))
    private Set<Role> roles;

    public User(String title, String firstName, String lastName, String gender, String bvn, String phoneNumber,String email,LocalDate dob,String accountNumber) {
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.bvn = bvn;
        this.phoneNumber = phoneNumber;
        this.email =email;
        this.dob = dob;
        this.accountNumber=accountNumber;
    }
    public User(String title, String firstName, String lastName, String gender, String bvn, String phoneNumber,String email,LocalDate dob) {
        this.title = title;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.bvn = bvn;
        this.phoneNumber = phoneNumber;
        this.email =email;
        this.dob = dob;
    }
}
