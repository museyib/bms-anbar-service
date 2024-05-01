package az.inci.bmsanbar.model.v3;

import lombok.Data;

import java.util.List;
import java.util.Objects;

@Data
public class PurchaseTrx {
    private int trxId;
    private String trxNo;
    private String invCode;
    private String invName;
    private String whsCode;
    private double qty;
    private double countedQty;
    private String uom;
    private double uomFactor;
    private double price;
    private double amount;
    private List<String> barcodes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PurchaseTrx that = (PurchaseTrx) o;
        return trxId == that.trxId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(trxId);
    }
}
