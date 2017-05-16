package at.ac.univie.swe2.SS2017.team403;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.opencsv.CSVReader;

public class PDFWriteUtility {
	
	public static void convertCSVToPDF(String filepath) throws DocumentException, IOException{
		
		File csvFile = new File(filepath+".csv");
		FileReader fileReader = new FileReader(filepath+".csv");
		CSVReader reader = new CSVReader(fileReader, ',');
		FileOutputStream fileOutputStream = new FileOutputStream(filepath+".pdf");
		String[] nextLine;
		
		int columnCount = reader.readNext().length;
		Boolean isColumnBiggerFive = ((columnCount>5)?true:false);
		
		Document pdfFile = new Document();
		PdfPTable pdfTable = new PdfPTable(5);
		pdfTable.setWidthPercentage(100);
		PdfPCell pdfTableCell;
		Phrase csvPhrase;
		
		PdfWriter.getInstance(pdfFile, fileOutputStream);
		pdfFile.open();
		
		while((nextLine = reader.readNext()) != null){
			
			for(int i=0;i<columnCount;++i){
				csvPhrase = new Phrase(nextLine[i]);
				pdfTableCell = new PdfPCell(csvPhrase);
				pdfTable.addCell(pdfTableCell);
			}
			
			if(isColumnBiggerFive){
				for(int j=0;j<(4+(columnCount%5));++j){
					pdfTable.addCell(" ");
				}
			}
			
		}
		
		pdfFile.add(pdfTable);
		
		pdfFile.close();
		reader.close();
		csvFile.delete();
		
	}

}
