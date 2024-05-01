/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package az.inci.bmsanbar.model;

import lombok.Data;

import java.util.List;

/**
 * @author User
 */
@Data
public class PickDoc
{
    private String trxNo;
    private String trxDate;
    private String description;
    private String notes;
    private String pickArea;
    private int itemCount;
    private int pickedItemCount;
    private String prevTrxNo;
    private String bpName;
    private String sbeName;
    private String bpCode;
    private String sbeCode;
    private String approveUser;
    private String pickUser;
    private int trxTypeId;
    private double amount;
    private String srcWhsCode;
    private String srcWhsName;
    private String whsCode;
    private List<PickTrx> trxList;
}
