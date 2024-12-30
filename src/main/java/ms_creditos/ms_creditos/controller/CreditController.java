package ms_creditos.ms_creditos.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;

import com.ms_creditos.api.V1Api;
import com.ms_creditos.model.BalanceResponse;
import com.ms_creditos.model.CreditRequest;
import com.ms_creditos.model.CreditResponse;
import com.ms_creditos.model.TransactionRequest;
import com.ms_creditos.model.TransactionResponse;

import lombok.RequiredArgsConstructor;
import ms_creditos.ms_creditos.service.CreditService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CreditController implements V1Api {

    private final CreditService creditService;

    /**
     * @return Mono Response Entity of void to delete with status
     * @param idCredit Current Id Account to delete
     */
    @Override
    public Mono<ResponseEntity<Void>> delete(final String idCredit, final ServerWebExchange exchange) {
        return creditService.deleteById(idCredit)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    /**
     * @return Mono Response Entity of Transaction response to deposit with status
     * @param idCredit Current Id Account to deposit
     */
    @Override
    public Mono<ResponseEntity<TransactionResponse>> deposit(final String idCredit,
            final @Valid Mono<TransactionRequest> transactionRequest, final ServerWebExchange exchange) {
        return transactionRequest
            .flatMap(request -> creditService.deposit(idCredit, request)
                    .map(creditResponse -> {
                        return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(creditResponse);
                    }));
    }

    /**
     * @return Mono Response Entity of Flux Credit response to search with status
     */
    @Override
    public Mono<ResponseEntity<Flux<CreditResponse>>> findAllCredits(final ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(creditService.findAll()));
    }

    /**
     * @return Mono Response Entity of Credit response to search with status
     * @param idCredit Current Id Account to search
     */
    @Override
    public Mono<ResponseEntity<CreditResponse>> findById(final String idCredit, final ServerWebExchange exchange) {
        return creditService.findById(idCredit)
                .map(creditResponse -> {
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(creditResponse);
                });
    }

    /**
     * @return Mono Response Entity of Credit response to insert with status
     * @param creditRequest Current Credit request to insert
     */
    @Override
    public Mono<ResponseEntity<CreditResponse>> insert(final @Valid Mono<CreditRequest> creditRequest,
            final ServerWebExchange exchange) {
        return creditRequest
                .flatMap(request -> creditService.insert(request)
                        .map(clienteResponse -> {
                            return ResponseEntity
                                    .status(HttpStatus.CREATED)
                                    .body(clienteResponse);
                        }));
    }

    /**
     * @return Mono Response Entity of Credit response to update with status
     * @param idCredit Current Id Account to update
     * @param creditRequest Current Credit request to update
     */
    @Override
    public Mono<ResponseEntity<CreditResponse>> update(final String idCredit,
            final @Valid Mono<CreditRequest> creditRequest,
            final ServerWebExchange exchange) {
        return creditRequest
                .flatMap(request -> creditService.update(idCredit, request)
                        .map(clienteResponse -> {
                            return ResponseEntity
                                    .status(HttpStatus.CREATED)
                                    .body(clienteResponse);
                        }));
    }

    /**
     * @return Mono Response Entity of Transaction response to withdraw with status
     * @param idCredit Current Id Account to withdraw
     */
    @Override
    public Mono<ResponseEntity<TransactionResponse>> withdraw(final String idCredit,
            final @Valid Mono<TransactionRequest> transactionRequest, final ServerWebExchange exchange) {
        return transactionRequest
            .flatMap(request -> creditService.withdraw(idCredit, request)
                    .map(creditResponse -> {
                        return ResponseEntity
                                .status(HttpStatus.CREATED)
                                .body(creditResponse);
                    }));
    }

    /**
     * @return Mono Response Entity of Flux of Transaction response to balances with status
     * @param idCredit Current Id Credit to search balances
     */
    @Override
    public Mono<ResponseEntity<BalanceResponse>> getBalancesByCredit(final String idCredit,
            final ServerWebExchange exchange) {
        return creditService.getBalancesByCredit(idCredit)
            .map(creditResponse -> {
                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(creditResponse);
            });
    }

    /**
     * @return Mono Response Entity of Flux of Transaction response to transactions with status
     * @param idCredit Current Id credit to search transactions
     */
    @Override
    public Mono<ResponseEntity<Flux<TransactionResponse>>> getTransactionsByCredit(final String idCredit,
            final ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(creditService.getTransactionsByCredit(idCredit)));
    }

}
