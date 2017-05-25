package at.ac.univie.swe2.SS2017.team403;

import java.io.FileWriter;
import java.io.IOException;

/**
 * This class is used to write a worksheet into a csv-file. Each cell content
 * (of a worksheet) is stored into a csv-file-cell.
 */
public class CsvWriteUtility {

	public static void convertWorkSheetToCsv(Worksheet worksheet, FileWriter writer) throws IOException {
		Integer numberOfColumns = worksheet.getMaxUsedArea().getLastColumn();
		Integer numberOfRows = worksheet.getMaxUsedArea().getLastRow();
		StringBuilder cellContents;

		for (int i = 1; i <= numberOfRows; ++i) {
			cellContents = new StringBuilder();
			for (int j = 1; j <= numberOfColumns; ++j) {
				cellContents.append((worksheet.getCell(i, j).getTextValue()));
				if (j != numberOfColumns)
					cellContents.append(",");
			}

			writer.append(cellContents.toString());
			writer.append(System.lineSeparator());
			
		}

		writer.flush();
		writer.close();
	}

}
