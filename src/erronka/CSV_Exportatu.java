package erronka;

import java.io.FileWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CSV_Exportatu {
	private static Connection conn;

	// CON ESTO NOS ASEGURAMOS DE QUE LA CONEXION A LA BASE DE DATOS NUNCA VAYA A
	// SER NULA
	public CSV_Exportatu() throws SQLException {
		conn = DB.konektatu();
	}

	public void csvExportazioa(String probintziaIzena) {
		// Erabiliko ditugun query-a datu baseko datuak jasotzeko
		String query = "SELECT K.KODEA, K.IZENA, K.KOKALEKUA, K.HELBIDEA, K.POSTAKODEA, "
				+ "H.IZENA AS HERRIA, P.IZENA AS PROBINTZIA, K.KATEGORIA, K.EDUKIERA " + "FROM KANPINAK K "
				+ "JOIN HERRIAK H ON K.HERRI_KODEA = H.KODEA " + "JOIN PROBINTZIAK P ON K.PROBINTZIA_KODEA = P.KODEA "
				+ "WHERE P.IZENA = '" + probintziaIzena + "'";

		// Konexia irekitzen saiatzen dugu
		try (Statement statement = conn.createStatement();
				ResultSet resultSet = statement.executeQuery(query);
				FileWriter fw = new FileWriter("sortutako_fitx\\CSV_Exportazioa.csv")) {

			// Fitxategian datu-basearen edukia idazteko
			fw.append("Kodea;Izena;Kokalekua;Helbidea;Postakodea;Herria;Probintzia;Kategoria;Edukiera\n");
			while (resultSet.next()) {
				String kodea = resultSet.getString("KODEA");
				String izena = resultSet.getString("IZENA");
				String kokalekua = resultSet.getString("KOKALEKUA");
				String helbidea = resultSet.getString("HELBIDEA");
				String postaKodea = resultSet.getString("POSTAKODEA");
				String herria = resultSet.getString("HERRIA");
				String probintzia = resultSet.getString("PROBINTZIA");
				String kategoria = resultSet.getString("KATEGORIA");
				int edukiera = resultSet.getInt("EDUKIERA");

				fw.append(String.join(";", kodea, izena, kokalekua, helbidea, postaKodea, herria, probintzia, kategoria,
						String.valueOf(edukiera)));
				fw.append("\n");
			}

			System.out.println("Datuak CSV-ra ondo exportatu dira.");

		} catch (Exception e) {
			System.out.println("Errorea exportazioa egiterakoan: " + e.getMessage());
			e.printStackTrace();
		}
	}
}
