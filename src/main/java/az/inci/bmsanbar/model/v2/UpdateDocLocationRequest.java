package az.inci.bmsanbar.model.v2;

import lombok.Data;

@Data
public class UpdateDocLocationRequest
{
    private String latitude;
    private String longitude;
    private String address;
    private String userId;
}
