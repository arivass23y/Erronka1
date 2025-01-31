package erronka;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DB {

	public static Connection konektatu() throws SQLException {
		KonfKargatu konfigurazioa = new KonfKargatu();
		String url = konfigurazioa.getURL();
		String user = konfigurazioa.getUSER();
		String password = konfigurazioa.getPASSWORD();

		return DriverManager.getConnection(url, user, password);
	}

}
