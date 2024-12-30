package ms_creditos.ms_creditos.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.ms_creditos.model.Transaction;

import reactor.core.publisher.Flux;

public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
    Flux<Transaction> findAllByProductId(String productId);
}
