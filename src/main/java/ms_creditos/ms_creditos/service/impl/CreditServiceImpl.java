package ms_creditos.ms_creditos.service.impl;

import java.util.Collections;

import org.springframework.stereotype.Service;

import com.ms_creditos.model.BalanceResponse;
import com.ms_creditos.model.Credit;
import com.ms_creditos.model.CreditRequest;
import com.ms_creditos.model.CreditResponse;
import com.ms_creditos.model.TransactionRequest;
import com.ms_creditos.model.TransactionResponse;

import lombok.RequiredArgsConstructor;
import ms_creditos.ms_creditos.exceptions.CreditNotFoundExcepction;
import ms_creditos.ms_creditos.repository.CreditRepository;
import ms_creditos.ms_creditos.service.CreditService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService{

    private final CreditRepository creditRepository;

    @Override
    public Flux<CreditResponse> findAll() {
        return creditRepository.findAll().map(this::toResponseFromEntity);
    }

    @Override
    public Mono<CreditResponse> findById(String id) {
        return creditRepository.findById(id)
        .switchIfEmpty(Mono
                .error(new CreditNotFoundExcepction("Credit with ID " + id + " not found")))
        .map(this::toResponseFromEntity);
    }

    @Override
    public Mono<CreditResponse> insert(CreditRequest creditRequest) {
        return creditRepository.save(toEntityFromRequest(creditRequest)).map(this::toResponseFromEntity);
    }

    @Override
    public Mono<CreditResponse> update(String id, CreditRequest creditRequest) {
        return creditRepository.findById(id)
                .flatMap(creditEntity -> {
                    creditEntity.setClientId(creditRequest.getClientId());
                    creditEntity.setCreditLimit(creditRequest.getCreditLimit());
                    creditEntity.setCurrentBalance(creditRequest.getCurrentBalance());
                    creditEntity.setNroCredit(creditRequest.getNroCredit());
                    creditEntity.setOpeningDate(creditRequest.getOpeningDate());
                    return creditRepository.save(creditEntity);
                })
                .switchIfEmpty(Mono.error(
                        new CreditNotFoundExcepction("Credit with ID " + id + " not found")))
                .map(this::toResponseFromEntity);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return creditRepository.findById(id)
                .switchIfEmpty(Mono.error(new CreditNotFoundExcepction("Credit with ID " + id + " not found")))
                .flatMap(creditEntity -> {
                    return creditRepository.deleteById(creditEntity.getId());
                });
    }

    @Override
    public Mono<TransactionResponse> deposit(String id, TransactionRequest transactionRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deposit'");
    }

    @Override
    public Mono<TransactionResponse> withdrawal(String id, TransactionRequest transactionRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'withdrawal'");
    }

    @Override
    public Flux<TransactionResponse> getTransactionsByCreditAndClient(String idCredit, String idClient) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getTransactionsByCreditAndClient'");
    }

    @Override
    public Mono<BalanceResponse> getBalancesByCreditAndClient(String idCredit, String idClient) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getBalancesByCreditAndClient'");
    }

    private CreditResponse toResponseFromEntity(Credit credit) {
        CreditResponse creditResponse = new CreditResponse();
        creditResponse.setCreditLimit(credit.getCreditLimit());
        creditResponse.setCurrentBalance(credit.getCurrentBalance());
        creditResponse.setId(credit.getId());
        creditResponse.setHolders(credit.getHolders());
        creditResponse.setNroCredit(credit.getNroCredit());
        creditResponse.setOpeningDate(credit.getOpeningDate());
        creditResponse.setAuthorizedSigners(credit.getAuthorizedSigners());
        creditResponse.setTransactions(credit.getTransactions());
        creditResponse.setType(credit.getType());
        creditResponse.setClientId(credit.getClientId());
        return creditResponse;
    }

    private Credit toEntityFromRequest(CreditRequest creditRequest) {
        Credit credit = new Credit();
        credit.setClientId(creditRequest.getClientId());
        credit.setCreditLimit(creditRequest.getCreditLimit());
        credit.setCurrentBalance(creditRequest.getCurrentBalance());
        credit.setNroCredit(creditRequest.getNroCredit());
        credit.setOpeningDate(creditRequest.getOpeningDate());
        credit.setAuthorizedSigners(Collections.emptyList());
        credit.setHolders(Collections.emptyList());
        credit.setTransactions(Collections.emptyList());
        credit.setType(creditRequest.getType());
        return credit;
    }
}
