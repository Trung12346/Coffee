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
import java.util.Calendar;
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
    //DEPRECATED METHOD{-----------------------------------------------------------------------------------------------------------------------
    public String searchMembership_old(String phone, boolean validate) throws SQLException, JsonProcessingException {
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
    //DEPRECATED METHOD}-----------------------------------------------------------------------------------------------------------------------

    public String searchMembership(String phone) throws SQLException, JsonProcessingException {
        String query = String.format("SELECT * FROM membership WHERE phone LIKE '%s'", phone);
        System.out.println(phone);
        System.out.println("1");
        Statement stm = conn.createStatement();
        //System.out.println("HELLO");
        try {
            System.out.println("2");
            ResultSet memberRs = stm.executeQuery(query);
            System.out.println("3");
            memberRs.next();
            System.out.println("4");
            int membershipId = memberRs.getInt("membership_id");
            System.out.println("5");
            //System.out.println("HELLO");
            query = String.format("SELECT TOP 1 expiration_date FROM point_history WHERE change_type = -1 AND membership_id = '%s' ORDER BY point_change_id DESC", membershipId);
            stm = conn.createStatement();
            ResultSet timeRs = stm.executeQuery(query);
            timeRs.next();
            java.sql.Timestamp lastestSpendSql = timeRs.getTimestamp("expiration_date");
            java.util.Date lastestSpendUtil = new Date(lastestSpendSql.getTime());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            query = String.format("SELECT SUM(point_change) AS point FROM point_history WHERE change_type = 1 AND membership_id = 1 AND expiration_date > CURRENT_TIMESTAMP AND expiration_date > '%s'", sdf.format(lastestSpendUtil));
            stm = conn.createStatement();
            ResultSet pointRs = stm.executeQuery(query);
            pointRs.next();
            int point = pointRs.getInt("point");
            MembershipDataSet mds = new MembershipDataSet(membershipId,
                    memberRs.getString("membership_name"),
                    memberRs.getString("phone"),
                    memberRs.getString("sercurity_code"),
                    point
            );
            return JSON.StringifyJSON(mds);
        } catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }
//DEPRECATED METHOD{-----------------------------------------------------------------------------------------------------------------------

    public void createTransaction_old(String js) throws SQLException {
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
        String query = String.format("INSERT INTO recipt VALUES ('%s', '%s', '%s', %s, %d)",
                dateTime,
                products.toString(),
                vouchers.toString(),
                "NULL",
                staffId
        );

        Statement stm = conn.createStatement();
        stm.executeUpdate(query);
    }

    //DEPRECATED METHOD}-----------------------------------------------------------------------------------------------------------------------
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
//        String query = String.format("INSERT INTO recipt VALUES ('%s', '%s', '%s', %d, %d)",
//                dateTime,
//                products.toString(),
//                vouchers.toString(),
//                membershipId,
//                staffId
//        );
        String query = String.format("INSERT INTO receipt VALUES ('%s', %d, %d)", dateTime, membershipId, staffId);
        Statement stm = conn.createStatement();
        stm.executeUpdate(query);
        query = "SELECT @@identity AS last_record";
        ResultSet rs = stm.executeQuery(query);
        rs.next();
        int lastRecord = rs.getInt("last_record");
        for (int i = 0; i < products.length(); i++) {

            float productPrice = Float.parseFloat(products.getJSONObject(i).get("productPrice").toString());
            for (int x = 0; x < vouchers.length(); x++) {
                if (products.getJSONObject(i).get("productId") == vouchers.getJSONObject(x).get("productId")) {
                    productPrice = Float.parseFloat(vouchers.getJSONObject(i).get("newPrice").toString());

                    query = String.format("INSERT INTO receipt_details VALUES (%d, %d, %d, %d)",
                            lastRecord,
                            products.getJSONObject(i).get("productId"),
                            products.getJSONObject(i).get("quantity"),
                            vouchers.getJSONObject(x).get("id")
                    );
                } else {
                    query = String.format("INSERT INTO receipt_details VALUES (%d, %d, %d, NULL)",
                            lastRecord,
                            products.getJSONObject(i).get("productId"),
                            products.getJSONObject(i).get("quantity")
                    );
                }
                stm = conn.createStatement();
                stm.executeUpdate(query);
            }
        }
        System.out.println("membershipid");
        System.out.println(membershipId);
        if(membershipId != 0) {
            Calendar c = Calendar.getInstance();
            c.setTime(utilDate);
            c.add(Calendar.DATE, 60);
            utilDate = c.getTime();
            if(Boolean.parseBoolean(jo.get("usePoint").toString())) {
                query = String.format("INSERT INTO point_history (membership_id, change_type, point_change, expiration_date) VALUES (%d, -1, NULL, '%s')", membershipId, sdf.format(utilDate));
                stm = conn.createStatement();
                stm.executeUpdate(query);
            }
            
            double point = Math.floor(Float.parseFloat(jo.get("total").toString()) / 10);

            c.add(Calendar.SECOND, 1);
            utilDate = c.getTime();
            
            query = String.format("INSERT INTO point_history (membership_id, change_type, point_change, expiration_date) VALUES (%d, 1, %d, '%s'), (%d, 1, %d, '%s')",
                    membershipId, (int)point, sdf.format(utilDate),
                    membershipId, (int)Float.parseFloat(jo.get("replenishPoint").toString()), sdf.format(utilDate));
            System.out.println(query);
            stm = conn.createStatement();
            stm.executeUpdate(query);
        }
    }
}
