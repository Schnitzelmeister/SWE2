package at.ac.univie.swe2.SS2017.team403;

import com.itextpdf.text.DocumentException;
import com.opencsv.CSVReader;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class WorkbookMainGui extends javax.swing.JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Creates new form WorkbookGui
	 */
	public WorkbookMainGui() {
		initComponents();
	}

	public static Workbook getActiveWorkbook() {
		return activeWorkbook;
	}

	/**
	 * Write Csv Format File
	 * 
	 * @throws IOException
	 */
	public void writeCSV(String workSheetName, String filePath) throws IOException {
		FileWriter writer = new FileWriter(filePath + ".csv");
		CsvWriteUtility.convertWorkSheetToCsv(activeWorkbook.getSheet(workSheetName), writer);
		writer.close();
	}

	/**
	 * Write csv in pdf
	 * 
	 * @param workSheetName
	 * @param filepath
	 * @throws IOException
	 * @throws DocumentException
	 */
	public void writePDF(String workSheetName, String filepath) throws IOException, DocumentException {
		writeCSV(workSheetName, filepath);
		PDFWriteUtility.convertCSVToPDF(filepath);
	}

	/**
	 * Open CSV format File
	 */
	public void openCSV(String fileLocation, char delimiter, char quotation) throws IOException {
		CSVReader reader = new CSVReader(new FileReader(fileLocation), ',');
		List<String[]> csvValues = reader.readAll();
		reader.close();

		TableModel model = new CustomTableModel(csvValues);
		jTable1.setModel(model);
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 */
	private void initComponents() {

		activeWorkbook = new Workbook();
		jTabbedPane1 = new javax.swing.JTabbedPane();
		jScrollPane1 = new javax.swing.JScrollPane();
		jTable1 = new javax.swing.JTable();
		menuBar = new javax.swing.JMenuBar();
		fileMenu = new javax.swing.JMenu();
		openMenuItem = new javax.swing.JMenuItem();
		saveMenuItem = new javax.swing.JMenuItem();
		saveAsMenuItem = new javax.swing.JMenuItem();
		exitMenuItem = new javax.swing.JMenuItem();
		editMenu = new javax.swing.JMenu();
		openNewTab = new javax.swing.JMenuItem();
		cutMenuItem = new javax.swing.JMenuItem();
		copyMenuItem = new javax.swing.JMenuItem();
		deleteMenuItem = new javax.swing.JMenuItem();
		helpMenu = new javax.swing.JMenu();
		contentsMenuItem = new javax.swing.JMenuItem();
		aboutMenuItem = new javax.swing.JMenuItem();

		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

		Worksheet worksheet = activeWorkbook.addSheet("tab1");

		jScrollPane1.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
				jScrollPane1MouseWheelMoved(evt);
			}
		});

		jTable1.setModel(new javax.swing.table.DefaultTableModel(new Object[80][80], new String[30]));

		jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		jTable1.setGridColor(new java.awt.Color(0, 0, 0));
		jScrollPane1.setViewportView(jTable1);

		jTabbedPane1.addTab("tab1", jScrollPane1);

		fileMenu.setText("File");

		openMenuItem.setText("Open");
		openMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(openMenuItem);

		saveMenuItem.setText("Save");
		saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(saveMenuItem);

		saveAsMenuItem.setText("Save As ...");
		saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveAsMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(saveAsMenuItem);

		exitMenuItem.setText("Exit");
		exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				exitMenuItemActionPerformed(evt);
			}
		});
		fileMenu.add(exitMenuItem);

		menuBar.add(fileMenu);

		editMenu.setText("Edit");

		openNewTab.setText("New Tab");
		openNewTab.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openNewTabActionPerformed(evt);
			}
		});
		editMenu.add(openNewTab);

		cutMenuItem.setText("Cut");
		editMenu.add(cutMenuItem);

		copyMenuItem.setText("Copy");
		editMenu.add(copyMenuItem);

		deleteMenuItem.setText("Delete");
		editMenu.add(deleteMenuItem);

		menuBar.add(editMenu);

		helpMenu.setText("Help");

		contentsMenuItem.setText("Contents");
		helpMenu.add(contentsMenuItem);

		aboutMenuItem.setText("About");
		helpMenu.add(aboutMenuItem);

		menuBar.add(helpMenu);

		setJMenuBar(menuBar);

		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE));

		pack();
		setLocationRelativeTo(null);
	}

	private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		System.exit(0);
	}

	private void openNewTabActionPerformed(java.awt.event.ActionEvent evt) {
		JScrollPane jPane = new javax.swing.JScrollPane();
		JTable albumTable = new javax.swing.JTable();
		albumTable.setModel(new javax.swing.table.DefaultTableModel(new Object[80][80], new String[30]));

		jPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		jPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		jPane.setViewportView(albumTable);
		int tabCount = jTabbedPane1.getTabCount() + 1;
		jTabbedPane1.addTab("tab " + tabCount, jPane);
	}

	private void openMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
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
			JOptionPane.showMessageDialog(this, "Die Datei konnte nicht gefunden werden ", "Fehler",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void showMultipleInputMessageDialog(String absolutePath) throws IOException {

		// JOption Pane code
		final JCheckBox checkBoxForSemiColon = new JCheckBox();
		final JCheckBox checkBoxForComma = new JCheckBox();
		final JCheckBox checkBoxForQuotation = new JCheckBox();
		final JCheckBox checkBoxForAlternativeDelimiter = new JCheckBox();
		// final JCheckBox checkBoxForAlternativeQuote = new JCheckBox();
		final JTextField alternativeDelimiter = new JTextField();
		alternativeDelimiter.setEnabled(false);

		Object[] inputFields = { "Semicolon as delimiter: ;", checkBoxForSemiColon, "Comma as delimiter: ,",
				checkBoxForComma, "Default Quotation: \" ", checkBoxForQuotation, };

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
				}
			}
		});

		checkBoxForComma.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getSource() == checkBoxForComma) {
					checkBoxForSemiColon.setSelected(false);
				}
			}
		});

		int option = JOptionPane.showConfirmDialog(this, inputFields, "Please Choose a delimiter",
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (option == JOptionPane.OK_OPTION && checkBoxForSemiColon.isSelected() && checkBoxForQuotation.isSelected())
			openCSV(absolutePath, ';', '"');

		if (option == JOptionPane.OK_OPTION && checkBoxForComma.isSelected() && checkBoxForQuotation.isSelected())
			openCSV(absolutePath, ',', '"');

		// TODO implement third option with optional imput through textfield
	}

	private void saveAsMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		JFileChooser chooser = new JFileChooser();
		Component parent = null;
		FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV (durch Trennzeichen getrennt) (*.csv)",
				"csv");
		FileNameExtensionFilter filter2 = new FileNameExtensionFilter("PDF (*.pdf)", "pdf");
		chooser.setFileFilter(filter);
		chooser.addChoosableFileFilter(filter2);
		int returnVal = chooser.showSaveDialog(parent);

		if (returnVal == JFileChooser.APPROVE_OPTION) {

			try {
				if (chooser.getFileFilter().getDescription().contains("CSV")) {
					writeCSV(jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()),
							choosedAbsolutFile = chooser.getSelectedFile().getAbsolutePath());
				} else if (chooser.getFileFilter().getDescription().contains("PDF")) {
					writePDF(jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()),
							choosedAbsolutFile = chooser.getSelectedFile().getAbsolutePath());
				}
			} catch (IOException | DocumentException e1) {
				System.out.println("Fehler beim internen Auslesen!");
				e1.printStackTrace();
			}

			chooser.getSelectedFile().getName();
			System.out.println("Die Datei wurde unter: " + choosedAbsolutFile + " gespeichert!");
		} else {
			System.out.println("Das Fenster wurde geschlossen!");
		}
	}

	private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		if (choosedAbsolutFile != null) {
			try {
				writeCSV(jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()), choosedAbsolutFile);
			} catch (IOException e) {
				System.out.println("Fehler beim internen Auslesen!");
				e.printStackTrace();
			}
			System.out.println("Die Datei wurde unter: " + choosedAbsolutFile + " gespeichert!");
		} else {
			JFileChooser chooser = new JFileChooser();
			Component parent = null;
			FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV (durch Trennzeichen getrennt) (*.csv)",
					"csv");
			FileNameExtensionFilter filter2 = new FileNameExtensionFilter("PDF (*.pdf)", "pdf");
			chooser.setFileFilter(filter);
			chooser.addChoosableFileFilter(filter2);
			int returnVal = chooser.showSaveDialog(parent);

			if (returnVal == JFileChooser.APPROVE_OPTION) {

				try {
					if (chooser.getFileFilter().getDescription().contains("CSV")) {
						writeCSV(jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()),
								choosedAbsolutFile = chooser.getSelectedFile().getAbsolutePath());
					} else if (chooser.getFileFilter().getDescription().contains("PDF")) {
						writePDF(jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex()),
								choosedAbsolutFile = chooser.getSelectedFile().getAbsolutePath());
					}
				} catch (IOException e1) {
					System.out.println("Fehler beim internen Auslesen!");
					e1.printStackTrace();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				chooser.getSelectedFile().getName();
				System.out.println("Die Datei wurde unter: " + choosedAbsolutFile + " gespeichert!");
			} else {
				System.out.println("Das Fenster wurde geschlossen!");
			}
		}
	}

	private void jScrollPane1MouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
		int verticalExtent = jScrollPane1.getVerticalScrollBar().getModel().getExtent();
		int horizontalExtent = jScrollPane1.getHorizontalScrollBar().getModel().getExtent();

		if ((jScrollPane1.getVerticalScrollBar().getValue() + verticalExtent) == jScrollPane1.getVerticalScrollBar()
				.getMaximum()) {
			sizeRows++;
			DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
			model.setRowCount(++sizeRows);
		}
		if ((jScrollPane1.getHorizontalScrollBar().getValue() + horizontalExtent) == jScrollPane1
				.getHorizontalScrollBar().getMaximum()) {
			sizeColumns++;
			DefaultTableModel model = (DefaultTableModel) jTable1.getModel();
			model.setColumnCount(++sizeColumns);
		}
	}

	/**
	 * @param args
	 *            the command line arguments
	 */
	public static void main(String args[]) {
		try {
			for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					javax.swing.UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (ClassNotFoundException ex) {
			java.util.logging.Logger.getLogger(WorkbookMainGui.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(WorkbookMainGui.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(WorkbookMainGui.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(WorkbookMainGui.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				new WorkbookMainGui().setVisible(true);
			}
		});
	}

	// Variables declaration
	private javax.swing.JMenuItem aboutMenuItem;
	private javax.swing.JMenuItem contentsMenuItem;
	private javax.swing.JMenuItem copyMenuItem;
	private javax.swing.JMenuItem cutMenuItem;
	private javax.swing.JMenuItem deleteMenuItem;
	private javax.swing.JMenu editMenu;
	private javax.swing.JMenuItem exitMenuItem;
	private javax.swing.JMenu fileMenu;
	private javax.swing.JMenu helpMenu;
	private javax.swing.JScrollPane jScrollPane1;
	private javax.swing.JTabbedPane jTabbedPane1;
	private javax.swing.JTable jTable1;
	private javax.swing.JMenuBar menuBar;
	private javax.swing.JMenuItem openMenuItem;
	private javax.swing.JMenuItem openNewTab;
	private javax.swing.JMenuItem saveAsMenuItem;
	private javax.swing.JMenuItem saveMenuItem;

	private static Workbook activeWorkbook;
	private String choosedAbsolutFile = null;
	private Integer sizeRows = 80;
	private Integer sizeColumns = 30;

}