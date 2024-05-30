package marketshelfs.detection.beans;

import lombok.Data;

import java.util.Date;

@Data
public class ExceptionResponse {

    private Date timeStamp;
    private int status;
    private Object message;

    public ExceptionResponse() {
        super();
    }

    public ExceptionResponse(Date timestamp, int status, Object message) {
        this.timeStamp = timestamp;
        this.status = status;
        this.message = message;
    }

    public Date getTimestamp() {
        return timeStamp;
    }

    public int getStatus() {
        return status;
    }

    public Object getMessage() {
        return message;
    }
}