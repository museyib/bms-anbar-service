package az.inci.bmsanbar.model.v2;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateDeliveryRequestItem
{
    private String trxNo;
    private String note;
    private String deliverPerson;
    private String driverCode;
}
