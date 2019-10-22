package market.henry.auth.model;

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
public class AccountDocument {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private String accountType;
    private String passportPhoto;
    private String regulatoryId;
    private String utilityBill;
    private String referenceForm;
    private String signature;
    private String proofOfAddress;
}