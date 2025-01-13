package ms_creditos.ms_creditos.exceptions;

public class CreditPersonalAlreadyExists extends RuntimeException {
    public CreditPersonalAlreadyExists(final String message) {
        super(message);
    }
}
