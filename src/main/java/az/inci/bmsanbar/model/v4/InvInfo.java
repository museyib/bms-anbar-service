package az.inci.bmsanbar.model.v4;

import lombok.Data;

@Data
public class InvInfo {
    private String invCode;
    private String invName;
    private String defaultUomCode;
    private double whsQty;
    private String info;
    private String whsCode;
}
