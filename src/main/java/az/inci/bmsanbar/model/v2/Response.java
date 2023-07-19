package az.inci.bmsanbar.model.v2;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Response
{
    private int statusCode;
    private String systemMessage;
    private String developerMessage;
    private Object data;
}
