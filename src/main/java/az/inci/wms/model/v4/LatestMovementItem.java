package az.inci.wms.model.v4;

import lombok.Data;

@Data
public class LatestMovementItem {
    private String trxNo;
    private String trxDate;
    private double quantity;
}
