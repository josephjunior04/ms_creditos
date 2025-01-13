package ms_creditos.ms_creditos.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ms_creditos.model.Credit;

import reactor.core.publisher.Flux;

public interface CreditRepository extends ReactiveMongoRepository<Credit, String> {

    Flux<Credit> findByClientId(String clientId);
    Flux<Credit> findByTypeAndClientId(String type, String clientId);
}
