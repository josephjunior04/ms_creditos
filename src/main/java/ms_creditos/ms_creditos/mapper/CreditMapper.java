package ms_creditos.ms_creditos.mapper;

import org.springframework.stereotype.Component;

import com.ms_creditos.model.Credit;
import com.ms_creditos.model.CreditRequest;
import com.ms_creditos.model.CreditResponse;

@Component
public final class CreditMapper {

    private CreditMapper() {
        //Not called
    }

    public static Credit toEntityFromRequest(final CreditRequest creditRequest) {
        Credit credit = new Credit();
        credit.setClientId(creditRequest.getClientId());
        credit.setCreditLimit(creditRequest.getCreditLimit());
        credit.setCurrentBalance(creditRequest.getCurrentBalance());
        credit.setNroCredit(creditRequest.getNroCredit());
        credit.setOpeningDate(creditRequest.getOpeningDate());
        credit.setType(creditRequest.getType());
        return credit;
    }

    public static CreditResponse toResponseFromEntity(final Credit credit) {
        CreditResponse creditResponse = new CreditResponse();
        creditResponse.setCreditLimit(credit.getCreditLimit());
        creditResponse.setCurrentBalance(credit.getCurrentBalance());
        creditResponse.setId(credit.getId());
        creditResponse.setNroCredit(credit.getNroCredit());
        creditResponse.setOpeningDate(credit.getOpeningDate());
        creditResponse.setType(credit.getType());
        creditResponse.setClientId(credit.getClientId());
        return creditResponse;
    }
}
