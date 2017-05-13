package at.ac.univie.swe2.SS2017.team403;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
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

public class Application implements ActionListener {

	// gui controls
	private JFrame frmClientInterface;
	private JPanel panelMain;

	private Workbook activeWorkbook = null;
	private JTable table_1;
	private JMenuBar menuBar;

	/**
	 * Open proprietary format File
	 */
	public void openFile(String fileName) {
		// activeWorkbook
	}

	/**
	 * Write Csv Format File
	 * 
	 * @throws IOException
	 */
	public void writeCSV(Workbook workbook, String workSheetName, String filePath) throws IOException {
		FileWriter writer = new FileWriter(filePath);
		CsvWriteUtility.convertWorkSheetToCsv(workbook.getSheet(workSheetName), writer);

		writer.flush();
		writer.close();
	}

	/**
	 * Open CSV format File
	 * 
	 * @throws IOException
	 */
	public void openCSV(String fileLocation, char delimiter, String quotation) throws IOException {
		CSVReader reader = new CSVReader(new FileReader(fileLocation), ';');
		List<String[]> csvValues = reader.readAll();
		System.out.println(csvValues.size());
		 
		String[] columnNames = csvValues.get(0);		
		String[][] emptyString = null;
		
		
		
		
		TableModel model = new DefaultTableModel(emptyString, columnNames);
		table_1.setModel(model);
		
		reader.close();
	}

	/**
	 * Calculate Data in worksheet
	 */
	public void calculate() {

	}

	public void actionPerformed(ActionEvent e) {

	}

	public static void main(String[] args) {

		/*
		 * EventQueue.invokeLater(new MyRunnable(brokerId, clientId,
		 * remoteHostBoerse, remotePortUDPBoerse, remotePortRMIBoerse,
		 * remoteHostBoerseSOAP, remoteHostBoerseREST, remoteHostBroker,
		 * remotePortRMIBroker) );
		 * 
		 */
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					if (System.getSecurityManager() == null) {
						// System.setSecurityManager(new SecurityManager());
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
		frmClientInterface.setBounds(100, 100, 613, 553);
		frmClientInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmClientInterface.getContentPane().setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
		tabbedPane.setBounds(12, 26, 584, 452);
		frmClientInterface.getContentPane().add(tabbedPane);

		JPanel panelConnection = new JPanel();
		tabbedPane.addTab("Sheet1", null, panelConnection, null);
		panelConnection.setLayout(null);

		table_1 = new JTable();
		table_1.setBounds(27, 30, 527, 371);
		panelConnection.add(table_1);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(535, 30, 19, 371);
		panelConnection.add(scrollPane);

		menuBar = new JMenuBar();
		frmClientInterface.setJMenuBar(menuBar);

		JMenu fileMenu = new JMenu("Datei");
		menuBar.add(fileMenu);

		JMenuItem mntmNeu = new JMenuItem("Neu");
		fileMenu.add(mntmNeu);

		JMenuItem openFileMenuItem = new JMenuItem("Datei \u00F6ffnen...");
		openFileMenuItem.setSelectedIcon(
				new ImageIcon(Application.class.getResource("/com/sun/java/swing/plaf/windows/icons/File.gif")));
		fileMenu.add(openFileMenuItem);

		/*
		 * CSV Datei öffnen (Nur CSV Dateien lassen sich öffnen)
		 */
		openFileMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
		});
		// open JFileChooser in order to search for CSV file
		// openFileMenuItem

		JMenuItem saveFileMenutItem = new JMenuItem("Speichern");
		saveFileMenutItem.setSelectedIcon(
				new ImageIcon(Application.class.getResource("/com/sun/java/swing/plaf/windows/icons/FloppyDrive.gif")));
		fileMenu.add(saveFileMenutItem);

		JMenuItem mntmSpeichernUnter = new JMenuItem("Speichern unter...");
		fileMenu.add(mntmSpeichernUnter);

	}
}
