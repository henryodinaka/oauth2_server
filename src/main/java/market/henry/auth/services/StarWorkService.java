package market.henry.auth.services;

import lombok.extern.slf4j.Slf4j;
import market.henry.auth.dto.LoanRequest;
import market.henry.auth.enums.LoanStatus;
import market.henry.auth.enums.ResponseCode;
import market.henry.auth.model.StartWorkFlow;
import market.henry.auth.repo.StarWorkRepo;
import market.henry.auth.utils.Response;
import market.henry.auth.utils.Validation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class StarWorkService {

    @Autowired
    private StarWorkRepo starWorkRepo;

    public ResponseEntity setup(LoanRequest loanRequest){
        log.info("Incoming loan request {}",loanRequest);
        String error = Validation.validateLoanRequest(loanRequest);
        if (error !=null) return ResponseEntity.ok(new Response(400,error,null));
        StartWorkFlow startWorkFlow = generate(loanRequest);
        startWorkFlow = starWorkRepo.save(startWorkFlow);
        return Response.setUpResponse(ResponseCode.PROCEED,"Loan request is being processed...you shall receive notification within 24 hrs",null);

    }
    private StartWorkFlow generate(LoanRequest loanRequest){
        return StartWorkFlow.builder()
                .accountNumber(loanRequest.getAccountNumber())
                .amount(loanRequest.getAmount())
                .applicationNumber(loanRequest.getApplicationNumber())
                .interestRate(loanRequest.getInterestRate())
                .productCode(loanRequest.getProductCode())
                .tenor(loanRequest.getTenor())
                .loanStatus(LoanStatus.UNAPPROVED.name())
                .build();
    }

    public List<StartWorkFlow> getStarStatus(List<String > applicationNumbers){
        return starWorkRepo.getApprovedStar(applicationNumbers);
    }

    public List<StartWorkFlow> upadteAndDisburse(List<String > applicationNumbers){
        List<StartWorkFlow> disbursedStar = starWorkRepo.getApprovedStar(applicationNumbers);
        List<StartWorkFlow> updates = new ArrayList<>();
        disbursedStar.forEach(ds ->{
            ds.setActive(true);
            updates.add(ds);
        });
        return starWorkRepo.save(updates);

    }
}
