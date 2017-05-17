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
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;


public class Application implements ActionListener {
	
	private String choosedAbsolutFile = null;

	//gui controls
	private JFrame frmClientInterface;
	private JPanel panelMain;
	
	//current Application Workbook - static object
	private static Workbook activeWorkbook = null;
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
		CSVReader reader = new CSVReader(new FileReader(fileLocation), ',');
		List<String[]> csvValues = reader.readAll();
		reader.close();
		
		TableModel model = new CustomTableModel(csvValues);
		table_1.setModel(model);
	}

	/**
	 * Calculate Data in worksheet
	 */
	public void calculate() 
	{
		
	}
		
	public void actionPerformed(ActionEvent e){
	
	}
	
	
	public static void testFormulas() 
	{
		Workbook wbk = Application.getActiveWorkbook();
		Worksheet sheet = wbk.addSheet("sheet1");
		sheet.getCell(1, 1).setNumericValue(11);
		sheet.getCell(2, 1).setNumericValue(12);
		sheet.getCell(3, 1).setNumericValue(13);
		sheet.getCell(1, 2).setFormula("=SUM(RC[-1]:R[3]C[-1])+COUNT(RC[-1]:R[3]C[-1]))+MEAN(RC[-1]:R[3]C[-1]))");;
		//org.junit.Assert.assertEquals(51.0, sheet.getCell(1, 2).getNumericValue(), 0 );

		sheet = wbk.addSheet("sheet2");
		sheet.getCell(1, 2).setFormula("=SUM(sheet1!RC[-1]:R[3]C[-1])+COUNT(sheet1!RC[-1]:R[3]C[-1]))+MEAN(sheet1!RC[-1]:R[3]C[-1]))");;

		//org.junit.Assert.assertEquals(51.0, sheet.getCell(1, 2).getNumericValue(), 0 );
	}
	
	public static void main(String[] args) {

	/*	
        final String regExCellPattern = "(\\[[\\w\\-. ]+\\])?(([?\\w+]?'?\\w+'?)!)?[\\$]?[A-Z]+[\\$]?[0-9]+($|(?=[\\s\\.\\#\\+\\-\\*\\/\\=\\~\\>\\<\\!\\|\\(\\)\\,\\;]))";
        //"(\\[[\\w\\-. ]+\\])?(([?\\w+]?'?\\w+'?)!)?[\\$]?[A-Z]+[\\$]?[0-9]+($|(?=[\\s\\.\\#\\+\\-\\*\\/\\=\\~\\>\\<\\!\\|\\(\\)\\,\\;]))";
        //final String regExRangePattern = "(\\[[\\w\\-. ]+\\])?(([?\\w+]?'?\\w+'?)!)?[\\$]?[A-Z]+[\\$]?[0-9]+:[\\$]?[A-Z]+[\\$]?[0-9]+($|(?=[\\s\\.\\#\\+\\-\\*\\/\\=\\~\\>\\<\\!\\|\\(\\)\\,\\;]))";

        final Pattern regExCell = Pattern.compile(regExCellPattern, Pattern.CASE_INSENSITIVE);
        //final Pattern regExRange = Pattern.compile(regExRangePattern, Pattern.CASE_INSENSITIVE);

        Matcher matcher = regExCell.matcher("RC[-1]ssd");
        System.out.println(matcher.find());
        System.out.println(matcher.start());
        
		return; 

*/		
		
		EventQueue.invokeLater(new Runnable() {			
			public void run() {	
				try {		
					if (System.getSecurityManager() == null) {			       
						//    System.setSecurityManager(new SecurityManager());			       
					}										
						Application gui = new Application();
						
						boolean createDefaultWorkbook = true;
						if (args.length > 0) {
							//open file
							if (args[0].toUpperCase().endsWith(".wbk")) {
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
							Application.activeWorkbook = new Workbook();
							Application.activeWorkbook.addSheet("Sheet 1");
							Application.activeWorkbook.addSheet("Sheet 2");
							Application.activeWorkbook.addSheet("Sheet 3");
						}
				
						gui.frmClientInterface.setVisible(true);
						
						//testFormulas();
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
						String filePath = chooser.getSelectedFile().getPath();
						System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
						System.out.println("The filepath is: " + chooser.getSelectedFile().getPath());
						System.out.println("The absolute filepath is: " + chooser.getSelectedFile().getAbsolutePath());

						openCSV(filePath, ';', '"');
					} else {
						System.out.println("The user pressed the CANCEL or X Button");
					}
					
				} catch (IOException o) {
					System.out.println("Exception occured: File could not be found. ");
					JOptionPane.showMessageDialog(frmClientInterface,
						    "Die Datei konnte nicht gefunden werden ",
						    "Fehler",
						    JOptionPane.ERROR_MESSAGE);
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

}
