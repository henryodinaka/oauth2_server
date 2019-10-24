package market.henry.auth.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StartWorkFlow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String applicationNumber;
    private String productCode;
    private String tenor;
    private String amount;
    private String accountNumber;
    private String interestRate;
    private String loanStatus;
    private boolean active = false;

    public StartWorkFlow( String applicationNumbers,String loanStatus) {
        this.loanStatus = loanStatus;
        this.applicationNumber = applicationNumbers;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
