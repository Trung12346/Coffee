/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Service.dbConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;

/**
 *
 * @author maith
 */
public class LoginDAO {

    public static String SHA256(String originalString) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(
                originalString.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
        for (int i = 0; i < encodedhash.length; i++) {
            String hex = Integer.toHexString(0xff & encodedhash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static boolean isExist(String username) throws SQLException {
        Connection conn = dbConnection.connect();
        String query = String.format("SELECT username FROM account WHERE username LIKE '%s'", username);
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(query);
        if (rs.next()) {
            return true;
        }
        return false;
    }

    public static boolean isEmailExist(String email) throws SQLException {
        Connection conn = dbConnection.connect();
        String query = String.format("SELECT staff_id FROM staff WHERE email LIKE '%s'", email);
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(query);
        if (rs.next()) {
            GlobalVariables.userId = rs.getInt("staff_id");
            return true;
        }
        return false;

    }

    public static String checkCredens(String username, String password) throws SQLException, NoSuchAlgorithmException {
        Connection conn = dbConnection.connect();
        String query = String.format("SELECT * FROM account WHERE username LIKE '%s'", username);
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(query);
        rs.next();
        //System.out.println(rs.getString("hash_token"));

        String passwordNSalt = password + rs.getString("salt");
        //System.out.println(SHA256(passwordNSalt));
        //System.out.println(rs.getString("hash_token"));
        if (SHA256(passwordNSalt).equals(rs.getString("hash_token"))) {
            return rs.getString("args");
        }
        return null;
    }
}
