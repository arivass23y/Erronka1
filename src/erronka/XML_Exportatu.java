package erronka;

import java.io.File;

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
	
	public void datuakInportatu() {
		
	}
	
	public void toXML() {
	    try {
	        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
	        Document doc = docBuilder.newDocument();

	        // EL TOCHO, LA RAIZ, ES HIJO DE DOC, EL DOCUMENTO, POR ESO ES APPEND CHILD
	        Element raiz = doc.createElement("liburutegia");
	        doc.appendChild(raiz);

	        for (int i = 0; i < this.liburuak.length; i++) {
	            // CREAMOS OTRO ELEMENTO, EN ESTE CASO TIENE ATRIBUTO ID <liburua id = "x">
	            Element liburua = doc.createElement("liburua");
	            liburua.setAttribute("id", this.liburuak[i].getId());
	            raiz.appendChild(liburua);

	            // AÑADE ELEMENTOS SIMPLES A <liburua>
	            addElementWithText(doc, liburua, "Izenburua", this.liburuak[i].getIzenburua());
	            addElementWithText(doc, liburua, "Urtea", this.liburuak[i].getUrtea());
	            addElementWithText(doc, liburua, "Generoa", this.liburuak[i].getGeneroa());

	            // Create <idazlea> node and add it to <liburua>
	            //SE HACE DISTINTO A LOS DE ARRIBA POR NO SER UN ELEMENTO, SI NO UN NODO
	            Element idazlea = doc.createElement("idazlea");
	            liburua.appendChild(idazlea);

	            // IGUAL QUE LOS DE ARRIBA PERO EN EL NODO IDAZLEA
	            addElementWithText(doc, idazlea, "IdazleIzena", this.liburuak[i].getIdazlea().getIzena());
	            addElementWithText(doc, idazlea, "IdazleAbizena", this.liburuak[i].getIdazlea().getAbizena());
	        }

	        TransformerFactory transformerFactory = TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        DOMSource source = new DOMSource(doc);
	        StreamResult result = new StreamResult(new File("sortutako_fitx\\CSV-a pasatuta.xml"));
	        //Linea magica
	        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	        transformer.transform(source, result);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	//PARA AÑADIR DIRECTAMENTE EL ELEMENTO DENTRO DE UN NODO, CON TEXTO Y TODO
	private static void addElementWithText(Document doc, Element parent, String tagName, String textContent) {
        Element element = doc.createElement(tagName);
        element.setTextContent(textContent);
        parent.appendChild(element); // Atributu gurasoari gehitzen zaio
    }
}
