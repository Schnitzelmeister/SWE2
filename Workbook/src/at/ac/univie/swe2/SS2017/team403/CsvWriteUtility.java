package at.ac.univie.swe2.SS2017.team403;

import java.io.FileWriter;
import java.io.IOException;

/**
 * This class is used to write a worksheet into a csv-file. 
 * Each cell content (of a worksheet) is stored into a csv-file-cell.
 */
public class CsvWriteUtility {

	public static void convertWorkSheetToCsv(Worksheet worksheet, FileWriter writer) throws IOException {
		Integer numberOfColumns = worksheet.getMaxUsedRangeArea().getLastColumn();
		Integer numberOfRows = worksheet.getMaxUsedRangeArea().getLastRow();
		StringBuilder cellContents = new StringBuilder();

		for (int i = 0; i < numberOfRows; ++i) {
			for (int j = 0; j < numberOfColumns; ++j) {
				if (!worksheet.getCell(i, j).toString().isEmpty()) {
					cellContents.append((worksheet.getCell(i, j).toString()));
				}
				cellContents.append(",");
			}

			writer.append(cellContents.toString());
			writer.append(System.lineSeparator());
			cellContents = new StringBuilder();
		}
		
		writer.flush();
		writer.close();
	}

}
