/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;
import Service.TransactionDAO;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import Model.Table2;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import Model.ProductDataSet;
import Model.TransactionDataSet;
import java.util.concurrent.atomic.AtomicInteger;
import Model.VoucherDataSet;
import java.util.Arrays;
import Model.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import Model.objectToInt;
import java.text.SimpleDateFormat;
import org.json.*;
/**
 *
 * @author maith
 */
public class CreateTransaction extends javax.swing.JFrame {

    /**
     * Creates new form CreateTransaction
     */
    TransactionDAO transactDAO = new TransactionDAO();
    DefaultTableModel Model_1;
    DefaultTableModel Model_2;
    ArrayList<Table2> Table2Rows = new ArrayList();
    public CreateTransaction() throws SQLException {
        initComponents();
        
        Model_1 = (DefaultTableModel) jTable1.getModel();
        Model_2 = (DefaultTableModel) jTable2.getModel();
        loadTable_1();
    }
    public void loadTable_1() throws SQLException {
        ResultSet rs = transactDAO.getProducts();
        Model_1.setRowCount(1);
        while(rs.next()) { 
            Model_1.addRow(new Object[]{
                String.valueOf(rs.getInt("product_id")),
                rs.getString("product_name"),
                rs.getFloat("product_price")
            });
        }
    }
    public void loadTable_2(ArrayList<Table2> rows) {
        Model_2.setRowCount(1);
        rows.forEach(row -> {
            Model_2.addRow(new Object[]{
                row.id,
                row.product,
                row.price,
                row.quantity
            });
        });
    }
    public String requestRecipt() throws JsonProcessingException, SQLException {
        ArrayList<Integer> productIds = new ArrayList();
        ArrayList quantities = new ArrayList();
        ArrayList<ProductDataSet> products = new ArrayList();
        
        for(int i = 1; i < jTable2.getRowCount(); i++) {
            productIds.add(Integer.parseInt(jTable2.getValueAt(i, 0).toString()));
        }
        for(int i = 1; i < jTable2.getRowCount(); i++) {
            quantities.add(jTable2.getValueAt(i, 3));
        }
        
        AtomicInteger index = new AtomicInteger();
        productIds.forEach(productId -> {
            try {
                ResultSet rs = transactDAO.selectProductById(Integer.parseInt(productId.toString()));
                rs.next();
                products.add(new ProductDataSet(rs.getInt("product_id"),
                                                rs.getString("product_name"),
                                                rs.getFloat("product_price"),
                                                Integer.parseInt(quantities.get(index.getAndIncrement()).toString())
                                                ));
            } catch (SQLException ex) {
                Logger.getLogger(CreateTransaction.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
        int[] ids = objectToInt.objectToInteger(productIds);
        ArrayList<VoucherDataSet> vouchers = new ArrayList();
        try {
            JSONArray jo = JSON.parseJSON(transactDAO.searchVoucher(ids)).getJSONArray("vouchers");
            for(int i = 0; i < jo.length(); i++) {
//                VoucherDataSet ds = new VoucherDataSet(
//                                                        new JSONObject(jo.get(i)).getInt("id"),
//                                                        new java.sql.Date(new JSONObject(jo.get(i)).getInt("startDate")),
//                                                        new java.sql.Date(new JSONObject(jo.get(i)).getInt("endDate")),
//                                                        new JSONObject(jo.get(i)).getInt("productId"),
//                                                        new JSONObject(jo.get(i)).getFloat("newPrice")
//                        
//                                                    );
                //vouchers.add(jo.get(i));
            }
//            vouchers = new ArrayList(
//                JSON.parseJSON(transactDAO.searchVoucher(ids)).getJSONArray("vouchers")
//            );
        } catch(SQLException ex) {
            System.out.println("khong co voucher");
        }
        
        java.util.Date currentTime = new java.util.Date();
        java.sql.Timestamp currentTimeSql = new java.sql.Timestamp(currentTime.getTime());
        
        
        TransactionDataSet transaction = new TransactionDataSet(
                                                                currentTimeSql,
                                                                products,
                                                                vouchers
                                                                );
//        java.util.Date test = new java.util.Date(currentTimeSql.getTime());
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
//        System.out.println(sdf.format(test));
        return JSON.StringifyJSON(transaction);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jTextField1 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jTextField2 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Sản phẩm", "Giá"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTable1);

        jScrollPane1.setViewportView(jScrollPane4);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Sản phẩm", "Giá", "Số lượng"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(jTable2);

        jScrollPane2.setViewportView(jScrollPane5);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jTextArea1.setText("HOÁ ĐƠN:");
        jScrollPane3.setViewportView(jTextArea1);

        jLabel1.setText("Sản phẩm");

        jLabel2.setText("Đơn");

        jLabel3.setText("Hội viên");

        jButton1.setText("Submit");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Thêm");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Xoá");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel4.setText("Code");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton3)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(22, 22, 22)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel1)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 666, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton1)
                                    .addComponent(jButton2))))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addGap(23, 23, 23)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel2)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane2)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel4))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 328, javax.swing.GroupLayout.PREFERRED_SIZE)))))))
                .addContainerGap(32, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addGap(21, 21, 21))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))))
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTable1.getSelectedRow();
        if(selectedRow >= 1) {
            ArrayList products = new ArrayList();
            for(int i = 1; i < jTable2.getRowCount(); i++) {
                products.add(jTable2.getValueAt(i, 0));
            }
            
            try {
                if(products.contains(jTable1.getValueAt(selectedRow, 0).toString())) {
                    JOptionPane.showMessageDialog(rootPane, "Da co san pham");
                    throw new Exception("product was already added");
                }
                ResultSet rs = transactDAO.selectProductById(Integer.parseInt(jTable1.getValueAt(selectedRow, 0).toString()));
                rs.next();
                Table2Rows.add(new Table2(String.valueOf(rs.getInt("product_id")),
                                        rs.getString("product_name"),
                                        String.valueOf(rs.getFloat("product_price")),
                                        "1"
                                        ));
                loadTable_2(Table2Rows);
            } catch (SQLException ex) {
                Logger.getLogger(CreateTransaction.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CreateTransaction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTable2.getSelectedRow();
        if(selectedRow >= 1) {
            Table2Rows.remove(selectedRow - 1);
            loadTable_2(Table2Rows);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            // TODO add your handling code here:
            System.out.println(requestRecipt());
        } catch (JsonProcessingException | SQLException ex) {
            Logger.getLogger(CreateTransaction.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(CreateTransaction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(CreateTransaction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(CreateTransaction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(CreateTransaction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new CreateTransaction().setVisible(true);
                } catch (SQLException ex) {
                    Logger.getLogger(CreateTransaction.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
