package az.inci.bmsanbar.model.v2;

import az.inci.bmsanbar.model.v3.ShipmentRequestItem;
import lombok.Data;

import java.util.List;

@Data
public class ShipmentRequest
{
    private String regionCode;
    private String driverCode;
    private String srcTrxNo;
    private String vehicleCode;
    private String userId;
    private String shipStatus;
    private List<ShipmentRequestItem> requestItems;
}
