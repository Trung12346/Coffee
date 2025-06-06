/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;
import Service.dbConnection;
import java.sql.*;
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
    
}
