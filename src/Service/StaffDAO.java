/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;
import Service.dbConnection;
import java.sql.*;
/**
 *
 * @author ADMIN
 */
public class StaffDAO {
    public ResultSet loaddata() {
//        Connection conn = null;
        try {
            Connection conn = dbConnection.connect();
//            if (conn == null) {
//                throw new SQLException("Không thể kết nối đến database");
//            }
            String query = "SELECT staff_id, age, email, phone FROM staff";
            PreparedStatement stmt = conn.prepareStatement(query);
            return stmt.executeQuery(); 
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean addStaff(int age, String email, String phone) {
//        Connection conn = null;
        try {
            Connection conn = dbConnection.connect();
//            if (conn == null) {
//                throw new SQLException("Không thể kết nối đến database");
//            }
            String query = "INSERT INTO staff (age, email, phone) VALUES (?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, age);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
//    public boolean deleteStaff(int staffId) {
//        Connection conn = null;
//        try {
//            conn = dbConnection.connect();
//            if (conn == null) {
//                throw new SQLException("Không thể kết nối đến database");
//            }
//            String query = "DELETE FROM staff WHERE staff_id = ?";
//            PreparedStatement stmt = conn.prepareStatement(query);
//            stmt.setInt(1, staffId);
//            int rowsAffected = stmt.executeUpdate();
//            return rowsAffected > 0;
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return false;
//        } finally {
//            if (conn != null) {
//                try {
//                    conn.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }
    public boolean updateStaff(int staffId, int age, String email, String phone) {
        Connection conn = null;
        try {
            conn = dbConnection.connect();
            if (conn == null) {
                throw new SQLException("Không thể kết nối đến database");
            }
            String query = "UPDATE staff SET age = ?, email = ?, phone = ? WHERE staff_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, age);
            stmt.setString(2, email);
            stmt.setString(3, phone);
            stmt.setInt(4, staffId);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
