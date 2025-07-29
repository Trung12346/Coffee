/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;
import java.sql.*;
/**
 *
 * @author ADMIN
 */
public class MembershipDAO {
    public ResultSet hienthi(){
        try {
            Connection conn=dbConnection.connect();
            String query = "select * from membership";
            PreparedStatement ps = conn.prepareStatement(query);
            return ps.executeQuery();
        } catch (SQLException e) {
            return null;
        }
    }
      public boolean add(String membership_name, String phone, String security_code) {
        try {
            Connection conn = dbConnection.connect(); 
            //String query = "INSERT INTO membership (membership_name, phone, sercurity_code, rank_name, expiration_date) VALUES (?, ?, ?, ?, ?)";
            String query = "INSERT INTO membership (membership_name, phone, sercurity_code, point) VALUES (?, ?, ?, 0)";
            PreparedStatement ps = conn.prepareStatement(query);
            //System.out.println(rank_name);
            ps.setString(1, membership_name);
            ps.setString(2, phone);
            ps.setString(3, security_code);

            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }
    
}
