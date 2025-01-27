package erronka;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

public class XML_Exportatu {
	
	public void Exportazioa() {
        
        try {
            File file = new File("fitxategiak/3PAG2_E1_kanpinak.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            
            NodeList kanpinaNode = doc.getElementsByTagName("row");
            Connection conn = DB.getConnection();
            
            String queryProbintzia = "INSERT INTO PROBINTZIAK (KODEA, IZENA) VALUES (?, ?)";
            PreparedStatement pstmtProbintzia = conn.prepareStatement(queryProbintzia);
            
            for (int i = 0; i < kanpinaNode.getLength(); i++) {
                Element element = (Element) kanpinaNode.item(i);
                
                
                pstmtProbintzia.setString(1, element.getElementsByTagName("territorycode").item(0).getTextContent());
                pstmtProbintzia.setString(2, element.getElementsByTagName("territory").item(0).getTextContent());
                
                pstmtProbintzia.executeUpdate();
            }
            
            pstmtProbintzia.close();
            
            String queryHerria = "INSERT INTO HERRIAK (KODEA, IZENA) VALUES (?, ?)";
            PreparedStatement pstmtHerria = conn.prepareStatement(queryHerria);
            
            for (int i = 0; i < kanpinaNode.getLength(); i++) {
                Element element = (Element) kanpinaNode.item(i);
                
                
                pstmtHerria.setString(1, element.getElementsByTagName("municipalitycode").item(0).getTextContent());
                pstmtHerria.setString(2, element.getElementsByTagName("municipality").item(0).getTextContent());
                
                pstmtHerria.executeUpdate();
            }
            
            pstmtHerria.close();
            
            String queryKanpinak = "INSERT INTO KANPINAK (KODEA, IZENA, DESKRIBAPENA, KOKALEKUA, TELEFONOA, HELBIDEA, EMAILA, WEBGUNEA, KATEGORIA, EDUKIERA, POSTAKODEA, HERRI_KODEA, PROBINTZIA_KODEA, FRIENDLY_URL, PHYSICAL_URL, DATA_XML, METADATA_XML, ZIP_FILE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(queryKanpinak);
            
            for (int i = 0; i < kanpinaNode.getLength(); i++) {
                Element element = (Element) kanpinaNode.item(i);
                
                pstmt.setString(1, element.getAttribute("num"));
                pstmt.setString(2, element.getElementsByTagName("documentName").item(0).getTextContent());
                pstmt.setString(3, element.getElementsByTagName("documentDescription").item(0).getTextContent());
                pstmt.setString(4, element.getElementsByTagName("country").item(0).getTextContent());
                pstmt.setString(5, element.getElementsByTagName("phone").item(0).getTextContent());
                pstmt.setString(6, element.getElementsByTagName("address").item(0).getTextContent());
                pstmt.setString(7, element.getElementsByTagName("tourismEmail").item(0).getTextContent());
                pstmt.setString(8, element.getElementsByTagName("web").item(0).getTextContent());
                pstmt.setString(9, element.getElementsByTagName("category").item(0).getTextContent());
                pstmt.setInt(10, Integer.parseInt(element.getElementsByTagName("capacity").item(0).getTextContent()));
                pstmt.setString(11, element.getElementsByTagName("postalCode").item(0).getTextContent());
                pstmt.setInt(12, Integer.parseInt(element.getElementsByTagName("municipalitycode").item(0).getTextContent()));
                pstmt.setInt(13, Integer.parseInt(element.getElementsByTagName("territorycode").item(0).getTextContent()));
                pstmt.setString(14, element.getElementsByTagName("friendlyUrl").item(0).getTextContent());
                pstmt.setString(15, element.getElementsByTagName("physicalUrl").item(0).getTextContent());
                pstmt.setString(16, element.getElementsByTagName("dataXML").item(0).getTextContent());
                pstmt.setString(17, element.getElementsByTagName("metadataXML").item(0).getTextContent());
                pstmt.setString(18, element.getElementsByTagName("zipFile").item(0).getTextContent());

                
                pstmt.executeUpdate();
            }
            pstmt.close();
            conn.close();
            System.out.println("Datuak ondo exportatu dira.");
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
