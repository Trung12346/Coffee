/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Model.IngredientDataSet;
import Service.TransactionDAO;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import Model.Table2;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import Model.ProductDataSet;
import Model.TransactionDataSet;
import java.util.concurrent.atomic.AtomicInteger;
import Model.VoucherDataSet;
import java.util.Arrays;
import Model.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import Model.objectToInt;
import java.text.SimpleDateFormat;
import org.json.*;
import Model.MembershipDataSet;
import Model.Recipt;
import Model.ReciptRow;
import Model.TableCellListener;
import Service.GlobalVariables;
import static Service.IngredientDAO.searchIngredient;
import com.itextpdf.kernel.geom.PageSize;
import java.util.HashSet;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.*;
import com.itextpdf.layout.element.Paragraph;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.util.Locale;
import javax.swing.AbstractAction;
import javax.swing.Action;

/**
 *
 * @author maith
 */
public class CreateTransaction extends javax.swing.JPanel {

    /**
     * Creates new form CreateTransaction
     */
    TransactionDAO transactDAO = new TransactionDAO();
    DefaultTableModel Model_1;
    DefaultTableModel Model_2;
    DefaultTableModel Model_3;
    ArrayList<Table2> Table2Rows = new ArrayList();
    ArrayList<TransactionDataSet> awaitPayments = new ArrayList();

