package erronka;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

public class XML_Inportatu {
	private static Connection conn;

	public XML_Inportatu() throws SQLException {
		conn = DB.konektatu();
	}

	public void Inportazioak() throws NumberFormatException, Exception {
		System.out.println("Datuak inportatzen...");

		inportatuHerriak();
		inportatuProbintziak();
		inportatuKanpinak();
		inportatuEtiketak();
		inportatuKanpinEtiketak();

		System.out.println("Datuak ondo inportatu dira.");
	}

	public void inportatuHerriak() {
		try {
			KonfKargatu konfigurazioa = new KonfKargatu();
			File file = new File(konfigurazioa.getXML());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();

			NodeList kanpinaNode = doc.getElementsByTagName("row");
			Connection conn = DB.konektatu();
			conn.setAutoCommit(false);

			String query = "INSERT INTO HERRIAK (KODEA, IZENA) VALUES (?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(query);

			for (int i = 0; i < kanpinaNode.getLength(); i++) {
				Element element = (Element) kanpinaNode.item(i);

				String herriKodea = element.getElementsByTagName("municipalitycode").item(0).getTextContent();
				String herriIzena = element.getElementsByTagName("municipality").item(0).getTextContent();

				if (!herriaKonfirmatu(conn, Integer.parseInt(herriKodea))) {
					pstmt.setString(1, herriKodea);
					pstmt.setString(2, herriIzena);
					pstmt.executeUpdate();
				}
			}

			pstmt.close();
			conn.close();
		} catch (Exception e) {
			System.out.println("Errorea HERRIAK taulan datuak sartzerakoan.");
			e.printStackTrace();
		}

	}

	public void inportatuProbintziak() throws NumberFormatException, Exception {
		try {
			KonfKargatu konfigurazioa = new KonfKargatu();
			File file = new File(konfigurazioa.getXML());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();

			NodeList kanpinaNode = doc.getElementsByTagName("row");
			Connection conn = DB.konektatu();
			conn.setAutoCommit(false);

			String query = "INSERT INTO PROBINTZIAK (KODEA, IZENA) VALUES (?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(query);

			for (int i = 0; i < kanpinaNode.getLength(); i++) {
				Element element = (Element) kanpinaNode.item(i);

				String probintziaKodea = element.getElementsByTagName("territorycode").item(0).getTextContent();
				String probintziaIzena = element.getElementsByTagName("territory").item(0).getTextContent();

				if (!probintziaKonfirmatu(conn, Integer.parseInt(probintziaKodea))) {
					pstmt.setString(1, probintziaKodea);
					pstmt.setString(2, probintziaIzena);
					pstmt.executeUpdate();
				}
			}

			pstmt.close();
			conn.close();
		} catch (Exception e) {
			System.out.println("Errorea PROBINTZIAK taulan datuak sartzerakoan.");
			e.printStackTrace();
		}

	}

