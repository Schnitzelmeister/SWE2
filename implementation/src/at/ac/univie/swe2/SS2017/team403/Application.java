package at.ac.univie.swe2.SS2017.team403;

import com.itextpdf.text.DocumentException;
import com.opencsv.CSVReader;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.TreeMap;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.AttributeSet;
import javax.swing.JMenuItem;
import javax.swing.JMenu;

public class Application extends javax.swing.JFrame implements WorkbookListener {
	private static final long serialVersionUID = 1L;

    private class BoxKeyListener implements KeyListener {
        private JButton ok;
        private JButton cancel;

        public BoxKeyListener(JButton ok, JButton cancel) {
            this.ok = ok;
            this.cancel = cancel;
        }

        @Override
        public void keyPressed(KeyEvent e){
            if(e.getKeyCode() == KeyEvent.VK_ENTER){
            	e.consume();
            	ok.doClick();
            }
            else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
            	e.consume();
            	cancel.doClick();
            }
        }

        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

	/**
	 * Creates new form WorkbookGui
	 */
	public Application() {
		initComponents();
	}

	private static Workbook activeWorkbook = new Workbook();
	public static Workbook getActiveWorkbook() {
		return activeWorkbook;
	}

	/**
	 * Open proprietary Format File
	 * @param filePath a string which contains the path to File
	 * @throws IOException IOException throws I0Exception 
	 */
	public void openFile(String filefilePathName) throws IOException
	{
		//activeWorkbook
		loadModel();
	}
	
	/**
	 * Write Csv Format File
	 * @param workSheetName a String which contains the name of worksheet
	 * @param filePath a string which contains the path fo write a csv
	 * @throws IOException IOException throws I0Exception 
	 */
	public void writeCSV(String workSheetName, String filePath) throws IOException {
		FileWriter writer = new FileWriter(filePath + ".csv");
		CsvWriteUtility.convertWorkSheetToCsv(activeWorkbook.getWorksheet(workSheetName), writer);
		writer.close();
	}

	/**
	 * Write csv in pdf
	 * @param workSheetName a String which contains the name of worksheet
	 * @param filepath  a string which contains the path fo write a pdf
	 * @throws IOException IOException if not found file
	 * @throws DocumentException DocumentException
	 */
	public void writePDF(String workSheetName, String filepath) throws IOException, DocumentException {
		writeCSV(workSheetName, filepath);
		PDFWriteUtility.convertCSVToPDF(filepath);
	}

