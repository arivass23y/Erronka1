package erronka;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {
	
    private static String URL = "jdbc:oracle:thin:@localhost:1521:xe";
    private static String USER = "EUSKANP";
    private static String PASSWORD = "EUSKANP";
    
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
