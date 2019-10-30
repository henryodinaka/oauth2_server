package market.henry.auth.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Entity
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AccountDocument {

    @SequenceGenerator(name = "seqAccoutId", sequenceName = "seqAccoutId", initialValue = 1)
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