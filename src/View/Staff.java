/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Service.StaffDAO;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Formatter;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import com.toedter.calendar.JDateChooser;
import com.toedter.calendar.JTextFieldDateEditor;
import java.text.ParseException;

/**
 *
 * @author ADMIN
 */
public class Staff extends javax.swing.JPanel {

    DefaultTableModel model;
    public StaffDAO nhanvienDAO = new StaffDAO();
    final String manager = "+admin";
    final String staff = "+membership +report +createTransaction +warehouseLog";
    JPasswordField passwordfield = new JPasswordField(20);

    public boolean isvalid(long age) {
        if (age > 16) {
            JOptionPane.showMessageDialog(this, "tuoi thanh cong");
            return true;
        }
        JOptionPane.showMessageDialog(this, "them tuoi khong thanh cong");
        return false;
    }
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

    public Staff() throws SQLException {
        initComponents();
        model = (DefaultTableModel) tbnhanvien.getModel();
        model.setRowCount(0); // Khởi tạo JTable rỗng
        loadDataToTable();
        txtmanhanvien.setVisible(false);
        jLabel2.setVisible(false);
        btxoa.setVisible(false);
        btload.setVisible(false);
        passwordfield.setBounds(280, 260, 100, 20);
        JTextFieldDateEditor editorTuoi = (JTextFieldDateEditor) txttuoi.getDateEditor();

        editorTuoi.setEditable(false);

    }

