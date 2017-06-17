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
import javax.swing.event.ChangeEvent;

import javax.swing.event.*;

import at.ac.univie.swe2.SS2017.team403.datagenerator.*;
import at.ac.univie.swe2.SS2017.team403.model.Customer;
import at.ac.univie.swe2.SS2017.team403.model.CustomerStorage;

import javax.swing.JRadioButton;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.Random;
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
import java.awt.Color;
import javax.swing.border.BevelBorder;
import javax.swing.JComboBox;

public class Application {

	private JFrame frame;
	private JTextField textFieldCustomerEmail;
	private JTextField textFieldCustomerFirstName;
	private JTextField textFieldCustomerLastName;
	private JTextField textFieldCustomerPhoneNr;
	private JTextField textField_6;
	private JTextField textFieldSubscriptionBillingDate;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BackOfficeSystem.initialize("config.xml");
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
		BackOfficeSystem sys = BackOfficeSystem.getSystem();
		frame = new JFrame();
		frame.setBounds(100, 100, 755, 473);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);

		JMenu dateiMenu = new JMenu("Datei");
		menuBar.add(dateiMenu);

		JMenuItem mntmSchliessen = new JMenuItem("Schliessen");
		dateiMenu.add(mntmSchliessen);
		frame.getContentPane().setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 719, 394);
		frame.getContentPane().add(tabbedPane);

		JPanel panel1 = new JPanel();
		tabbedPane.addTab("Kunden", null, panel1, null);
		panel1.setLayout(null);

		JTabbedPane tabbedPane_2 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_2.setBounds(10, 11, 694, 344);
		panel1.add(tabbedPane_2);

		JPanel panel = new JPanel();
		tabbedPane_2.addTab("Kunden hinzuf\u00FCgen", null, panel, null);
		panel.setLayout(null);

		JLabel lblNewLabel = new JLabel("Email:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(10, 11, 96, 22);
		panel.add(lblNewLabel);

		textFieldCustomerEmail = new JTextField();
		textFieldCustomerEmail.setBounds(116, 12, 146, 20);
		panel.add(textFieldCustomerEmail);
		textFieldCustomerEmail.setColumns(10);

		textFieldCustomerFirstName = new JTextField();
		textFieldCustomerFirstName.setColumns(10);
		textFieldCustomerFirstName.setBounds(116, 43, 146, 20);
		panel.add(textFieldCustomerFirstName);

		JLabel lblVorname = new JLabel("Vorname:");
		lblVorname.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblVorname.setBounds(10, 41, 96, 22);
		panel.add(lblVorname);

		JLabel lblNachname = new JLabel("Nachname:");
		lblNachname.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNachname.setBounds(10, 74, 96, 22);
		panel.add(lblNachname);

		textFieldCustomerLastName = new JTextField();
		textFieldCustomerLastName.setColumns(10);
		textFieldCustomerLastName.setBounds(116, 76, 146, 20);
		panel.add(textFieldCustomerLastName);

		JLabel lblTelefonnummer = new JLabel("Telefonnummer:");
		lblTelefonnummer.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTelefonnummer.setBounds(10, 107, 96, 22);
		panel.add(lblTelefonnummer);

		textFieldCustomerPhoneNr = new JTextField();
		textFieldCustomerPhoneNr.setColumns(10);
		textFieldCustomerPhoneNr.setBounds(116, 109, 146, 20);
		panel.add(textFieldCustomerPhoneNr);

		JButton btnNewButton_1 = new JButton("Neuen Kunden erstellen");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Random randomGenerator = new Random();
				Integer localId = randomGenerator.nextInt(10000);
				Integer remoteId = randomGenerator.nextInt(10000);
				String phoneNumber = textFieldCustomerPhoneNr.getText();
				String firstName = textFieldCustomerFirstName.getText();
				String lastName = textFieldCustomerLastName.getText();
				String email = textFieldCustomerEmail.getText();

				sys.addCustomer(
						new Customer(localId.toString(), remoteId.toString(), lastName, firstName, email, phoneNumber));
				System.out.println("The customer has been added");
			}
		});
		btnNewButton_1.setBounds(510, 282, 169, 23);
		panel.add(btnNewButton_1);

		JPanel panel_3 = new JPanel();
		tabbedPane_2.addTab("Rechnungen ausstellen", null, panel_3, null);
		panel_3.setLayout(null);

		JButton btnRechnungenAusstellen = new JButton("Rechnungen ausstellen");
		btnRechnungenAusstellen.setBounds(496, 282, 183, 23);
		panel_3.add(btnRechnungenAusstellen);

		JPanel panel_1 = new JPanel();
		tabbedPane_2.addTab("Subscription hinzuf\u00FCgen", null, panel_1, null);
		panel_1.setLayout(null);

		JButton btnRechnungAusstellen = new JButton("Subscription ausstellen");
		btnRechnungAusstellen.setBounds(507, 282, 172, 23);
		panel_1.add(btnRechnungAusstellen);

		JLabel label = new JLabel("Kunden aus\u00E4hlen:");
		label.setFont(new Font("Tahoma", Font.PLAIN, 12));
		label.setBounds(10, 11, 123, 22);
		panel_1.add(label);

		Choice choiceCustomerForSubscription = new Choice();
		choiceCustomerForSubscription.setBounds(139, 13, 123, 22);
		panel_1.add(choiceCustomerForSubscription);

		JLabel lblRechnungsdatum = new JLabel("Rechnungsdatum:");
		lblRechnungsdatum.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblRechnungsdatum.setBounds(10, 44, 123, 22);
		panel_1.add(lblRechnungsdatum);

		JLabel lblArtikelnummer = new JLabel("Produkt/Plan");
		lblArtikelnummer.setToolTipText("\r\n");
		lblArtikelnummer.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblArtikelnummer.setBounds(10, 77, 123, 22);
		panel_1.add(lblArtikelnummer);

		JLabel lblEinzelpreis = new JLabel("Einzelpreis:");
		lblEinzelpreis.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblEinzelpreis.setBounds(10, 110, 123, 22);
		panel_1.add(lblEinzelpreis);

		textField_6 = new JTextField();
		textField_6.setEnabled(false);
		textField_6.setColumns(10);
		textField_6.setBounds(139, 111, 123, 22);
		panel_1.add(textField_6);

		Choice choice_2 = new Choice();
		choice_2.setBounds(139, 79, 123, 20);
		panel_1.add(choice_2);

		textFieldSubscriptionBillingDate = new JTextField();
		textFieldSubscriptionBillingDate.setColumns(10);
		textFieldSubscriptionBillingDate.setBounds(139, 44, 123, 22);
		panel_1.add(textFieldSubscriptionBillingDate);

		JPanel unpaidInvoicesTab = new JPanel();
		tabbedPane_2.addTab("Offene Rechnungen", null, unpaidInvoicesTab, null);
		unpaidInvoicesTab.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("Kunden aus\u00E4hlen:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(10, 11, 123, 22);
		unpaidInvoicesTab.add(lblNewLabel_1);

		JList listForOpenInvoices = new JList();
		listForOpenInvoices.setBackground(Color.WHITE);
		listForOpenInvoices.setBounds(298, 300, 322, -260);
		unpaidInvoicesTab.add(listForOpenInvoices);

		JLabel lblListeOffenerRechnungen = new JLabel("Liste offener Rechnungen:");
		lblListeOffenerRechnungen.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblListeOffenerRechnungen.setBounds(298, 11, 185, 22);
		unpaidInvoicesTab.add(lblListeOffenerRechnungen);

		JComboBox comboBoxCustomerInvoice = new JComboBox();
		comboBoxCustomerInvoice.setBounds(10, 39, 278, 20);
		unpaidInvoicesTab.add(comboBoxCustomerInvoice);

		tabbedPane_2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (tabbedPane_2.getSelectedIndex() == 3) {
					comboBoxCustomerInvoice.setModel(new JComboBox(sys.getCustomers()).getModel());
				}
			}
		});

		JButton btnNewButton_2 = new JButton("Best\u00E4tigen");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Customer selectedCustomer = (Customer) comboBoxCustomerInvoice.getModel().getSelectedItem();
				System.out.println("RemoteID des Kunden: " + selectedCustomer.getRemoteId());
				// TODO Wie koennen wir Invoices zu einem Kunden zuordnen?
				// TODO Informationen in Liste übernehmen
			}
		});
		btnNewButton_2.setBounds(170, 12, 118, 22);
		unpaidInvoicesTab.add(btnNewButton_2);

		JList listAllCustomersTab = new JList();
		tabbedPane_2.addTab("Alle Kunden", null, listAllCustomersTab, null);

		tabbedPane_2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (tabbedPane_2.getSelectedIndex() == 4) {
					listAllCustomersTab.setListData(sys.getCustomers());
				}
			}
		});

		JPanel productsPanel = new JPanel();
		tabbedPane.addTab("Produkte", null, productsPanel, null);
		productsPanel.setLayout(null);

		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.setBounds(10, 11, 694, 344);
		productsPanel.add(tabbedPane_1);

		JList list = new JList();
		tabbedPane_1.addTab("Produkte", null, list, null);

		JList list_2 = new JList();
		tabbedPane_1.addTab("Pl\u00E4ne", null, list_2, null);
	}
}
