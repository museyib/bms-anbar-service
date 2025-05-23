package az.inci.wms.model.v4;

import lombok.Data;

import java.util.List;

@Data
public class ProductApproveRequest {
    private String trxNo;
    private String trxDate;
    private String notes;
    private int status;
    private String userId;
    private List<ProductApproveRequestItem> requestItems;
}
