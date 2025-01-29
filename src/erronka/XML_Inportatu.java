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
	private NodeList kanpinaNode;
	
	public void Inportazioa() throws SQLException {

        try {
        	KonfKargatu konfigurazioa = new KonfKargatu();
            File file = new File(konfigurazioa.getXML());
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(file);
            doc.getDocumentElement().normalize();
            
            kanpinaNode = doc.getElementsByTagName("row");
            conn = DB.konektatu();
            conn.setAutoCommit(false);
            
            System.out.println("Datuak inportatzen...");
            
            inportatuHerriak();
            inportatuProbintziak();
            inportatuKanpinak();
            //inportatuEtiketak();
            
            conn.close();
            System.out.println("Datuak ondo inportatu dira.");
            
        } catch (Exception e) {
            e.printStackTrace();
            conn.rollback();
            System.out.println("Errore bat egon da inportatzerakoan, rollback egiten.");
        }
    }
	
	public void inportatuHerriak() throws NumberFormatException, Exception{
		 String queryHerria = "INSERT INTO HERRIAK (KODEA, IZENA) VALUES (?, ?)";
         PreparedStatement pstmtHerria = conn.prepareStatement(queryHerria);
         
         for (int i = 0; i < kanpinaNode.getLength(); i++) {
             Element element = (Element) kanpinaNode.item(i);
             
             String herriKodea = element.getElementsByTagName("municipalitycode").item(0).getTextContent();
             String herriIzena = element.getElementsByTagName("municipality").item(0).getTextContent();
             
             if(!herriaKonfirmatu(Integer.parseInt(herriKodea))) {
            	 pstmtHerria.setString(1, herriKodea);
                 pstmtHerria.setString(2, herriIzena);
                 pstmtHerria.executeUpdate();
             }else {
            	 System.out.println("Errorea: herria badago sartuta: " + herriIzena);
             }
         }
         
         pstmtHerria.close();
	}
	
	public void inportatuProbintziak() throws NumberFormatException, Exception {
        String queryProbintzia = "INSERT INTO PROBINTZIAK (KODEA, IZENA) VALUES (?, ?)";
        PreparedStatement pstmtProbintzia = conn.prepareStatement(queryProbintzia);
        
        for (int i = 0; i < kanpinaNode.getLength(); i++) {
            Element element = (Element) kanpinaNode.item(i);
             
            String probintziaKodea = element.getElementsByTagName("territorycode").item(0).getTextContent();
            String probintziaIzena = element.getElementsByTagName("territory").item(0).getTextContent();
            

            if(!probintziaKonfirmatu(Integer.parseInt(probintziaKodea))) {
                pstmtProbintzia.setString(1, probintziaKodea);
                pstmtProbintzia.setString(2, probintziaIzena);
                pstmtProbintzia.executeUpdate();
            }else {
           	 System.out.println("Errorea: probintzia badago sartuta: " + probintziaIzena);
            }
        }
        
        pstmtProbintzia.close();
	}
	
	public void inportatuKanpinak() throws SQLException {
		String queryKanpinak = "INSERT INTO KANPINAK (KODEA, IZENA, DESKRIBAPENA, KOKALEKUA, TELEFONOA, HELBIDEA, EMAILA, WEBGUNEA, KATEGORIA, EDUKIERA, POSTAKODEA, HERRI_KODEA, PROBINTZIA_KODEA, FRIENDLY_URL, PHYSICAL_URL, DATA_XML, METADATA_XML, ZIP_FILE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(queryKanpinak);
        
        for (int i = 0; i < kanpinaNode.getLength(); i++) {
            Element element = (Element) kanpinaNode.item(i);

            pstmt.setString(1, element.getElementsByTagName("signatura").item(0).getTextContent());
            pstmt.setString(2, element.getElementsByTagName("documentName").item(0).getTextContent());
            pstmt.setString(3, element.getElementsByTagName("documentDescription").item(0).getTextContent());
            pstmt.setString(4, element.getElementsByTagName("country").item(0).getTextContent());
            pstmt.setString(5, element.getElementsByTagName("phone").item(0).getTextContent().replace(" ",""));
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
	}
	
	/*public void inportatuEtiketak() throws SQLException {
        String queryEtiketak = "INSERT INTO ETIKETAK (ETIKETA) VALUES (?)";
        PreparedStatement pstmtEtiketak = conn.prepareStatement(queryEtiketak);
        
        for (int i = 0; i < kanpinaNode.getLength(); i++) {
            Element element = (Element) kanpinaNode.item(i);       
            
            pstmtEtiketak.setString(1, element.getElementsByTagName("territorycode").item(0).getTextContent());
            
            pstmtEtiketak.executeUpdate();
        }
        
        pstmtEtiketak.close();
	}*/
	
    public static boolean herriaKonfirmatu(int herriKode) throws Exception {
        String query = "SELECT KODEA FROM HERRIAK WHERE KODEA = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, herriKode);
        ResultSet rs = pstmt.executeQuery();
        return rs.next();
    }
    
    public static boolean probintziaKonfirmatu(int probintziaKode) throws Exception {
        String query = "SELECT KODEA FROM PROBINTZIAK WHERE KODEA = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, probintziaKode);
        ResultSet rs = pstmt.executeQuery();
        return rs.next();
    }
}
