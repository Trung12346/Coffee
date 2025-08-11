/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Model.STable1;
import Service.dbConnection;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class WarehouseDAO {

    // Thêm sản phẩm vào bảng warehouse_log
    public static ArrayList<STable1> getIngredients() throws SQLException {
        Connection conn = dbConnection.connect();
        String query = "SELECT * FROM ingredients";
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(query);
        ArrayList<STable1> table_1Rows = new ArrayList();
        while(rs.next()) {
            table_1Rows.add(new STable1(rs.getInt("ingredient_id"), rs.getString("ingredient_label"), rs.getString("unit")));
        }
        
        return table_1Rows;
    }
    
    public boolean addWarehouseLog(String label, String quantityStr, int id) {
        if (label.isEmpty() || quantityStr.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                JOptionPane.showMessageDialog(null, "Số lượng phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Số lượng phải là số nguyên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        java.util.Date currentTime = new java.util.Date();
        java.sql.Timestamp currentTimeSql = new java.sql.Timestamp(currentTime.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        sdf.setLenient(false);
        Timestamp importTime;
        try {
            //Date parsedDate = sdf.parse(timeStr);
            //importTime = new Timestamp(parsedDate.getTime());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Thời gian phải đúng định dạng yyyy-MM-dd HH:mm:ss!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String sql = "INSERT INTO storage VALUES (CURRENT_TIMESTAMP, ?, ?)";
        try (Connection conn = dbConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setFloat(2, quantity);

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi thêm sản phẩm: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Tải dữ liệu từ bảng warehouse_log vào JTable
    public void loadTableData(DefaultTableModel model) {
        model.setRowCount(0); // Xóa dữ liệu cũ trong bảng

        try (Connection conn = dbConnection.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM storage")) {

            while (rs.next()) {
                Object[] row = {
                    new SimpleDateFormat("yy/MM/dd HH:mm:ss").format(rs.getTimestamp("restock_time")),
                    rs.getInt("item"),
                    rs.getFloat("quantity")
                };
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Lỗi khi tải dữ liệu: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}
