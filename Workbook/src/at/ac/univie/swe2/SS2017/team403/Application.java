package at.ac.univie.swe2.SS2017.team403;

import java.awt.Component;
import java.awt.EventQueue;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	
	private Workbook activeWorkbook = null;
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
		
		FileWriter writer = new FileWriter(filePath);	
		//CsvWriteUtility.convertWorkSheetToCsv(Workbook.getSheet(workSheetName), writer); Sobald Csv File geöffnet werden kann
		writer.flush();
		writer.close();
	}

	/**
	 * Open CSV format File
	 */
	public void openCSV(String fileLocation, char delimiter, String quotation) throws IOException{	
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
	
	public static void main(String[] args) {

		/*
		EventQueue.invokeLater(new MyRunnable(brokerId, clientId, 
		    	 remoteHostBoerse, remotePortUDPBoerse, remotePortRMIBoerse,
		    	 remoteHostBoerseSOAP, remoteHostBoerseREST,
		    	 remoteHostBroker, remotePortRMIBroker) 
		);
		
		*/
		EventQueue.invokeLater(new Runnable() {			
			public void run() {	
				try {		
					if (System.getSecurityManager() == null) {			       
						//    System.setSecurityManager(new SecurityManager());			       
					}										
						Application gui = new Application();	
				
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
						String filePath = chooser.getSelectedFile().getPath();
						System.out.println("You chose to open this file: " + chooser.getSelectedFile().getName());
						System.out.println("The filepath is: " + chooser.getSelectedFile().getPath());
						System.out.println("The absolute filepath is: " + chooser.getSelectedFile().getAbsolutePath());

						openCSV(filePath, ';', "not important yet");
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
			        FileNameExtensionFilter filter2 = new FileNameExtensionFilter("beispiel (*.bsp)", "beispiel"); // proprietäres Datenformat
			        chooser.setFileFilter(filter);
			        chooser.addChoosableFileFilter(filter2);
			        int returnVal = chooser.showSaveDialog(parent);
			        
			        
			        if(returnVal == JFileChooser.APPROVE_OPTION) {
			        
			        	try {
							writeCSV(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()) ,choosedAbsolutFile = chooser.getSelectedFile().getAbsolutePath());  
						} catch (IOException e1) {
							System.out.println("Fehler beim internen Auslesen!");
							e1.printStackTrace();
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
		        FileNameExtensionFilter filter2 = new FileNameExtensionFilter("beispiel (*.bsp)", "beispiel"); // proprietäres Datenformat
		        chooser.setFileFilter(filter);
		        chooser.addChoosableFileFilter(filter2);
		        int returnVal = chooser.showSaveDialog(parent);
		        
		        
		        if(returnVal == JFileChooser.APPROVE_OPTION) {
		        
		        	try {
						writeCSV(tabbedPane.getTitleAt(tabbedPane.getSelectedIndex()) ,choosedAbsolutFile = chooser.getSelectedFile().getAbsolutePath());  
					} catch (IOException e1) {
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
