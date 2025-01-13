package ms_creditos.ms_creditos.service.impl.strategy;

import com.ms_creditos.model.Credit;
import com.ms_creditos.model.CreditRequest;

import reactor.core.publisher.Mono;

public interface CreditValidationStrategy {
    Mono<Void> validate(CreditRequest creditRequest);
    Mono<Credit> save(CreditRequest creditRequest);
}
