/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Service.MembershipDAO;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import javax.swing.JOptionPane;
import javax.swing.*;

/**
 *
 * @author ADMIN
 */
public class Membership extends JPanel {
    MembershipDAO mmbs= new MembershipDAO();
    private LocalDate ngayhethan;
    private LocalDate ngayhientai;

    /**
     * Creates new form membership
     */
    DefaultTableModel model;
    public Membership() {
        initComponentsOverride();
        model = (DefaultTableModel) tblmembership.getModel();
        loaddata();
    }
    public void loaddata(){
        model.setRowCount(0);
        ResultSet rs = mmbs.hienthi();
        try {
            while(rs.next()){
            model.addRow(new Object[]{
            rs.getInt("membership_id"),
                rs.getString("membership_name"),
                rs.getString("phone"),
                rs.getTimestamp("expiration_date").toString(),
                rs.getString("rank_name")
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
    private void initComponentsOverride() {
        label = new javax.swing.JLabel();
        txtten = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtsdt = new javax.swing.JTextField();
        rank = new javax.swing.JLabel();
        btnnsignin = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblmembership = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        cbboxexpirday = new javax.swing.JComboBox<>();
        jComboBox1 = new javax.swing.JComboBox<>();

        label.setText("Tên");

        jLabel1.setText("Số Điện Thoại:");

        rank.setText("Hạng");

        btnnsignin.setText("Đăng Ký");
        btnnsignin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnsigninActionPerformed(evt);
            }
        });

        tblmembership.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Hội Viên Mã", "Tên Hội viên", "Số Điện Thoại Hội Viên", "Hết Hạn", "Hạng"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblmembership);

        jLabel2.setText("Thời Hạn");

        cbboxexpirday.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "3 tháng", "6 tháng", "12 tháng" }));
        cbboxexpirday.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbboxexpirdayActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dong", "Bac", "Kim cuong" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(rank)
                    .addComponent(jLabel1)
                    .addComponent(label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtten)
                    .addComponent(txtsdt)
                    .addComponent(cbboxexpirday, 0, 154, Short.MAX_VALUE)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(64, 64, 64)
                .addComponent(btnnsignin)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label)
                    .addComponent(txtten, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtsdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rank)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnnsignin))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbboxexpirday, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        label = new javax.swing.JLabel();
        txtten = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtsdt = new javax.swing.JTextField();
        rank = new javax.swing.JLabel();
        btnnsignin = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblmembership = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        cbboxexpirday = new javax.swing.JComboBox<>();
        jComboBox1 = new javax.swing.JComboBox<>();

        label.setText("Tên");

        jLabel1.setText("Số Điện Thoại:");

        rank.setText("Hạng");

        btnnsignin.setText("Đăng Ký");
        btnnsignin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnsigninActionPerformed(evt);
            }
        });

        tblmembership.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Hội Viên Mã", "Tên Hội viên", "Số Điện Thoại Hội Viên", "Hết Hạn", "Hạng"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane1.setViewportView(tblmembership);

        jLabel2.setText("Thời Hạn");

        cbboxexpirday.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "3 tháng", "6 tháng", "12 tháng" }));
        cbboxexpirday.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbboxexpirdayActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Dong", "Bac", "Kim cuong" }));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 671, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel2)
                    .addComponent(rank)
                    .addComponent(jLabel1)
                    .addComponent(label))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtten)
                    .addComponent(txtsdt)
                    .addComponent(cbboxexpirday, 0, 154, Short.MAX_VALUE)
                    .addComponent(jComboBox1, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(64, 64, 64)
                .addComponent(btnnsignin)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(46, 46, 46)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(label)
                    .addComponent(txtten, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtsdt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(rank)
                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnnsignin))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(cbboxexpirday, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void cbboxexpirdayActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbboxexpirdayActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbboxexpirdayActionPerformed

    private void btnnsigninActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnsigninActionPerformed
        // TODO add your handling code here:
       String ten = txtten.getText();
        String sdt = txtsdt.getText();
        ngayhientai = LocalDate.now();

if (ten.isEmpty() || sdt.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
    return;
}

    String thoiHanSelected = (String) cbboxexpirday.getSelectedItem();
    int monthsToAdd = Integer.parseInt(thoiHanSelected.split(" ")[0]);

    ngayhethan = ngayhientai.plusMonths(monthsToAdd);

    String securityCode = "MB" + String.format("%02d", (int)(Math.random() * 100));

    LocalDateTime localDateTime = ngayhethan.atStartOfDay();
    Timestamp expirationDate = Timestamp.valueOf(localDateTime.atZone(ZoneId.systemDefault()).toLocalDateTime());

    String rankName = (String) jComboBox1.getSelectedItem();

    MembershipDAO mmbs = new MembershipDAO();
    boolean isAdded = mmbs.add(ten, sdt, securityCode, rankName, expirationDate);

    if (isAdded) {
    JOptionPane.showMessageDialog(this, "Dang ky thanh cong");
    loaddata();
    } else {
    JOptionPane.showMessageDialog(this, "Dang ky that bai");
    }


    }//GEN-LAST:event_btnnsigninActionPerformed

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
//            java.util.logging.Logger.getLogger(Membership.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Membership.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Membership.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Membership.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Membership().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnnsignin;
    private javax.swing.JComboBox<String> cbboxexpirday;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel label;
    private javax.swing.JLabel rank;
    private javax.swing.JTable tblmembership;
    private javax.swing.JTextField txtsdt;
    private javax.swing.JTextField txtten;
    // End of variables declaration//GEN-END:variables
}
