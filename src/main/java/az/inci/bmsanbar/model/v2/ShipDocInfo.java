package az.inci.bmsanbar.model.v2;

import lombok.Data;

@Data
public class ShipDocInfo
{
    private String driverCode;
    private String driverName;
    private String vehicleCode;
    private String deliverNotes;
    private String shipStatus;
    private String shipStatusDescription;
    private String targetCode;
    private String targetName;
    private double longitude;
    private double latitude;
}