	public void inportatuKanpinak() {
		try {
			KonfKargatu konfigurazioa = new KonfKargatu();
			File file = new File(konfigurazioa.getXML());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();

			NodeList kanpinaNode = doc.getElementsByTagName("row");
			Connection conn = DB.konektatu();
			conn.setAutoCommit(false);

			String queryKanpinak = "INSERT INTO KANPINAK (KODEA, IZENA, DESKRIBAPENA, KOKALEKUA, TELEFONOA, HELBIDEA, EMAILA, WEBGUNEA, KATEGORIA, EDUKIERA, POSTAKODEA, HERRI_KODEA, PROBINTZIA_KODEA, FRIENDLY_URL, PHYSICAL_URL, DATA_XML, METADATA_XML, ZIP_FILE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement pstmt = conn.prepareStatement(queryKanpinak);

			for (int i = 0; i < kanpinaNode.getLength(); i++) {
				Element element = (Element) kanpinaNode.item(i);

				pstmt.setString(1, getElementText(element, "signatura"));
				pstmt.setString(2, getElementText(element, "documentName"));
				pstmt.setString(3, getElementText(element, "documentDescription"));
				pstmt.setString(4, getElementText(element, "country"));
				pstmt.setString(5, getElementText(element, "phone").replace(" ", ""));
				pstmt.setString(6, getElementText(element, "address"));
				pstmt.setString(7, getElementText(element, "tourismEmail"));
				pstmt.setString(8, getElementText(element, "web"));
				pstmt.setString(9, getElementText(element, "category"));
				pstmt.setInt(10, Integer.parseInt(getElementText(element, "capacity")));
				pstmt.setString(11, getElementText(element, "postalCode"));
				pstmt.setInt(12, Integer.parseInt(getElementText(element, "municipalitycode")));
				pstmt.setInt(13, Integer.parseInt(getElementText(element, "territorycode")));
				pstmt.setString(14, getElementText(element, "friendlyUrl"));
				pstmt.setString(15, getElementText(element, "physicalUrl"));
				pstmt.setString(16, getElementText(element, "dataXML"));
				pstmt.setString(17, getElementText(element, "metadataXML"));
				pstmt.setString(18, getElementText(element, "zipFile"));

				pstmt.executeUpdate();

			}
			pstmt.close();
			conn.close();

		} catch (Exception e) {
			System.out.println("Errorea KANPINAK taulan datuak sartzerakoan.");
			e.printStackTrace();
		}
	}

	public void inportatuEtiketak() {
		try {
			KonfKargatu konfigurazioa = new KonfKargatu();
			File file = new File(konfigurazioa.getXML());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();

			NodeList kanpinaNode = doc.getElementsByTagName("row");
			Connection conn = DB.konektatu();
			conn.setAutoCommit(false);

			String query = "INSERT INTO ETIKETAK (ETIKETA) VALUES (?)";
			PreparedStatement pstmt = conn.prepareStatement(query);

			for (int i = 0; i < kanpinaNode.getLength(); i++) {
				Element element = (Element) kanpinaNode.item(i);

				String etiketaIzena = element.getElementsByTagName("templateType").item(0).getTextContent();

				if (!etiketaKonfirmatu(conn, etiketaIzena)) {
					pstmt.setString(1, etiketaIzena);
					pstmt.executeUpdate();
				}

			}

			pstmt.close();
			conn.close();

		} catch (Exception e) {
			System.out.println("Errorea ETIKETAK taulan datuak sartzerakoan.");
			e.printStackTrace();
		}

	}

	public static boolean herriaKonfirmatu(Connection conn, int herriKode) throws Exception {
		String query = "SELECT COUNT(*) FROM HERRIAK WHERE KODEA = ?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, herriKode);
		ResultSet rs = pstmt.executeQuery();
		boolean exists = false;
		if (rs.next()) {
			exists = rs.getInt(1) > 0; // Si hay al menos una coincidencia, el código ya existe
		}
		rs.close();
		pstmt.close();
		return exists;
	}

	public static boolean probintziaKonfirmatu(Connection conn, int probintziaKode) throws Exception {
		String query = "SELECT COUNT(*) FROM PROBINTZIAK WHERE KODEA = ?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, probintziaKode);
		ResultSet rs = pstmt.executeQuery();
		boolean exists = false;
		if (rs.next()) {
			exists = rs.getInt(1) > 0; // Si hay al menos una coincidencia, el código ya existe
		}
		rs.close();
		pstmt.close();
		return exists;
	}

	public static boolean etiketaKonfirmatu(Connection conn, String etiketa) throws SQLException {
		String query = "SELECT COUNT(*) FROM ETIKETAK WHERE ETIKETA = ?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setString(1, etiketa);
		ResultSet rs = pstmt.executeQuery();
		boolean exists = false;
		if (rs.next()) {
			exists = rs.getInt(1) > 0;
		}
		rs.close();
		pstmt.close();
		return exists;
	}

