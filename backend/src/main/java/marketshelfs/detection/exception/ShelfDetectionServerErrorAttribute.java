package marketshelfs.detection.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class ShelfDetectionServerErrorAttribute extends DefaultErrorAttributes {

    public ShelfDetectionServerErrorAttribute()  {
        super();
    }

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        final Map<String,Object> defaultErrorAttributes = super.getErrorAttributes(webRequest,ErrorAttributeOptions.defaults());
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        errorAttributes.put("timestamp",new Date());
        errorAttributes.put("status",defaultErrorAttributes.get("status"));
        Object message = defaultErrorAttributes.get("message");
        message = "No message available".equals(message)
                ? defaultErrorAttributes.get("error")
                : message;
        errorAttributes.put("message",message);
        return errorAttributes;
    }
}
