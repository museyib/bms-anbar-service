package az.inci.wms.model.v4;

import lombok.Data;

@Data
public class UpdateDocLocationRequest {
    private String latitude;
    private String longitude;
    private String address;
    private String userId;
}
