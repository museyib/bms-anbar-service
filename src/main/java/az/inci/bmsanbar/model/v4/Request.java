package az.inci.bmsanbar.model.v4;

import lombok.Data;

@Data
public class Request<T> {
    private String userId;
    private String deviceId;
    private T data;
}
