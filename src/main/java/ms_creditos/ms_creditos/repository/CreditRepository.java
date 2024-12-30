package ms_creditos.ms_creditos.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ms_creditos.model.Credit;

public interface CreditRepository extends ReactiveMongoRepository<Credit, String> {

}
