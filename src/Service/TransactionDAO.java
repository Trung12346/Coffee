/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;
import java.sql.*;
import Model.MembershipDataSet;
import Model.JSON;
import Model.VoucherDataSet;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.ArrayList;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONObject;
/**
 *
 * @author maith
 */
public class TransactionDAO {
    Connection conn = dbConnection.connect();
    
    
    public ResultSet getProducts() throws SQLException {
        String query = "SELECT * FROM product";
        Statement stm = conn.createStatement();
        return stm.executeQuery(query);
    }
    
    public ResultSet selectProductById(int id) throws SQLException {
        String query = String.format("SELECT * FROM product WHERE product_id = %d", id);
        Statement stm = conn.createStatement();
        return stm.executeQuery(query);
    }
    
    public String searchVoucher(int[] productIds) throws SQLException, JsonProcessingException {
        String query;
        int index = 0;
        ArrayList<VoucherDataSet> vouchers = new ArrayList();
        Statement stm = conn.createStatement();
        
        for(int i = 0; i < productIds.length; i++) {
            try {
                query = String.format("SELECT * FROM voucher WHERE product_id = %d", productIds[i]);
                ResultSet rs = stm.executeQuery(query);
                rs.next();
                vouchers.add(new VoucherDataSet(rs.getInt("voucher_id"),
                                            rs.getDate("start_date"),
                                            rs.getDate("end_date"),
                                            rs.getInt("product_id"),
                                            rs.getFloat("new_product_price")
                                            ));
            } catch(SQLException e) {}   
        }
        String test = String.format("{\"vouchers\": %s}", JSON.StringifyJSON(vouchers));
        JSONArray ja = new JSONArray(JSON.parseJSON(test).getJSONArray("vouchers"));
        JSONObject test1 = JSON.parseJSON(ja.get(0).toString());
        
        System.out.println(ja.get(0).toString());
        System.out.println(test1);
        return String.format("{\"vouchers\": %s}", JSON.StringifyJSON(vouchers));
        
    }
//    public static void main(String[] args) throws SQLException, JsonProcessingException {
//        int[] ids = {1};
//        TransactionDAO td = new TransactionDAO();
//        System.out.println(td.searchVoucher(ids));
//    }
    public String searchMembership(String phone) throws SQLException, JsonProcessingException {
        String query = String.format("SELECT TOP 1 * FROM  membership WHERE phone LIKE '%s' JOIN membership_rank ON membership.rank_name = membership_rank.rank_name ORDER BY membership_id DESC", phone);
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(query);
        rs.next();
        java.util.Date currentDate = new java.util.Date();
        java.util.Date expirDate = new java.util.Date(rs.getDate("expiration_date").getTime());  
        if(!currentDate.before(expirDate) || rs.getString("membership_phone") == null) { 
            return null;
        }
        return JSON.StringifyJSON(new MembershipDataSet(rs.getInt("membership_id"),
                                                        rs.getString("membership_name"),
                                                        rs.getString("phone"),
                                                        rs.getString("sercurity_code"),
                                                        rs.getString("rank_name"),
                                                        rs.getDate("expiration_date")
                                                        ));
    }
//    public boolean createRecipt() {
//        
//    }
}
