package erronka;

import java.util.Properties;
import java.io.File;
import java.io.FileInputStream;

public class KonfKargatu {
	private String URL;
	private String USER;
	private String PASSWORD;
	private String XML;

	public KonfKargatu() {
		Properties propietateak = new Properties();

		try {
			propietateak.load(new FileInputStream(new File("fitxategiak/konfigurazio.txt")));
			URL = propietateak.get("URL").toString();
			USER = propietateak.get("USER").toString();
			PASSWORD = propietateak.get("PASSWORD").toString();
			XML = propietateak.get("XML").toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getURL() {
		return URL;
	}

	public String getUSER() {
		return USER;
	}

	public String getPASSWORD() {
		return PASSWORD;
	}

	public String getXML() {
		return XML;
	}

}
