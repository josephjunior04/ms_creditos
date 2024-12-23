package ms_creditos.ms_creditos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.ms_creditos.model.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CreditNotFoundExcepction.class)
    public ResponseEntity<ErrorResponse> handleCreditNotFoundException(CreditNotFoundExcepction ex) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setError("Cuenta not found");
        errorResponse.setMessage(ex.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}
