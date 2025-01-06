package ms_creditos.ms_creditos.service.impl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ms_creditos.model.BalanceResponse;
import com.ms_creditos.model.Credit;
import com.ms_creditos.model.CreditDetailResponse;
import com.ms_creditos.model.CreditRequest;
import com.ms_creditos.model.CreditResponse;
import com.ms_creditos.model.DebtResponse;
import com.ms_creditos.model.Transaction;
import com.ms_creditos.model.TransactionRequest;
import com.ms_creditos.model.TransactionResponse;
import com.ms_creditos.model.TransactionType;

import lombok.RequiredArgsConstructor;
import ms_creditos.ms_creditos.client.ClientReactiveClient;
import ms_creditos.ms_creditos.exceptions.CreditNotFoundExcepction;
import ms_creditos.ms_creditos.repository.CreditRepository;
import ms_creditos.ms_creditos.repository.TransactionRepository;
import ms_creditos.ms_creditos.service.CreditService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CreditServiceImpl implements CreditService {

    private final CreditRepository creditRepository;
    private final TransactionRepository transactionRepository;
    private final ClientReactiveClient clientReactiveClient;

    /**
     * @return Flux Credit response to all credits
     */
    @Override
    public Flux<CreditResponse> findAll() {
        return creditRepository.findAll().map(this::toResponseFromEntity);
    }

    /**
     * @param id Credit Id to search
     * @return Mono Credit response to seach
     */
    @Override
    public Mono<CreditResponse> findById(final String id) {
        return creditRepository.findById(id)
            .switchIfEmpty(Mono.error(new CreditNotFoundExcepction("Credit with ID " + id + " not found")))
            .map(this::toResponseFromEntity);
    }

    /**
     * @param creditRequest Credit request to saved
     * @return Mono Credit response to saved
     */
    @Override
    public Mono<CreditResponse> insert(final CreditRequest creditRequest) {
        clientReactiveClient.findById(creditRequest.getClientId()).subscribe(System.out::println);
        return creditRepository.save(toEntityFromRequest(creditRequest)).map(this::toResponseFromEntity);
    }

    /**
     * @param id Current id credit to update
     * @param creditRequest Credit request to update
     * @return Mono Credit response to update
     */
    @Override
    public Mono<CreditResponse> update(final String id, final CreditRequest creditRequest) {
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

    /**
     * @param id Current id credit to delete
     * @return Mono void response to delete
     */
    @Override
    public Mono<Void> deleteById(final String id) {
        return creditRepository.findById(id)
                .switchIfEmpty(Mono.error(new CreditNotFoundExcepction("Credit with ID " + id + " not found")))
                .flatMap(creditEntity -> {
                    return creditRepository.deleteById(creditEntity.getId());
                });
    }

    /**
     * @param id Current id credit to deposit
     * @param transactionRequest Transaction request to deposit
     * @return Mono Transaction response to deposit
     */
    @Override
    public Mono<TransactionResponse> deposit(final String id, final TransactionRequest transactionRequest) {
        return transactionRepository
                .save(toEntityFromRequestTransaction(transactionRequest, id, TransactionType.DEPOSIT))
                .map(this::toResponseFromEntity);
    }

    private Transaction toEntityFromRequestTransaction(
        final TransactionRequest transactionRequest,
        final String id, final TransactionType transactionType) {
        Transaction transaction = new Transaction();
        transaction.setProductId(id);
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setDate(LocalDate.now());
        transaction.setMotive(transactionRequest.getMotive());
        transaction.setType(transactionType);
        return transaction;
    }

    private TransactionResponse toResponseFromEntity(final Transaction transaction) {
        TransactionResponse transactionResponse = new TransactionResponse();
        transactionResponse.setAmount(transaction.getAmount());
        transactionResponse.setDate(transaction.getDate());
        transactionResponse.setMotive(transaction.getMotive());
        transactionResponse.setType(transaction.getType());
        return transactionResponse;
    }

    /**
     * @param id Current id credit to withdraw
     * @return Mono Transaction response to withdraw
     */
    @Override
    public Mono<TransactionResponse> withdraw(final String id, final TransactionRequest transactionRequest) {
        return transactionRepository
                .save(toEntityFromRequestTransaction(transactionRequest, id, TransactionType.WITHDRAWAL))
                .map(this::toResponseFromEntity);
    }

    /**
     * @param idCredit Current id account to search
     * @return Flux Transaction response when has been account founded
     */
    @Override
    public Flux<TransactionResponse> getTransactionsByCredit(final String idCredit) {
        Flux<Transaction> transactionFlux = transactionRepository.findAllByProductId(idCredit);
        return transactionFlux.map(this::toResponseFromEntity);
    }

    /**
     * @param idCredit Current id account to search
     * @return Mono Balance response when has been account founded
     */
    @Override
    public Mono<BalanceResponse> getBalancesByCredit(final String idCredit) {
        return creditRepository.findById(idCredit).map(this::toBalanceResponseFromCredit);
    }

    private BalanceResponse toBalanceResponseFromCredit(final Credit credit) {
        BalanceResponse balanceResponse = new BalanceResponse();
        balanceResponse.setProductId(credit.getId());
        balanceResponse.setBalanceCredit(credit.getCurrentBalance());
        balanceResponse.setNroCredit(credit.getNroCredit());
        balanceResponse.setTypeCredit(credit.getType());
        return balanceResponse;
    }

    private CreditResponse toResponseFromEntity(final Credit credit) {
        CreditResponse creditResponse = new CreditResponse();
        creditResponse.setCreditLimit(credit.getCreditLimit());
        creditResponse.setCurrentBalance(credit.getCurrentBalance());
        creditResponse.setId(credit.getId());
        creditResponse.setHolders(credit.getHolders());
        creditResponse.setNroCredit(credit.getNroCredit());
        creditResponse.setOpeningDate(credit.getOpeningDate());
        creditResponse.setAuthorizedSigners(credit.getAuthorizedSigners());
        creditResponse.setType(credit.getType());
        creditResponse.setClientId(credit.getClientId());
        return creditResponse;
    }

    private Credit toEntityFromRequest(final CreditRequest creditRequest) {
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

    /**
     * @param clientId Current id client to search debts overdue
     * @return Mono Debt response by client
     */
    @Override
    public Mono<DebtResponse> getDebtsOverdue(final String clientId) {
        return creditRepository.findByClientId(clientId).collectList()
                .map(credits -> {
                    List<CreditDetailResponse> overdueDebts = credits.stream()
                            .filter(credit -> credit.getCurrentBalance().compareTo(credit.getCreditLimit()) != 0
                                           && credit.getExpirationDate().isBefore(LocalDate.now()))
                            .map(credit -> {
                                CreditDetailResponse detail = new CreditDetailResponse();
                                detail.setCreditId(credit.getId());
                                detail.setPendingAmount(credit.getCurrentBalance());
                                detail.setDueDate(credit.getExpirationDate());
                                return detail;
                            })
                            .collect(Collectors.toList());

                    DebtResponse response = new DebtResponse();
                    response.setHasDebt(!overdueDebts.isEmpty());
                    response.setDebts(overdueDebts);
                    return response;
                });
    }
}
