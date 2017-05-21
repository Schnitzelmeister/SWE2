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
import javax.swing.JViewport;
import javax.swing.event.ChangeEvent;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.AttributeSet;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

public class WorkbookMainGui extends javax.swing.JFrame implements WorkbookListener {

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
		CSVReader reader = new CSVReader(new FileReader(fileLocation), delimiter, quotation);
		List<String[]> csvValues = reader.readAll();
		String worksheetName = "Worksheet " + Application.getActiveWorkbook().getNewId();
		Worksheet sheet = Application.getActiveWorkbook().addSheet(worksheetName);
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

		//System.out.println("gelesen rows=" + r + ", cols=" + sheet.getUsedArea().getC2());

		TableModel model = new CustomTableModel(sheet);
		returnTableForCurrentTab().setModel(model);
	}

	public JTable returnTableForCurrentTab() {
		Component selected = jTabbedPane1.getSelectedComponent();
		JViewport viewport = ((JScrollPane) selected).getViewport();
		JTable table = (JTable) viewport.getView();
		return table;
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

		Integer numberOfWorksheets = activeWorkbook.getNumberOfSheets() + 1;
		Worksheet worksheet = activeWorkbook.addSheet("Worksheet "+ numberOfWorksheets );

		jScrollPane1.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
			public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
				jScrollPane1MouseWheelMoved(evt);
			}
		});

		jTable1.setModel(new CustomTableModel(worksheet));

		jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable1.setCursor(new
		java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		jTable1.setGridColor(new java.awt.Color(0, 0, 0));
		jScrollPane1.setViewportView(jTable1);

		jTabbedPane1.addTab(worksheet.getName(), jScrollPane1);

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

		closeCurrentTab = new JMenuItem("Close Tab");
		closeCurrentTab.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				closeTab();
			}
		});
		editMenu.add(closeCurrentTab);

		renameTabMenuItem = new JMenuItem("Rename Tab");
		editMenu.add(renameTabMenuItem);
		renameTabMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				renameTabActionPerformed(evt);
			}
		});

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
		
		mnCharts = new JMenu("Charts");
		menuBar.add(mnCharts);
		
		createLinechartMenu = new JMenuItem("Create Linechart");
		mnCharts.add(createLinechartMenu);
		createLinechartMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				createLineChartActionPerformed(evt);
			}
		});
		
		createBarchartMenu = new JMenuItem("Create Barchart");
		mnCharts.add(createBarchartMenu);
		createBarchartMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				createBarchartMenuActionPerformed(evt);
			}
		});
		
		
		javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
		getContentPane().setLayout(layout);
		layout.setHorizontalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 800, Short.MAX_VALUE));
		layout.setVerticalGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
				.addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 579, Short.MAX_VALUE));

		pack();
		setLocationRelativeTo(null);
	}

	private void createLineChartActionPerformed(java.awt.event.ActionEvent evt){
		//TODO open new tab and place linechart on tab
	}
	
	private void createBarchartMenuActionPerformed(java.awt.event.ActionEvent evt){
		//TODO open new tab and place linechart on tab
	}
	
	private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		System.exit(0);
	}

	private void renameTabActionPerformed(java.awt.event.ActionEvent evt) {
		try {
			JTable currentTable = returnTableForCurrentTab();
			CustomTableModel model = (CustomTableModel) currentTable.getModel();
			String oldName = model.getWorksheetName();

			JTextField inputFieldnewName = new JTextField();

			Object[] inputFields = { "New name: ", inputFieldnewName };
			int option = JOptionPane.showConfirmDialog(this, inputFields, "Please choose a tab name",
					JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (option == JOptionPane.CANCEL_OPTION)
				return;

			if (option == JOptionPane.OK_OPTION) {
				String nameToString = inputFieldnewName.getText();
				if (nameToString.length() < 1 || nameToString.length() > 20)
					throw new IllegalArgumentException("Invalid value for tab name");
				afterWorksheetRenamed(oldName, nameToString);
				return;
			}
		} catch (IllegalArgumentException i) {
			JOptionPane.showMessageDialog(this, i.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
		}

	}

	private void openNewTabActionPerformed(java.awt.event.ActionEvent evt) {
		String worksheetName = "Worksheet " + (Application.getActiveWorkbook().getNewId() - 1);
		Application.getActiveWorkbook().addSheet(worksheetName);
		afterWorksheetAdded(worksheetName);
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
		} catch (IllegalArgumentException i) {
			System.out.println("showMultipleInputMessageDialog threw IllegalArgumentException ");
			JOptionPane.showMessageDialog(this, i.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
		}
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
				checkBoxForComma, "Optional delimiter: ", checkBoxForAlternativeDelimiter, alternativeDelimiter,
				"Default quotation: \" ", checkBoxForDefaultQuote, "Alternative quotation: ",
				checkBoxForAlternativeQuote, alternativeQotation };

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

		if (option == JOptionPane.CANCEL_OPTION)
			return;

		if (option == JOptionPane.OK_OPTION && checkBoxForSemiColon.isSelected()
				&& checkBoxForDefaultQuote.isSelected()) {
			openCSV(absolutePath, ';', '"');
			return;
		}

		if (option == JOptionPane.OK_OPTION && checkBoxForComma.isSelected() && checkBoxForDefaultQuote.isSelected()) {
			openCSV(absolutePath, ',', '"');
			return;
		}

		if (option == JOptionPane.OK_OPTION && checkBoxForSemiColon.isSelected()
				&& checkBoxForAlternativeQuote.isSelected()) {
			String alternativeQuoteString = alternativeQotation.getText();
			if (alternativeQuoteString.length() > 1 || alternativeQuoteString.length() == 0)
				throw new IllegalArgumentException("The value entered cannot be used as a quote");
			char alternativeQuoteChar = alternativeQuoteString.charAt(0);
			openCSV(absolutePath, ';', alternativeQuoteChar);
			return;
		}

		if (option == JOptionPane.OK_OPTION && checkBoxForComma.isSelected()
				&& checkBoxForAlternativeQuote.isSelected()) {
			String alternativeQuoteString = alternativeQotation.getText();
			if (alternativeQuoteString.length() > 1 || alternativeQuoteString.length() == 0)
				throw new IllegalArgumentException("The value entered cannot be used as a quote");
			char alternativeQuoteChar = alternativeQuoteString.charAt(0);
			openCSV(absolutePath, ',', alternativeQuoteChar);
			return;
		}

		if (option == JOptionPane.OK_OPTION && checkBoxForAlternativeDelimiter.isSelected()
				&& checkBoxForAlternativeQuote.isSelected()) {
			String alternativeDelimiterString = alternativeDelimiter.getText();
			if (alternativeDelimiterString.length() > 1 || alternativeDelimiterString.length() == 0)
				throw new IllegalArgumentException("The value entered cannot be used as a delimiter");
			char alternativeDelimiterChar = alternativeDelimiterString.charAt(0);

			String alternativeQuoteString = alternativeQotation.getText();
			if (alternativeQuoteString.length() > 1 || alternativeQuoteString.length() == 0)
				throw new IllegalArgumentException("The value entered cannot be used as a quote");
			char alternativeQuoteChar = alternativeQuoteString.charAt(0);

			openCSV(absolutePath, alternativeDelimiterChar, alternativeQuoteChar);
			return;
		}

		if (option == JOptionPane.OK_OPTION && checkBoxForAlternativeDelimiter.isSelected()
				&& checkBoxForDefaultQuote.isSelected()) {
			String alternativeDelimiterString = alternativeDelimiter.getText();
			if (alternativeDelimiterString.length() > 1 || alternativeDelimiterString.length() == 0)
				throw new IllegalArgumentException("The value entered cannot be used as a delimiter");
			char alternativeDelimiterChar = alternativeDelimiterString.charAt(0);
			openCSV(absolutePath, alternativeDelimiterChar, '"');
			return;
		}

		throw new IllegalArgumentException("Missing input");
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
			TableModel model = (CustomTableModel) jTable1.getModel();
			model.setRowCount(++sizeRows);
		}
		if ((jScrollPane1.getHorizontalScrollBar().getValue() + horizontalExtent) == jScrollPane1
				.getHorizontalScrollBar().getMaximum()) {
			sizeColumns++;
			TableModel model = (CustomTableModel) jTable1.getModel();
			model.setColumnCount(++sizeColumns);
		}
	}

	public void closeTab() {
		Component selected = jTabbedPane1.getSelectedComponent();
		JViewport viewport = ((JScrollPane) selected).getViewport();
		JTable mytable = (JTable) viewport.getView();
		TableModel model = mytable.getModel();
		CustomTableModel custommodel = (CustomTableModel) model;
		String sheetName = custommodel.getWorksheetName();

		if (selected != null)
			jTabbedPane1.remove(selected);

		afterWorksheetRemoved(sheetName);
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

	public boolean isNumber(String str) {
		try {
			double d = Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
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

	private static Workbook activeWorkbook = new Workbook();
	private String choosedAbsolutFile = null;
	private Integer sizeRows = 80;
	private Integer sizeColumns = 30;
	private JMenuItem closeCurrentTab;
	private JMenuItem renameTabMenuItem;
	private JMenu mnCharts;
	private JMenuItem createLinechartMenu;
	private JMenuItem createBarchartMenu;

	@Override
	public void afterWorksheetAdded(String worksheetName) {
		JScrollPane jPane = new JScrollPane();
		JTable albumTable = new JTable();
		Worksheet sheet = Application.getActiveWorkbook().getSheet(worksheetName);
		
		TableModel model = new CustomTableModel(sheet);
		albumTable.setModel(model);

		jPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		jPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		jPane.setViewportView(albumTable);
		int tabCount = jTabbedPane1.getTabCount();
		albumTable.setName("TableNr.: " + tabCount);
		jTabbedPane1.addTab(worksheetName, jPane);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.ac.univie.swe2.SS2017.team403.WorkbookListener#afterWorksheetRemoved(
	 * java.lang.String) This method is being invoced after the user decides to
	 * close the tab by the methode closeTab()
	 */
	@Override
	public void afterWorksheetRemoved(String worksheetName) {
		Application.getActiveWorkbook().removeSheet(worksheetName);
	}

	/*
	 * (non-Javadoc)
	 * @see at.ac.univie.swe2.SS2017.team403.WorkbookListener#afterWorksheetRenamed(java.lang.String, java.lang.String)
	 * The method is being invoced when the user decides to rename the current worksheet which implicitely changes the name of the tab
	 */
	@Override
	public void afterWorksheetRenamed(String worksheetOldName, String worksheetNewName) {
		JTable currentTable = returnTableForCurrentTab();
		CustomTableModel model = (CustomTableModel) currentTable.getModel();
		Worksheet sheet = Application.getActiveWorkbook().getSheet(model.getWorksheetName());
		System.out.println("The worksheet with the name: "+worksheetOldName+" has been changed to the name: "+worksheetNewName);
		sheet.setName(worksheetNewName);
		int indexOfTab = jTabbedPane1.getSelectedIndex();
		jTabbedPane1.setTitleAt(indexOfTab, worksheetNewName);
	}

	@Override
	public void afterCellChanged(String worksheetName, int row, int column, Object newValue) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterDiagramAdded(String diagramName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterDiagramRemoved(String diagramName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterDiagramRenamed(String diagramOldName, String diagramNewName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void afterDiagramChanged(String diagramName) {
		// TODO Auto-generated method stub

	}

}
