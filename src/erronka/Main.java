package erronka;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws NumberFormatException, Exception {

		Scanner sc = new Scanner(System.in);
		System.out.println("**************************************");
		System.out.println("Zer akzioak egin nahi duzu?");
		System.out.println("1) XML-a datu-basera inportatu");
		System.out.println("2) Exportatu datuak (XML)");
		System.out.println("3) Exportatu datuak (CSV)");
		System.out.println("4) Programa itxi");
		System.out.println("**************************************");

		int zenbakia = sc.nextInt();
		System.out.println();

		switch (zenbakia) {
		case 1:
			XML_Inportatu xmlInportatu = new XML_Inportatu();
			xmlInportatu.Inportazioak();
			break;
		case 2:
			System.out.println("Sartu inportatu nahi duzun probintziaren izena");
			String probintziaXML = sc.next();
			XML_Exportatu xmlExportatu = new XML_Exportatu();
			xmlExportatu.xmlExportazioa(probintziaXML);
			break;
		case 3:
			System.out.println("Sartu inportatu nahi duzun probintziaren izena");
			String probintziaCSV = sc.next();
			CSV_Exportatu csvExportatu = new CSV_Exportatu();
			csvExportatu.csvExportazioa(probintziaCSV);
			break;
		case 4:
			System.out.println("Programa amaitu da.");
			break;
		}
	}
}
