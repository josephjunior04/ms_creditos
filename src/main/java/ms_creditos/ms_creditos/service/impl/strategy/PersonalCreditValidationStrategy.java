package ms_creditos.ms_creditos.service.impl.strategy;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.ms_creditos.model.Credit;
import com.ms_creditos.model.CreditRequest;
import com.ms_creditos.model.CreditStatus;
import com.ms_creditos.model.Quota;
import com.ms_creditos.model.QuotaStatus;

import lombok.RequiredArgsConstructor;
import ms_creditos.ms_creditos.exceptions.ClientNotFoundException;
import ms_creditos.ms_creditos.exceptions.CreditPersonalAlreadyExists;
import ms_creditos.ms_creditos.repository.CreditRepository;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PersonalCreditValidationStrategy implements CreditValidationStrategy {

    private final CreditRepository creditRepository;
    private final WebClient webClient;

    private static final int TOTAL_PERCENT = 100;
    private static final int NUMBERS_MONTH_PER_YEAR = 12;

    /**
     * @param CreditRequest creditRequest
     * @return Response Mono void of validate
     */
    @Override
    public Mono<Void> validate(final CreditRequest creditRequest) {
        return validateClientExists(creditRequest.getClientId())
                .then(validateOnlyOneCreditByType(creditRequest))
                .then(validateNoDebt(creditRequest.getClientId()));
    }

    private Mono<Void> validateOnlyOneCreditByType(final CreditRequest creditRequest) {
        return creditRepository.findByTypeAndClientId(creditRequest.getType().getValue(), creditRequest.getClientId())
                .hasElements()
                .flatMap(hasCredit -> {
                    if (hasCredit) {
                        return Mono.error(new CreditPersonalAlreadyExists("A personal credit already exists"));
                    }
                    return Mono.empty();
                });
    }

    private Mono<Void> validateNoDebt(final String clientId) {
        return creditRepository.findByClientId(clientId)
                .filter(this::hasOutstandingDebt)
                .hasElements()
                .flatMap(this::handleDebtValidation);
    }

    private boolean hasOutstandingDebt(final Credit credit) {
        return credit.getStatus().equals(CreditStatus.DEFAULTED);
    }

    private Mono<Void> handleDebtValidation(final boolean hasDebt) {
        if (hasDebt) {
            return Mono.error(new CreditPersonalAlreadyExists("The client has outstanding debts"));
        }
        return Mono.empty();
    }

    private Mono<Void> validateClientExists(final String clientId) {
        return webClient.get()
                .uri("http://localhost:8080/v1/clients/{id}", clientId)
                .retrieve()
                .bodyToMono(Object.class)
                .onErrorResume(WebClientResponseException.NotFound.class,
                        ex -> Mono.error(new ClientNotFoundException("Client not found for ID: " + clientId)))
                .then();
    }

    /**
     * @param CreditRequest creditRequest
     * @return Credit Mono saved
     */
    @Override
    public Mono<Credit> save(final CreditRequest creditRequest) {
        return validate(creditRequest)
                .then(Mono.just(toEntityFromRequest(creditRequest)))
                .flatMap(creditRepository::save);
    }

    private Credit toEntityFromRequest(final CreditRequest creditRequest) {
        Credit credit = new Credit();
        credit.setClientId(creditRequest.getClientId());
        credit.setCreditLimit(creditRequest.getCreditLimit());
        credit.setCurrentBalance(creditRequest.getCreditLimit());
        credit.setNroCredit(creditRequest.getNroCredit());
        credit.setOpeningDate(LocalDate.now());
        credit.setNumbersOfQuota(creditRequest.getNumbersOfQuota());
        credit.setInterestRate(creditRequest.getInterestRate());
        credit.setType(creditRequest.getType());
        credit.setStatus(CreditStatus.ACTIVE);
        credit.quotas(calculateQuotas(creditRequest));
        return credit;
    }

    private List<Quota> calculateQuotas(final CreditRequest creditRequest) {
        BigDecimal monthlyRate = calculateMonthlyRate(creditRequest.getInterestRate());
        BigDecimal monthlyPayment = calculateMonthlyPayment(
            creditRequest.getCreditLimit(), monthlyRate, creditRequest.getNumbersOfQuota());
        LocalDate initialPaymentDate = getInitialPaymentDate();

        return generateQuotas(creditRequest.getNumbersOfQuota(), monthlyPayment, initialPaymentDate);
    }

    private BigDecimal calculateMonthlyRate(final BigDecimal interestRate) {
        return interestRate
                .divide(BigDecimal.valueOf(TOTAL_PERCENT), MathContext.DECIMAL64)
                .divide(BigDecimal.valueOf(NUMBERS_MONTH_PER_YEAR), MathContext.DECIMAL64);
    }

    private BigDecimal calculateMonthlyPayment(final BigDecimal amount, final BigDecimal monthlyRate,
            final int numberQuotas) {
        return amount.multiply(monthlyRate)
                .divide(BigDecimal.ONE.subtract(
                        BigDecimal.ONE.divide(
                                monthlyRate.add(BigDecimal.ONE).pow(numberQuotas),
                                MathContext.DECIMAL64)),
                        MathContext.DECIMAL64);
    }

    private LocalDate getInitialPaymentDate() {
        LocalDate currentDate = LocalDate.now();
        return currentDate.withDayOfMonth(currentDate.lengthOfMonth());
    }

    private List<Quota> generateQuotas(final int numberQuotas, final BigDecimal monthlyPayment,
            final LocalDate initialPaymentDate) {
        return IntStream.rangeClosed(1, numberQuotas)
                .mapToObj(i -> createQuota(i, monthlyPayment, initialPaymentDate))
                .collect(Collectors.toList());
    }

    private Quota createQuota(final int number, final BigDecimal amount, final LocalDate initialPaymentDate) {
        Quota quota = new Quota();
        quota.setNumber(number);
        quota.setAmount(amount);
        quota.setDueDate(getPaymentDateByQuotas(initialPaymentDate, number - 1));
        quota.setStatus(QuotaStatus.PENDING);
        return quota;
    }

    private LocalDate getPaymentDateByQuotas(final LocalDate initialDate, final int monthsToAdd) {
        return initialDate.plusMonths(monthsToAdd)
                .withDayOfMonth(initialDate.plusMonths(monthsToAdd).lengthOfMonth());
    }

}
