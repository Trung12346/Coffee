package Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *;
 * @author pc
 */
public class dbConnection {
    private static final String URL = "jdbc:sqlserver://localhost:1433;databaseName=coffee;encrypt=false";
    private static final String USER = "sa";
    private static final String PASSWORD = "123456789";

    public static Connection connect() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Lỗi kết nối: " + e.getMessage());
            return null;
        }
    }
}