package az.inci.bmsanbar.model.v4;

import lombok.Data;

@Data
public class UpdateDocLocationRequest {
    private String latitude;
    private String longitude;
    private String address;
    private String userId;
}
