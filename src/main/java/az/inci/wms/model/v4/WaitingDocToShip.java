package az.inci.wms.model.v4;

import lombok.Data;

@Data
public class WaitingDocToShip {
    private String trxNo;
    private String trxDate;
    private String whsCode;
    private String bpCode;
    private String bpName;
    private String sbeCode;
    private String sbeName;
}