    public CreateTransaction() throws SQLException {
        initComponents();

        Model_1 = (DefaultTableModel) jTable1.getModel();
        Model_2 = (DefaultTableModel) jTable2.getModel();
        Model_3 = (DefaultTableModel) jTable3.getModel();
        loadTable_1();
        MembershipInte frameMembershipInte = new MembershipInte();
        frameMembershipInte.setSize(275, 140);
        frameMembershipInte.setLocation(0, 0);

        jPanel2.removeAll();
        jPanel2.add(frameMembershipInte, BorderLayout.CENTER);
        jPanel2.repaint();
        jPanel2.revalidate();
        Action action = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                TableCellListener tcl = (TableCellListener) e.getSource();
//                System.out.println("Row   : " + tcl.getRow());
//                System.out.println("Column: " + tcl.getColumn());
//                System.out.println("Old   : " + tcl.getOldValue());
//                System.out.println("New   : " + tcl.getNewValue());
                if(tcl.getColumn() == 2) {
                    Table2Rows.get(tcl.getRow() - 1).quantity = tcl.getNewValue().toString();
                }
            }
        };

        TableCellListener tcl = new TableCellListener(jTable2, action);
    }

    public void loadTable_1() throws SQLException {
        ResultSet rs = transactDAO.getProducts();
        Model_1.setRowCount(1);
        while (rs.next()) {
            Model_1.addRow(new Object[]{
                String.valueOf(rs.getInt("product_id")),
                rs.getString("product_name"),
                rs.getFloat("product_price"),
                transactDAO.howManyCraftable(rs.getInt("product_id"))
            });
        }
    }

    public void loadTable_2(ArrayList<Table2> rows) {
        Model_2.setRowCount(1);
        rows.forEach(row -> {
            Model_2.addRow(new Object[]{
                //row.id,
                row.product,
                row.price,
                row.quantity
            });
        });
    }

    public String requestRecipt() throws JsonProcessingException, SQLException, FileNotFoundException, Exception {
        Locale.setDefault(Locale.US);
        ArrayList<Integer> productIds = new ArrayList();
        ArrayList quantities = new ArrayList();
        ArrayList<ProductDataSet> products = new ArrayList();

//        for (int i = 1; i < jTable2.getRowCount(); i++) {
//            productIds.add(Integer.parseInt(jTable2.getValueAt(i, 0).toString()));
//        }
        Table2Rows.forEach(row -> {
            productIds.add(Integer.parseInt(row.id));
            quantities.add(Integer.parseInt(row.quantity));
        });
        
//        for (int i = 1; i < jTable2.getRowCount(); i++) {
//            quantities.add(jTable2.getValueAt(i, 3));
//        }

        AtomicInteger index = new AtomicInteger();
        productIds.forEach(productId -> {
            try {
                ResultSet rs = transactDAO.selectProductById(Integer.parseInt(productId.toString()));
                rs.next();

                //CONTINUE HERE
                products.add(new ProductDataSet(rs.getInt("product_id"),
                        rs.getString("product_name"),
                        rs.getFloat("product_price"),
                        Integer.parseInt(quantities.get(index.getAndIncrement()).toString())
                ));
            } catch (SQLException ex) {
                Logger.getLogger(CreateTransaction.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        for (int i = 0; i < productIds.size(); i++) {
            int readyQuantity = transactDAO.howManyCraftable(productIds.get(i));
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

            if (readyQuantity < quantity) {
                JOptionPane.showMessageDialog(this, String.format("san pham %s da het hang", productName));
                throw new Exception();
            }
        }

        int[] ids = objectToInt.objectToInteger(productIds);
        ArrayList<VoucherDataSet> vouchers = new ArrayList();
        try {
            String vouchersRawJSON = transactDAO.searchVoucher(ids);
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
            JSONObject jo = JSON.parseJSON(transactDAO.searchMembership(jTextField1.getText()));
            //if(jo.getString("securityCode").equals(jTextField2.getText())) {
            mds = new MembershipDataSet(
                    jo.getInt("id"),
                    jo.getString("name"),
                    jo.getString("phone"),
                    jo.getString("securityCode"),
                    jo.getInt("point")
            );
            if (jCheckBox1.isSelected()) {
                membershipDiscount = jo.getInt("point");
            }

            //}
        } catch (NullPointerException ex) {
            System.out.println("found none");
        }
        System.out.println("HEY");
        System.out.println(mds.id);
        TransactionDataSet transaction = new TransactionDataSet(
                currentTimeSql,
                products,
                vouchers,
                mds,
                GlobalVariables.userId,
                0,
                jCheckBox1.isSelected(),
                0,
                jComboBox1.getSelectedItem().toString(),
                "Chưa thanh toán",
                0,
                jTextArea2.getText()
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

        String note = jTextArea2.getText();
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
                if(Math.ceil((float) note.length() / columnSize) % 2 == 0 && i != Math.ceil((float) note.length() / (columnSize * 2))) {
                    secondStr = note.substring(columnSize * 2 * i - 20, columnSize * 2 * i);
                } else {
                    secondStr = "";
                }
                rr.setParams(note.substring(columnSize * 2 * i - 40, columnSize * 2 * i - 20), secondStr);
                r.addReciptRow(rr.row);
            }
        }

        jTextArea1.setText(r.getRecipt());
        System.out.println(r.getRecipt());

        PdfWriter pdfw = new PdfWriter("Receipt.pdf");
        PdfDocument pdfd = new PdfDocument(pdfw);
        pdfd.setDefaultPageSize(PageSize.A4);
        Document d = new Document(pdfd);
        d.add(new Paragraph(r.getRecipt()));
        d.close();

        transaction.total = total;
        transaction.amount = totalAfterVoucher;
//        java.util.Date test = new java.util.Date(currentTimeSql.getTime());
//        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
//        System.out.println(sdf.format(test));
        System.out.println(JSON.StringifyJSON(transaction));

        products.forEach(product -> {
            try {
                transactDAO.consumeMaterial(product.productId, product.quantity);
            } catch (SQLException ex) {
                Logger.getLogger(CreateTransaction.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        awaitPayments.add(transaction);
        return JSON.StringifyJSON(transaction);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel6 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jTextField2 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Sản phẩm", "Giá", "Còn lại"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTable1);

        jScrollPane1.setViewportView(jScrollPane4);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Sản phẩm", "Giá", "Số lượng"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(jTable2);

        jScrollPane2.setViewportView(jScrollPane5);

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setFont(new java.awt.Font("Monospaced", 0, 12)); // NOI18N
        jTextArea1.setRows(5);
        jTextArea1.setText("HOÁ ĐƠN:");
        jScrollPane3.setViewportView(jTextArea1);

        jLabel1.setText("Sản phẩm");

        jLabel2.setText("Đơn");

        jButton2.setText("Thêm sản phẩm");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Xoá sản phẩm khỏi đơn");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel3.setText("SDT Hội viên");

        jButton1.setText("Tạo Hoá Đơn");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Sử dụng điểm");

        jLabel6.setText("0 điểm");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tiền mặt", "Tài khoản" }));

        jLabel7.setText("Số tiền khách trả");

        jLabel4.setText("Phương thức thanh toán:");

        jLabel8.setText("Tiền thừa:");

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel9.setText("0 VND");
        jLabel9.setToolTipText("");

        jButton4.setText("Xác nhận đã thanh toán");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel5.setText("Đang có:");

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane8.setViewportView(jTextArea2);

        jLabel11.setText("Ghi chú:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel11)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jScrollPane8)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheckBox1)
                                    .addComponent(jLabel4)
                                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGap(162, 162, 162)
                        .addComponent(jButton4)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addGap(24, 24, 24)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addGap(175, 175, 175))
        );

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Mã hoá đơn", "Sản phẩm", "Thành tiền"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTable3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTable3MouseClicked(evt);
            }
        });
        jScrollPane7.setViewportView(jTable3);

        jScrollPane6.setViewportView(jScrollPane7);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 313, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 142, Short.MAX_VALUE)
        );

        jLabel10.setText("Thanh toán đang chờ được xác nhận:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jButton3)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton2)
                                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 665, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 666, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addGap(21, 21, 21))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jLabel10)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 232, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(274, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow >= 1) {
            ArrayList products = new ArrayList();
            for (int i = 1; i < jTable2.getRowCount(); i++) {
                products.add(jTable2.getValueAt(i, 0));
            }

            try {
                if (products.contains(jTable1.getValueAt(selectedRow, 0).toString())) {
                    JOptionPane.showMessageDialog(this, "Da co san pham");
                    throw new Exception("product was already added");
                }
                ResultSet rs = transactDAO.selectProductById(Integer.parseInt(jTable1.getValueAt(selectedRow, 0).toString()));
                rs.next();
                Table2Rows.add(new Table2(String.valueOf(rs.getInt("product_id")),
                        rs.getString("product_name"),
                        String.valueOf(rs.getFloat("product_price")),
                        "1"
                ));
                loadTable_2(Table2Rows);
            } catch (SQLException ex) {
                Logger.getLogger(CreateTransaction.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CreateTransaction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        int selectedRow = jTable2.getSelectedRow();
        if (selectedRow >= 1) {
            Table2Rows.remove(selectedRow - 1);
            loadTable_2(Table2Rows);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        String phone = jTextField1.getText().trim();
        //String sc = jTextField2.getText();
        try {
            // TODO add your handling code here:
            transactDAO.createTransaction(requestRecipt());
            Model_3.setRowCount(0);
            awaitPayments.forEach(row -> {
                String products = "";
                for (int i = 0; i < row.products.size(); i++) {
                    if (i == row.products.size() - 1) {
                        products += row.products.get(i).productName;
                    } else {
                        products += row.products.get(i).productName + ", ";
                    }

                }

                Model_3.addRow(new Object[]{TransactionDAO.lastRecordGlb, products, row.amount});
            });
        } catch (JsonProcessingException | SQLException | FileNotFoundException ex) {
            Logger.getLogger(CreateTransaction.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTable3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTable3MouseClicked
        // TODO add your handling code here:
        int selectedRow = jTable3.getSelectedRow();
        if (selectedRow > -1) {
            TransactionDataSet receipt = awaitPayments.get(selectedRow);
            ArrayList<ProductDataSet> products = receipt.products;
            Table2Rows = new ArrayList();
            products.forEach(product -> {
                Table2Rows.add(new Table2(Integer.toString(product.productId),
                        product.productName,
                        Float.toString(product.productPrice),
                        Integer.toString(product.quantity)
                ));

            });
            loadTable_2(Table2Rows);

            try {
                requestRecipt();
            } catch (SQLException ex) {
                Logger.getLogger(CreateTransaction.class.getName()).log(Level.SEVERE, null, ex);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(CreateTransaction.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                Logger.getLogger(CreateTransaction.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }//GEN-LAST:event_jTable3MouseClicked

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        try {
            float customerPaid = Float.parseFloat(jTextField2.getText());
            
            float amount = awaitPayments.get(jTable3.getSelectedRow()).amount;
            float changes = 0;
            if(customerPaid < amount) {
                throw new Exception();
            } else {
                changes = customerPaid - amount;
            }
            jLabel9.setText(String.format("%,.0f VND", changes));
            transactDAO.setReceiptCompleted(awaitPayments.get(jTable3.getSelectedRow()).reciptId);
            
        } catch(Exception ex) {
            System.out.println(ex);
        }
        
        
    }//GEN-LAST:event_jButton4ActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(CreateTransaction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(CreateTransaction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(CreateTransaction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(CreateTransaction.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    new CreateTransaction().setVisible(true);
//                } catch (SQLException ex) {
//                    Logger.getLogger(CreateTransaction.class.getName()).log(Level.SEVERE, null, ex);
//                }
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
