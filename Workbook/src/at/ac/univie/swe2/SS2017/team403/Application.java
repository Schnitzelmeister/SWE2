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


public class Application implements ActionListener {

	//gui controls
	private JFrame frmClientInterface;
	private JPanel panelMain;

	
	private Workbook activeWorkbook = null;
	private JTable table;
	private JTextField textField;
	
	/**
	 * Open proprietary format File
	 */
	public void OpenFile(String fileName) 
	{
		//activeWorkbook
	}

	/**
	 * Open CSV format File
	 */
	public void OpenCSV(String fileName, String delimiter, String quotation) 
	{
		
	}

	/**
	 * Calculate Data in worksheet
	 */
	public void Calculate() 
	{
		
	}
	
	
	public void actionPerformed(ActionEvent e) {
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
		frmClientInterface.setBounds(100, 100, 613, 520);
		frmClientInterface.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmClientInterface.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.BOTTOM);
		tabbedPane.setBounds(12, 26, 584, 452);
		frmClientInterface.getContentPane().add(tabbedPane);
		
		JPanel panelConnection = new JPanel();
		tabbedPane.addTab("Sheet1", null, panelConnection, null);
		panelConnection.setLayout(null);
		
		table = new JTable();
		table.setBounds(27, 30, 527, 371);
		panelConnection.add(table);
		
		textField = new JTextField();
		textField.setBounds(67, 11, 530, 20);
		frmClientInterface.getContentPane().add(textField);
		textField.setColumns(10);
		
		
	}
}
