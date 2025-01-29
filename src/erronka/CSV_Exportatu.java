package erronka;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CSV_Exportatu {

	private Connection connection;
    public CSV_Exportatu(Connection connection) {
        this.connection = connection;  
    }
    
    // Datuak CSV fitxategi batera esportatzeko metodoa
    public void exportData(String csvPath) {
        String sql = "SELECT K.KODEA, K.IZENA, K.KOKALEKUA, K.HELBIDEA, K.POSTAKODEA, " +
                     "H.IZENA AS HERRIA, P.IZENA AS PROBINTZIA, K.KATEGORIA, K.EDUKIERA " +
                     "FROM KANPINAK K " +
                     "JOIN HERRIAK H ON K.HERRI_KODEA = H.KODEA " +
                     "JOIN PROBINTZIAK P ON K.PROBINTZIA_KODEA = P.KODEA";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql);
             FileWriter fileWriter = new FileWriter(csvPath)) {

            fileWriter.append("KODEA,IZENA,KOKALEKUA,HELBIDEA,POSTAKODEA,HERRIA,PROBINTZIA,KATEGORIA,EDUKIERA\n");
           // ResultSet erregistroak irakurri eta CSV fitxategian idatzi
            while (resultSet.next()) {
                String kodea = resultSet.getString("KODEA");
                String izena = resultSet.getString("IZENA");
                String kokalekua = resultSet.getString("KOKALEKUA");
                String helbideaKoma = resultSet.getString("HELBIDEA");
                
                // Helbide-eremuko komak kendu, CSV formatuan arazoak saihesteko
                String helbidea = helbideaKoma.replaceAll(",", "");
                
                String postaKodea = resultSet.getString("POSTAKODEA");
                String herria = resultSet.getString("HERRIA");
                String probintzia = resultSet.getString("PROBINTZIA");
                String kategoria = resultSet.getString("KATEGORIA");
                int edukiera = resultSet.getInt("EDUKIERA");
                
                System.out.println(kodea + ":" + izena+ ":" + kokalekua+ ":" + helbidea+ ":" + postaKodea+ ":" + herria+ ":" + probintzia+ ":" + kategoria );
                
                fileWriter.append(String.join(",",
                        kodea,
                        "\"" + izena + "\"",
                        "\"" + kokalekua + "\"",
                        "\"" + helbidea + "\"",
                        postaKodea.trim(),
                        "\"" + herria + "\"",
                        "\"" + probintzia + "\"",
                        kategoria,
                        String.valueOf(edukiera)));
                fileWriter.append("\n");
            }

            System.out.println("Datuak CSVra exportatu dira.");

        } catch (SQLException e) {
            System.out.println("Errorea datu-basearekin konexioan: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("Errorea CSV fitxategian idazterakoan: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
