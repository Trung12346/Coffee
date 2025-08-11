/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;
import Service.dbConnection;
import java.sql.*;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author ADMIN
 */
public class SanPhamDAO {
    public ResultSet hienthi(){
        try {
            Connection conn=dbConnection.connect();
            String query = "SELECT * FROM Product";
            PreparedStatement ps= conn.prepareStatement(query);
            return ps.executeQuery();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean add(String ten,Float gia){
        try {
            Connection conn=dbConnection.connect();
            String query = "insert into product(product_name,product_price) values(?,?)";
            PreparedStatement ps=conn.prepareStatement(query);
            ps.setString(1, ten);
            ps.setFloat(2, gia);
            return ps.executeUpdate()>0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public void update(int id, String name, float price) throws SQLException {
        Locale.setDefault(Locale.US);
        Connection conn=dbConnection.connect();
        
        String query = String.format("UPDATE product SET product_price = %f, product_name = '%s' WHERE product_id = %d", price, name, id);
        Statement stm = conn.createStatement();
        stm.executeUpdate(query);
    }
    public boolean isExist(String name) {
        try {
            Connection conn = dbConnection.connect();
            
            String query = String.format("SELECT * FROM product WHERE product_name LIKE N'%s'", name);
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(query);
            rs.next();
            rs.getInt("product_id");
            return true;
            
        } catch (SQLException ex) {
            
            Logger.getLogger(SanPhamDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