    private void loadDataToTable() throws SQLException {
        model.setRowCount(0); // Xóa dữ liệu cũ
        ResultSet rs = nhanvienDAO.loaddata();

        try {
            if (rs != null) {
                while (rs.next()) {
//                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                    Date ngaysinh = rs.getDate("ngaysinh");
//                    LocalDate ngaysinh1 = LocalDate.parse((CharSequence) ngaysinh, formatter);
//                    System.out.println(ngaysinh1);

                    String ngaysinh1 = sdf.format(ngaysinh);
                    model.addRow(new Object[]{
                        rs.getInt("staff_id"),
                        rs.getString("staff_name"),
                        ngaysinh1,
                        rs.getInt("age"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("identitycard")
                    });
                }
                rs.close();
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
        txttuoi.setDate(null);
        txtemail.setText("");
        txtphone.setText("");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtmanhanvien = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
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
        jLabel6 = new javax.swing.JLabel();
        txtten = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtpassword = new javax.swing.JTextField();
        jRadioButton1 = new javax.swing.JRadioButton();
        jRadioButton2 = new javax.swing.JRadioButton();
        jLabel8 = new javax.swing.JLabel();
        txtcccd = new javax.swing.JTextField();
        txttuoi = new com.toedter.calendar.JDateChooser();

        setBackground(java.awt.SystemColor.info);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(102, 0, 0));
        jLabel1.setText("QUẢN LÝ NHÂN VIÊN ");

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(153, 102, 0));
        jLabel2.setText("Nhập ID:");

        txtmanhanvien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtmanhanvienActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(153, 102, 0));
        jLabel3.setText("Ngày Sinh:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(153, 102, 0));
        jLabel4.setText("Nhập Email:");

        txtemail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtemailActionPerformed(evt);
            }
        });

        tbnhanvien.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Tên", "Ngày Sinh", "Tuổi", "Email", "Phone", "CCCD"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Long.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(153, 102, 0));
        jLabel5.setText("Nhập Phone:");

        txtphone.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtphoneActionPerformed(evt);
            }
        });

        btthem.setBackground(new java.awt.Color(153, 102, 0));
        btthem.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btthem.setForeground(java.awt.SystemColor.info);
        btthem.setText("Thêm");
        btthem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btthemActionPerformed(evt);
            }
        });

        btxoa.setBackground(new java.awt.Color(153, 102, 0));
        btxoa.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btxoa.setForeground(java.awt.SystemColor.info);
        btxoa.setText("Xóa");
        btxoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btxoaActionPerformed(evt);
            }
        });

        btsua.setBackground(new java.awt.Color(153, 102, 0));
        btsua.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btsua.setForeground(java.awt.SystemColor.info);
        btsua.setText("Sửa");
        btsua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btsuaActionPerformed(evt);
            }
        });

        btload.setBackground(new java.awt.Color(153, 102, 0));
        btload.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btload.setForeground(java.awt.SystemColor.info);
        btload.setText("Load");
        btload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btloadActionPerformed(evt);
            }
        });

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(153, 102, 0));
        jLabel6.setText("Nhập Tên:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(153, 102, 0));
        jLabel7.setText("Nhập Mật Khẩu:");

        txtpassword.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                txtpasswordInputMethodTextChanged(evt);
            }
        });

        buttonGroup1.add(jRadioButton1);
        jRadioButton1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jRadioButton1.setForeground(new java.awt.Color(153, 102, 0));
        jRadioButton1.setText("Quản Lý");

        buttonGroup1.add(jRadioButton2);
        jRadioButton2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jRadioButton2.setForeground(new java.awt.Color(153, 102, 0));
        jRadioButton2.setText("Nhân viên");
        jRadioButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton2ActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(153, 102, 0));
        jLabel8.setText("CCCD:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(413, 761, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(479, 479, 479))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel2))
                        .addGap(73, 73, 73)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtten)
                            .addComponent(txtmanhanvien)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel3))
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jRadioButton2)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButton1))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtpassword)
                                .addComponent(txttuoi, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE)))))
                .addGap(124, 124, 124)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel8))
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txtemail)
                            .addComponent(txtcccd, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(175, 175, 175))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btsua, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtphone))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(21, 21, 21)
                                .addComponent(btthem, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btxoa, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btload, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(306, 306, 306))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addGap(73, 73, 73)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtmanhanvien, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(45, 45, 45)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(txtten, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(55, 55, 55)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txttuoi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtpassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel8)
                                        .addComponent(txtcccd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGap(45, 45, 45)
                                    .addComponent(jLabel4))
                                .addGroup(layout.createSequentialGroup()
                                    .addGap(56, 56, 56)
                                    .addComponent(txtemail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(139, 139, 139))
                        .addGroup(layout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel5)
                                .addComponent(txtphone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(40, 40, 40)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(btsua, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btthem, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btxoa, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btload, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jRadioButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 73, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtemailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtemailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtemailActionPerformed

    private void txtphoneActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtphoneActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtphoneActionPerformed
 
    private void btthemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btthemActionPerformed
        try {
            java.util.Date date = txttuoi.getDate(); // Lấy đối tượng java.util.Date
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String ngaysinhStr = sdf.format(date);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate ngaysinh = LocalDate.parse(ngaysinhStr, formatter);

            LocalDate now = LocalDate.now();
            long tuoi = ChronoUnit.YEARS.between(ngaysinh, now);

            String identitycard = txtcccd.getText().trim();
            String regex = "^[0-9]{12}$";
            String regexphone = "^[0-9]{9,10}$";
            String email = txtemail.getText().trim();
            String phone = txtphone.getText().trim();
            String name = txtten.getText().trim();
            String password = txtpassword.getText();
            
            for (int i = 0; i < tbnhanvien.getRowCount(); i++) {
                if (identitycard.equals(model.getValueAt(i, 6).toString())||phone.equals(model.getValueAt(i, 5))) {
                    JOptionPane.showMessageDialog(this, "Ten can cuoc cong dan hoac sdt ko duoc trung");
                    return;
                }
            }
            if (!phone.matches(regexphone)) {
                JOptionPane.showMessageDialog(this, "sdt khong hop le");
                return;
            }
            if (!identitycard.matches(regex)) {
                JOptionPane.showMessageDialog(this, "cccd khong hop le");
                return;
            }
            if (tuoi < 16) {
                JOptionPane.showMessageDialog(this, "Tuoi khong hop le");
                return;
            }
            if (email.length() > 100 || phone.length() > 10) {
                javax.swing.JOptionPane.showMessageDialog(this, "Email hoặc phone quá dài!");
                return;
            }
            String args = "";
            if (jRadioButton1.isSelected()) {
                args = manager;
            } else if (jRadioButton2.isSelected()) {
                args = staff;
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Khong de trong quyen");
                throw new Exception();
            }
            boolean added = nhanvienDAO.addStaff(name, tuoi, email, phone, args, password, identitycard, ngaysinh);
            if (added) {
                javax.swing.JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
                loadDataToTable(); // Tải lại JTable
                clearFields();
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Thêm nhân viên thất bại!");
            }
        } catch (Exception ex) {
            System.out.println(ex);
        }    }//GEN-LAST:event_btthemActionPerformed

    private void btsuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btsuaActionPerformed
        try {
            java.util.Date date = txttuoi.getDate(); // Lấy đối tượng java.util.Date
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            String ngaysinhStr = sdf.format(date);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            LocalDate ngaysinh = LocalDate.parse(ngaysinhStr, formatter);

            LocalDate now = LocalDate.now();
            int age = (int) ChronoUnit.YEARS.between(ngaysinh, now);

            int staffId = Integer.parseInt(txtmanhanvien.getText().trim());
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

        } catch (SQLException ex) {
            Logger.getLogger(Staff.class.getName()).log(Level.SEVERE, null, ex);
        }    }//GEN-LAST:event_btsuaActionPerformed

    private void btloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btloadActionPerformed
        try {
            loadDataToTable();
        } catch (SQLException ex) {
            Logger.getLogger(Staff.class.getName()).log(Level.SEVERE, null, ex);
        }
        javax.swing.JOptionPane.showMessageDialog(this, "Tải dữ liệu thành công!");    }//GEN-LAST:event_btloadActionPerformed

    private void tbnhanvienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbnhanvienMouseClicked
        int selectedRow = tbnhanvien.getSelectedRow();
        String dateString = model.getValueAt(selectedRow, 2).toString();
        if (selectedRow >= 0) {
            txtmanhanvien.setText(model.getValueAt(selectedRow, 0).toString());
            txtten.setText(model.getValueAt(selectedRow, 1).toString());
            try {
                java.util.Date date = sdf.parse(dateString);
                txttuoi.setDate(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            txtemail.setText(model.getValueAt(selectedRow, 4).toString());
            txtphone.setText(model.getValueAt(selectedRow, 5).toString());
            txtcccd.setText(model.getValueAt(selectedRow, 6).toString());
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

    private void txtpasswordInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_txtpasswordInputMethodTextChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_txtpasswordInputMethodTextChanged

    private void jRadioButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton2ActionPerformed

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
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JRadioButton jRadioButton1;
    private javax.swing.JRadioButton jRadioButton2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable tbnhanvien;
    private javax.swing.JTextField txtcccd;
    private javax.swing.JTextField txtemail;
    private javax.swing.JTextField txtmanhanvien;
    private javax.swing.JTextField txtpassword;
    private javax.swing.JTextField txtphone;
    private javax.swing.JTextField txtten;
    private com.toedter.calendar.JDateChooser txttuoi;
    // End of variables declaration//GEN-END:variables
}
