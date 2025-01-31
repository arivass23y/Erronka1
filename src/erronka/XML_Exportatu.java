package erronka;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class XML_Exportatu {
	private static Connection conn;

	// CON ESTO NOS ASEGURAMOS DE QUE LA CONEXION A LA BASE DE DATOS NUNCA VAYA A
	// SER NULA
	public XML_Exportatu() throws SQLException {
		conn = DB.konektatu();
	}

	public void xmlExportazioa(String probintziaIzena) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.newDocument();

			System.out.println("XML-a exportatzen...");

			// EL TOCHO, LA RAIZ, ES HIJO DE DOC, EL DOCUMENTO, POR ESO ES APPEND CHILD
			Element raiz = doc.createElement("kanpinak");
			doc.appendChild(raiz);

			String query = "SELECT K.KODEA, K.IZENA, K.DESKRIBAPENA, K.KATEGORIA, K.EDUKIERA, K.KOKALEKUA, K.HELBIDEA, K.POSTAKODEA, "
					+ "K.TELEFONOA, K.EMAILA, K.WEBGUNEA, K.FRIENDLY_URL, K.PHYSICAL_URL, K.DATA_XML, K.METADATA_XML, K.ZIP_FILE, "
					+ "H.IZENA AS HERRIA, H.KODEA AS HERRIKODEA, P.IZENA AS PROBINTZIA, P.KODEA AS PROBINTZIAKODEA, K.KATEGORIA, K.EDUKIERA "
					+ "FROM KANPINAK K " + "JOIN HERRIAK H ON K.HERRI_KODEA = H.KODEA "
					+ "JOIN PROBINTZIAK P ON K.PROBINTZIA_KODEA = P.KODEA " + "WHERE P.IZENA = '" + probintziaIzena
					+ "'";

			PreparedStatement pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				// CREAMOS OTRO ELEMENTO, EN ESTE CASO TIENE ATRIBUTO ID <kanpina id = "x">
				Element kanpina = doc.createElement("kanpina");
				kanpina.setAttribute("id", rs.getString("KODEA"));
				raiz.appendChild(kanpina);

				// AÑADE ELEMENTOS SIMPLES A <kanpina>
				addElementWithText(doc, kanpina, "izena", rs.getString("IZENA"));
				addElementWithText(doc, kanpina, "deskribapena", rs.getString("DESKRIBAPENA"));
				addElementWithText(doc, kanpina, "kategoria", rs.getString("KATEGORIA"));
				addElementWithText(doc, kanpina, "edukiera", String.valueOf(rs.getInt("EDUKIERA")));
				addElementWithText(doc, kanpina, "kokalekua", rs.getString("KOKALEKUA"));

				// Create <idazlea> node and add it to <liburua>
				// SE HACE DISTINTO A LOS DE ARRIBA POR NO SER UN ELEMENTO, SI NO UN NODO
				Element helbidea = doc.createElement("helbidea");
				kanpina.appendChild(helbidea);

				// IGUAL QUE LOS DE ARRIBA PERO EN EL NODO IDAZLEA
				addElementWithText(doc, helbidea, "kalea", rs.getString("HELBIDEA"));
				addElementWithText(doc, helbidea, "postakodea", rs.getString("POSTAKODEA"));

				// Crear el elemento <herria>
				Element herria = doc.createElement("herria");
				herria.setTextContent(rs.getString("HERRIA"));
				herria.setAttribute("id", rs.getString("HERRIKODEA"));
				helbidea.appendChild(herria);

				// Crear el elemento <probintzia>
				Element probintzia = doc.createElement("probintzia");
				probintzia.setTextContent(rs.getString("PROBINTZIA"));
				probintzia.setAttribute("id", rs.getString("PROBINTZIAKODEA"));
				helbidea.appendChild(probintzia);

				addElementWithText(doc, kanpina, "telefonoa", rs.getString("TELEFONOA"));
				addElementWithText(doc, kanpina, "emaila", rs.getString("EMAILA"));
				addElementWithText(doc, kanpina, "webgunea", rs.getString("WEBGUNEA"));

				// Create <idazlea> node and add it to <liburua>
				// SE HACE DISTINTO A LOS DE ARRIBA POR NO SER UN ELEMENTO, SI NO UN NODO
				Element estekak = doc.createElement("estekak");
				kanpina.appendChild(estekak);

				// IGUAL QUE LOS DE ARRIBA PERO EN EL NODO IDAZLEA
				addElementWithText(doc, estekak, "friendly", rs.getString("FRIENDLY_URL"));
				addElementWithText(doc, estekak, "physical", rs.getString("PHYSICAL_URL"));

				// Create <idazlea> node and add it to <liburua>
				// SE HACE DISTINTO A LOS DE ARRIBA POR NO SER UN ELEMENTO, SI NO UN NODO
				Element fitxategiak = doc.createElement("fitxategiak");
				kanpina.appendChild(fitxategiak);

				// IGUAL QUE LOS DE ARRIBA PERO EN EL NODO IDAZLEA
				addElementWithText(doc, fitxategiak, "dataXML", rs.getString("DATA_XML"));
				addElementWithText(doc, fitxategiak, "metadataXML", rs.getString("METADATA_XML"));
				addElementWithText(doc, fitxategiak, "zipFile", rs.getString("ZIP_FILE"));
			}

			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("sortutako_fitx\\XML_Exportazioa.xml"));

			// Linea magica
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(source, result);

			System.out.println("XML-a ondo exportatu da!");

			rs.close();
			pstmt.close();
			conn.close();

		} catch (Exception e) {
			System.out.println("Errorea XML-a exportatzerakoan: " + e);
			e.printStackTrace();
		}
	}

	// PARA AÑADIR DIRECTAMENTE EL ELEMENTO DENTRO DE UN NODO, CON TEXTO Y TODO
	private static void addElementWithText(Document doc, Element parent, String tagName, String textContent) {
		Element element = doc.createElement(tagName);
		element.setTextContent(textContent);
		parent.appendChild(element); // Atributu gurasoari gehitzen zaio
	}
}
