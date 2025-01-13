package ms_creditos.ms_creditos.service.impl.strategy;

import com.ms_creditos.model.Credit;
import com.ms_creditos.model.CreditRequest;

import reactor.core.publisher.Mono;

public class BusinessCreditValidationStrategy implements CreditValidationStrategy {

    /**
     * @param CreditRequest creditRequest
     * @return Credit Mono saved
     */
    @Override
    public Mono<Void> validate(final CreditRequest creditRequest) {
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }

    /**
     * @param CreditRequest creditRequest
     * @return Credit Mono saved
     */
    @Override
    public Mono<Credit> save(final CreditRequest creditRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'save'");
    }

}
