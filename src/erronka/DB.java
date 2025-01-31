package erronka;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Datu-basearekin konexioa kudeatzeko klasea.
 */
public class DB {

	/**
	 * Metodo honek datu-basearekin konexioa ezartzen du.
	 * 
	 * @return Datu-basearekiko konexioa.
	 * @throws SQLException Konexioa ezartzean errorea gertatzen bada.
	 */
	public static Connection konektatu() throws SQLException {
		// KonfKargatu hasieratzen da konfigurazio.txt-eko datuak lortzeko
		KonfKargatu konfigurazioa = new KonfKargatu();
		String url = konfigurazioa.getURL();
		String user = konfigurazioa.getUSER();
		String password = konfigurazioa.getPASSWORD();

		return DriverManager.getConnection(url, user, password);
	}
}
