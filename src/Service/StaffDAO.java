/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
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
            String query = "SELECT staff_id,staff_name,ngaysinh,age, email,phone,identitycard FROM staff";
            PreparedStatement stmt = conn.prepareStatement(query);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean updateaccount(int id,String password){
        try {
            Connection conn = dbConnection.connect();
            String salt = "";
            for (int i = 0; i < 32; i++) {
                int randomindex = new Random().nextInt(characters.length());
                salt+=characters.charAt(randomindex);
            }
            String passwHash = LoginDAO.SHA256(password+salt);
            Statement stm = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_UPDATABLE);
            String query1 = String.format("UPDATE account set hash_token=?,salt =? where staff_id= ? ", passwHash,salt,id);
            PreparedStatement stmt = conn.prepareStatement(query1);
            stmt.setString(1, passwHash);
            stmt.setString(2, salt);
            stmt.setInt(3, id);
            int rs = stmt.executeUpdate();
            return rs>0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addStaff(String name, long age, String email, String phone, String args, String password, String identitycard, LocalDate ngaysinh) throws NoSuchAlgorithmException {
//        Connection conn = null;
        try {
            Connection conn = dbConnection.connect();
//            if (conn == null) {
//                throw new SQLException("Không thể kết nối đến database");
//            }
            String salt = "";
            for (int i = 0; i < 32; i++) {
                int randomIndex = new Random().nextInt(characters.length());
                salt += characters.charAt(randomIndex);
            }
            String passwordHash = LoginDAO.SHA256(password + salt);
            Statement stm = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
            String query_0 = String.format("INSERT INTO staff (staff_name, age, email, phone,identitycard,ngaysinh) VALUES ('%s', %d, '%s', '%s','%s','%s')", name, age, email, phone,identitycard,ngaysinh);
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
            String query_4 = String.format("INSERT INTO account VALUES ('%s', %d, '%s', '%s', '%s')", generateUniqueUsername(name), id, passwordHash, salt, args);

            stm.executeUpdate(query_4);
            conn.commit();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public ArrayList getStaffIds() throws SQLException {
        ArrayList ids = new ArrayList();
        Connection conn = dbConnection.connect();
        String query = "SELECT staff_id FROM staff";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        while (rs.next()) {
            ids.add(rs.getInt("staff_id"));

        }
        return ids;
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
                                    
   public boolean updateStaff(
    String name,
    long age,
    String email,
    String phone,
    String password,       
    String identitycard,
    LocalDate ngaysinh,
    int staffid) {
    
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    Connection conn = null;
    PreparedStatement stmtStaff = null;
    PreparedStatement stmtAccount = null;
    
    try {
        conn = dbConnection.connect();
        conn.setAutoCommit(false);

       
        String updateStaffSQL = "UPDATE staff SET staff_name=?, age=?, email=?, phone=?, identitycard=?, ngaysinh=? WHERE staff_id=?";
        stmtStaff = conn.prepareStatement(updateStaffSQL);
        stmtStaff.setString(1, name);
        stmtStaff.setLong(2, age);
        stmtStaff.setString(3, email);
        stmtStaff.setString(4, phone);
        stmtStaff.setString(5, identitycard);
        stmtStaff.setDate(6, java.sql.Date.valueOf(ngaysinh));
        stmtStaff.setInt(7, staffid);
        int affectedRows = stmtStaff.executeUpdate();

        if (affectedRows == 0) {
            throw new SQLException("Cập nhật nhân viên thất bại, không có dòng nào bị ảnh hưởng.");
        }

        if (password != null && !password.trim().isEmpty()) {
           
            StringBuilder saltBuilder = new StringBuilder();
            Random random = new Random();
            for (int i = 0; i < 32; i++) {
                int randomIndex = random.nextInt(characters.length());
                saltBuilder.append(characters.charAt(randomIndex));
            }
            String salt = saltBuilder.toString();
            String passwordHash = LoginDAO.SHA256(password + salt);

            String updateAccountSQL = "UPDATE account SET hash_token=?, salt=? WHERE staff_id=?";
            stmtAccount = conn.prepareStatement(updateAccountSQL);
            stmtAccount.setString(1, passwordHash);
            stmtAccount.setString(2, salt);
            stmtAccount.setInt(3, staffid);
            int accRows = stmtAccount.executeUpdate();

            if (accRows == 0) {
                String username = generateUniqueUsername(name);
                String insertAccountSQL = "INSERT INTO account (username, staff_id, hash_token, salt, args) VALUES (?, ?, ?, ?, ?)";
                stmtAccount.close();
                stmtAccount = conn.prepareStatement(insertAccountSQL);
                stmtAccount.setString(1, username);
                stmtAccount.setInt(2, staffid);
                stmtAccount.setString(3, passwordHash);
                stmtAccount.setString(4, salt);
                stmtAccount.setString(5, ""); 
                stmtAccount.executeUpdate();
            }
        }

        conn.commit();
        return true;

    } catch (Exception e) {
        e.printStackTrace();
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return false;

    } finally {
        try {
            if (stmtStaff != null) stmtStaff.close();
            if (stmtAccount != null) stmtAccount.close();
            if (conn != null) conn.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
    public static String generateUniqueUsername(String baseUsername) throws SQLException {
    Connection conn = dbConnection.connect();
    String newUsername = baseUsername;
    int counter = 1;

    while (true) {
        String query = "SELECT COUNT(*) AS cnt FROM account WHERE username = ?";
        PreparedStatement ps = conn.prepareStatement(query);
        ps.setString(1, newUsername);
        ResultSet rs = ps.executeQuery();

        rs.next();
        int count = rs.getInt("cnt");

        rs.close();
        ps.close();

        if (count == 0) {
            // Không trùng, dùng username này
            break;
        } else {
            // Đã trùng → thêm số vào cuối
            newUsername = baseUsername + counter;
            counter++;
        }
    }

    conn.close();

    // Nếu username đã bị thay đổi thì báo cho người dùng
    if (!newUsername.equals(baseUsername)) {
        javax.swing.JOptionPane.showMessageDialog(null,
            "Tên bạn đã trùng, tên tài khoản của bạn là: " + newUsername);
    }

    return newUsername;
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
