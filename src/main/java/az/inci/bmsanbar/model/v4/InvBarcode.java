/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package az.inci.bmsanbar.model.v4;

import lombok.Data;

/**
 * @author User
 */
@Data
public class InvBarcode {

    private String invCode;
    private String barcode;
    private String uom;
    private double uomFactor;
    private boolean defined;
}
