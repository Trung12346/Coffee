/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package View;

import Service.StaffDAO;
import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import java.util.concurrent.atomic.AtomicInteger;
import Model.objectToInt;
import Service.CongDAO;
import Service.dbConnection;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import javax.swing.JOptionPane;
import java.sql.Statement;
import java.time.Duration;
import com.toedter.calendar.JDateChooser;
import javax.swing.JTable;

/**
 *
 * @author maith
 */
public class ChamCongFixed extends javax.swing.JPanel {

    DefaultTableModel model;
    private StaffDAO stdao = new StaffDAO();
    private CongDAO cdao = new CongDAO();

    /**
     * Creates new form ChamCongFixed
     */
    public ChamCongFixed() throws SQLException {
        initComponents();
        model = (DefaultTableModel) tablecong.getModel();

        ArrayList<Integer> staffIds = stdao.getStaffIds();
        ArrayList<Integer> todayCongIds = cdao.getTodayCongIds();
        staffIds.forEach(id -> {
            if (!todayCongIds.contains(id)) {
                try {
                    cdao.addCong(id);
                } catch (SQLException ex) {
                    Logger.getLogger(ChamCong.class.getName()).log(Level.SEVERE, null, ex);
                    javax.swing.JOptionPane.showMessageDialog(null, "Lỗi khi thêm công: " + ex.getMessage(), "Lỗi", javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        displayData();
        loadTable_2();
    }

    private void displayData() throws SQLException {
        model.setRowCount(0);
        ResultSet rs = cdao.getTodayCong();
        try {
            while (rs.next()) {
                int showsUp = rs.getInt("shows_up");
                String trangThai;
                switch (showsUp) {
                    case 0 ->
                        trangThai = "Vắng";
                    case 1 ->
                        trangThai = "Đi muộn";
                    case 2 ->
                        trangThai = "Có mặt";
                    default ->
                        trangThai = "Không xác định";
                }

                Time showUpTime = rs.getTime("show_up_time");
                boolean checked = (showUpTime != null);

                model.addRow(new Object[]{
                    rs.getInt("cong_id"),
                    rs.getInt("staff_id"),
                    rs.getString("staff_name"),
                    rs.getDate("date"),
                    showUpTime,
                    rs.getTime("shift_start") + "-" + rs.getTime("shift_end"),
                    trangThai,
                    checked
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi khi hiển thị dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void loadTable_2() throws SQLException {
        System.out.println("loading 2");
        ArrayList<String[]> cong = cdao.getAllCong();
        Object[] headers = cdao.getUsernames();
        DefaultTableModel model_2 = new DefaultTableModel(headers, 2);
        jTable1 = new JTable(model_2);
    }

    private void handleAssignShift(int ca) {
        int row = tablecong.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Hãy chọn một nhân viên để phân ca.");
            return;
        }

        int congId = (int) model.getValueAt(row, 0);

        Time shiftStart, shiftEnd;
        if (ca == 1) {
            shiftStart = Time.valueOf("06:00:00");
            shiftEnd = Time.valueOf("11:00:00");
        } else {
            shiftStart = Time.valueOf("11:00:00");
            shiftEnd = Time.valueOf("17:00:00");
        }

        try {
            if (cdao.isShiftTaken(shiftStart, shiftEnd)) {
                JOptionPane.showMessageDialog(this, "Ca này đã có người làm rồi.");
                return;
            }

            cdao.updateShift(congId, shiftStart, shiftEnd);
            model.setValueAt(shiftStart.toString() + " - " + shiftEnd.toString(), row, 5);
            JOptionPane.showMessageDialog(this, "Phân ca thành công");
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi phân ca: " + ex.getMessage());
        }
    }

    private void ganCa(String caLam) {
        int row = tablecong.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Hãy chọn một dòng trong bảng.");
            return;
        }

        tablecong.setValueAt(caLam, row, 7);

        String[] parts = caLam.split("-");
        if (parts.length != 2) {
            JOptionPane.showMessageDialog(null, "Định dạng ca làm không hợp lệ.");
            return;
        }

        String shiftStart = parts[0];
        String shiftEnd = parts[1];

        String congId = tablecong.getValueAt(row, 0).toString();

        try {
            Connection conn = dbConnection.connect();
            PreparedStatement stmt = conn.prepareStatement("UPDATE cong SET shift_start = ?, shift_end = ? WHERE cong_id = ?");
            stmt.setTime(1, Time.valueOf(shiftStart));
            stmt.setTime(2, Time.valueOf(shiftEnd));
            stmt.setString(3, congId);
            stmt.executeUpdate();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi cập nhật database.");
        }
    }

    private boolean isShiftDuplicate(String caLam, int row) {
        for (int i = 0; i < tablecong.getRowCount(); i++) {
            if (i != row) {
                Object caKhac = tablecong.getValueAt(i, 5);
                if (caKhac != null && caLam.equals(caKhac.toString())) {
                    return true; // trùng ca
                }
            }
        }
        return false;
    }

    private void assignShift(int row, String caLam) {
        tablecong.setValueAt(caLam, row, 5);
        String[] parts = caLam.split("-");
        String shiftStart = parts[0];
        String shiftEnd = parts[1];
        String congId = tablecong.getValueAt(row, 0).toString();

        try (Connection conn = dbConnection.connect()) {
            PreparedStatement stmt = conn.prepareStatement(
                    "UPDATE cong SET shift_start = ?, shift_end = ? WHERE cong_id = ?"
            );
            stmt.setTime(1, Time.valueOf(shiftStart));
            stmt.setTime(2, Time.valueOf(shiftEnd));
            stmt.setString(3, congId);
            stmt.executeUpdate();
            displayData();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Lỗi cập nhật ca làm.");
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablecong = new javax.swing.JTable();
        btnCa1 = new javax.swing.JButton();
        btnCa2 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();

        setBackground(new java.awt.Color(255, 255, 225));

        tablecong.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Công", "Mã nhân viên", "Username", "Ngày", "Thời gian có mặt", "Ca làm", "Trạng thái", "Công"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablecong.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablecongMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablecong);

        jScrollPane1.setViewportView(jScrollPane2);

        btnCa1.setText("Ca 1");
        btnCa1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCa1ActionPerformed(evt);
            }
        });

        btnCa2.setText("Ca 2");
        btnCa2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCa2ActionPerformed(evt);
            }
        });

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null}
            },
            new String [] {
                "Title 1"
            }
        ));
        jScrollPane4.setViewportView(jTable1);

