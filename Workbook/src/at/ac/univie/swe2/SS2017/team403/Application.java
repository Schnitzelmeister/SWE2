package at.ac.univie.swe2.SS2017.team403;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.ImageIcon;


public class Application implements ActionListener {

	//gui controls
	private JFrame frmClientInterface;
	private JPanel panelMain;
	
	private Workbook activeWorkbook = null;
	private JTable table_1;
	private JMenuBar menuBar;
	
	
	
	/**
	 * Open proprietary format File
	 */
	public void openFile(String fileName) 
	{
		//activeWorkbook
	}

	/**
	 * Open CSV format File
	 */
	public void openCSV(String fileName, String delimiter, String quotation) 
	{
		
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
		String[] columns = {"ID", "Vorname", "Nachname", "Jahr"};
		
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
		
		menuBar = new JMenuBar();
		frmClientInterface.setJMenuBar(menuBar);
		
		JMenu fileMenu = new JMenu("Datei");
		menuBar.add(fileMenu);
		
		JMenuItem mntmNeu = new JMenuItem("Neu");
		fileMenu.add(mntmNeu);
		
		JMenuItem openFileMenuItem = new JMenuItem("Datei \u00F6ffnen...");
		openFileMenuItem.setSelectedIcon(new ImageIcon(Application.class.getResource("/com/sun/java/swing/plaf/windows/icons/File.gif")));
		fileMenu.add(openFileMenuItem);
		
		JMenuItem saveFileMenutItem = new JMenuItem("Speichern");
		saveFileMenutItem.setSelectedIcon(new ImageIcon(Application.class.getResource("/com/sun/java/swing/plaf/windows/icons/FloppyDrive.gif")));
		fileMenu.add(saveFileMenutItem);
		
		JMenuItem mntmSpeichernUnter = new JMenuItem("Speichern unter...");
		fileMenu.add(mntmSpeichernUnter);
		
		
	}
}
