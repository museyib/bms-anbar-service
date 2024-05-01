package az.inci.bmsanbar.model.v3;

import lombok.Data;

@Data
public class UpdatePurchaseTrxRequestItem {
    private int trxId;
    private double qty;
}
