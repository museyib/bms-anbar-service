package az.inci.bmsanbar.model.v4;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ArchiveDeliveryRequest {
    private String trxNo;
    private String driverCode;
}
