package az.inci.bmsanbar.model.v4;

import lombok.Data;

import java.util.List;

@Data
public class UpdateDeliveryRequest {
    private String status;
    private List<UpdateDeliveryRequestItem> requestItems;
}
