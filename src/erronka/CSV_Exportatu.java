package erronka;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CSV_Exportatu {

	private Connection connection;

	public CSV_Exportatu(Connection connection) {
		this.connection = connection;
	}

	public void exportData(String csvPath) {
		//Erabiliko ditugun query-a datu baseko datuak jasotzeko
		String sql = "SELECT K.KODEA, K.IZENA, K.KOKALEKUA, K.HELBIDEA, K.POSTAKODEA, "
				+ "H.IZENA AS HERRIA, P.IZENA AS PROBINTZIA, K.KATEGORIA, K.EDUKIERA " + "FROM KANPINAK K "
				+ "JOIN HERRIAK H ON K.HERRI_KODEA = H.KODEA " + "JOIN PROBINTZIAK P ON K.PROBINTZIA_KODEA = P.KODEA";

		//Konexia irekitzen saiatzen dugu
		try (Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(sql);
				FileWriter fileWriter = new FileWriter(csvPath)) {
			
			//Fitxategian datu-basearen edukia idazteko
			fileWriter.append("KODEA,IZENA,KOKALEKUA,HELBIDEA,POSTAKODEA,HERRIA,PROBINTZIA,KATEGORIA,EDUKIERA\n");
			while (resultSet.next()) {
				String kodea = resultSet.getString("KODEA");
				String izena = resultSet.getString("IZENA");
				String kokalekua = resultSet.getString("KOKALEKUA");
				String helbideaKoma = resultSet.getString("HELBIDEA");
				//Helbide eremuan egon daitekeen komak kentzeko
				String helbidea = helbideaKoma.replaceAll(",", "");

				String postaKodea = resultSet.getString("POSTAKODEA");
				String herria = resultSet.getString("HERRIA");
				String probintzia = resultSet.getString("PROBINTZIA");
				String kategoria = resultSet.getString("KATEGORIA");
				int edukiera = resultSet.getInt("EDUKIERA");

				//Kontsolan hartutako datuak erakusteko
				System.out.println("Kodea: " + kodea + "; Izena:" + izena + "; Kokalekua: " + kokalekua + "; Helbidea: "
						+ helbidea + "; Posta kodea: " + postaKodea + "; Herria: " + herria + "; Probintzia: "
						+ probintzia + "; Kategoria: " + kategoria);
				//\" idaztean, komillak erabiltzen diren eremuak komillak izango dituzte csv fitxategian idaztean. 
				//Komillak behar ez dituzten eremuak ez dute erabiliko
				fileWriter.append(String.join(",", kodea, "\"" + izena + "\"", "\"" + kokalekua + "\"",
						"\"" + helbidea + "\"", postaKodea.trim(), "\"" + herria + "\"", "\"" + probintzia + "\"",
						kategoria, String.valueOf(edukiera)));
				fileWriter.append("\n");
			}

			System.out.println("Datuak CSVra exportatu dira.");

		} catch (Exception e) {
			System.out.println("Errorea aurkitu da: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
