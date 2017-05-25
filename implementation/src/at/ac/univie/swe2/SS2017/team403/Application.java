package at.ac.univie.swe2.SS2017.team403;

import com.itextpdf.text.DocumentException;
import com.opencsv.CSVReader;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
import javax.swing.ListSelectionModel;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
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

	private TreeMap<String, DiagramInvalidateStrategy> diagrams = new TreeMap<String, DiagramInvalidateStrategy>();
	private TreeMap<String, JTable> tables = new TreeMap<String, JTable>();
	private boolean isCurrentWorkbook = true;
	private String activeObjectName;
	
	// gui variables declaration
	private JLabel labelBox;
	private JTextArea textBox;

	private javax.swing.JMenu editMenu;
	private javax.swing.JMenuItem exitMenuItem;
	private javax.swing.JMenu fileMenu;
	private javax.swing.JTabbedPane jTabbedPanel;
	private javax.swing.JMenuBar menuBar;
	private javax.swing.JMenuItem openMenuItem;
	private javax.swing.JMenuItem openNewTab;
	private javax.swing.JMenuItem saveAsMenuItem;
	private javax.swing.JMenuItem newMenuItem;

	private String fileActualName = null;
	private JMenuItem closeCurrentTab;
	private JMenuItem renameTabMenuItem;
	private JMenu mnCharts;
	private JMenuItem createLinechartMenu;
	private JMenuItem createBarchartMenu;
	private JMenuItem removeDiagram;
	private JMenuItem renameDiagram;

	
	/**
	 * Open proprietary Format File
	 * @param filePath a string which contains the path to File
	 * @throws IllegalArgumentException to handle the Exceptions by opening a File 
	 */
	public void openFile(String filePath) throws IllegalArgumentException
	{
		try {
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath));
			activeWorkbook = (Workbook)ois.readObject();
			ois.close();
			fileActualName = filePath;
		}
		catch (FileNotFoundException e) {
			throw new IllegalArgumentException("File not found " + filePath + " " + e.getMessage());
		}
	    catch(ClassNotFoundException e) {
			throw new IllegalArgumentException("Illegal DataSource " + filePath + " " + e.getMessage());
		}
		catch (IOException e) {
			throw new IllegalArgumentException("IO Exception in DataSource " + filePath + " " + e.getMessage());
		}

		loadModel();
	}

	/**
	 * Save proprietary Format File
	 * @param filePath a string which contains the path to File
	 * @param overwrite a boolean value that is true if the file exists and the file will be overwrite.
	 * @throws IllegalArgumentException throws if the filepath not acceptable
	 */
	public void saveFile(String filePath, boolean overwrite) throws IllegalArgumentException
	{
		File f = new File(filePath);
		if(!overwrite && f.exists() && !f.isDirectory())
			throw new IllegalArgumentException("File exists " + filePath);

		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath));
			oos.writeObject(activeWorkbook);
			oos.flush();
			oos.close();
			fileActualName = filePath;
		} catch (IOException e) {
			e.printStackTrace();
			//throw new IllegalArgumentException("IO Serialization Exception occured");
		}
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
	 * 
	 * Open CSV format File
	 * @param fileLocation fileLocation a String that gives the location of a file
	 * @param delimiter delimiter the char that used as delimiter in csv
	 * @param quotation quotation the quotation
	 * @throws IllegalArgumentException if the location not true
	 */
	public void openCSV(String fileLocation, char delimiter, char quotation) throws IllegalArgumentException {
		Worksheet sheet = null;
		try {
			CSVReader reader = new CSVReader(new FileReader(fileLocation), delimiter, quotation);
			List<String[]> csvValues = reader.readAll();
			sheet = Application.getActiveWorkbook().addSheet(getDefaultSheetName());
			int r = 0;
			for (String[] ar : csvValues) {
				++r;
				for (int c = 0; c < ar.length; ++c) {
					String v = ar[c].replace(',', '.');
					if (isNumber(v))
						sheet.getCell(r, c + 1).setNumericValue(Double.valueOf(v));
					else
						sheet.getCell(r, c + 1).setTextValue(ar[c]);
				}
			}
			reader.close();
		}
		catch (IOException e) {
			throw new IllegalArgumentException("failed to open " + fileLocation);			
		}
		
		loadWorksheet(sheet);
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
		
		jTabbedPanel = new javax.swing.JTabbedPane();
		jTabbedPanel.setTabPlacement(JTabbedPane.BOTTOM);
		menuBar = new javax.swing.JMenuBar();
		fileMenu = new javax.swing.JMenu();
		openMenuItem = new javax.swing.JMenuItem();
		newMenuItem = new javax.swing.JMenuItem();
		saveAsMenuItem = new javax.swing.JMenuItem();
		exitMenuItem = new javax.swing.JMenuItem();
		editMenu = new javax.swing.JMenu();
		openNewTab = new javax.swing.JMenuItem();
		setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
		fileMenu.setText("File");
		
		openMenuItem.setText("Open");
		openMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				openFileActionPerformem(evt);
			}
		});
		fileMenu.add(openMenuItem);
		newMenuItem.setText("New");
		newMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				int dialogResult = JOptionPane.showConfirmDialog (null, "All unsaved data will be lost. Do you want to create new Workbook ?", "Warning", JOptionPane.YES_NO_OPTION);
				if(dialogResult == JOptionPane.YES_OPTION){
					fileActualName = null;
					Application.activeWorkbook = new Workbook();
					Application.activeWorkbook.addSheet("Sheet 1");
					Application.activeWorkbook.addSheet("Sheet 2");
					Application.activeWorkbook.addSheet("Sheet 3");
					loadModel();
				}
			}
		});
		fileMenu.add(newMenuItem);
		saveAsMenuItem.setText("Save As ...");
		saveAsMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				saveActionPerformed(evt);
			}
		});
		fileMenu.add(saveAsMenuItem);
		exitMenuItem.setText("Exit");
		exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				int dialogResult = JOptionPane.showConfirmDialog (null, "All unsaved data will be lost. Do you want to close the application ?", "Warning", JOptionPane.YES_NO_OPTION);
				if(dialogResult == JOptionPane.YES_OPTION){
					System.exit(0);
				}
			}
		});
		fileMenu.add(exitMenuItem);
		menuBar.add(fileMenu);
		editMenu.setText("Worksheets");
		openNewTab.setText("Add New Worksheet");
		openNewTab.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				String name = getNewName(getDefaultSheetName(), "Please choose a worksheet name", "Worksheet name:");
				if (name != null)
					getActiveWorkbook().addSheet(name);
			}
		});
		editMenu.add(openNewTab);
		closeCurrentTab = new JMenuItem("Remove Worksheet");
		closeCurrentTab.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (!isCurrentWorkbook)
					return;
				
				int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Remove current Worksheet '" + activeObjectName + "'?", "Warning", JOptionPane.YES_NO_OPTION);
				if(dialogResult == JOptionPane.YES_OPTION){
					getActiveWorkbook().removeSheet(activeObjectName);
				}
			}
		});
		
		editMenu.add(closeCurrentTab);
		renameTabMenuItem = new JMenuItem("Rename Worksheet");
		editMenu.add(renameTabMenuItem);
		
		renameTabMenuItem.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (!isCurrentWorkbook)
					return ;
				String name = getNewName(activeObjectName, "Please choose new worksheet name", "Worksheet name:");
				if (name != null)
					getActiveWorkbook().getWorksheet(activeObjectName).setWorksheetName(name);
			}
		});
		menuBar.add(editMenu);
		mnCharts = new JMenu("Charts");
		menuBar.add(mnCharts);
		createLinechartMenu = new JMenuItem("Create Linechart");
		mnCharts.add(createLinechartMenu);
		
		createLinechartMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				String name = getNewName(getDefaultChartName(), "Please choose a chart name", "Chart name:");
				if (name != null) {
					DiagramCreator creator = new DiagramLineCreator();
					Application.getActiveWorkbook().addDiagram(name, creator);
				}
			}
		});
		createBarchartMenu = new JMenuItem("Create Barchart");
		
		createBarchartMenu.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				String name = getNewName(getDefaultChartName(), "Please choose a chart name", "Chart name:");
				if (name != null) {
					DiagramCreator creator = new DiagramBarCreator();
					Application.getActiveWorkbook().addDiagram(name, creator);
				}
			}
		});
		
		mnCharts.add(createBarchartMenu);
		renameDiagram = new JMenuItem("Rename Diagram");
		mnCharts.add(renameDiagram);
		renameDiagram.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (isCurrentWorkbook)
					return ;

				String name = getNewName(activeObjectName, "Please choose new chart name", "Chart name:");
				if (name != null)
					getActiveWorkbook().getDiagram(activeObjectName).setName(name);
			}
		});
		
		removeDiagram = new JMenuItem("Remove Diagram");
		mnCharts.add(removeDiagram);
		removeDiagram.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				if (isCurrentWorkbook)
					return;
				
				int dialogResult = JOptionPane.showConfirmDialog (null, "Would You Like to Remove current Chart '" + activeObjectName + "'?", "Warning", JOptionPane.YES_NO_OPTION);
				if(dialogResult == JOptionPane.YES_OPTION){
					getActiveWorkbook().removeDiagram(activeObjectName);
				}
			}
		});

		setJMenuBar(menuBar);
		
		
		pane.add(box, BorderLayout.PAGE_START);
		pane.add(jTabbedPanel, BorderLayout.CENTER);
		jTabbedPanel.addChangeListener(new ChangeListener() {
	        public void stateChanged(ChangeEvent e) {
	        	tabChanged(e);
	        }
	    });
		
		pack();
		setLocationRelativeTo(null);
	}

	private String getDefaultSheetName() {
		return _getDefaultName("Sheet ");
	}

	private String getDefaultChartName() {
		return _getDefaultName("Chart ");
	}
	
	private String _getDefaultName(String prefix) {
		for (int i = 1; i < Integer.MAX_VALUE; i++) {
			String ret = prefix + i;
			if (!getActiveWorkbook().getWorksheets().containsKey(ret) && !getActiveWorkbook().getDiagrams().containsKey(ret))
				return ret;
		}
		return null;
	}
	
	private String getNewName(String defaultName, String msg, String nameName) {
		JTextField inputFieldnewName = new JTextField();
		inputFieldnewName.setText(defaultName);
		
		Object[] inputFields = { nameName, inputFieldnewName };
		int option = JOptionPane.showConfirmDialog(this, inputFields, msg,
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (option == JOptionPane.OK_OPTION) {
			return inputFieldnewName.getText();
		}
		return null;
	}


	private String getOpenFileName() {
		try {
			JFileChooser chooser = new JFileChooser();
			Component parent = null;
			FileNameExtensionFilter filter = new FileNameExtensionFilter("Supported File Extensions (CSV, WBK)", "csv", "wbk");
			chooser.setFileFilter(filter);
			int returnVal = chooser.showOpenDialog(parent);

			if (returnVal == JFileChooser.APPROVE_OPTION)
				return chooser.getSelectedFile().getAbsolutePath();
		}
		catch (IllegalArgumentException i) {
			JOptionPane.showMessageDialog(this, i.getMessage(), "Fehler", JOptionPane.ERROR_MESSAGE);
		}
		
		return null;
	}
	
	private void openFileActionPerformem(java.awt.event.ActionEvent evt) throws IllegalArgumentException {
		String fName = getOpenFileName();
		if (fName == null)
			return;
		
		if (fName.toUpperCase().endsWith(".CSV"))
			this.showMultipleInputMessageDialog(fName);
		else if (fName.toUpperCase().endsWith(".WBK"))
			this.openFile(fName);
		else
			throw new IllegalArgumentException("Unsupported file format " + fName);
	}

	private void showMultipleInputMessageDialog(String fName) {

		final String absolutePath = fName;
		// JOption Pane code
		final JCheckBox checkBoxForSemiColon = new JCheckBox();
		final JCheckBox checkBoxForComma = new JCheckBox();
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

	private void saveActionPerformed(java.awt.event.ActionEvent evt) {
		JFileChooser chooser = new JFileChooser();
		Component parent = null;
		FileNameExtensionFilter filter = new FileNameExtensionFilter("WBK (*.wbk)",
				"wbk");
		FileNameExtensionFilter filter2 = new FileNameExtensionFilter("CSV (durch Trennzeichen getrennt) (*.csv)",
				"csv");
		FileNameExtensionFilter filter3 = new FileNameExtensionFilter("PDF (*.pdf)", "pdf");
		chooser.setFileFilter(filter);
		chooser.addChoosableFileFilter(filter2);
		chooser.addChoosableFileFilter(filter3);
		int returnVal = chooser.showSaveDialog(parent);

		if (returnVal == JFileChooser.APPROVE_OPTION) {

			try {
				if (chooser.getFileFilter().getDescription().contains("WBK")) {
					saveFile(fileActualName = chooser.getSelectedFile().getAbsolutePath(), true);
				} else if (chooser.getFileFilter().getDescription().contains("CSV")) {
					writeCSV(activeObjectName,
							chooser.getSelectedFile().getAbsolutePath());
				} else if (chooser.getFileFilter().getDescription().contains("PDF")) {
					writePDF(activeObjectName,
							chooser.getSelectedFile().getAbsolutePath());
				}
			} catch (IOException | DocumentException e1) {
				System.out.println("Fehler beim internen Auslesen!");
				e1.printStackTrace();
			}

			//chooser.getSelectedFile().getName();
			//System.out.println("Die Datei wurde unter: " + choosedAbsolutFile + " gespeichert!");
		} else {
			//System.out.println("Das Fenster wurde geschlossen!");
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
						catch(IllegalArgumentException e) {
							e.printStackTrace();
							JOptionPane.showMessageDialog(null, e.getMessage());
						}
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
				
				gui.loadModel();
								
				gui.setSize(new Dimension(1000, 500));
				gui.setLocationRelativeTo(null);

				gui.setVisible(true);
			}
		});
	}

	private boolean isNumber(String str) {
		try {
			Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
	
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
		jTabbedPanel.remove(jTabbedPanel.indexOfTab(worksheetName));
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
		jTabbedPanel.setTitleAt(jTabbedPanel.indexOfTab(worksheetOldName), worksheetNewName);
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
		jTabbedPanel.remove(jTabbedPanel.indexOfTab(diagramName));
	}

	@Override
	public void afterDiagramRenamed(String diagramOldName, String diagramNewName) {
		diagrams.put(diagramNewName, diagrams.get(diagramOldName));
		diagrams.remove(diagramOldName);
		jTabbedPanel.setTitleAt(jTabbedPanel.indexOfTab(diagramOldName), diagramNewName);
		diagrams.get(diagramNewName).Invalidate();
	}

	@Override
	public void afterDiagramChanged(String diagramName) {
		diagrams.get(diagramName).Invalidate();
	}

	private void selectedCellChanged() {
		boxDataCanceled();
	}

	private void tabChanged(ChangeEvent e) throws IllegalArgumentException {
		if (jTabbedPanel.getSelectedIndex() == -1)
			return;
		activeObjectName = jTabbedPanel.getTitleAt(jTabbedPanel.getSelectedIndex());
		isCurrentWorkbook = (Application.getActiveWorkbook().getWorksheets().containsKey(activeObjectName));
		
		if (isCurrentWorkbook) {
			labelBox.setText("Cell value:");
		}
		else {
			labelBox.setText("Diagram area:");
			Diagram d = Application.getActiveWorkbook().getDiagram(activeObjectName);
			String a = null;
			if (d instanceof DiagramBar)
				a = ((DiagramBar)d).getValues();
			else if (d instanceof DiagramLine)
				a = ((DiagramLine)d).getValues();
			else
				throw new IllegalArgumentException("Unknown DiagramClass " + d.getClass().getName());
			
			if (a == null)
				textBox.setText("");
			else
				textBox.setText(a);
		}
		
	}
	
	private void boxDataAccepted() throws IllegalArgumentException {
		try {
			textBox.setText(textBox.getText().trim());
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
					((DiagramBar)d).setValues(textBox.getText());
				else if (d instanceof DiagramLine)
					((DiagramLine)d).setValues(textBox.getText());
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
			String a = null;
			if (d instanceof DiagramBar)
				a = ((DiagramBar)d).getValues();
			else if (d instanceof DiagramLine)
				a = ((DiagramLine)d).getValues();
			else
				throw new IllegalArgumentException("Unknown DiagramClass " + d.getClass().getName());
			
			if (a == null)
				textBox.setText("");
			else
				textBox.setText(a);
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

		jTabbedPanel.removeAll();
		
		for (Worksheet w : Application.getActiveWorkbook().getWorksheets().values())
			loadWorksheet(w);

		for (Diagram d : Application.getActiveWorkbook().getDiagrams().values())
			loadDiagram(d);
		
		jTabbedPanel.setSelectedIndex(0);
		
		//observe Application
		Application.activeWorkbook.addListener(this);

	}
	
	private void loadWorksheet(Worksheet worksheet) {

		if ( jTabbedPanel.indexOfTab(worksheet.getWorksheetName()) != -1 )
			jTabbedPanel.remove(jTabbedPanel.indexOfTab(worksheet.getWorksheetName()));
		
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
		jTabbedPanel.addTab(worksheet.getWorksheetName(), jPane);
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
		if ( jTabbedPanel.indexOfTab(diagram.getName()) != -1 )
			jTabbedPanel.remove(jTabbedPanel.indexOfTab(diagram.getName()));

		JPanel container = new JPanel();
		jTabbedPanel.addTab(diagram.getName(), container);
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
