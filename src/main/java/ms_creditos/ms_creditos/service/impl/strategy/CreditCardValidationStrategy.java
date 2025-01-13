package ms_creditos.ms_creditos.service.impl.strategy;

import org.springframework.stereotype.Component;

import com.ms_creditos.model.Credit;
import com.ms_creditos.model.CreditRequest;

import lombok.RequiredArgsConstructor;
//import ms_creditos.ms_creditos.repository.CreditRepository;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CreditCardValidationStrategy implements CreditValidationStrategy {

    //private final CreditRepository creditRepository;

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
