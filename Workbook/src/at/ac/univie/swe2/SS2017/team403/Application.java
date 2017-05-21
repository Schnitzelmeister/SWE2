package at.ac.univie.swe2.SS2017.team403;

import java.awt.Component;

import java.awt.EventQueue;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.opencsv.CSVReader;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;


public class Application implements ActionListener, WorkbookListener {
	
	private String choosedAbsolutFile = null;

	//gui controls
	private JFrame frmClientInterface;
	private JPanel panelMain;
	
	//current Application Workbook - static object
	private static Workbook activeWorkbook = new Workbook();
	public static Workbook getActiveWorkbook() { return activeWorkbook; }
	
	private JMenuBar menuBar;
	private JTable table_1;
	
	/**
	 * Open proprietary format File
	 */
	public void openFile(String fileName) 
	{
		//activeWorkbook
	}
	
	/**
	 * Write Csv Format File
	 * @throws IOException 
	 */
	public void writeCSV(String workSheetName, String filePath) throws IOException{
		
		FileWriter writer = new FileWriter(filePath+".csv");	
		//CsvWriteUtility.convertWorkSheetToCsv(Workbook.getSheet(workSheetName), writer); Sobald Csv File geöffnet werden kann
		writer.flush();
		writer.close();
	}
	
	/**
	 * Write csv in pdf
	 * @param workSheetName
	 * @param filepath
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public void writePDF(String workSheetName, String filepath) throws IOException, DocumentException{
		writeCSV(workSheetName, filepath);
		PDFWriteUtility.convertCSVToPDF(filepath);
	}

	/**
	 * Open CSV format File
	 */
	public void openCSV(String fileLocation, char delimiter, char quotation) throws IOException{	
		CSVReader reader = new CSVReader(new FileReader(fileLocation), delimiter, quotation);
		List<String[]> csvValues = reader.readAll();
		Worksheet sheet = Application.getActiveWorkbook().addSheet("csv");
		int r = 0;
		for (String[] ar : csvValues) {
			++r;
			for (int c = 0; c < ar.length; ++c) {
				if (isNumber(ar[c]))
					sheet.getCell(r, c + 1).setNumericValue(Double.valueOf(ar[c]));
				else
					sheet.getCell(r, c + 1).setTextValue(ar[c]);
			}
		}

		reader.close();
		
		TableModel model = new CustomTableModel(csvValues);
		table_1.setModel(model);
	}

