package az.inci.wms.model.v4;

import lombok.Data;

@Data
public class InternalUseRequestItem {
    private String invCode;
    private String invName;
    private double qty;
    private String invBrand;
    private String barcode;
    private String notes;
}
