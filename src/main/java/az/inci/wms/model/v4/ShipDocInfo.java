package az.inci.wms.model.v4;

import lombok.Data;

@Data
public class ShipDocInfo {
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
    private String trxId;
    private boolean confirmFlag;
    private boolean deliverFlag;
    private boolean archiveFlag;
}