        jScrollPane3.setViewportView(jScrollPane4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 669, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(82, 82, 82)
                            .addComponent(btnCa1)
                            .addGap(28, 28, 28)
                            .addComponent(btnCa2))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(68, 68, 68)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1288, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(142, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(195, 195, 195)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCa1)
                    .addComponent(btnCa2))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 322, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(32, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnCa1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCa1ActionPerformed
        // TODO add your handling code here:
        int row = tablecong.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Hãy chọn một dòng.");
            return;
        }
        String caLam = "06:00:00-11:00:00";
        if (isShiftDuplicate(caLam, row)) {
            JOptionPane.showMessageDialog(null, "Ca làm này đã được phân, vui lòng chọn lại");
            return;
        }
        assignShift(row, caLam);
    }//GEN-LAST:event_btnCa1ActionPerformed

    private void btnCa2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCa2ActionPerformed
        // TODO add your handling code here:
        int row = tablecong.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Hãy chọn một dòng.");
            return;
        }
        String caLam = "13:00:00-17:00:00";
        if (isShiftDuplicate(caLam, row)) {
            JOptionPane.showMessageDialog(null, "Ca làm này đã được phân, vui lòng chọn lại");
            return;
        } else {
            assignShift(row, caLam);
        }
    }//GEN-LAST:event_btnCa2ActionPerformed

    private void tablecongMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablecongMouseClicked
        // TODO add your handling code here:
        int row = tablecong.getSelectedRow();
        int column = tablecong.getSelectedColumn();

        if (column == 7) { // cột checkbox
            Boolean isChecked = Boolean.valueOf(tablecong.getValueAt(row, column).toString());
            if (!isChecked) {
                return;
            }

            String caLam = tablecong.getValueAt(row, 5).toString();

            // Nếu chưa phân ca
            if (caLam == null || !caLam.contains("-")) {
                JOptionPane.showMessageDialog(null, "Vui lòng phân ca trước khi chấm công.");
                tablecong.setValueAt(false, row, 7);
                return;
            }

            // Nếu ca bị trùng
            if (isShiftDuplicate(caLam, row)) {
                JOptionPane.showMessageDialog(null, "Ca làm này đã được phân, vui lòng chọn lại");
                tablecong.setValueAt("", row, 5); // xóa ca cũ
                tablecong.setValueAt(false, row, 7); // bỏ check
                return;
            }

            // Kiểm tra đã chấm công chưa
            Object showUpTime = tablecong.getValueAt(row, 4);
            if (showUpTime != null && !showUpTime.toString().isBlank()) {
                JOptionPane.showMessageDialog(null, "Bạn đã chấm công ca này rồi. Không thể chấm lại.");
                tablecong.setValueAt(false, row, 7);
                return;
            }

            // Xử lý thời gian
            LocalTime now = LocalTime.now();
            String[] parts = caLam.split("-");
            LocalTime batDau = LocalTime.parse(parts[0].trim());

            if (now.isBefore(batDau)) {
                JOptionPane.showMessageDialog(null, "Ca làm chưa bắt đầu, vui lòng thử lại sau.");
                tablecong.setValueAt(false, row, 7);
                return;
            }

            long delayInMinutes = Duration.between(batDau, now).toMinutes();
            int showsUp;
            String trangThai;
            if (delayInMinutes > 20) {
                showsUp = 0;
                trangThai = "Vắng";
            } else if (delayInMinutes <= 5) {
                showsUp = 2;
                trangThai = "Đúng giờ";
            } else {
                showsUp = 1;
                trangThai = "Đi muộn";
            }

            int congId = (int) tablecong.getValueAt(row, 0);
            cdao.updateShowsUp(congId, now, showsUp);

            tablecong.setValueAt(now.toString(), row, 4);
            tablecong.setValueAt(trangThai, row, 6);
        }
    }//GEN-LAST:event_tablecongMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCa1;
    private javax.swing.JButton btnCa2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable tablecong;
    // End of variables declaration//GEN-END:variables
}
