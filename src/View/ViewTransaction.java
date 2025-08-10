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
import Service.dbConnection;
import com.toedter.calendar.JTextFieldDateEditor;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;
import javax.swing.JTextField;

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
    private JLabel jLabel3;

    public ViewTransaction() {
        initComponents();
        model = (DefaultTableModel) tbldoanhthu.getModel();
        loaddata(vtdao.Loaddata());
        JTextFieldDateEditor editorStart = (JTextFieldDateEditor) jdtstart.getDateEditor();
        JTextFieldDateEditor editorEnd = (JTextFieldDateEditor) jdtend.getDateEditor();
        editorStart.setEditable(false);
        editorEnd.setEditable(false);
    }

 public void loaddata(ResultSet rs) {
   model.setRowCount(0);

    try {
        int rowCount = 0;
        while (rs.next()) {
            String productIds = rs.getString("product_ids");
            String voucherIds = rs.getString("voucher_ids");

            System.out.println("Debug - Processing row: receipt_id = " + rs.getInt("receipt_id") + 
                             ", product_ids = [" + productIds + "], voucher_ids = [" + voucherIds + "]");

            MembershipDataSet mds = new MembershipDataSet();
            float membershipDiscount = 0;
            try {
                mds = vtdao.getMembershipById(rs.getInt("membership_id"));
                membershipDiscount = mds.discount;
            } catch (Exception ex) {}

            float total = 0;
            float totalAfterVoucher = 0;

            if (!productIds.equals("0")) {
                String[] productIdArray = productIds.split(", ");
                for (String productId : productIdArray) {
                    int prodId = Integer.parseInt(productId.trim());
                    float productPrice = getProductPrice(prodId);
                    float voucherPrice = getVoucherPrice(prodId, voucherIds);
                    total += productPrice;
                    totalAfterVoucher += (voucherPrice != -1 ? voucherPrice : productPrice);
                }
            }

            totalAfterVoucher = (totalAfterVoucher / 100) * (100 - membershipDiscount);

            model.addRow(new Object[]{
                rs.getInt("receipt_id"),
                rs.getTimestamp("receipt_date").toString(),
                productIds,
                voucherIds,
                rs.getInt("membership_id"),
                rs.getInt("staff_id"),
                (total - totalAfterVoucher) * -1,
                totalAfterVoucher
            });
            rowCount++;
        }
        System.out.println("Debug - Total rows loaded: " + rowCount);
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Debug - Error in loaddata: " + e.getMessage());
    }
}

// Phương thức giả để lấy giá sản phẩm (cần triển khai thực tế)
private float getProductPrice(int productId) {
    // Truy vấn bảng product để lấy product_price
    try {
        Connection conn = dbConnection.connect();
        String query = "SELECT product_price FROM product WHERE product_id = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setInt(1, productId);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            return rs.getFloat("product_price");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return 0;
}

// Phương thức giả để lấy giá sau voucher (cần triển khai thực tế)
private float getVoucherPrice(int productId, String voucherIds) {
    // Nếu không có voucherIds, trả về -1 ngay lập tức
    if (voucherIds == null || voucherIds.trim().isEmpty() || voucherIds.equals("0")) {
        return -1;
    }

    try (Connection conn = dbConnection.connect();
         PreparedStatement ps = conn.prepareStatement(
             "SELECT new_product_price FROM voucher WHERE product_id = ? AND voucher_id IN (SELECT value FROM STRING_SPLIT(?, ','))")) {
        
        ps.setInt(1, productId);
        ps.setString(2, voucherIds);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getFloat("new_product_price");
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return -1;
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
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tbldoanhthu = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jdtstart = new com.toedter.calendar.JDateChooser();
        jdtend = new com.toedter.calendar.JDateChooser();
        jLabel3 = new javax.swing.JLabel();

        setBackground(java.awt.SystemColor.info);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(153, 102, 0));
        jLabel1.setText("Ngày Bắt Đầu:");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 102, 0));
        jLabel2.setText("Ngày Kết Thúc:");

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

        jButton1.setBackground(new java.awt.Color(153, 102, 0));
        jButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton1.setForeground(java.awt.SystemColor.info);
        jButton1.setText("Xem Doanh Thu");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(153, 102, 0));
        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jButton2.setForeground(java.awt.SystemColor.info);
        jButton2.setText("Reset ");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jdtstart.setDateFormatString("yyyy-MM-dd");
        ((JTextField) jdtstart.getDateEditor().getUiComponent()).setEditable(false);

        jdtend.setDateFormatString("yyyy-MM-dd");
        ((JTextField) jdtend.getDateEditor().getUiComponent()).setEditable(false);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 102, 0));
        jLabel3.setText("DOANH THU");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jdtend, javax.swing.GroupLayout.PREFERRED_SIZE, 429, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jdtstart, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 638, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(125, 125, 125))
            .addGroup(layout.createSequentialGroup()
                .addGap(372, 372, 372)
                .addComponent(jLabel3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel3)
                .addGap(44, 44, 44)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1)
                        .addComponent(jdtstart, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jdtend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)))
                .addGap(40, 173, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE)
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
         model.setRowCount(0);

    java.util.Date startdate = jdtstart.getDate();
    java.util.Date enddate = jdtend.getDate();

    // Validate rỗng
    if (startdate == null && enddate == null) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một ngày để lọc.");
        return;
    }

    // Nếu chỉ có 1 ngày thì gán cho ngày còn lại
    if (startdate == null) startdate = enddate;
    if (enddate == null) enddate = startdate;

    // Validate ngày kết thúc < ngày bắt đầu
    if (enddate.before(startdate)) {
        JOptionPane.showMessageDialog(this, "Ngày kết thúc không được trước ngày bắt đầu.");
        return;
    }

    // Format yyyy-MM-dd
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    String startStr = sdf.format(startdate);
    String endStr = sdf.format(enddate);

    try {
        ResultSet rs = vtdao.fillter(startStr, endStr);
        if (rs != null) {
            rs.last();
            boolean hasData = rs.getRow() > 0;
            rs.beforeFirst();

            if (hasData) {
                loaddata(rs);
            } else {
                JOptionPane.showMessageDialog(this, "Không có dữ liệu trong khoảng thời gian đã chọn.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Không có dữ liệu từ truy vấn lọc.");
        }
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(this, "Lọc không thành công: " + ex.getMessage());
    }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
         jdtstart.setDate(null);
    jdtend.setDate(null);
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
    private com.toedter.calendar.JDateChooser jdtend;
    private com.toedter.calendar.JDateChooser jdtstart;
    private javax.swing.JTable tbldoanhthu;
    // End of variables declaration//GEN-END:variables
}
