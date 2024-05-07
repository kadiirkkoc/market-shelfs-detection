package marketshelfs.detection.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class ShelfDetectionServerException extends RuntimeException{

    @Serial
    private static final long serialVersionUID = 45487L;

    private HttpStatus errorCode;

    public ShelfDetectionServerException(){
        super("Unexpected Exception Encountered");
    }

    public ShelfDetectionServerException(String message, HttpStatus errorCode){
        super(message);
        this.errorCode=errorCode;
    }

    public HttpStatus getErrorCode() {
        return errorCode;
    }
}
