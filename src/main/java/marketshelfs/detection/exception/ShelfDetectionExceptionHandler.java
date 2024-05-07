package marketshelfs.detection.exception;

import marketshelfs.detection.beans.ExceptionResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.convert.ConversionFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.UnsatisfiedServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ShelfDetectionExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(ShelfDetectionExceptionHandler.class);

    private static final String SINGLE_QUOTE = "'";
    private static final String INTERNAL_ERROR = "Internal Error. Please contact support.";

    @ExceptionHandler
    public ResponseEntity<Object> handleMainServiceException(final ShelfDetectionServerException exception){
        LOGGER.error(exception);
        return new ResponseEntity<>(exception.getMessage(), exception.getErrorCode());
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleException(final Exception exception){
        LOGGER.error(exception);
        return new ResponseEntity<>(INTERNAL_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleValidationException(final MethodArgumentNotValidException exception){
        LOGGER.error(exception);
        Map<String,String> errors = new HashMap<>();
        exception.getBindingResult()
                .getAllErrors()
                .forEach(error ->{
                    errors.put(((FieldError) error).getField(),error.getDefaultMessage());
                });
        return  ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleDateTimeValidationException(final DateTimeParseException exception){
        LOGGER.error(exception);
        return new ResponseEntity(
                "Invalid Date & Time format " + exception.getParsedString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleTypeMismatchException(
            final MethodArgumentTypeMismatchException exception
    ){
        LOGGER.error(exception);

        String message = "INTERNAL_ERROR";
        if (exception.getMostSpecificCause() instanceof NumberFormatException ||
                exception.getMostSpecificCause() instanceof DateTimeParseException) {
            message = formatException(exception);
        }

        ExceptionResponse response = new ExceptionResponse();
        response.setMessage(message);
        response.setStatus(500);
        response.setTimeStamp(new Date());
        return ResponseEntity.badRequest().body(response);
    }

    private String formatException(MethodArgumentTypeMismatchException exception) {
        String exceptionName = exception.getName();
        String userSpecifiedType = SINGLE_QUOTE + exception.getValue() + SINGLE_QUOTE;
        Class<?> requiredType = exception.getRequiredType();
        String expectedType = SINGLE_QUOTE
                + (requiredType == null ? "NO-TYPE" : requiredType.getSimpleName()) + SINGLE_QUOTE;

        return String.format("Type Mismatch for %s for given value %s, expected value of type %s",
                exceptionName, userSpecifiedType, expectedType);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleMethodNotSupportedException(final HttpRequestMethodNotSupportedException exception){
        LOGGER.error(exception);

        return new ResponseEntity<>(exception.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleJsonParseErrorException(final HttpMessageNotReadableException exception){
        LOGGER.error(exception);
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleConflictException(final ConversionFailedException exception){
        LOGGER.error(exception);
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<Object> handleMissingRequestParam(
            final UnsatisfiedServletRequestParameterException exception) {
        LOGGER.error(exception);
        return new ResponseEntity<>("Missing request parameter", HttpStatus.BAD_REQUEST);
    }
}
