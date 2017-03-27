import java.io.FileReader;

import org.json.JSONArray;
import org.json.JSONObject;

import au.com.bytecode.opencsv.CSVReader;


public class CsvtoJSONUtil {

	/**
	 * Convertit un fichier CSV en supposant qu'il y a une entête
	 *
	 * @param csvInputFile : le chemin absolut du fichier CSV.
	 *
	 * @param separator : le séparateur
	 *
	 * @throws Exception
	 */
	public JSONObject convertToCSV (String csvInputFile,char separator )
			throws Exception {

		JSONObject jsonObject = new JSONObject();
		JSONArray JSheet = new JSONArray();

		if (null != csvInputFile ) {

			try (CSVReader reader = new CSVReader(new FileReader(csvInputFile),separator)) {

				// on commence par stocker les entêtes
				String[] columnHeader = reader.readNext();
				// pour stocker chaque ligne du fichier
				String[] eachLine = reader.readNext();


				while (eachLine != null) {

					if (eachLine.length == columnHeader.length) { // il n'y a pas de colonnes vides
						addMatchJsonObject(columnHeader, eachLine, JSheet);
					}
					else {
						addNonMatchJsonObject(columnHeader, eachLine, JSheet);
					}
					eachLine= reader.readNext(); // on passe à la ligne suivante
				}
			}

		}

		jsonObject.put("sheet",JSheet);
		return jsonObject;
	}

	/**
	 * converti une ligne de fichier csv en JSON avec tous les champs non vides
	 * @param Json
	 * @param columnHeader
	 * @param eachLine
	 * @param JSheet
	 */
	private void addNonMatchJsonObject(String[] columnHeader,
			String[] line, JSONArray jSheet) {
		// Convert each row to JSON object.
		JSONObject Jrow = new JSONObject();

		for (int columnIndex = 0; columnIndex < line.length; columnIndex++) {
			// on créer l'objet json
			if (line[columnIndex] != null && !line[columnIndex].isEmpty() ) { // champ non vide
				Jrow.put(columnHeader[columnIndex],line[columnIndex]);
			}
			else {
				Jrow.put(columnHeader[columnIndex],"");
			}
		}
		jSheet.put(Jrow);
		// End of converting each row.
	}

	/**
	 * converti une ligne de fichier csv en JSON avec certains champs pouvant être vides
	 * @param columnHeader
	 * @param line
	 * @param jSheet
	 * @param Json
	 * @param JSheet
	 */
	private void addMatchJsonObject(String[] columnHeader,
			String[] line, JSONArray jSheet) {
		// Convert each row to JSON object.
		JSONObject Jrow = new JSONObject();

		for (int columnIndex = 0; columnIndex < line.length; columnIndex++) {
			// on créer l'objet json
			Jrow.put(columnHeader[columnIndex],line[columnIndex]);
			// end of converting each column
		}

		jSheet.put(Jrow);
		// End of converting each row.
	}

	public static void main(String[] args) {

		CsvtoJSONUtil converter = new CsvtoJSONUtil();

		String path = "src/main/resources/chantiers-perturbants.csv";

		try {
			JSONObject json = converter.convertToCSV(path, ";".charAt(0));
			System.out.println(json.toString());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
