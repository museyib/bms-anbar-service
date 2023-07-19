package az.inci.bmsanbar.model;

import lombok.Data;

@Data
public class Inventory
{
    private String invCode;
    private String invName;
    private String invBrand;
    private String barcode;
    private double price;
    private String internalCount;
    private String defaultUomCode;
    private double whsQty;
}
