package market.henry.auth.dto;

import lombok.Data;

@Data
public class LoanRequest {

    private String applicationNumber;
    private String productCode;
    private String tenor;
    private String amount;
    private String accountNumber;
    private String interestRate;


}