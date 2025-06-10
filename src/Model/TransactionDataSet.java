/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;
import java.sql.Timestamp;
import java.util.ArrayList;
/**
 *
 * @author maith
 */
public class TransactionDataSet {
    public int reciptId;
    public Timestamp reciptDate;
    public ArrayList<ProductDataSet> products;
    public ArrayList<VoucherDataSet> vouchers;
    public MembershipDataSet membership;

    public TransactionDataSet(int reciptId, Timestamp reciptDate, ArrayList<ProductDataSet> products, ArrayList<VoucherDataSet> vouchers, MembershipDataSet membership) {
        this.reciptId = reciptId;
        this.reciptDate = reciptDate;
        this.products = products;
        this.vouchers = vouchers;
        this.membership = membership;
    }

    public TransactionDataSet(Timestamp reciptDate, ArrayList<ProductDataSet> products, ArrayList<VoucherDataSet> vouchers, MembershipDataSet membership) {
        this.reciptDate = reciptDate;
        this.products = products;
        this.vouchers = vouchers;
        this.membership = membership;
    }

    public TransactionDataSet(Timestamp reciptDate, ArrayList<ProductDataSet> products, ArrayList<VoucherDataSet> vouchers) {
        this.reciptDate = reciptDate;
        this.products = products;
        this.vouchers = vouchers;
    }
    
}
