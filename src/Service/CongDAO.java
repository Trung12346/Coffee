/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;
import java.sql.*;
import java.util.ArrayList;
/**
 *
 * @author admin
 */
public class CongDAO {
    Connection conn = dbConnection.connect();
    public void addCong(int id) throws SQLException {
        String query = String.format("INSERT INTO cong VALUES (%d, FORMAT(GETDATE(), 'yyyy-MM-dd'),0)", id);
        String test = "SELECT FORMAT(GETDATE(), 'yyyy-MM-dd') AS date";
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(test);
        rs.next();
        System.out.println(rs.getString("date"));
        stm.executeUpdate(query);
    }
    public static void main(String[] args) throws SQLException {
        CongDAO c = new CongDAO();
        c.addCong(1);
    }
    public void update(int id, boolean cong) throws SQLException{
        String query = "UPDATE cong SET cong=?  where cong_id = ? ";
        PreparedStatement ps = conn.prepareStatement(query);
        int congSQL = 0;
        if(cong){
        congSQL = 1;
        }
        ps.setInt(2, id);
        ps.setInt(1, congSQL);
        ps.executeUpdate();
    }
    public ResultSet getTodayCong() throws SQLException {
        String query = 
                "SELECT cong_id, cong, today_cong.staff_id, staff.staff_name FROM " +
                "(SELECT cong_id,cong, staff_id FROM cong where cong_date like FORMAT(GETDATE(), 'yyyy-MM-dd')) AS today_cong " +
                "JOIN staff ON staff.staff_id = today_cong.staff_id";
        Statement stmt = conn.createStatement();
        ResultSet rs= stmt.executeQuery(query);
        return rs;
    }
    public ArrayList getTodayCongIds() throws SQLException {
          ArrayList ids = new ArrayList();
        Connection conn = dbConnection.connect();
        String query = "SELECT staff_id FROM cong where cong_date like FORMAT(GETDATE(), 'yyyy-MM-dd')";
        Statement stmt = conn.createStatement();
        ResultSet rs= stmt.executeQuery(query);
        while(rs.next()){
        ids.add(rs.getInt("staff_id"));
        }
        return ids;
    }
}
