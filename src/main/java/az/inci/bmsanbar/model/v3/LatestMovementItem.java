package az.inci.bmsanbar.model.v3;

import lombok.Data;

@Data
public class LatestMovementItem {
    private String trxNo;
    private String trxDate;
    private double quantity;
}
