package az.inci.bmsanbar.model.v4;

import lombok.Data;

import java.util.List;

@Data
public class InternalUseRequest {
    private String userId;
    private String whsCode;
    private String expCenterCode;
    private String trxNo;
    private String notes;
    private List<InternalUseRequestItem> requestItems;
}
