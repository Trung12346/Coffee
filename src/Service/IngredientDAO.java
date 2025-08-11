/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Model.IngredientDataSet;
import Model.ProductDataSet;
import Model.RMTable1;
import Model.RMTable2;
import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author maith
 */
public class IngredientDAO {

    public static void addProductIngredient(int productId, float quantity, String label, String unit) throws SQLException {
        Locale.setDefault(Locale.US);
        Connection conn = dbConnection.connect();
        String query;
        Statement stm;
        if(searchIngredient(label) == null) {
            query = String.format("INSERT INTO ingredients VALUES (N'%s', N'%s')", label, unit);
            stm = conn.createStatement();
            stm.executeUpdate(query);
            
        }
        IngredientDataSet ids = searchIngredient(label);
        query = String.format("INSERT INTO storage VALUES (CURRENT_TIMESTAMP, %d, 0)", ids.ingredientId);
        stm = conn.createStatement();
        stm.executeUpdate(query);
        
        query = String.format("INSERT INTO product_ingredients VALUES (%d, %d, %.3f)", productId, ids.ingredientId, quantity);
        stm = conn.createStatement();
        stm.executeUpdate(query);
    }

    public static ArrayList<RMTable2> getProductIngredients(int productId) throws SQLException {
        Connection conn = dbConnection.connect();
        ArrayList<RMTable2> rows = new ArrayList();

        String query = String.format("SELECT product_id, p_i.ingredient_id, ingredient_label, quantity, unit FROM (SELECT * FROM product_ingredients WHERE product_id = %d) p_i INNER JOIN ingredients i ON p_i.ingredient_id = i.ingredient_id", productId);
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(query);

        while (rs.next()) {
            rows.add(new RMTable2(rs.getInt("ingredient_id"), rs.getString("ingredient_label"), rs.getFloat("quantity"), rs.getString("unit")));
        }
        return rows;
    }

    public static ArrayList<RMTable1> getProducts() throws SQLException {
        Connection conn = dbConnection.connect();
        ArrayList<ProductDataSet> products = new ArrayList();
        ArrayList<RMTable1> rows = new ArrayList();

        String query = "SELECT * FROM product ORDER BY product_id ASC";
        Statement stm = conn.createStatement();

        ResultSet rs = stm.executeQuery(query);
        while (rs.next()) {
            products.add(new ProductDataSet(rs.getInt("product_id"), rs.getString("product_name"), rs.getFloat("product_price")));
            System.out.println(rs.getInt("product_id"));
        }
        products.forEach(product -> {
            String query_1 = String.format("SELECT product_id, ingredient_id, quantity, ready_quantity  FROM (SELECT * FROM product_ingredients WHERE product_id = %d) p INNER JOIN (SELECT item, SUM(quantity) AS ready_quantity FROM storage GROUP BY item) s ON p.ingredient_id = s.item ", product.productId);
            System.out.println(query_1);
            Statement stm_1;
            try {
                stm_1 = conn.createStatement();
                ResultSet rs_1 = stm_1.executeQuery(query_1);

                int smallestAcquirable = 0;
                while (rs_1.next()) {
                    float availableQuantity = rs_1.getFloat("ready_quantity");
                    float neededQuantity = rs_1.getFloat("quantity");
                    int doable = (int) Math.floor(availableQuantity / neededQuantity);
                    if (smallestAcquirable <= 0) {
                        smallestAcquirable = doable;
                    }
                    if (smallestAcquirable > doable) {
                        smallestAcquirable = doable;
                    }

                }
                rows.add(new RMTable1(product.productId, product.productName, smallestAcquirable));
            } catch (SQLException ex) {
                Logger.getLogger(IngredientDAO.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        return rows;
    }

    public static void updateRecipe(int productId, int ingredientId, float quantity, String unit, ArrayList deletedItems) throws SQLException {
        Locale.setDefault(Locale.US);
        Connection conn = dbConnection.connect();
        Statement stm = conn.createStatement();

        String query = String.format("UPDATE product_ingredients SET quantity = %.3f WHERE product_id = %d AND ingredient_id = %d", quantity, productId, ingredientId);
        System.out.println(query);
        stm.executeUpdate(query);

        query = String.format("UPDATE ingredients SET unit = N'%s' WHERE ingredient_id = %d", unit, ingredientId);
        System.out.println(query);
        stm = conn.createStatement();
        stm.executeUpdate(query);
        
        deletedItems.forEach(item -> {
            try {
                String query_1 = String.format("DELETE FROM product_ingredients WHERE product_id = %d AND ingredient_id = %d", productId, item);
                Statement stm_1 = conn.createStatement();
                stm_1.executeUpdate(query_1);
            } catch (SQLException ex) {
                Logger.getLogger(IngredientDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    public static IngredientDataSet searchIngredient(String label) {
        Locale.setDefault(Locale.US);
        Connection conn = dbConnection.connect();
        

        String query = String.format("SELECT * FROM ingredients WHERE ingredient_label LIKE N'%s'", label);
        try {
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);
            rs.next();

            return new IngredientDataSet(rs.getInt("ingredient_id"), rs.getString("ingredient_label"), rs.getString("unit"));
        } catch (SQLException ex) {
            return null;
        }
    }

    public static void main(String[] args) throws SQLException {
        getProducts();
    }
}