	// Metodo para comprobar que el nodo no este vacio
	private String getElementText(Element element, String tagName) {
		NodeList nodeList = element.getElementsByTagName(tagName);
		if (nodeList != null && nodeList.getLength() > 0 && nodeList.item(0) != null) {
			return nodeList.item(0).getTextContent();
		}
		return ""; // Retorna una cadena vacía si el nodo no existe
	}

	public void inportatuKanpinEtiketak() {
		try {
			KonfKargatu konfigurazioa = new KonfKargatu();
			File file = new File(konfigurazioa.getXML());
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();

			NodeList kanpinaNode = doc.getElementsByTagName("row");
			Connection conn = DB.konektatu();
			conn.setAutoCommit(false);

			String queryInsertKanpinEtiketa = "INSERT INTO KANPIN_ETIKETAK (ID_ETIKETA, KANPIN_KODEA) VALUES (?, ?)";
			PreparedStatement pstmtKanpinEtiketa = conn.prepareStatement(queryInsertKanpinEtiketa);

			for (int i = 0; i < kanpinaNode.getLength(); i++) {
				Element element = (Element) kanpinaNode.item(i);
				String kanpinKodea = getElementText(element, "signatura"); // Código del kanpin
				NodeList etiketakList = element.getElementsByTagName("templateType");

				for (int j = 0; j < etiketakList.getLength(); j++) {
					String etiketaIzena = etiketakList.item(j).getTextContent().trim();

					// Verificar si la etiqueta existe o insertarla si no existe
					int etiketaId = getOrCreateEtiketa(conn, etiketaIzena);

					// Insertar la relación si no existe
					if (!kanpinEtiketaKonfirmatu(conn, etiketaId, kanpinKodea)) {
						pstmtKanpinEtiketa.setInt(1, etiketaId);
						pstmtKanpinEtiketa.setString(2, kanpinKodea);
						pstmtKanpinEtiketa.executeUpdate();
					}
				}
			}

			pstmtKanpinEtiketa.close();
			conn.commit();
			conn.close();

		} catch (Exception e) {
			System.out.println("Errorea KANPIN_ETIKETAK taulan datuak sartzerakoan.");
			e.printStackTrace();
		}
	}

	// Método para obtener el ID de una etiqueta o insertarla si no existe
	private int getOrCreateEtiketa(Connection conn, String etiketaIzena) throws SQLException {
		String querySelect = "SELECT ID FROM ETIKETAK WHERE ETIKETA = ?";
		PreparedStatement pstmtSelect = conn.prepareStatement(querySelect);
		pstmtSelect.setString(1, etiketaIzena);
		ResultSet rs = pstmtSelect.executeQuery();

		if (rs.next()) {
			int id = rs.getInt(1);
			rs.close();
			pstmtSelect.close();
			return id;
		}

		rs.close();
		pstmtSelect.close();

		// Insertar la etiqueta porque no existe
		String queryInsert = "INSERT INTO ETIKETAK (ETIKETA) VALUES (?) RETURNING ID";
		PreparedStatement pstmtInsert = conn.prepareStatement(queryInsert);
		pstmtInsert.setString(1, etiketaIzena);
		ResultSet rsInsert = pstmtInsert.executeQuery();

		int newId = -1;
		if (rsInsert.next()) {
			newId = rsInsert.getInt(1);
		}

		rsInsert.close();
		pstmtInsert.close();
		return newId;
	}

	// Método para verificar si la relación ya existe
	private boolean kanpinEtiketaKonfirmatu(Connection conn, int etiketaId, String kanpinKodea) throws SQLException {
		String query = "SELECT COUNT(*) FROM KANPIN_ETIKETAK WHERE ID_ETIKETA = ? AND KANPIN_KODEA = ?";
		PreparedStatement pstmt = conn.prepareStatement(query);
		pstmt.setInt(1, etiketaId);
		pstmt.setString(2, kanpinKodea);
		ResultSet rs = pstmt.executeQuery();

		boolean exists = false;
		if (rs.next()) {
			exists = rs.getInt(1) > 0;
		}

		rs.close();
		pstmt.close();
		return exists;
	}

}
