package az.inci.bmsanbar.model.v2;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductApproveRequestItem
{
    private String invCode;
    private String invName;
    private String invBrand;
    private String barcode;
    private double qty;
}
