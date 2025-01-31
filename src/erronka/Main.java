package erronka;

import java.sql.SQLException;
import java.util.Scanner;

public class Main {
	public static void main(String[] args){
		
        Scanner sc = new Scanner(System.in);
        System.out.println("**************************************");
    	System.out.println("Zer akzioak egin nahi duzu?");
        System.out.println("1) XML-a datu-basera inportatu");
        System.out.println("2) Exportatu datuak (XML)");
        System.out.println("3) Exportatu datuak (CSV)");
        System.out.println("4) Programa itxi");
    	System.out.println("**************************************");
    	
        int aukera = sc.nextInt();
        System.out.println();

    	if(aukera==1) {
    		XML_Inportatu xmlInportatu;
			try {
				xmlInportatu = new XML_Inportatu();
				xmlInportatu.Inportazioak();
			} catch (NumberFormatException e) {

				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}else if(aukera==2) {
    		XML_Exportatu xmlExportatu;
			try {
				xmlExportatu = new XML_Exportatu();
				xmlExportatu.xmlExportazioa();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    		
		}else if(aukera==3) {
			CSV_Exportatu csvExportatu;
			try {
				csvExportatu = new CSV_Exportatu();
				csvExportatu.csvExportazioa();
			} catch (SQLException e) {
				e.printStackTrace();
			}		
		}else {
			
		}
	}
}
