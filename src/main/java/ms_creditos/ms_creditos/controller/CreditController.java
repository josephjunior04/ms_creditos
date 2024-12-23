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

    @Override
    public Mono<ResponseEntity<Void>> delete(String idCredit, ServerWebExchange exchange) {
        return creditService.deleteById(idCredit)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }

    @Override
    public Mono<ResponseEntity<TransactionResponse>> deposit(String idCredit,
            @Valid Mono<TransactionRequest> transactionRequest, ServerWebExchange exchange) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deposit'");
    }

    @Override
    public Mono<ResponseEntity<Flux<CreditResponse>>> findAllCredits(ServerWebExchange exchange) {
        return Mono.just(ResponseEntity.ok(creditService.findAll()));
    }

    @Override
    public Mono<ResponseEntity<CreditResponse>> findById(String idCredit, ServerWebExchange exchange) {
        return creditService.findById(idCredit)
                .map(creditResponse -> {
                    return ResponseEntity
                            .status(HttpStatus.OK)
                            .body(creditResponse);
                });
    }

    @Override
    public Mono<ResponseEntity<BalanceResponse>> getBalancesByCreditAndClient(String idCredit, String idClient,
            ServerWebExchange exchange) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBalancesByCreditAndClient'");
    }

    @Override
    public Mono<ResponseEntity<TransactionResponse>> getTransactionsByCreditAndClient(String idCredit, String idClient,
            ServerWebExchange exchange) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTransactionsByCreditAndClient'");
    }

    @Override
    public Mono<ResponseEntity<CreditResponse>> insert(@Valid Mono<CreditRequest> creditRequest,
            ServerWebExchange exchange) {
        return creditRequest
                .flatMap(request -> creditService.insert(request)
                        .map(clienteResponse -> {
                            return ResponseEntity
                                    .status(HttpStatus.CREATED)
                                    .body(clienteResponse);
                        }));
    }

    @Override
    public Mono<ResponseEntity<CreditResponse>> update(String idCredit, @Valid Mono<CreditRequest> creditRequest,
            ServerWebExchange exchange) {
        return creditRequest
                .flatMap(request -> creditService.update(idCredit, request)
                        .map(clienteResponse -> {
                            return ResponseEntity
                                    .status(HttpStatus.CREATED)
                                    .body(clienteResponse);
                        }));
    }

    @Override
    public Mono<ResponseEntity<TransactionResponse>> withdrawal(String idCredit,
            @Valid Mono<TransactionRequest> transactionRequest, ServerWebExchange exchange) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'withdrawal'");
    }

}