	/**
	 * Open CSV format File
	 * 
	 * @param fileLocation a String that gives the location of a file
	 * @param delimiter the char that used as delimiter in csv
	 * @param quotation the quatiation
	 * @throws IOException IOException by open a file
	 */
	public void openCSV(String fileLocation, char delimiter, char quotation) throws IOException {
		CSVReader reader = new CSVReader(new FileReader(fileLocation), delimiter, quotation);
		List<String[]> csvValues = reader.readAll();
		String worksheetName = "Worksheet " + Application.getActiveWorkbook().generateNewId();
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

		loadModel();
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 */
	private void initComponents() {
		Container pane = this.getContentPane();

		Box box = Box.createHorizontalBox();
		labelBox = new JLabel();
		textBox = new JTextArea();
		textBox.setLineWrap(true);
		textBox.getDocument().putProperty("filterNewlines", Boolean.TRUE);
        box.add(labelBox);
        box.add(textBox);
        JButton jButtonOK = new JButton("OK");
        jButtonOK.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e) {
        		boxDataAccepted();
        	}
        });
        box.add(jButtonOK);
        
        
        JButton jButtonCancel = new JButton("Cancel");
        jButtonCancel.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e) {
        		boxDataCanceled();
        	}
        });
        box.add(jButtonCancel);
        
        textBox.addKeyListener(new BoxKeyListener(jButtonOK, jButtonCancel));

        
		
		jTabbedPane1 = new javax.swing.JTabbedPane();
		jTabbedPane1.setTabPlacement(JTabbedPane.BOTTOM);
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
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		Integer numberOfWorksheets = activeWorkbook.getWorksheets().size() + 1;
		Worksheet worksheet = activeWorkbook.addSheet("Worksheet " + numberOfWorksheets);
		jTable1.setModel(new CustomTableModel(worksheet, jTable1, jScrollPane1));
		jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		jTable1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		jTable1.setGridColor(new java.awt.Color(0, 0, 0));
		jScrollPane1.setViewportView(jTable1);
		jTabbedPane1.addTab(worksheet.getWorksheetName(), jScrollPane1);
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
		editMenu.setText("Worksheets");
		openNewTab.setText("Add New Worksheet");
		openNewTab.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openNewTabActionPerformed(evt);
			}
		});
		editMenu.add(openNewTab);
		closeCurrentTab = new JMenuItem("Remove Worksheet");
		/*closeCurrentTab.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				closeTab();
			}
		});
		*/
		editMenu.add(closeCurrentTab);
		renameTabMenuItem = new JMenuItem("Rename Worksheet");
		editMenu.add(renameTabMenuItem);
		/*renameTabMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				renameTabActionPerformed(evt);
			}
		});*/
		menuBar.add(editMenu);
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
		
		renameDiagram = new JMenuItem("Rename Diagram");
		mnCharts.add(renameDiagram);
		
		removeDiagram = new JMenuItem("Remove Diagram");
		mnCharts.add(removeDiagram);
		createBarchartMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				createBarchartMenuActionPerformed(evt);
			}
		});
		setJMenuBar(menuBar);
		
		
		pane.add(box, BorderLayout.PAGE_START);
		pane.add(jTabbedPane1, BorderLayout.CENTER);
		jTabbedPane1.addChangeListener(new ChangeListener() {
	        public void stateChanged(ChangeEvent e) {
	        	tabChanged(e);
	        }
	    });
		
		pack();
		setLocationRelativeTo(null);

	}

	private void createLineChartActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO open new tab and place linechart on tab
		//Application.getActiveWorkbook().addDiagram("name", DiagramLine.class);
	}

	private void createBarchartMenuActionPerformed(java.awt.event.ActionEvent evt) {
		// TODO open new tab and place linechart on tab
	}

	private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {
		System.exit(0);
	}

	private void renameTabActionPerformed(java.awt.event.ActionEvent evt) {
		/*try {
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
*/
	}

	private void openNewTabActionPerformed(java.awt.event.ActionEvent evt) {
		String worksheetName = "Worksheet " + (Application.getActiveWorkbook().generateNewId() - 1);
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
		/*int verticalExtent = jScrollPane1.getVerticalScrollBar().getModel().getExtent();
		int horizontalExtent = jScrollPane1.getHorizontalScrollBar().getModel().getExtent();

		if ((jScrollPane1.getVerticalScrollBar().getValue() + verticalExtent) == jScrollPane1.getVerticalScrollBar()
				.getMaximum()) {
			sizeRows++;
			TableModel model = (CustomTableModel) jTable1.getModel();
			// model.setRowCount(++sizeRows); //TODO
		}
		if ((jScrollPane1.getHorizontalScrollBar().getValue() + horizontalExtent) == jScrollPane1
				.getHorizontalScrollBar().getMaximum()) {
			sizeColumns++;
			TableModel model = (CustomTableModel) jTable1.getModel();
			// model.setColumnCount(++sizeColumns); //TODO
		}*/
	}

	public void closeTab() {
		/*
		Component selected = jTabbedPane1.getSelectedComponent();
		JViewport viewport = ((JScrollPane) selected).getViewport();
		JTable mytable = (JTable) viewport.getView();
		TableModel model = mytable.getModel();
		CustomTableModel custommodel = (CustomTableModel) model;
		String sheetName = custommodel.getWorksheetName();

		if (selected != null)
			jTabbedPane1.remove(selected);

		afterWorksheetRemoved(sheetName);*/
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
			java.util.logging.Logger.getLogger(Application.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (InstantiationException ex) {
			java.util.logging.Logger.getLogger(Application.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (IllegalAccessException ex) {
			java.util.logging.Logger.getLogger(Application.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		} catch (javax.swing.UnsupportedLookAndFeelException ex) {
			java.util.logging.Logger.getLogger(Application.class.getName()).log(java.util.logging.Level.SEVERE,
					null, ex);
		}

		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				Application gui = new Application();
				
				boolean createDefaultWorkbook = true;
				
				if (args.length > 0) {
					//open file
					if (args[0].toUpperCase().endsWith(".wbk")) {
						try { gui.openFile(args[0]); }
						catch(IOException e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(null, e.getMessage());
						}
						createDefaultWorkbook = false;
					}
					//open csv
					else if (args.length > 2) {
						try { gui.openCSV(args[0], args[1].charAt(0), args[2].charAt(0)); }
						catch(IOException e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(null, e.getMessage());
						}
						createDefaultWorkbook = false;
					}
				}
				
				if (createDefaultWorkbook) {
					//create default workbook
					System.out.println("create default workbook");

					Application.activeWorkbook = new Workbook();
					Application.activeWorkbook.addSheet("Sheet 1");
					Application.activeWorkbook.addSheet("Sheet 2");
					Application.activeWorkbook.addSheet("Sheet 3");
				}
				
				gui.loadModel();
								
				gui.setVisible(true);
			}
		});
	}

	public boolean isNumber(String str) {
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	// Variables declaration
	private JLabel labelBox;
	private JTextArea textBox;


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

	private String choosedAbsolutFile = null;
	private Integer sizeRows = 80;
	private Integer sizeColumns = 30;
	private JMenuItem closeCurrentTab;
	private JMenuItem renameTabMenuItem;
	private JMenu mnCharts;
	private JMenuItem createLinechartMenu;
	private JMenuItem createBarchartMenu;
	private JMenuItem removeDiagram;
	private JMenuItem renameDiagram;
	
	@Override
	public void afterWorksheetAdded(String worksheetName) {
		loadWorksheet(Application.getActiveWorkbook().getWorksheet(worksheetName));
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
		tables.remove(worksheetName);
		jTabbedPane1.remove(jTabbedPane1.indexOfTab(worksheetName));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * at.ac.univie.swe2.SS2017.team403.WorkbookListener#afterWorksheetRenamed(
	 * java.lang.String, java.lang.String) The method is being invoced when the
	 * user decides to rename the current worksheet which implicitely changes
	 * the name of the tab
	 */
	@Override
	public void afterWorksheetRenamed(String worksheetOldName, String worksheetNewName) {
		tables.put(worksheetNewName, tables.get(worksheetOldName));
		tables.remove(worksheetOldName);
		jTabbedPane1.setTitleAt(jTabbedPane1.indexOfTab(worksheetOldName), worksheetNewName);
	}

	@Override
	public void afterCellChanged(String worksheetName, int row, int column, Object newValue) {
		JTable t = tables.get(worksheetName);
		((CustomTableModel)t.getModel()).fireTableCellUpdated(row - 1, column - 1);
	}

	@Override
	public void afterDiagramAdded(String diagramName) {
		loadDiagram(Application.getActiveWorkbook().getDiagram(diagramName));
	}

	@Override
	public void afterDiagramRemoved(String diagramName) {
		diagrams.remove(diagramName);
		jTabbedPane1.remove(jTabbedPane1.indexOfTab(diagramName));
	}

	@Override
	public void afterDiagramRenamed(String diagramOldName, String diagramNewName) {
		diagrams.put(diagramNewName, diagrams.get(diagramOldName));
		diagrams.remove(diagramOldName);
		jTabbedPane1.setTitleAt(jTabbedPane1.indexOfTab(diagramOldName), diagramNewName);
	}

	@Override
	public void afterDiagramChanged(String diagramName) {
		diagrams.get(diagramName).Invalidate();
	}

	
	
	private TreeMap<String, DiagramInvalidateStrategy> diagrams = new TreeMap<String, DiagramInvalidateStrategy>();
	private TreeMap<String, JTable> tables = new TreeMap<String, JTable>();
	private boolean isCurrentWorkbook = true;
	private String activeObjectName;

	private void selectedCellChanged() {
		boxDataCanceled();
	}

	private void tabChanged(ChangeEvent e) throws IllegalArgumentException {
		if (jTabbedPane1.getSelectedIndex() == -1)
			return;
		activeObjectName = jTabbedPane1.getTitleAt(jTabbedPane1.getSelectedIndex());
		isCurrentWorkbook = (Application.getActiveWorkbook().getWorksheets().containsKey(activeObjectName));
		
		if (isCurrentWorkbook) {
			labelBox.setText("Cell value:");
		}
		else {
			labelBox.setText("Diagram area:");
			Diagram d = Application.getActiveWorkbook().getDiagram(activeObjectName);
			Area a = null;
			if (d instanceof DiagramBar)
				a = ((DiagramBar)d).getValues();
			else if (d instanceof DiagramLine)
				a = ((DiagramLine)d).getValues();
			else
				throw new IllegalArgumentException("Unknown DiagramClass " + d.getClass().getName());
			
			if (a == null)
				textBox.setText("");
			else
				textBox.setText(a.getCellReferences());
		}
		
	}
	
	private void boxDataAccepted() throws IllegalArgumentException {
		try {
			if (isCurrentWorkbook) {
				JTable t = tables.get(activeObjectName);
				Cell cell = Application.getActiveWorkbook().getWorksheet(activeObjectName).getCell(t.getSelectedRow() + 1, t.getSelectedColumn() + 1);
				if (textBox.getText().startsWith("="))
					cell.setFormula(textBox.getText());
				else if (isNumber(textBox.getText()))
					cell.setNumericValue(Double.parseDouble(textBox.getText()));
				else
					cell.setTextValue(textBox.getText());
				
				t.grabFocus();
			}
			else {
				Diagram d = Application.getActiveWorkbook().getDiagram(activeObjectName);
				if (d instanceof DiagramBar)
					((DiagramBar)d).setValues(Range.getRangeByAddress(textBox.getText(), null).getWorksheetAreas().firstKey());
				else if (d instanceof DiagramLine)
					((DiagramLine)d).setValues(Range.getRangeByAddress(textBox.getText(), null).getWorksheetAreas().firstKey());
				else
					throw new IllegalArgumentException("Unknown DiagramClass " + d.getClass().getName());
			}
		}
		catch(IllegalArgumentException e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
			boxDataCanceled();
		}
	}
	
	private void boxDataCanceled() throws IllegalArgumentException {
		if (isCurrentWorkbook) {
			JTable t = tables.get(activeObjectName);
			if (t.getSelectedRow() < 0 || t.getSelectedColumn() < 0) {
				textBox.setText("");
				return;
			}
			
			Cell cell = Application.getActiveWorkbook().getWorksheet(activeObjectName).getCell(t.getSelectedRow() + 1, t.getSelectedColumn() + 1, false);
			if (cell == null)
				textBox.setText("");
			else if (cell.getFormula() != null)
				textBox.setText(cell.getFormula());
			else if (cell.getCellValue() == null)
				textBox.setText("");
			else
				textBox.setText(cell.getTextValue());
			
			t.grabFocus();
		}
		else {
			Diagram d = Application.getActiveWorkbook().getDiagram(activeObjectName);
			Area a = null;
			if (d instanceof DiagramBar)
				a = ((DiagramBar)d).getValues();
			else if (d instanceof DiagramLine)
				a = ((DiagramLine)d).getValues();
			else
				throw new IllegalArgumentException("Unknown DiagramClass " + d.getClass().getName());
			
			if (a == null)
				textBox.setText("");
			else
				textBox.setText(a.getCellReferences());
		}

	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * load current object modell in gui
	 */
	private void loadModel() {
		//observe Application
		Application.activeWorkbook.removeListener(this);

		jTabbedPane1.removeAll();
		
		for (Worksheet w : Application.getActiveWorkbook().getWorksheets().values()) {
			System.out.println(w.getWorksheetName());
			loadWorksheet(w);
		}

		for (Diagram d : Application.getActiveWorkbook().getDiagrams().values())
			loadDiagram(d);
		
		jTabbedPane1.setSelectedIndex(0);
		
		//observe Application
		Application.activeWorkbook.addListener(this);

	}
	
	private void loadWorksheet(Worksheet worksheet) {

		if ( jTabbedPane1.indexOfTab(worksheet.getWorksheetName()) != -1 )
			jTabbedPane1.remove(jTabbedPane1.indexOfTab(worksheet.getWorksheetName()));
		
		JScrollPane jPane = new JScrollPane();
		JTable table = new JTable();
		CustomTableModel model = new CustomTableModel(worksheet, table, jPane);
		table.setModel(model);
		table.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
		table.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
		table.setRowSelectionAllowed(false);
		table.setGridColor(new java.awt.Color(0, 0, 0));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setCellSelectionEnabled(true);
		
		jPane.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		jPane.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		table.setBorder(new EtchedBorder(EtchedBorder.RAISED));
		table.setShowHorizontalLines(true);
		table.setShowVerticalLines(true);
		jPane.setViewportView(table);
		jTabbedPane1.addTab(worksheet.getWorksheetName(), jPane);
		tables.put(worksheet.getWorksheetName(), table);

		ListSelectionListener l = new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				//if (e.getValueIsAdjusting())
					selectedCellChanged();
			}
		};
		
		table.getSelectionModel().addListSelectionListener(l);
		table.getColumnModel().getSelectionModel().addListSelectionListener(l);
		
		table.addKeyListener(new KeyListener() {
	        @Override
	        public void keyPressed(KeyEvent e){
	        }

	        @Override
	        public void keyTyped(KeyEvent e) {
	            switch(e.getKeyCode()) {
	            	case KeyEvent.VK_ENTER:
	        		case KeyEvent.VK_LEFT:
	        		case KeyEvent.VK_RIGHT:
	        		case KeyEvent.VK_UP:
	        		case KeyEvent.VK_DOWN:
	        		case KeyEvent.VK_PAGE_UP:
	        		case KeyEvent.VK_PAGE_DOWN:
	        			break;
	        		default:
	        			textBox.grabFocus();
	        			textBox.setText(e.getKeyChar() + "");
	        			break;
	            }
	        }

	        @Override
	        public void keyReleased(KeyEvent e) {
	        }
		});
		
		table.setRowSelectionInterval(0, 0);
	    table.setColumnSelectionInterval(0, 0);

	}

	private void loadDiagram(Diagram diagram) throws IllegalArgumentException {
		if ( jTabbedPane1.indexOfTab(diagram.getName()) != -1 )
			jTabbedPane1.remove(jTabbedPane1.indexOfTab(diagram.getName()));

		JPanel container = new JPanel();
		jTabbedPane1.addTab(diagram.getName(), container);
		DiagramInvalidateStrategy strategy;

		if (diagram instanceof DiagramBar)
			strategy = new BarDiagramInvalidateStrategy();
		else if (diagram instanceof DiagramLine)
			strategy = new LineDiagramInvalidateStrategy();
		else
			throw new IllegalArgumentException("Unknown DiagramClass " + diagram.getClass().getName());
		
		diagrams.put(diagram.getName(), strategy);
		strategy.Initialize(diagram, container);
		strategy.Invalidate();
	}

}