	private boolean isNumber(String string) {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Calculate Data in worksheet
	 */
	public void calculate(){
		
	}
		
	public void actionPerformed(ActionEvent e){
	
	}
	
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {			
			public void run() {	
				try {		
					if (System.getSecurityManager() == null) {			       
						//System.setSecurityManager(new SecurityManager());			       
					}										
						Application gui = new Application();
						
						boolean createDefaultWorkbook = true;
						if (args.length > 0) {
							//open file
							if (args[0].toUpperCase().endsWith(".WBK")) {
								gui.openFile(args[0]);
								createDefaultWorkbook = false;
							}
							//open csv
							else if (args.length > 2) {
								gui.openCSV(args[0], args[1].charAt(0), args[2].charAt(0));
								createDefaultWorkbook = false;
							}
						}
						
						if (createDefaultWorkbook) {
							//create default workbook
							//Application.activeWorkbook = new Workbook();
							Application.activeWorkbook.addSheet("Sheet 1");
							Application.activeWorkbook.addSheet("Sheet 2");
							Application.activeWorkbook.addSheet("Sheet 3");
							
							//observe Application
							Application.activeWorkbook.addListener(gui);
							Application.activeWorkbook.removeListener(gui);

							
							at.ac.univie.swe2.SS2017.team403.test.TestJunit.testFormulas();
							Application.activeWorkbook.getSheet("sheet1").setWorksheetName("SHEEET1");
							Application.activeWorkbook.getSheet("SHEEET1").getCell(1, 1).setFormula("=657-33");
							Application.activeWorkbook.removeSheet("SHEEET1");
							
						}
				
						gui.frmClientInterface.setVisible(true);
						
				} catch (Exception e) {
					e.printStackTrace();				
				}		
				}		
			});
			
	}

	
	
	/**
	 * Create the application.
	 */
	public Application() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {		
		frmClientInterface = new JFrame();
		frmClientInterface.setTitle("Workbook");
		frmClientInterface.setBounds(100, 100, 620, 533);
		frmClientInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		menuBar = new JMenuBar();
		frmClientInterface.setJMenuBar(menuBar);
		
		
		JMenu fileMenu = new JMenu("Datei");
		menuBar.add(fileMenu);
		
		JMenuItem mntmNeu = new JMenuItem("Neu");
		fileMenu.add(mntmNeu);
		
		JMenuItem openFileMenuItem = new JMenuItem("Datei \u00F6ffnen...");
		openFileMenuItem.setSelectedIcon(new ImageIcon(Application.class.getResource("/com/sun/java/swing/plaf/windows/icons/File.gif")));
		fileMenu.add(openFileMenuItem);
		
		/*
		 * CSV Datei öffnen (Nur CSV Dateien lassen sich öffnen)
		 */
		openFileMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				try {
					JFileChooser chooser = new JFileChooser();
					Component parent = null;
					FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV File", "csv");
					chooser.setFileFilter(filter);
					int returnVal = chooser.showOpenDialog(parent);

					if (returnVal == JFileChooser.APPROVE_OPTION) {
						String filePath = chooser.getSelectedFile().getAbsolutePath();
						System.out.println("The absolute filepath is: " + chooser.getSelectedFile().getAbsolutePath());
						showMultipleInputMessageDialog(filePath);
					} else {
						System.out.println("The user pressed the CANCEL or X Button");
					}
				} catch (IOException o) {
					System.out.println("Exception occured: File could not be found. ");
					// JOptionPane.showMessageDialog(this, "Die Datei konnte nicht gefunden werden ", "Fehler",
							//JOptionPane.ERROR_MESSAGE); //TODO
				} catch (IllegalArgumentException i) {
					System.out.println("showMultipleInputMessageDialog threw IllegalArgumentException ");
					//JOptionPane.showMessageDialog(this, i.getMessage(), "Fehler",
						//	JOptionPane.ERROR_MESSAGE); //TODO
				}  
			}
		}
		);
		//open JFileChooser in order to search for CSV file
		//openFileMenuItem
		
		
		JMenuItem mntmSpeichernUnter = new JMenuItem("Speichern unter...");
		fileMenu.add(mntmSpeichernUnter);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		frmClientInterface.getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JScrollPane worksheet1 = new JScrollPane();
		tabbedPane.addTab("New tab", null, worksheet1, null);
		
		table_1 = new JTable();
		worksheet1.setViewportView(table_1);
		
		JScrollPane worksheet2 = new JScrollPane();
		tabbedPane.addTab("New tab", null, worksheet2, null);
		
		JScrollPane worksheet3 = new JScrollPane();
		tabbedPane.addTab("New tab", null, worksheet3, null);
		
		
		
		JMenuItem saveFileMenuItem = new JMenuItem("Speichern");
		saveFileMenuItem.setSelectedIcon(new ImageIcon(Application.class.getResource("/com/sun/java/swing/plaf/windows/icons/FloppyDrive.gif")));
		fileMenu.add(saveFileMenuItem);
		
		saveFileMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if(choosedAbsolutFile!=null){
					try {
						writeCSV(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()) ,choosedAbsolutFile);
					} catch (IOException e) {
						System.out.println("Fehler beim internen Auslesen!");
						e.printStackTrace();
					}
					System.out.println("Die Datei wurde unter: "+choosedAbsolutFile+" gespeichert!");
				}else{
					JFileChooser chooser = new JFileChooser();
			        Component parent = null;
			        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV (durch Trennzeichen getrennt) (*.csv)", "csv");
			        FileNameExtensionFilter filter2 = new FileNameExtensionFilter("PDF (*.pdf)", "pdf");
			        chooser.setFileFilter(filter);
			        chooser.addChoosableFileFilter(filter2);
			        int returnVal = chooser.showSaveDialog(parent);
			        
			        
			        if(returnVal == JFileChooser.APPROVE_OPTION) {
			        
			        	try {
			        		if(chooser.getFileFilter().getDescription().contains("CSV")){
			        			writeCSV(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()) ,choosedAbsolutFile = chooser.getSelectedFile().getAbsolutePath());
			        		}
			        		else if(chooser.getFileFilter().getDescription().contains("PDF")){
			        			writePDF(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()) ,choosedAbsolutFile = chooser.getSelectedFile().getAbsolutePath());
			        		}
			        	} catch (IOException e1) {
							System.out.println("Fehler beim internen Auslesen!");
							e1.printStackTrace();
						} catch (DocumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			        	
			        	chooser.getSelectedFile().getName();
			        	System.out.println("Die Datei wurde unter: "+choosedAbsolutFile+" gespeichert!");
			        }else{
			        	System.out.println("Das Fenster wurde geschlossen!");
			        }   
				}
				
			}
			
		});
		
		
		mntmSpeichernUnter.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser chooser = new JFileChooser();
		        Component parent = null;
		        FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV (durch Trennzeichen getrennt) (*.csv)", "csv");
		        FileNameExtensionFilter filter2 = new FileNameExtensionFilter("PDF (*.pdf)", "pdf");
		        chooser.setFileFilter(filter);
		        chooser.addChoosableFileFilter(filter2);
		        int returnVal = chooser.showSaveDialog(parent);
		        
		        
		        if(returnVal == JFileChooser.APPROVE_OPTION) {
		        
		        	try {
		        		if(chooser.getFileFilter().getDescription().contains("CSV")){
		        			writeCSV(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()) ,choosedAbsolutFile = chooser.getSelectedFile().getAbsolutePath());
		        		}
		        		else if(chooser.getFileFilter().getDescription().contains("PDF")){
		        			writePDF(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()) ,choosedAbsolutFile = chooser.getSelectedFile().getAbsolutePath());
		        		}
					} catch (IOException | DocumentException e1) {
						System.out.println("Fehler beim internen Auslesen!");
						e1.printStackTrace();
					}
		        	
		        	chooser.getSelectedFile().getName();
		            System.out.println("Die Datei wurde unter: "+choosedAbsolutFile+" gespeichert!");
		        }else{
		        	System.out.println("Das Fenster wurde geschlossen!");
		        }               
			}
			
		});
		
		
	}

	private void showMultipleInputMessageDialog(String absolutePath) throws IOException, IllegalArgumentException {

		// JOption Pane code
		final JCheckBox checkBoxForSemiColon = new JCheckBox();
		final JCheckBox checkBoxForComma = new JCheckBox();
		final JCheckBox checkBoxForQuotation = new JCheckBox();
		final JCheckBox checkBoxForAlternativeDelimiter = new JCheckBox();
		final JCheckBox checkBoxForDefaultQuote = new JCheckBox();
		final JCheckBox checkBoxForAlternativeQuote = new JCheckBox();
		final JTextField alternativeDelimiter = new JTextField();
		final JTextField alternativeQotation = new JTextField();

		alternativeDelimiter.setEnabled(false);
		alternativeQotation.setEnabled(false);
		
		Object[] inputFields = { "Semicolon as delimiter: ;", checkBoxForSemiColon, "Comma as delimiter: ,",
				checkBoxForComma,"Optional delimiter: ", checkBoxForAlternativeDelimiter, alternativeDelimiter, "Default quotation: \" ", checkBoxForDefaultQuote
				, "Alternative quotation: ", checkBoxForAlternativeQuote, alternativeQotation};

		checkBoxForAlternativeDelimiter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == checkBoxForAlternativeDelimiter) {
					if (checkBoxForAlternativeDelimiter.isSelected()) {
						alternativeDelimiter.setEnabled(true);
					} else {
						alternativeDelimiter.setEnabled(false);
					}
				}
			}
		});

		checkBoxForSemiColon.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == checkBoxForSemiColon) {
					checkBoxForComma.setSelected(false);
					checkBoxForAlternativeDelimiter.setSelected(false);
					alternativeDelimiter.setEnabled(false);
				}
			}
		});

		checkBoxForAlternativeDelimiter.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == checkBoxForAlternativeDelimiter) {
					checkBoxForComma.setSelected(false);
					checkBoxForSemiColon.setSelected(false);
					alternativeDelimiter.setEnabled(false);
				}
			}
		});
		
		checkBoxForComma.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == checkBoxForComma) {
					checkBoxForSemiColon.setSelected(false);
					checkBoxForAlternativeDelimiter.setSelected(false);
					alternativeDelimiter.setEnabled(false);
				}
			}
		});

		checkBoxForDefaultQuote.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == checkBoxForDefaultQuote) {
					checkBoxForAlternativeQuote.setSelected(false);
					alternativeQotation.setEnabled(false);
				}
			}
		});
		
		checkBoxForAlternativeQuote.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == checkBoxForAlternativeQuote) {
					checkBoxForDefaultQuote.setSelected(false);
					alternativeQotation.setEnabled(true);
				}
			}
		});
		
		int option = JOptionPane.showConfirmDialog(this, inputFields, "Please Choose a delimiter",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

		if(option == JOptionPane.CANCEL_OPTION)
			return;
		
		if (option == JOptionPane.OK_OPTION && checkBoxForSemiColon.isSelected() && checkBoxForDefaultQuote.isSelected()){
			openCSV(absolutePath, ';', '"');
			return;
		}
			
		if (option == JOptionPane.OK_OPTION && checkBoxForComma.isSelected() && checkBoxForDefaultQuote.isSelected()){
			openCSV(absolutePath, ',', '"');
			return;
		}
			
		if (option == JOptionPane.OK_OPTION && checkBoxForSemiColon.isSelected() && checkBoxForAlternativeQuote.isSelected()){
			String alternativeQuoteString = alternativeQotation.getText();
			if(alternativeQuoteString.length() > 1 || alternativeQuoteString.length() == 0) throw new IllegalArgumentException("The value entered cannot be used as a quote");
			char alternativeQuoteChar = alternativeQuoteString.charAt(0);
			openCSV(absolutePath, ';', alternativeQuoteChar);
			return;
		}
		
		if (option == JOptionPane.OK_OPTION && checkBoxForComma.isSelected() && checkBoxForAlternativeQuote.isSelected()){
			String alternativeQuoteString = alternativeQotation.getText();
			if(alternativeQuoteString.length() > 1 || alternativeQuoteString.length() == 0) throw new IllegalArgumentException("The value entered cannot be used as a quote");
			char alternativeQuoteChar = alternativeQuoteString.charAt(0);
			openCSV(absolutePath, ',', alternativeQuoteChar);
			return;
		}
		
		if (option == JOptionPane.OK_OPTION && checkBoxForAlternativeDelimiter.isSelected() && checkBoxForAlternativeQuote.isSelected()){
			String alternativeDelimiterString = alternativeDelimiter.getText();
			if(alternativeDelimiterString.length() > 1 || alternativeDelimiterString.length() == 0) throw new IllegalArgumentException("The value entered cannot be used as a delimiter");
			char alternativeDelimiterChar = alternativeDelimiterString.charAt(0);  
			
			String alternativeQuoteString = alternativeQotation.getText();
			if(alternativeQuoteString.length() > 1 || alternativeQuoteString.length() == 0) throw new IllegalArgumentException("The value entered cannot be used as a quote");
			char alternativeQuoteChar = alternativeQuoteString.charAt(0);
			
			openCSV(absolutePath, alternativeDelimiterChar, alternativeQuoteChar);
			return;
		}
		
		if (option == JOptionPane.OK_OPTION && checkBoxForAlternativeDelimiter.isSelected() && checkBoxForDefaultQuote.isSelected()){
			String alternativeDelimiterString = alternativeDelimiter.getText();
			if(alternativeDelimiterString.length() > 1 || alternativeDelimiterString.length() == 0) throw new IllegalArgumentException("The value entered cannot be used as a delimiter");
			char alternativeDelimiterChar = alternativeDelimiterString.charAt(0);  
			openCSV(absolutePath, alternativeDelimiterChar, '"');
			return;
		}

		throw new IllegalArgumentException("Missing input");
	}
	
	public void afterWorksheetAdded(String worksheetName) {
		System.out.println("AfterWorksheetAdded " + worksheetName);
	}
	
	public void afterWorksheetRemoved(String worksheetName) {
		System.out.println("AfterWorksheetRemoved " + worksheetName);
	}
	
	public void afterWorksheetRenamed(String worksheetOldName, String worksheetNewName) {
		System.out.println("AfterWorksheetRenamed " + worksheetOldName + " " + worksheetNewName);
	}
	
	public void afterCellChanged(String worksheetName, int row, int column, Object newValue){
		System.out.println("AfterCellChanged '" + worksheetName + "'!R" + row + "C" + column + " = " + newValue.toString());
	}
	
	public void afterDiagramAdded(String diagramName){
		System.out.println("AfterDiagramAdded " + diagramName);
	}
	
	public void afterDiagramRemoved(String diagramName){
		System.out.println("AfterDiagramRemoved " + diagramName);
	}
	
	public void afterDiagramRenamed(String diagramOldName, String diagramNewName){
		System.out.println("AfterDiagramRenamed " + diagramOldName + " " + diagramNewName);
	}
	
	public void afterDiagramChanged(String diagramName){
		System.out.println("AfterDiagramChanged " + diagramName);
	}

	
}
