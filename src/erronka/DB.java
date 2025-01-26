package erronka;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

    private static String URL = "jdbc:mysql://localhost:3306/world";
    private static String USER = "root";
    private static String PASSWORD = "root";

    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
