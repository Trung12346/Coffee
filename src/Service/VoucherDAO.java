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
public class VoucherDAO {

    public ResultSet hienthi() {
        try {
            Connection conn = dbConnection.connect();
            String query = "SELECT * FROM voucher";
            PreparedStatement ps = conn.prepareStatement(query);
            return ps.executeQuery();
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
    }

    public boolean add(Timestamp startdate, Timestamp enddate, int product_id, float Giasp) {
        try {
            Connection conn = dbConnection.connect();
            String query = "insert into voucher(start_date, end_date, product_id, new_product_price) values (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);

            // Chuyển Timestamp sang java.sql.Date
            ps.setDate(1, new java.sql.Date(startdate.getTime()));
            ps.setDate(2, new java.sql.Date(enddate.getTime()));

            ps.setInt(3, product_id);
            ps.setFloat(4, Giasp);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean update (Timestamp startdate,Timestamp enddate,int productid,float giasp){
        try {
            Connection conn= dbConnection.connect();
            String query = "UPDATE voucher set start_date = ?,end_date = ?,new_product_price = ? where product_id=? ";
            PreparedStatement ps = conn.prepareStatement(query);
            
            ps.setDate(1, new java.sql.Date(startdate.getTime()));
            ps.setDate(2, new java.sql.Date(enddate.getTime()));
            ps.setFloat(3, giasp);
            ps.setInt(4, productid);
            return ps.executeUpdate()>0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
