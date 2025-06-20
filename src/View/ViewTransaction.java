/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Model.JSON;
import Model.MembershipDataSet;
import Model.ProductDataSet;
import Model.TransactionDataSet;
import Model.VoucherDataSet;
import Service.ViewTransactionDAO;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author maith
 */
public class ViewTransaction extends javax.swing.JPanel {

    ViewTransactionDAO vtdao = new ViewTransactionDAO();

    /**
     * Creates new form NewJFrame
     */
    DefaultTableModel model;

    public ViewTransaction() {
        initComponents();
        model = (DefaultTableModel) tbldoanhthu.getModel();
        loaddata(vtdao.Loaddata());
    }

    public void loaddata(ResultSet rs) {
        model.setRowCount(0);
        
        ArrayList<TransactionDataSet> transactions = new ArrayList();
        ArrayList<ProductDataSet> products = new ArrayList();
        ArrayList<VoucherDataSet> vouchers = new ArrayList();
        try {
            while (rs.next()) {
                JSONArray jaProducts = new JSONArray(rs.getString("products"));
                JSONArray jaVouchers = new JSONArray(rs.getString("vouchers"));
                System.out.println(jaProducts);
                //jaProducts.forEach(object -> {
                for (int i = 0; i < jaProducts.length(); i++) {
                    JSONObject jo = JSON.parseJSON(jaProducts.get(i).toString());
                    System.out.println(jo);
                    products.add(new ProductDataSet(
                            jo.getInt("productId"),
                            jo.getString("productName"),
                            jo.getFloat("productPrice"),
                            jo.getInt("quantity")
                    ));
                }

                //});
                //jaVouchers.forEach(object -> {
                for (int i = 0; i < jaVouchers.length(); i++) {
                    JSONObject jo = JSON.parseJSON(jaVouchers.get(i).toString());
                    vouchers.add(new VoucherDataSet(
                            jo.getInt("id"),
                            new java.sql.Date(jo.getLong("startDate")),
                            new java.sql.Date(jo.getLong("endDate")),
                            jo.getInt("productId"),
                            jo.getFloat("newPrice")
                    ));
                }

                //});
                MembershipDataSet mds = new MembershipDataSet();
                float membershipDiscount = 0;
                try {
                    mds = vtdao.getMembershipById(rs.getInt("membership_id"));
                    membershipDiscount = mds.discount;

                } catch (Exception ex) {
                }

                TransactionDataSet transaction = new TransactionDataSet(
                        rs.getTimestamp("recipt_date"),
                        products,
                        vouchers,
                        mds,
                        rs.getInt("staff_id")
                );
                float total = 0;
                float totalAfterVoucher = 0;
                JSONObject jo = new JSONObject();
                //ArrayList<Integer> voucherProductId = new ArrayList();
                transaction.vouchers.forEach(voucher -> {
                    //voucherProductId.add(voucher.productId);
                    jo.put(String.valueOf(voucher.productId), voucher.newPrice);
                });

                for (ProductDataSet product : transaction.products) {
                    float productPrice = product.productPrice;
//            if(voucherProductId.contains(product.productId)) {
//                productPrice = 
//            }
                    for (String key : jo.keySet()) {
                        if (key.equals(String.valueOf(product.productId))) {
                            productPrice = Float.parseFloat(jo.get(key).toString());
                        }
                    }
                    float productTotal = product.productPrice * product.quantity;
                    float productTotalAfterVoucher = productPrice * product.quantity;
                    total += productTotal;
                    totalAfterVoucher += productTotalAfterVoucher;
                }

                totalAfterVoucher = (totalAfterVoucher / 100) * (100 - membershipDiscount);
                model.addRow(new Object[]{
                    rs.getInt("recipt_id"),
                    rs.getTimestamp("recipt_date").toString(),
                    rs.getString("products"),
                    rs.getString("vouchers"),
                    rs.getInt("membership_id"),
                    rs.getInt("staff_id"),
                    (total - totalAfterVoucher) * -1,
                    totalAfterVoucher
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtstart = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtend = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbldoanhthu = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        jLabel1.setText("Ngày bắt đầu:");

        jLabel2.setText("Ngày Kết thúc:");

        tbldoanhthu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã Hóa đơn", "Ngày xuất hóa đơn", "Products", "Vouchers", "Mã hội viên", "Nhân viên tạo", "Chiết khấu", "Thành tiền"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Float.class, java.lang.Float.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tbldoanhthu);

        jButton1.setText("Xem Doanh Thu");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Reset ");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(txtstart))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtend, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(176, 176, 176)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(151, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(txtstart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(58, 58, 58)
                        .addComponent(jButton1)))
                .addGap(18, 18, 18)
                .addComponent(jButton2)
                .addGap(22, 22, 22)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 238, Short.MAX_VALUE))
        );

    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        model.setRowCount(0);
        String startdate = txtstart.getText();
        String endddate = txtend.getText();

        SimpleDateFormat spdf = new SimpleDateFormat("yyyy-MM-dd");
        ResultSet rs = vtdao.fillter(startdate, endddate);
        ResultSet rs2 = vtdao.Loaddata();
        try {
            if (startdate.isEmpty() && endddate.isEmpty()) {
                loaddata(vtdao.Loaddata());
            } else {
                loaddata(vtdao.fillter(startdate, endddate));
                JOptionPane.showMessageDialog(this, "Loc thanh cong");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Loc khong thanh cong");
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        txtend.setText("");
        txtstart.setText("");
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(ViewTransaction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(ViewTransaction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(ViewTransaction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(ViewTransaction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new ViewTransaction().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tbldoanhthu;
    private javax.swing.JTextField txtend;
    private javax.swing.JTextField txtstart;
    // End of variables declaration//GEN-END:variables
}
