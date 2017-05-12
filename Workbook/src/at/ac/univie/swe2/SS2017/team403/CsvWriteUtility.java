package at.ac.univie.swe2.SS2017.team403;

import java.io.FileWriter;
import java.io.IOException;

public class CsvWriteUtility {
	
	public static void convertWorkSheetToCsv(Worksheet worksheet, FileWriter writer) throws IOException{
		Integer numberOfColumns = 0; // worksheet.getHighestNumberOfColumns();
		Integer numberOfRows = 0; // worksheet.getNumberOfRows();
		StringBuilder cellContents = new StringBuilder();
		
		for(int i=0;i<numberOfRows;++i){	
			
			for(int j=0;j<numberOfColumns;++j){
				if(!worksheet.getCell(i, j).toString().isEmpty()){
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
