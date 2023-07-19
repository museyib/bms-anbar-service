package az.inci.bmsanbar.model.v2;

import lombok.Data;

import java.util.List;

@Data
public class TransferRequest
{
    private String srcWhsCode;
    private String trgWhsCode;
    private String userId;
    private List<TransferRequestItem> requestItems;
}
