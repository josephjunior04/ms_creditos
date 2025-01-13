package ms_creditos.ms_creditos.service.impl.strategy;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ms_creditos.model.CreditType;

@Service
public class CreditValidationStrategyFactory {
    private final Map<Class<? extends CreditValidationStrategy>, CreditType> strategyTypeMap = Map.of(
            PersonalCreditValidationStrategy.class, CreditType.PERSONAL,
            BusinessCreditValidationStrategy.class, CreditType.BUSINESS,
            CreditCardValidationStrategy.class, CreditType.CARD);

    private final Map<CreditType, CreditValidationStrategy> strategies;

    public CreditValidationStrategyFactory(final List<CreditValidationStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        strategy -> determineCreditType(strategy),
                        strategy -> strategy));
    }

    private CreditType determineCreditType(final CreditValidationStrategy strategy) {
        return Optional.ofNullable(strategyTypeMap.get(strategy.getClass()))
                .orElseThrow(() -> new IllegalArgumentException(
                        "Unknown strategy type: " + strategy.getClass().getSimpleName()));
    }

    /**
     * @param creditType CreditType enum
     * @return CreditValidationStrategy strategy to use
     */
    public CreditValidationStrategy getStrategy(final CreditType creditType) {
        return Optional.ofNullable(strategies.get(creditType))
                .orElseThrow(() -> new UnsupportedOperationException("Unsupported credit type"));
    }
}
