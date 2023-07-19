/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package az.inci.bmsanbar.model;

import lombok.Data;

import java.util.Objects;

@Data
public class Trx
{
    private int position;
    private int trxId;
    private String trxNo;
    private String trxDate;
    private String pickStatus;
    private String invCode;
    private String invName;
    private double qty;
    private double pickedQty;
    private double packedQty;
    private String whsCode;
    private String pickArea;
    private String pickGroup;
    private String pickUser;
    private String approveUser;
    private String uom;
    private double uomFactor;
    private String invBrand;
    private String bpName;
    private String sbeName;
    private String barcode;
    private String prevTrxNo;
    private String notes;
    private int priority;
    private int trxTypeId;
    private double amount;
    private double price;
    private double discountRatio;
    private double discount;
    private double prevQty;
    private double prevQtySum;
    private String prevTrxDate;
    private int prevTrxId;

    @Override
    public boolean equals(Object o)
    {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        Trx trx = (Trx) o;
        return trxId == trx.trxId;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(trxId);
    }
}
