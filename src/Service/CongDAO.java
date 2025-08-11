/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Service;

import Model.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.sql.*;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author admin
 */
public class CongDAO {

    Connection conn = dbConnection.connect();

    public void addCong(int id) throws SQLException {
        String query = "INSERT INTO cong (staff_id, date, show_up_time, shows_up) VALUES (?, CAST(GETDATE() AS DATE), ?, ?)";
        try (Connection conn = dbConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, id);
            pstmt.setTime(2, null);
            pstmt.setBoolean(3, false);
            pstmt.executeUpdate();
        }
    }
//    public static void main(String[] args) throws SQLException {
//        CongDAO c = new CongDAO();
//        c.addCong(1);
//    }

    public void update(int congId, int showsUp, Time showUpTime) throws SQLException {
        String sql = "UPDATE cong SET shows_up = ?, show_up_time = ? WHERE cong_id = ?";
        try (Connection conn = dbConnection.connect(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, showsUp);
            pstmt.setTime(2, showUpTime);
            pstmt.setInt(3, congId);
            pstmt.executeUpdate();
        }
    }

    public ResultSet getTodayCong() throws SQLException {
        String query = "";
        if (GlobalVariables.args.contains("+admin")) {
            query = """
                SELECT c.cong_id, c.staff_id, s.staff_name, c.date, 
               c.show_up_time, c.shows_up, c.shift_start, c.shift_end
                FROM cong c
                JOIN staff s ON s.staff_id = c.staff_id
                WHERE c.date = CAST(GETDATE() AS DATE)
            """;
        } else {
            query = String.format("""
                SELECT c.cong_id, c.staff_id, s.staff_name, c.date, 
                c.show_up_time, c.shows_up, c.shift_start, c.shift_end
                FROM cong c
                JOIN staff s ON s.staff_id = c.staff_id
                WHERE c.date = CAST(GETDATE() AS DATE) AND c.staff_id = %d
            """, GlobalVariables.userId);
        }

        Statement stmt = conn.createStatement();
        return stmt.executeQuery(query);
    }

    public ArrayList<Integer> getTodayCongIds() throws SQLException {
        ArrayList<Integer> ids = new ArrayList<>();
        try (Connection conn = dbConnection.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT staff_id FROM cong WHERE date = CAST(GETDATE() AS DATE)")) {
            while (rs.next()) {
                ids.add(rs.getInt("staff_id"));
            }
        }
        return ids;
    }

    public boolean isShiftTaken(Time start, Time end) throws SQLException {
        String sql = """
        SELECT COUNT(*) FROM cong 
        WHERE date = CAST(GETDATE() AS DATE)
          AND shift_start = ? AND shift_end = ?
    """;
        try (Connection conn = dbConnection.connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTime(1, start);
            ps.setTime(2, end);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    public void updateShift(int congId, Time start, Time end) throws SQLException {
        String sql = "UPDATE cong SET shift_start = ?, shift_end = ? WHERE cong_id = ?";
        try (Connection conn = dbConnection.connect(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setTime(1, start);
            ps.setTime(2, end);
            ps.setInt(3, congId);
            ps.executeUpdate();
        }
    }

    public void updateShowsUp(int congId, LocalTime gioDen, int showsUp) {
        String sql = "UPDATE cong SET show_up_time = ?, shows_up = ? WHERE cong_id = ?";
        try (Connection conn = dbConnection.connect(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setTime(1, Time.valueOf(gioDen));
            ps.setInt(2, showsUp);
            ps.setInt(3, congId);

            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ArrayList<String[]> getAllCong() throws SQLException {
        ArrayList ids = new ArrayList();
        java.time.LocalDate minDate = null;
        java.time.LocalDate maxDate = null;
        ArrayList<String[]> cong = new ArrayList();
        
        
        String query = "SELECT staff_id FROM cong GROUP BY staff_id";
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(query);
        while(rs.next()) {
            ids.add(rs.getInt("staff_id"));
        }
        
        query = "SELECT MIN(date) AS min_date FROM cong";
        stm = conn.createStatement();
        rs = stm.executeQuery(query);
        rs.next();
        minDate = Date.valueOf(rs.getString("min_date")).toLocalDate();
        
        query = "SELECT MAX(date) AS max_date FROM cong";
        stm = conn.createStatement();
        rs = stm.executeQuery(query);
        rs.next();
        maxDate = Date.valueOf(rs.getString("max_date")).toLocalDate();
        
        long dayDifference = ChronoUnit.DAYS.between(minDate, maxDate);
        for (int i = 0; i < dayDifference + 1; i++) {
            query = String.format("SELECT shows_up FROM cong WHERE date LIKE '%s'", minDate.plusDays(i));
            stm = conn.createStatement();
            rs = stm.executeQuery(query);
            String[] congOfDay = new String[ids.size()];
            int x = 0;
            while(rs.next()) {
                congOfDay[x++] = rs.getString("shows_up");
            }
            cong.add(congOfDay);
        }
        
        return cong;
    }
    public String[] getUsernames() throws SQLException {
        String query = "SELECT COUNT(username) AS coun FROM account";
        Statement stm = conn.createStatement();
        ResultSet rs = stm.executeQuery(query);
        rs.next();
        int size = rs.getInt("coun");
        
        String[] headers = new String[size];
        
        query = "SELECT username FROM account ORDER BY staff_id ASC";
        stm = conn.createStatement();
        rs = stm.executeQuery(query);
        
        int i = 0;
        while(rs.next()) {
            headers[i++] = rs.getString("username");
        }
        
        return headers;
        
        
    }
    public static void main(String[] args) throws SQLException, JsonProcessingException {
        CongDAO dao = new CongDAO();
        ArrayList cong = dao.getAllCong();
        System.out.println(JSON.StringifyJSON(cong));
        System.out.println(Arrays.toString(dao.getUsernames()));
    }
}
