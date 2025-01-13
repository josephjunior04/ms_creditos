package ms_creditos.ms_creditos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ms_creditos.model.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * @param ex Custom Excepcion Credit not found
     * @return Response Entity of Error Response with HttpStatus
     */
    @ExceptionHandler(CreditNotFoundExcepction.class)
    public ResponseEntity<ErrorResponse> handleCreditNotFoundException(final CreditNotFoundExcepction ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("Cuenta not found");
        errorResponse.setMessage(ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * @param ex
     * @return Response Entity of Error Response with HttpStatus
     */
    @ExceptionHandler(ClientNotFoundException.class)
    public final ResponseEntity<ErrorResponse> handleClientNotFoundException(final ClientNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("CLIENT_NOT_FOUND");
        errorResponse.setMessage(ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * @param ex
     * @return Response Entity of Error Response with HttpStatus
     */
    @ExceptionHandler(CreditPersonalAlreadyExists.class)
    public final ResponseEntity<ErrorResponse> handleCreditAlreadyExists(final CreditPersonalAlreadyExists ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("CREDIT_ALREADY_EXISTS");
        errorResponse.setMessage(ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * @param ex
     * @return Response Entity of Error Response with HttpStatus
     */
    @ExceptionHandler(NoPendingQuotaException.class)
    public final ResponseEntity<ErrorResponse> handleNoPendingQuota(final NoPendingQuotaException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("NO_PENDING_QUOTA");
        errorResponse.setMessage(ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * @param ex
     * @return Response Entity of Error Response with HttpStatus
     */
    @ExceptionHandler(QuotaAlreadyPaidException.class)
    public final ResponseEntity<ErrorResponse> handleQuotaAlreadyPaid(final QuotaAlreadyPaidException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("QUOTA_ALREADY_PAID");
        errorResponse.setMessage(ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * @param ex
     * @return Response Entity of Error Response with HttpStatus
     */
    @ExceptionHandler(QuotaPaymentOrderException.class)
    public final ResponseEntity<ErrorResponse> handleQuotaPaymentOrder(final QuotaPaymentOrderException ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("QUOTA_PAYMENT_ORDER");
        errorResponse.setMessage(ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
