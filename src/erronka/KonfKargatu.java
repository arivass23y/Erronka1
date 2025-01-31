package erronka;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;

/**
 * Konfigurazio fitxategitik datuak kargatzeko klasea.
 */
public class KonfKargatu {
	private String URL;
	private String USER;
	private String PASSWORD;
	private String XML;

	/**
	 * Eraikitzaileak konfigurazio fitxategia irakurri eta datuak kargatzen ditu.
	 */
	public KonfKargatu() {
		// Properties objektua konfigurazio.txt-eko datuak kudeatzeko
		Properties propietateak = new Properties();
		try {
			// konfigurazio.txt kargatu eta irakurri
			propietateak.load(new FileInputStream(new File("fitxategiak/konfigurazio.txt")));
			URL = propietateak.get("URL").toString();
			USER = propietateak.get("USER").toString();
			PASSWORD = propietateak.get("PASSWORD").toString();
			XML = propietateak.get("XML").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return Datu-basearen URL-a.
	 */
	public String getURL() {
		return URL;
	}

	/**
	 * @return Datu-basearen erabiltzailea.
	 */
	public String getUSER() {
		return USER;
	}

	/**
	 * @return Datu-basearen pasahitza.
	 */
	public String getPASSWORD() {
		return PASSWORD;
	}

	/**
	 * @return XML fitxategiaren bidea.
	 */
	public String getXML() {
		return XML;
	}
}
