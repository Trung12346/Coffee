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

    public ResultSet hienthi() {
        try {
            Connection conn = dbConnection.connect();
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
            Statement stm = conn.createStatement();
            //String query = "INSERT INTO membership (membership_name, phone, sercurity_code, rank_name, expiration_date) VALUES (?, ?, ?, ?, ?)";
            String query = "INSERT INTO membership (membership_name, phone, sercurity_code, staff_id) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            //System.out.println(rank_name);
            ps.setString(1, membership_name);
            ps.setString(2, phone);
            ps.setString(3, security_code);
            ps.setInt(4, GlobalVariables.userId);
            int rows = ps.executeUpdate();
            query = "SELECT @@identity AS last_membership";
            ResultSet rs = stm.executeQuery(query);
            rs.next();
            int lastestMembershipId = rs.getInt("last_membership");
            query = String.format("INSERT INTO point_history VALUES (%d, -1, null, CURRENT_TIMESTAMP)", lastestMembershipId);
            stm = conn.createStatement();
            stm.executeUpdate(query);
            return rows > 0;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }

}
