/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.util.Random;
/**
 *
 * @author ADMIN
 */
public class StaffDAO {
    final String characters = "1234567890qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
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
    public boolean addStaff(String name, int age, String email, String phone, String args, String password) throws NoSuchAlgorithmException {
//        Connection conn = null;
        try {
            Connection conn = dbConnection.connect();
//            if (conn == null) {
//                throw new SQLException("Không thể kết nối đến database");
//            }
            String salt = "";
            for(int i = 0; i < 32; i++) {
                int randomIndex = new Random().nextInt(characters.length());
                salt += characters.charAt(randomIndex);
            }
            String passwordHash = LoginDAO.SHA256(password + salt);
            Statement stm = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query_0 = String.format("INSERT INTO staff (staff_name, age, email, phone) VALUES ('%s', %d, '%s', '%s')", name, age, email, phone);
            String query_1 = "GO";
//            String query_2 = "DECLARE @newId INT";
            String query_3 = "SELECT TOP 1 staff_id FROM staff ORDER BY staff_id DESC";
            
//            PreparedStatement stmt = conn.prepareStatement(query);
//            stmt.setString(1, name);
//            stmt.setInt(2, age);
//            stmt.setString(3, email);
//            stmt.setString(4, phone);
//            
//            stmt.setString(5, name);
//            stmt.setString(6, passwordHash);
//            stmt.setString(7, salt);
//            stmt.setString(8, args);
            conn.setAutoCommit(false);
            stm.executeUpdate(query_0);
            conn.commit();
            ResultSet rs = stm.executeQuery(query_3);
            rs.next();
            int id = rs.getInt("staff_id");
            String query_4 = String.format("INSERT INTO account VALUES ('%s', %d, '%s', '%s', '%s')", name, id, passwordHash, salt, args);

            stm.executeUpdate(query_4);
            conn.commit();
            return true;
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
    public static int getIdFromUsername(String username) throws SQLException {
        String query = String.format("SELECT staff_id FROM account WHERE username LIKE '%s'", username);
        System.out.println(query);
        Connection conn = dbConnection.connect();
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(query);
        rs.next();
        
        return rs.getInt("staff_id");
    }
}
