package Service;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import Model.MembershipDataSet;
import Service.dbConnection;
import java.sql.*;

/**
 *
 * @author ADMIN
 */
public class ViewTransactionDAO {

    public ResultSet Loaddata() {
        try {
            Connection conn = dbConnection.connect();
            String query = "SELECT * FROM recipt";
            PreparedStatement stmt = conn.prepareStatement(query);
            return stmt.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public MembershipDataSet getMembershipById(int id) {
        try {
            Connection conn = dbConnection.connect();
            String query = "SELECT * FROM (SELECT TOP 1 * FROM  membership WHERE membership_id = ?) "
                    + "AS found JOIN membership_rank ON found.rank_name = membership_rank.rank_name";

            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            return new MembershipDataSet(
                    rs.getInt("membership_id"),
                    rs.getString("membership_name"),
                    rs.getString("phone"),
                    rs.getString("sercurity_code"),
                    rs.getString("rank_name"),
                    rs.getDate("expiration_date"),
                    rs.getFloat("discount"));
        } catch (Exception e) {
            return null;
        }
    }
}
