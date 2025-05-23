package az.inci.wms.model.v4;

import lombok.Data;

@Data
public class CheckShipmentResponse {
    private String driverCode;
    private String driverName;
    private boolean shipped;
}
