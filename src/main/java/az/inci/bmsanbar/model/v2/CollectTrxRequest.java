package az.inci.bmsanbar.model.v2;

import lombok.Data;

@Data
public class CollectTrxRequest
{
    private int trxId;
    private double qty;
    private String pickStatus;
    private int seconds;
    private String notPickedReasonId;
}
