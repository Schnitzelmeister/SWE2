package at.ac.univie.swe2.SS2017.team403;

import java.awt.Component;
import java.awt.EventQueue;
import java.util.ArrayList;
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
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

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
	 */
	public void openCSV(String fileLocation, char delimiter, String quotation) throws IOException {

		CSVReader reader = new CSVReader(new FileReader(fileLocation), ',');
		List<String[]> csvValues = reader.readAll();
		reader.close();
		
		TableModel model = new CustomTableModel(csvValues);
		table_1.setModel(model);
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
		frmClientInterface.setBounds(100, 100, 732, 599);
		frmClientInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frmClientInterface.getContentPane().setLayout(new FormLayout(new ColumnSpec[] {
						ColumnSpec.decode("584px"),},
					new RowSpec[] {
						RowSpec.decode("452px"),}));
		
				JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
				frmClientInterface.getContentPane().add(tabbedPane, "1, 1, fill, fill");
				
						JPanel panelConnection = new JPanel();
						tabbedPane.addTab("Sheet1", null, panelConnection, null);
						panelConnection.setLayout(null);
						
								table_1 = new JTable();
								table_1.setBounds(27, 30, 527, 371);
								panelConnection.add(table_1);

		menuBar = new JMenuBar();
		frmClientInterface.setJMenuBar(menuBar);

		JScrollPane scrollPane = new JScrollPane(table_1);
		table_1.setFillsViewportHeight(true);
		
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

						openCSV(filePath, ';', "not important yet");

					} else {
						System.out.println("The user pressed the CANCEL or X Button");
					}

				} catch (IOException o) {
					System.out.println("Exception occured: File could not be found. ");
					JOptionPane.showMessageDialog(frmClientInterface, "Die Datei konnte nicht gefunden werden ",
							"Fehler", JOptionPane.ERROR_MESSAGE);
				}
			}
		});

		JMenuItem saveFileMenuItem = new JMenuItem("Speichern");
		saveFileMenuItem.setSelectedIcon(
				new ImageIcon(Application.class.getResource("/com/sun/java/swing/plaf/windows/icons/FloppyDrive.gif")));
		fileMenu.add(saveFileMenuItem);

		saveFileMenuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

			}

		});

		JMenuItem mntmSpeichernUnter = new JMenuItem("Speichern unter...");
		fileMenu.add(mntmSpeichernUnter);

		mntmSpeichernUnter.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				JFileChooser chooser = new JFileChooser();
				Component parent = null;
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"CSV (durch Trennzeichen getrennt) (*.csv)", "csv");
				FileNameExtensionFilter filter2 = new FileNameExtensionFilter("beispiel (*.bsp)", "beispiel"); // proprietäres
																												// Datenformat
				chooser.setFileFilter(filter);
				chooser.addChoosableFileFilter(filter2);
				int returnVal = chooser.showSaveDialog(parent);

				if (returnVal == JFileChooser.APPROVE_OPTION) {

					// Sobald Workbook implementiert ist
					// try {
					// writeCSV(workbook,
					// tabbedPane.getTitleAt(tabbedPane.getSelectedIndex())) ,
					// chooser.getSelectedFile().getAbsolutePath());
					// } catch (IOException e1) {
					// System.out.println("Fehler beim Auslesen!");
					// e1.printStackTrace();
					// }

					chooser.getSelectedFile().getName();
					System.out.println("Die Datei wurde gespeichert!");
				} else {
					System.out.println("Das Fenster wurde geschlossen!");
				}
			}

		});

	}
}
