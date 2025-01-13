package ms_creditos.ms_creditos.exceptions;

public class ClientNotFoundException extends RuntimeException {
    public ClientNotFoundException(final String message) {
        super(message);
    }
}
