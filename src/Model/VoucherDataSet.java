/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

import java.sql.Date;
/**
 *
 * @author maith
 */
public class VoucherDataSet {
    public int id;
    public Date startDate;
    public Date endDate;
    public int productId;
    public float newPrice;

    public VoucherDataSet(int id, Date startDate, Date endDate, int productId, float newPrice) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.productId = productId;
        this.newPrice = newPrice;
    }

    public VoucherDataSet(Date endDate, int productId) {
        this.endDate = endDate;
        this.productId = productId;
    }
    
}
