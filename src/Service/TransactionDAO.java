/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import java.sql.*;
import Model.MembershipDataSet;
import Model.JSON;
import Model.ProductDataSet;
import Model.RMTable1;
import Model.Recipt;
import Model.ReciptRow;
import Model.TransactionDataSet;
import Model.VoucherDataSet;
import Model.objectToInt;
import View.CreateTransaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author maith
 */
public class TransactionDAO {

    public static int lastRecordGlb;
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
        System.out.println("phone: ");
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
            System.out.println("7");
            timeRs.next();
            java.sql.Timestamp lastestSpendSql = timeRs.getTimestamp("expiration_date");
            java.util.Date lastestSpendUtil = new Date(lastestSpendSql.getTime());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            query = String.format("SELECT SUM(point_change) AS point FROM point_history WHERE change_type = 1 AND membership_id = %d AND expiration_date > CURRENT_TIMESTAMP AND expiration_date > '%s'", membershipId, sdf.format(lastestSpendUtil));
            stm = conn.createStatement();
            ResultSet pointRs = stm.executeQuery(query);
            System.out.println("8");
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

    public String searchMembershipById(int id) throws SQLException {
        String query = String.format("SELECT * FROM membership WHERE membership_id = %d", id);
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
        Locale.setDefault(Locale.US);
        JSONObject jo = JSON.parseJSON(js);
//        java.sql.Timestamp dateTimeSQL = new java.sql.Timestamp(jo.getLong("reciptDate"));
//        System.out.println(dateTimeSQL);
        JSONArray products = jo.getJSONArray("products");
        JSONArray vouchers = jo.getJSONArray("vouchers");
        int membershipId = jo.getJSONObject("membership").getInt("id");
        int staffId = jo.getInt("staffId");
        String paymentMethod = jo.getString("paymentMethod");
        String paymentState = jo.getString("paymentState");
        float amount = jo.getFloat("amount");
        String note = jo.getString("note");
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
        int usePoint = jo.getBoolean("usePoint") ? 1 : 0;
        int pointUsed = jo.getInt("pointUsed");
        String query = String.format("INSERT INTO receipt VALUES (N'%s', %d, %d, N'%s', N'%s', %f, N'%s', %d, %d)", dateTime, membershipId, staffId, paymentMethod, paymentState, amount, note, usePoint, pointUsed);
        Statement stm = conn.createStatement();
        stm.executeUpdate(query);
        query = "SELECT @@identity AS last_record";
        ResultSet rs = stm.executeQuery(query);
        rs.next();
        int lastRecord = rs.getInt("last_record");
        TransactionDAO.lastRecordGlb = lastRecord;
        for (int i = 0; i < products.length(); i++) {
            System.out.println("loop running");
            float productPrice = Float.parseFloat(products.getJSONObject(i).get("productPrice").toString());
            if (vouchers.length() > 0) {
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
                }
            } else {
                query = String.format("INSERT INTO receipt_details VALUES (%d, %d, %d, NULL)",
                        lastRecord,
                        products.getJSONObject(i).get("productId"),
                        products.getJSONObject(i).get("quantity")
                );
            }

            System.out.println("receipt details");
            System.out.println(query);
            stm = conn.createStatement();
            stm.executeUpdate(query);

        }
        System.out.println("membershipid");
        System.out.println(membershipId);
        if (membershipId != 0) {
            Calendar c = Calendar.getInstance();
            c.setTime(utilDate);
            c.add(Calendar.DATE, 60);
            utilDate = c.getTime();
            if (Boolean.parseBoolean(jo.get("usePoint").toString())) {
                query = String.format("INSERT INTO point_history (membership_id, change_type, point_change, expiration_date) VALUES (%d, -1, NULL, '%s')", membershipId, sdf.format(utilDate));
                stm = conn.createStatement();
                stm.executeUpdate(query);
            }

            double point = Math.floor(Float.parseFloat(jo.get("total").toString()) / 10);

            c.add(Calendar.SECOND, 1);
            utilDate = c.getTime();

            query = String.format("INSERT INTO point_history (membership_id, change_type, point_change, expiration_date) VALUES (%d, 1, %d, '%s'), (%d, 1, %d, '%s')",
                    membershipId, (int) point, sdf.format(utilDate),
                    membershipId, (int) Float.parseFloat(jo.get("replenishPoint").toString()), sdf.format(utilDate));
            System.out.println(query);
            stm = conn.createStatement();
            stm.executeUpdate(query);
        }
    }

    public int howManyCraftable(int productId) {
        String query_1 = String.format("SELECT product_id, ingredient_id, quantity, ready_quantity  FROM (SELECT * FROM product_ingredients WHERE product_id = %d) p INNER JOIN (SELECT item, SUM(quantity) AS ready_quantity FROM storage GROUP BY item) s ON p.ingredient_id = s.item ", productId);
        System.out.println(query_1);
        Statement stm_1;
        int smallestAcquirable = -1;
        try {
            stm_1 = conn.createStatement();
            ResultSet rs_1 = stm_1.executeQuery(query_1);

            while (rs_1.next()) {
                float availableQuantity = rs_1.getFloat("ready_quantity");
                float neededQuantity = rs_1.getFloat("quantity");
                int doable = (int) Math.floor(availableQuantity / neededQuantity);
                if (smallestAcquirable < 0) {
                    smallestAcquirable = doable;
                }
                if (smallestAcquirable > doable) {
                    smallestAcquirable = doable;
                }

            }

        } catch (SQLException ex) {
            Logger.getLogger(IngredientDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return smallestAcquirable;
    }

    public void consumeMaterial(int productId, int quantity) throws SQLException {
        Locale.setDefault(Locale.US);
        Statement stm;

        String query = String.format("SELECT product_id, ingredient_id, quantity, ready_quantity  FROM (SELECT * FROM product_ingredients WHERE product_id = %d) p INNER JOIN (SELECT item, SUM(quantity) AS ready_quantity FROM storage GROUP BY item) s ON p.ingredient_id = s.item ", productId);
        stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(query);
        while (rs.next()) {
            query = String.format("INSERT INTO storage VALUES (CURRENT_TIMESTAMP, %d, %f)", rs.getInt("ingredient_id"), 0 - (quantity * rs.getFloat("quantity")));
            stm = conn.createStatement();
            stm.executeUpdate(query);
        }
    }

    public void setReceiptCompleted(int receiptId) throws SQLException {
        Statement stm = conn.createStatement();
        String query = String.format("UPDATE receipt SET payment_state = N'Đã thanh toán' WHERE receipt_id = %d", receiptId);
        stm.executeUpdate(query);
    }

    public ArrayList<TransactionDataSet> getUncompletedReceipt() throws SQLException, JsonProcessingException {
        Statement stm = conn.createStatement();
        String query = "SELECT * FROM receipt WHERE payment_state LIKE N'Chưa thanh toán'";
        Locale.setDefault(Locale.US);
        ArrayList<TransactionDataSet> transactions = new ArrayList();
        ResultSet rs = stm.executeQuery(query);
        while (rs.next()) {
            query = String.format("SELECT * FROM receipt_details WHERE parent_receipt_id = %d", rs.getInt("receipt_id"));
            ArrayList<Integer> productIds = new ArrayList();
            ArrayList<ProductDataSet> products = new ArrayList();
            ArrayList quantities = new ArrayList();

            stm = conn.createStatement();
            ResultSet rd = stm.executeQuery(query);
            while (rd.next()) {
                productIds.add(rd.getInt("product_id"));
                quantities.add(rd.getInt("quantity"));
            }

            AtomicInteger index = new AtomicInteger();
            productIds.forEach(productId -> {
                try {
                    ResultSet p = selectProductById(Integer.parseInt(productId.toString()));
                    p.next();

                    //CONTINUE HERE
                    products.add(new ProductDataSet(p.getInt("product_id"),
                            p.getString("product_name"),
                            p.getFloat("product_price"),
                            Integer.parseInt(quantities.get(index.getAndIncrement()).toString())
                    ));
                } catch (SQLException ex) {
                    Logger.getLogger(CreateTransaction.class.getName()).log(Level.SEVERE, null, ex);
                }

            });

            for (int i = 0; i < productIds.size(); i++) {
                int readyQuantity = howManyCraftable(productIds.get(i));
                int productIndexFromProducts;
                int quantity = 1;
                String productName = "";
                for (int x = 0; x < products.size(); x++) {
                    if (productIds.get(i) == products.get(x).productId) {
                        productIndexFromProducts = x;
                        quantity = products.get(x).quantity;
                        productName = products.get(x).productName;
                    }
                }

            }

            int[] ids = objectToInt.objectToInteger(productIds);
            ArrayList<VoucherDataSet> vouchers = new ArrayList();
            try {
                String vouchersRawJSON = searchVoucher(ids);
                JSONArray ja = new JSONArray(JSON.parseJSON(vouchersRawJSON).getJSONArray("vouchers"));

                for (int i = 0; i < ja.length(); i++) {
                    JSONObject jo = JSON.parseJSON(ja.get(i).toString());
                    VoucherDataSet vds = new VoucherDataSet(
                            jo.getInt("id"),
                            new java.sql.Date(jo.getInt("startDate")),
                            new java.sql.Date(jo.getInt("endDate")),
                            jo.getInt("productId"),
                            jo.getFloat("newPrice")
                    );
                    vouchers.add(vds);
                }
//            vouchers = new ArrayList(
//                JSON.parseJSON(transactDAO.searchVoucher(ids)).getJSONArray("vouchers")
//            );
            } catch (SQLException ex) {
                System.out.println("khong co voucher");
            }

            java.util.Date currentTime = new java.util.Date();
            java.sql.Timestamp currentTimeSql = new java.sql.Timestamp(currentTime.getTime());
            MembershipDataSet mds = new MembershipDataSet();

            float membershipDiscount = 0;
            try {
                JSONObject jo = JSON.parseJSON(searchMembershipById(rs.getInt("membership_id"))); //CREATE METHOD TO GET PHONE FROM ID
                //if(jo.getString("securityCode").equals(jTextField2.getText())) {
                mds = new MembershipDataSet(
                        jo.getInt("id"),
                        jo.getString("name"),
                        jo.getString("phone"),
                        jo.getString("securityCode"),
                        jo.getInt("point")
                );
                if (rs.getInt("use_point") == 1) {
                    membershipDiscount = rs.getInt("point_used");
                }

                //}
            } catch (NullPointerException ex) {
                System.out.println("found none");
            }
            System.out.println("HEY");
            System.out.println(mds.id);
            TransactionDataSet transaction = new TransactionDataSet(
                    rs.getInt("receipt_id"),
                    currentTimeSql,
                    products,
                    vouchers,
                    mds,
                    GlobalVariables.userId,
                    0,
                    rs.getInt("use_point") == 1,
                    0,
                    rs.getString("payment_method"),
                    rs.getString("payment_state"),
                    0,
                    rs.getString("note"),
                    rs.getInt("point_used")
            );

            java.util.Date utilDate = new java.util.Date(transaction.reciptDate.getTime());
            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss dd/MM/yyyy");
            float total = 0;
            float totalAfterVoucher = 0;

            int columnSize = 20;
            Recipt r = new Recipt(columnSize, columnSize);
            ReciptRow rr = new ReciptRow(r.columnWidth_1, r.columnWidth_2);
            rr.setParams("HOA DON:", " ");
            r.addReciptRow(rr.row);
            rr.setParams("Ngay:", sdf.format(utilDate));
            r.addReciptRow(rr.row);
            rr.setParams("--------------------", "--------------------");
            r.addReciptRow(rr.row);

//        transaction.products.forEach(product -> {
//            float productTotal = product.productPrice * product.quantity;
//            total += productTotal;
//            rr.setParams(product.productName + " x" + String.valueOf(product.quantity), String.valueOf(productTotal));
//            r.addReciptRow(rr.row);
//        });
            JSONObject jo = new JSONObject();
            //ArrayList<Integer> voucherProductId = new ArrayList();
            transaction.vouchers.forEach(voucher -> {
                //voucherProductId.add(voucher.productId);
                jo.put(String.valueOf(voucher.productId), voucher.newPrice);
            });

            for (ProductDataSet product : transaction.products) {
                float productPrice = product.productPrice;
//            if(voucherProductId.contains(product.productId)) {
//                productPrice = 
//            }
                for (String key : jo.keySet()) {
                    if (key.equals(String.valueOf(product.productId))) {
                        productPrice = Float.parseFloat(jo.get(key).toString());
                    }
                }
                float productTotal = product.productPrice * product.quantity;
                float productTotalAfterVoucher = productPrice * product.quantity;
                total += productTotal;
                totalAfterVoucher += productTotalAfterVoucher;
                rr.setParams(product.productName + " x" + String.valueOf(product.quantity), String.format("%,.0f", product.productPrice));
                r.addReciptRow(rr.row);
            }
            System.out.println("md");
            System.out.println(membershipDiscount);
            float initialDiscount = membershipDiscount;
            if (membershipDiscount > totalAfterVoucher) {
                membershipDiscount = totalAfterVoucher;
                totalAfterVoucher = 0;

            } else {
                totalAfterVoucher -= membershipDiscount;
            }
            transaction.replenishPoint = initialDiscount - membershipDiscount;
            rr.setParams("", "");
            r.addReciptRow(rr.row);
            rr.setParams(String.format("Nhân viên phụ trách: %s", Integer.toString(GlobalVariables.userId)), "");
            r.addReciptRow(rr.row);
            rr.setParams("Đơn giá: ", String.format("%,.0f VND", total));
            r.addReciptRow(rr.row);
            rr.setParams("Chiết khấu:", "-" + String.valueOf(total - totalAfterVoucher));
            r.addReciptRow(rr.row);
            rr.setParams("Thành tiền:", String.format("%,.0f VND", totalAfterVoucher));
            r.addReciptRow(rr.row);
            rr.setParams("Ghi chú:", "");
            r.addReciptRow(rr.row);

            String note = rs.getString("note");
            int maxIndex = note.length() - 1;
            System.out.println("note max index");
            System.out.println(maxIndex);
            System.out.println((float) note.length() / columnSize);
            System.out.println(Math.ceil((float) note.length() / columnSize));
            int replenishSpaces = (int) Math.ceil((float) note.length() / columnSize) * columnSize - note.length();

            if (maxIndex > -1) {
                System.out.println("space need to replenished:");
                System.out.println(replenishSpaces);
                for (int i = 0; i < replenishSpaces; i++) {
                    note += " ";
                    System.out.println("space added");
                }
                for (int i = 1; i <= Math.ceil((float) note.length() / (columnSize * 2)); i++) {
                    System.out.println("adding row to receipt");
                    String secondStr;
                    if (Math.ceil((float) note.length() / columnSize) % 2 == 0 && i != Math.ceil((float) note.length() / (columnSize * 2))) {
                        secondStr = note.substring(columnSize * 2 * i - 20, columnSize * 2 * i);
                    } else {
                        secondStr = "";
                    }
                    rr.setParams(note.substring(columnSize * 2 * i - 40, columnSize * 2 * i - 20), secondStr);
                    r.addReciptRow(rr.row);
                }
            }

//            jTextArea1.setText(r.getRecipt());
//            System.out.println(r.getRecipt());
//
//            PdfWriter pdfw = new PdfWriter("Receipt.pdf");
//            PdfDocument pdfd = new PdfDocument(pdfw);
//            pdfd.setDefaultPageSize(PageSize.A4);
//            Document d = new Document(pdfd);
//            d.add(new Paragraph(r.getRecipt()));
//            d.close();
            transaction.total = total;
            transaction.amount = totalAfterVoucher;
//        java.util.Date test = new java.util.Date(currentTimeSql.getTime());
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
//        System.out.println(sdf.format(test));
            System.out.println(JSON.StringifyJSON(transaction));
            transactions.add(transaction);
        }
        return transactions;
    }
}
