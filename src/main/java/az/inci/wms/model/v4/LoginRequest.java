package az.inci.wms.model.v4;

import lombok.Data;

@Data
public class LoginRequest {
    private String userId;
    private String password;
}
