/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Service.StaffDAO;
import javax.swing.table.DefaultTableModel;
import java.sql.*;

/**
 *
 * @author ADMIN
 */
public class Staff extends javax.swing.JPanel {

    DefaultTableModel model;
    public StaffDAO nhanvienDAO = new StaffDAO();

    public Staff() {
        initComponents();
        model = (DefaultTableModel) tbnhanvien.getModel();
        model.setRowCount(0); // Khởi tạo JTable rỗng
        loadDataToTable();
        txtmanhanvien.setVisible(false);
        jLabel2.setVisible(false);
        btxoa.setVisible(false);
        btload.setVisible(false);
    }

    private void loadDataToTable() {
        model.setRowCount(0); // Xóa dữ liệu cũ
        ResultSet rs = nhanvienDAO.loaddata();
        try {
            if (rs != null) {
                while (rs.next()) {
                    model.addRow(new Object[]{
                        rs.getInt("staff_id"),
                        rs.getInt("age"),
                        rs.getString("email"),
                        rs.getString("phone")
                    });
                }
                rs.close(); // Đóng ResultSet
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Không thể tải dữ liệu!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            javax.swing.JOptionPane.showMessageDialog(this, "Lỗi tải dữ liệu: " + e.getMessage());
        }
    }

    private void clearFields() {
        txtmanhanvien.setText("");
        txttuoi.setText("");
        txtemail.setText("");
        txtphone.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtmanhanvien = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txttuoi = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtemail = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        tbnhanvien = new javax.swing.JTable();
        jLabel5 = new javax.swing.JLabel();
        txtphone = new javax.swing.JTextField();
        btthem = new javax.swing.JToggleButton();
        btxoa = new javax.swing.JToggleButton();
        btsua = new javax.swing.JToggleButton();
        btload = new javax.swing.JButton();

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 51, 51));
        jLabel1.setText("quản lý nhân viên");

        jLabel2.setText("nhập id:");

        txtmanhanvien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtmanhanvienActionPerformed(evt);
            }
        });

        jLabel3.setText("nhập tuổi:");

        txttuoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txttuoiActionPerformed(evt);
            }
        });

        jLabel4.setText("nhập email:");

        txtemail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtemailActionPerformed(evt);
            }
        });

        tbnhanvien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "id", "tuổi", "email", "phone"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tbnhanvien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbnhanvienMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tbnhanvien);

        jScrollPane3.setViewportView(jScrollPane2);

        jLabel5.setText("nhập phone:");

        txtphone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtphoneActionPerformed(evt);
            }
        });

        btthem.setText("thêm");
        btthem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btthemActionPerformed(evt);
            }
        });

        btxoa.setText("xóa");
        btxoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btxoaActionPerformed(evt);
            }
        });

        btsua.setText("sửa");
        btsua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btsuaActionPerformed(evt);
            }
        });

        btload.setText("load");
        btload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btloadActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(95, 95, 95)
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 531, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btload))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtphone, javax.swing.GroupLayout.DEFAULT_SIZE, 114, Short.MAX_VALUE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(txtemail))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtmanhanvien)
                                    .addComponent(txttuoi, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btthem, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btxoa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btsua, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(91, 91, 91))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtmanhanvien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addComponent(btsua)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btthem)))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txttuoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(3, 3, 3)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(btxoa))
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(txtemail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btload)
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtphone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(65, 65, 65)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txttuoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txttuoiActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txttuoiActionPerformed

    private void txtemailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtemailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtemailActionPerformed

    private void txtphoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtphoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtphoneActionPerformed

    private void btthemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btthemActionPerformed
        try {
            int age = Integer.parseInt(txttuoi.getText().trim());
            String email = txtemail.getText().trim();
            String phone = txtphone.getText().trim();
            if (email.length() > 100 || phone.length() > 10) {
                javax.swing.JOptionPane.showMessageDialog(this, "Email hoặc phone quá dài!");
                return;
            }
            boolean added = nhanvienDAO.addStaff(age, email, phone);
            if (added) {
                javax.swing.JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
                loadDataToTable(); // Tải lại JTable
                clearFields();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Thêm nhân viên thất bại!");
            }
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Tuổi phải là số nguyên!");
        }    }//GEN-LAST:event_btthemActionPerformed

    private void btsuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btsuaActionPerformed
        try {
            int staffId = Integer.parseInt(txtmanhanvien.getText().trim());
            int age = Integer.parseInt(txttuoi.getText().trim());
            String email = txtemail.getText().trim();
            String phone = txtphone.getText().trim();
            if (email.length() > 100 || phone.length() > 10) {
                javax.swing.JOptionPane.showMessageDialog(this, "Email hoặc phone quá dài!");
                return;
            }
            boolean updated = nhanvienDAO.updateStaff(staffId, age, email, phone);
            if (updated) {
                javax.swing.JOptionPane.showMessageDialog(this, "Sửa nhân viên thành công!");
                loadDataToTable(); // Tải lại JTable
                clearFields();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Sửa nhân viên thất bại!");
            }
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "ID và tuổi phải là số nguyên!");

        }    }//GEN-LAST:event_btsuaActionPerformed

    private void btloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btloadActionPerformed
        loadDataToTable();
        javax.swing.JOptionPane.showMessageDialog(this, "Tải dữ liệu thành công!");    }//GEN-LAST:event_btloadActionPerformed

    private void tbnhanvienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbnhanvienMouseClicked
        int selectedRow = tbnhanvien.getSelectedRow();
        if (selectedRow >= 0) {
            txtmanhanvien.setText(model.getValueAt(selectedRow, 0).toString());
            txttuoi.setText(model.getValueAt(selectedRow, 1).toString());
            txtemail.setText(model.getValueAt(selectedRow, 2).toString());
            txtphone.setText(model.getValueAt(selectedRow, 3).toString());
        }
    }//GEN-LAST:event_tbnhanvienMouseClicked

    private void txtmanhanvienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtmanhanvienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtmanhanvienActionPerformed

    private void btxoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btxoaActionPerformed
//        try {
//            int staffId = Integer.parseInt(txtmanhanvien.getText().trim());
//            boolean deleted = nhanvienDAO.deleteStaff(staffId);
//            if (deleted) {
//                javax.swing.JOptionPane.showMessageDialog(this, "Xóa nhân viên thành công!");
//                loadDataToTable(); // Tải lại JTable
//                clearFields();
//            } else {
//                javax.swing.JOptionPane.showMessageDialog(this, "Xóa nhân viên thất bại!");
//            }
//        } catch (NumberFormatException e) {
//            javax.swing.JOptionPane.showMessageDialog(this, "ID phải là số nguyên!");
//        }
    }//GEN-LAST:event_btxoaActionPerformed

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
//            java.util.logging.Logger.getLogger(Staff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Staff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Staff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Staff.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Staff().setVisible(true);
//            }
//        });
//    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btload;
    private javax.swing.JToggleButton btsua;
    private javax.swing.JToggleButton btthem;
    private javax.swing.JToggleButton btxoa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tbnhanvien;
    private javax.swing.JTextField txtemail;
    private javax.swing.JTextField txtmanhanvien;
    private javax.swing.JTextField txtphone;
    private javax.swing.JTextField txttuoi;
    // End of variables declaration//GEN-END:variables
}
