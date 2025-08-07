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
    public int staffId;
    public float total;
    public boolean usePoint;
    public float replenishPoint;
    public String paymentMethod;
    public String paymentState;
    public float amount;
    public String note;
    public int pointUsed;

    public TransactionDataSet(int reciptId, Timestamp reciptDate, ArrayList<ProductDataSet> products, ArrayList<VoucherDataSet> vouchers, MembershipDataSet membership) {
        this.reciptId = reciptId;
        this.reciptDate = reciptDate;
        this.products = products;
        this.vouchers = vouchers;
        this.membership = membership;
    }

    public TransactionDataSet(Timestamp reciptDate, ArrayList<ProductDataSet> products, ArrayList<VoucherDataSet> vouchers, MembershipDataSet membership, int staffId, float total, boolean usePoint, float replenishPoint, String paymentMethod, String paymentState, float amount, String note, int pointUsed) {
        this.reciptDate = reciptDate;
        this.products = products;
        this.vouchers = vouchers;
        this.membership = membership;
        this.staffId = staffId;
        this.total = total;
        this.usePoint = usePoint;
        this.replenishPoint = replenishPoint;
        this.paymentMethod = paymentMethod;
        this.paymentState = paymentState;
        this.amount = amount;
        this.note = note;
        this.pointUsed = pointUsed;
    }

    public TransactionDataSet(Timestamp reciptDate, ArrayList<ProductDataSet> products, ArrayList<VoucherDataSet> vouchers) {
        this.reciptDate = reciptDate;
        this.products = products;
        this.vouchers = vouchers;
    }

    public TransactionDataSet(int reciptId, Timestamp reciptDate, ArrayList<ProductDataSet> products, ArrayList<VoucherDataSet> vouchers, MembershipDataSet membership, int staffId, float total, boolean usePoint, float replenishPoint, String paymentMethod, String paymentState, float amount, String note, int pointUsed) {
        this.reciptId = reciptId;
        this.reciptDate = reciptDate;
        this.products = products;
        this.vouchers = vouchers;
        this.membership = membership;
        this.staffId = staffId;
        this.total = total;
        this.usePoint = usePoint;
        this.replenishPoint = replenishPoint;
        this.paymentMethod = paymentMethod;
        this.paymentState = paymentState;
        this.amount = amount;
        this.note = note;
        this.pointUsed = pointUsed;
    }
}
