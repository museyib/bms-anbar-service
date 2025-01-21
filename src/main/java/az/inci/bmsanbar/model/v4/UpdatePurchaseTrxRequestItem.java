package az.inci.bmsanbar.model.v4;

import lombok.Data;

@Data
public class UpdatePurchaseTrxRequestItem {
    private int trxId;
    private double qty;
}
