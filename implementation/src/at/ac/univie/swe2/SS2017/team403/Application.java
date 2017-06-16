package at.ac.univie.swe2.SS2017.team403;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JList;
import javax.swing.JTable;
import java.awt.GridLayout;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.Box;
import java.awt.Choice;
import javax.swing.JSplitPane;
import javax.swing.JSpinner;

public class Application {

	private JFrame frame;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Application window = new Application();
					window.frame.setVisible(true);
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
		frame = new JFrame();
		frame.setBounds(100, 100, 696, 476);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu dateiMenu = new JMenu("Datei");
		menuBar.add(dateiMenu);
		
		JMenuItem mntmSchliessen = new JMenuItem("Schliessen");
		dateiMenu.add(mntmSchliessen);
		frame.getContentPane().setLayout(null);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 660, 394);
		frame.getContentPane().add(tabbedPane);
		
		JPanel panel1 = new JPanel();
		tabbedPane.addTab("Kunden", null, panel1, null);
		panel1.setLayout(null);
		
		JTabbedPane tabbedPane_2 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_2.setBounds(10, 11, 635, 344);
		panel1.add(tabbedPane_2);
		
		JPanel panel = new JPanel();
		tabbedPane_2.addTab("Kunden hinzuf\u00FCgen", null, panel, null);
		panel.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Email:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(10, 11, 96, 22);
		panel.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(116, 12, 146, 20);
		panel.add(textField);
		textField.setColumns(10);
		
		textField_1 = new JTextField();
		textField_1.setColumns(10);
		textField_1.setBounds(116, 43, 146, 20);
		panel.add(textField_1);
		
		JLabel lblVorname = new JLabel("Vorname:");
		lblVorname.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblVorname.setBounds(10, 41, 96, 22);
		panel.add(lblVorname);
		
		JLabel lblNachname = new JLabel("Nachname:");
		lblNachname.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNachname.setBounds(10, 74, 96, 22);
		panel.add(lblNachname);
		
		JLabel lblFirma = new JLabel("Firma:");
		lblFirma.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblFirma.setBounds(10, 107, 96, 22);
		panel.add(lblFirma);
		
		textField_2 = new JTextField();
		textField_2.setColumns(10);
		textField_2.setBounds(116, 76, 146, 20);
		panel.add(textField_2);
		
		textField_3 = new JTextField();
		textField_3.setColumns(10);
		textField_3.setBounds(116, 109, 146, 20);
		panel.add(textField_3);
		
		JLabel lblTelefonnummer = new JLabel("Telefonnummer:");
		lblTelefonnummer.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTelefonnummer.setBounds(10, 140, 96, 22);
		panel.add(lblTelefonnummer);
		
		textField_4 = new JTextField();
		textField_4.setColumns(10);
		textField_4.setBounds(116, 140, 146, 20);
		panel.add(textField_4);
		
		JButton btnNewButton_1 = new JButton("Neuen Kunden erstellen");
		btnNewButton_1.setBounds(474, 282, 146, 23);
		panel.add(btnNewButton_1);
		
		JPanel panel_1 = new JPanel();
		tabbedPane_2.addTab("Rechnung ausstellen", null, panel_1, null);
		panel_1.setLayout(null);
		
		JButton btnRechnungAusstellen = new JButton("Rechnung ausstellen");
		btnRechnungAusstellen.setBounds(489, 282, 131, 23);
		panel_1.add(btnRechnungAusstellen);
		
		JLabel label = new JLabel("Kunden aus\u00E4hlen:");
		label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label.setBounds(10, 11, 123, 22);
		panel_1.add(label);
		
		Choice choice_1 = new Choice();
		choice_1.setBounds(139, 13, 123, 22);
		panel_1.add(choice_1);
		
		JLabel lblRechnungsdatum = new JLabel("Rechnungsdatum:");
		lblRechnungsdatum.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblRechnungsdatum.setBounds(10, 44, 123, 22);
		panel_1.add(lblRechnungsdatum);
		
		JSpinner spinner = new JSpinner();
		spinner.setBounds(139, 46, 123, 20);
		panel_1.add(spinner);
		
		JLabel lblMenge = new JLabel("Menge");
		lblMenge.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMenge.setBounds(10, 77, 123, 22);
		panel_1.add(lblMenge);
		
		JSpinner spinner_1 = new JSpinner();
		spinner_1.setBounds(139, 79, 123, 20);
		panel_1.add(spinner_1);
		
		JLabel lblArtikelnummer = new JLabel("Artikelnummer:");
		lblArtikelnummer.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblArtikelnummer.setBounds(10, 110, 123, 22);
		panel_1.add(lblArtikelnummer);
		
		textField_5 = new JTextField();
		textField_5.setBounds(139, 110, 123, 22);
		panel_1.add(textField_5);
		textField_5.setColumns(10);
		
		JLabel lblEinzelpreis = new JLabel("Einzelpreis:");
		lblEinzelpreis.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEinzelpreis.setBounds(10, 143, 123, 22);
		panel_1.add(lblEinzelpreis);
		
		textField_6 = new JTextField();
		textField_6.setColumns(10);
		textField_6.setBounds(139, 143, 123, 22);
		panel_1.add(textField_6);
		
		JPanel panel_2 = new JPanel();
		tabbedPane_2.addTab("Offene Rechnungen", null, panel_2, null);
		panel_2.setLayout(null);
		
		JLabel lblNewLabel_1 = new JLabel("Kunden aus\u00E4hlen:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(10, 11, 123, 22);
		panel_2.add(lblNewLabel_1);
		
		Choice choice = new Choice();
		choice.setBounds(10, 39, 123, 20);
		panel_2.add(choice);
		
		JButton btnNewButton_2 = new JButton("Best\u00E4tigen");
		btnNewButton_2.setBounds(139, 38, 89, 22);
		panel_2.add(btnNewButton_2);
		
		JList list_1 = new JList();
		list_1.setBounds(250, 308, 370, -268);
		panel_2.add(list_1);
		
		JLabel lblListeOffenerRechnungen = new JLabel("Liste offener Rechnungen:");
		lblListeOffenerRechnungen.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblListeOffenerRechnungen.setBounds(250, 11, 185, 22);
		panel_2.add(lblListeOffenerRechnungen);
		
		JPanel productsPanel = new JPanel();
		tabbedPane.addTab("Produkte", null, productsPanel, null);
		productsPanel.setLayout(null);
		
		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.setBounds(10, 11, 635, 344);
		productsPanel.add(tabbedPane_1);
		
		JPanel product1 = new JPanel();
		tabbedPane_1.addTab("Basic", null, product1, null);
		product1.setLayout(null);
		
		JRadioButton rdbtnNewRadioButton = new JRadioButton("Monatsabbonnoment (10 Euro)");
		rdbtnNewRadioButton.setBounds(6, 8, 185, 23);
		product1.add(rdbtnNewRadioButton);
		
		JRadioButton rdbtnPlan = new JRadioButton("Jahresabbonnoment (100 Euro)");
		rdbtnPlan.setBounds(6, 33, 185, 23);
		product1.add(rdbtnPlan);
		
		JButton btnAuswahlBesttigen = new JButton("Auswahl best\u00E4tigen");
		btnAuswahlBesttigen.setBounds(493, 282, 127, 23);
		product1.add(btnAuswahlBesttigen);
		
		JPanel product2 = new JPanel();
		tabbedPane_1.addTab("Standard", null, product2, null);
		product2.setLayout(null);
		
		JRadioButton rdbtnPlan_1 = new JRadioButton("Monatsabbonnoment (10 Euro)");
		rdbtnPlan_1.setBounds(6, 7, 175, 23);
		product2.add(rdbtnPlan_1);
		
		JRadioButton rdbtnPlan_2 = new JRadioButton("Jahresabbonnoment (100 Euro)");
		rdbtnPlan_2.setBounds(6, 33, 188, 23);
		product2.add(rdbtnPlan_2);
		
		JButton btnAuswahlBesttigen_1 = new JButton("Auswahl best\u00E4tigen");
		btnAuswahlBesttigen_1.setBounds(493, 282, 127, 23);
		product2.add(btnAuswahlBesttigen_1);
		
		JPanel product3 = new JPanel();
		tabbedPane_1.addTab("Premium", null, product3, null);
		product3.setLayout(null);
		
		JRadioButton rdbtnPlan_3 = new JRadioButton("Monatsabbonnoment (10 Euro)");
		rdbtnPlan_3.setBounds(6, 7, 175, 23);
		product3.add(rdbtnPlan_3);
		
		JRadioButton rdbtnPlan_4 = new JRadioButton("Jahresabbonnoment (100 Euro)");
		rdbtnPlan_4.setBounds(6, 33, 190, 23);
		product3.add(rdbtnPlan_4);
		
		JButton btnNewButton = new JButton("Auswahl best\u00E4tigen");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewButton.setBounds(493, 282, 127, 23);
		product3.add(btnNewButton);
		
		JPanel lifecycle = new JPanel();
		tabbedPane_1.addTab("Verlauf", null, lifecycle, null);
		lifecycle.setLayout(null);
		
		JList list = new JList();
		list.setBounds(10, 307, 610, -295);
		lifecycle.add(list);
	}
}
