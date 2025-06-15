/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import java.sql.*;
import Model.MembershipDataSet;
import Model.JSON;
import Model.ProductDataSet;
import Model.VoucherDataSet;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.text.SimpleDateFormat;
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

        for (int i = 0; i < productIds.length; i++) {
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
            } catch (SQLException e) {
            }
        }
        //test
//        String test = String.format("{\"vouchers\": %s}", JSON.StringifyJSON(vouchers));
//        JSONArray ja = new JSONArray(JSON.parseJSON(test).getJSONArray("vouchers"));
//        JSONObject test1 = JSON.parseJSON(ja.get(0).toString());
//        
//        System.out.println(ja.get(0).toString());
//        System.out.println(test1);
        return String.format("{\"vouchers\": %s}", JSON.StringifyJSON(vouchers));

    }

//    public static void main(String[] args) throws SQLException, JsonProcessingException {
//        TransactionDAO td = new TransactionDAO();
//        td.createTransaction("{\"reciptId\":0,\"reciptDate\":1749965819809,\"products\":[{\"productId\":1,\"productName\":\"sex\",\"productPrice\":20000.0,\"quantity\":14}],\"vouchers\":[{\"id\":1,\"startDate\":1782310528,\"endDate\":-1041427840,\"productId\":1,\"newPrice\":15000.0}],\"membership\":{\"id\":1,\"name\":null,\"phone\":null,\"securityCode\":null,\"rank\":null,\"expirDate\":null,\"discount\":0.0},\"staffId\":1}");
//    }
    public String searchMembership(String phone, boolean validate) throws SQLException, JsonProcessingException {
        String query = String.format("SELECT * FROM (SELECT TOP 1 * FROM  membership WHERE phone LIKE '%s' ORDER BY membership_id DESC) AS found JOIN membership_rank ON found.rank_name = membership_rank.rank_name", phone);
        Statement stm = conn.createStatement();
        try {
            ResultSet rs = stm.executeQuery(query);
            rs.next();
            if (validate) {
                java.util.Date currentDate = new java.util.Date();
                java.util.Date expirDate = new java.util.Date(rs.getDate("expiration_date").getTime());
                if (!currentDate.before(expirDate) || rs.getString("phone") == null) {
                    return null;
                }
            }

            return JSON.StringifyJSON(new MembershipDataSet(rs.getInt("membership_id"),
                    rs.getString("membership_name"),
                    rs.getString("phone"),
                    rs.getString("sercurity_code"),
                    rs.getString("rank_name"),
                    rs.getDate("expiration_date"),
                    rs.getFloat("discount")
            ));
        } catch (SQLException ex) {
            //System.out.println(ex);
            return null;
        }

    }

    public void createTransaction(String js) throws SQLException {
        JSONObject jo = JSON.parseJSON(js);
//        java.sql.Timestamp dateTimeSQL = new java.sql.Timestamp(jo.getLong("reciptDate"));
//        System.out.println(dateTimeSQL);
        JSONArray products = jo.getJSONArray("products");
        JSONArray vouchers = jo.getJSONArray("vouchers");
        int membershipId = jo.getJSONObject("membership").getInt("id");
        int staffId = jo.getInt("staffId");

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        java.util.Date dateTimeUtil = new java.util.Date(dateTimeSQL.getTime());
//        String dateTime = sdf.format(dateTimeUtil);
        java.util.Date utilDate = new java.util.Date(new java.sql.Timestamp(jo.getLong("reciptDate")).getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateTime = sdf.format(utilDate);
        System.out.println(dateTime);
        String query = String.format("INSERT INTO recipt VALUES ('%s', '%s', '%s', %d, %d)",
                dateTime,
                products.toString(),
                vouchers.toString(),
                membershipId,
                staffId
        );

        Statement stm = conn.createStatement();
        stm.executeUpdate(query);
    }
}
