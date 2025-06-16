/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;
import Service.dbConnection;
import java.sql.*;

public class BaocaoDAO {
    public ResultSet loaddata() {
        Connection conn = null;
        try {
            conn = dbConnection.connect();
            if (conn == null) {
                throw new SQLException("Không thể kết nối đến database");
            }
            String query = "SELECT report_id, report_date, content FROM report";
            PreparedStatement stmt = conn.prepareStatement(query);
            return stmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean addReport(String reportDate, String content) {
        Connection conn = null;
        try {
            conn = dbConnection.connect();
            if (conn == null) {
                throw new SQLException("Không thể kết nối đến database");
            }
            String query = "INSERT INTO report (report_date, content) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, reportDate);
            stmt.setString(2, content);
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
