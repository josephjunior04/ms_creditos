package ms_creditos.ms_creditos.service;

import com.ms_creditos.model.BalanceResponse;
import com.ms_creditos.model.CreditRequest;
import com.ms_creditos.model.CreditResponse;
import com.ms_creditos.model.TransactionRequest;
import com.ms_creditos.model.TransactionResponse;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CreditService {
    Flux<CreditResponse> findAll();
    Mono<CreditResponse> findById(String id);
    Mono<CreditResponse> insert(CreditRequest creditRequest);
    Mono<CreditResponse> update(String id, CreditRequest creditRequest);
    Mono<Void> deleteById(String id);
    Mono<TransactionResponse> deposit(String id, TransactionRequest transactionRequest);
    Mono<TransactionResponse> withdraw(String id, TransactionRequest transactionRequest);
    Flux<TransactionResponse> getTransactionsByCredit(String idCredit);
    Mono<BalanceResponse> getBalancesByCredit(String idCredit);
}
