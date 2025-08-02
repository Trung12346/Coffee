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
        String query = "SELECT r.receipt_id, r.receipt_date, r.membership_id, r.staff_id, " +
                      "COALESCE(STRING_AGG(CASE WHEN rd.product_id IS NOT NULL THEN CAST(rd.product_id AS VARCHAR(10)) END, ', '), '0') AS product_ids, " +
                      "COALESCE(STRING_AGG(CASE WHEN rd.voucher_id IS NOT NULL THEN CAST(rd.voucher_id AS VARCHAR(10)) END, ', '), '0') AS voucher_ids " +
                      "FROM receipt r " +
                      "LEFT JOIN receipt_details rd ON r.receipt_id = rd.parent_receipt_id " +
                      "GROUP BY r.receipt_id, r.receipt_date, r.membership_id, r.staff_id " +
                      "ORDER BY r.receipt_id";
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
    public ResultSet fillter(String startdate, String enddate) {
  try {
        Connection conn = dbConnection.connect();
        String query = "SELECT r.receipt_id, r.receipt_date, r.membership_id, r.staff_id, " +
                      "COALESCE(STRING_AGG(CASE WHEN rd.product_id IS NOT NULL THEN CAST(rd.product_id AS VARCHAR(10)) END, ', '), '0') AS product_ids, " +
                      "COALESCE(STRING_AGG(CASE WHEN rd.voucher_id IS NOT NULL THEN CAST(rd.voucher_id AS VARCHAR(10)) END, ', '), '0') AS voucher_ids " +
                      "FROM receipt r " +
                      "LEFT JOIN receipt_details rd ON r.receipt_id = rd.parent_receipt_id " +
                      "WHERE CONVERT(date, r.receipt_date) BETWEEN ? AND ? " +
                      "GROUP BY r.receipt_id, r.receipt_date, r.membership_id, r.staff_id " +
                      "ORDER BY r.receipt_id";
        PreparedStatement ps = conn.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ps.setString(1, startdate);
        ps.setString(2, enddate);
        ResultSet rs = ps.executeQuery();
        return rs;
    } catch (Exception e) {
        e.printStackTrace();
        System.out.println("Debug - Filter query error: " + e.getMessage());
        return null;
    }
}

}
