package market.henry.auth.services;

import lombok.extern.slf4j.Slf4j;
import market.henry.auth.dto.AccountCreationRequest;
import market.henry.auth.enums.ResponseCode;
import market.henry.auth.model.Account;
import market.henry.auth.model.AccountDocument;
import market.henry.auth.repo.AccountRepo;
import market.henry.auth.utils.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AccountService {
    @Autowired
    private AccountRepo accountRepo;

    public ResponseEntity setup(AccountCreationRequest accountCreationRequest){
        if (accountCreationRequest.isUpdate()){
            return null;
        }else {
            Account ac = generate(accountCreationRequest);
           ac = accountRepo.save(ac);
           return Response.setUpResponse(ResponseCode.SUCCESS,"",null);
        }
    }

    private Account generate(AccountCreationRequest accountCreationRequest) {
        return Account.builder()
                .accountTier(accountCreationRequest.getAccountTier())
                .address(accountCreationRequest.getAddress())
                .annualIncome(accountCreationRequest.getAnnualIncome())
                .branch(accountCreationRequest.getBranch())
                .bvn(accountCreationRequest.getBvn())
                .country(accountCreationRequest.getCountry())
                .dob(accountCreationRequest.getDob())
                .email(accountCreationRequest.getEmail())
                .firstName(accountCreationRequest.getFirstName())
                .gender(accountCreationRequest.getGender())
                .idCardNumber(accountCreationRequest.getIdCardNumber())
                .idType(accountCreationRequest.getIdType())
                .initialAmount(accountCreationRequest.getInitialAmount())
                .middleName(accountCreationRequest.getMiddleName())
                .nextOfKinName(accountCreationRequest.getNextOfKinName())
                .nextOfKinPhoneNumber(accountCreationRequest.getNextOfKinPhoneNumber())
                .phoneNumber(accountCreationRequest.getPhoneNumber())
                .state(accountCreationRequest.getState())
                .surname(accountCreationRequest.getSurname())
                .title(accountCreationRequest.getTitle())
                .accountDocument(
                        AccountDocument.builder()
                                .accountType(accountCreationRequest.getAccountType())
                                .passportPhoto(accountCreationRequest.getPassportPhoto())
                                .proofOfAddress(accountCreationRequest.getProofOfAddress())
                                .referenceForm(accountCreationRequest.getReferenceForm())
                                .regulatoryId(accountCreationRequest.getRegulatoryId())
                                .signature(accountCreationRequest.getSignature())
                                .utilityBill(accountCreationRequest.getUtilityBill())
                                .build()
                )
                .build();
    }

}
