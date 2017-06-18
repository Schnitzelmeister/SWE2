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
import at.ac.univie.swe2.SS2017.team403.model.Invoice;

import javax.swing.JRadioButton;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
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
import javax.swing.DefaultComboBoxModel;
import javax.swing.JScrollPane;

public class Application implements CustomerListener {

	private enum productsPlans {
		BasicMonthly, BasicYearly, StandardMonthly, StandardYearly, PremiumMonthly, PremiumYearly
	}

	private JFrame frmFastbillClient;
	private JTextField textFieldCustomerEmail;
	private JTextField textFieldCustomerFirstName;
	private JTextField textFieldCustomerLastName;
	private JTextField textFieldCustomerPhoneNr;
	private JTextField textFieldSubscriptionBillingDate;
	private JList listAllCustomersTab;
	private JComboBox comboBoxCustomerInvoice;
	private JComboBox comboBoxSelectCustomerSubscription;
	BackOfficeSystem sys = BackOfficeSystem.getSystem();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BackOfficeSystem.initialize("config.xml");
					Application window = new Application();
					window.frmFastbillClient.setVisible(true);
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

		sys.addListener(this);
		frmFastbillClient = new JFrame();
		frmFastbillClient.setTitle("FastBill Client\r\n");
		frmFastbillClient.setResizable(false);
		frmFastbillClient.setBounds(100, 100, 745, 473);
		frmFastbillClient.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		frmFastbillClient.setJMenuBar(menuBar);

		JMenu dateiMenu = new JMenu("Datei");
		menuBar.add(dateiMenu);

		JMenuItem mntmSchliessen = new JMenuItem("Schliessen");
		dateiMenu.add(mntmSchliessen);
		frmFastbillClient.getContentPane().setLayout(null);

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 11, 719, 394);
		frmFastbillClient.getContentPane().add(tabbedPane);

		JPanel panel1 = new JPanel();
		tabbedPane.addTab("Kunden", null, panel1, null);
		panel1.setLayout(null);

		JTabbedPane customerFunctionalityPane = new JTabbedPane(JTabbedPane.TOP);
		customerFunctionalityPane.setBounds(10, 11, 694, 344);
		panel1.add(customerFunctionalityPane);

		JPanel createCustomerTab = new JPanel();
		customerFunctionalityPane.addTab("Kunden hinzuf\u00FCgen", null, createCustomerTab, null);
		createCustomerTab.setLayout(null);

		JLabel lblNewLabel = new JLabel("Email:");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(10, 11, 96, 22);
		createCustomerTab.add(lblNewLabel);

		textFieldCustomerEmail = new JTextField();
		textFieldCustomerEmail.setBounds(116, 12, 146, 20);
		createCustomerTab.add(textFieldCustomerEmail);
		textFieldCustomerEmail.setColumns(10);

		textFieldCustomerFirstName = new JTextField();
		textFieldCustomerFirstName.setColumns(10);
		textFieldCustomerFirstName.setBounds(116, 43, 146, 20);
		createCustomerTab.add(textFieldCustomerFirstName);

		JLabel lblVorname = new JLabel("Vorname:");
		lblVorname.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblVorname.setBounds(10, 41, 96, 22);
		createCustomerTab.add(lblVorname);

		JLabel lblNachname = new JLabel("Nachname:");
		lblNachname.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNachname.setBounds(10, 74, 96, 22);
		createCustomerTab.add(lblNachname);

		textFieldCustomerLastName = new JTextField();
		textFieldCustomerLastName.setColumns(10);
		textFieldCustomerLastName.setBounds(116, 76, 146, 20);
		createCustomerTab.add(textFieldCustomerLastName);

		JLabel lblTelefonnummer = new JLabel("Telefonnummer:");
		lblTelefonnummer.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTelefonnummer.setBounds(10, 107, 96, 22);
		createCustomerTab.add(lblTelefonnummer);

		textFieldCustomerPhoneNr = new JTextField();
		textFieldCustomerPhoneNr.setColumns(10);
		textFieldCustomerPhoneNr.setBounds(116, 109, 146, 20);
		createCustomerTab.add(textFieldCustomerPhoneNr);

		JLabel confirmationLabelCreateCustomer = new JLabel("");
		confirmationLabelCreateCustomer.setFont(new Font("Tahoma", Font.PLAIN, 12));
		confirmationLabelCreateCustomer.setBounds(10, 140, 252, 23);
		createCustomerTab.add(confirmationLabelCreateCustomer);

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
				confirmationLabelCreateCustomer.setText("Der Kunde wurde erfolgreich hinzugefügt");
				System.out.println("The customer has been added");
			}
		});
		btnNewButton_1.setBounds(510, 282, 169, 23);
		createCustomerTab.add(btnNewButton_1);

		JPanel panel_3 = new JPanel();
		customerFunctionalityPane.addTab("Rechnungen ausstellen", null, panel_3, null);
		panel_3.setLayout(null);

		JButton btnRechnungenAusstellen = new JButton("Rechnungen ausstellen");
		btnRechnungenAusstellen.setBounds(496, 282, 183, 23);
		panel_3.add(btnRechnungenAusstellen);

		JPanel subscriptionTab = new JPanel();
		customerFunctionalityPane.addTab("Subscription hinzuf\u00FCgen", null, subscriptionTab, null);
		subscriptionTab.setLayout(null);

		JButton btnRechnungAusstellen = new JButton("Subscription ausstellen");
		btnRechnungAusstellen.setBounds(507, 282, 172, 23);
		subscriptionTab.add(btnRechnungAusstellen);

		JLabel lblKundenAuswhlen = new JLabel("Kunden ausw\u00E4hlen:");
		lblKundenAuswhlen.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblKundenAuswhlen.setBounds(10, 11, 123, 22);
		subscriptionTab.add(lblKundenAuswhlen);

		JLabel lblRechnungsdatum = new JLabel("Rechnungsdatum:");
		lblRechnungsdatum.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblRechnungsdatum.setBounds(10, 44, 123, 22);
		subscriptionTab.add(lblRechnungsdatum);

		JLabel lblArtikelnummer = new JLabel("Produkt/Plan");
		lblArtikelnummer.setToolTipText("\r\n");
		lblArtikelnummer.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblArtikelnummer.setBounds(10, 77, 123, 22);
		subscriptionTab.add(lblArtikelnummer);

		textFieldSubscriptionBillingDate = new JTextField();
		textFieldSubscriptionBillingDate.setColumns(10);
		textFieldSubscriptionBillingDate.setBounds(139, 44, 123, 22);
		subscriptionTab.add(textFieldSubscriptionBillingDate);

		comboBoxSelectCustomerSubscription = new JComboBox();
		comboBoxSelectCustomerSubscription.setBounds(139, 13, 316, 20);
		subscriptionTab.add(comboBoxSelectCustomerSubscription);

		JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(productsPlans.values()));
		comboBox_1.setBounds(139, 79, 123, 20);
		subscriptionTab.add(comboBox_1);

		JPanel unpaidInvoicesTab = new JPanel();
		customerFunctionalityPane.addTab("Offene Rechnungen", null, unpaidInvoicesTab, null);
		unpaidInvoicesTab.setLayout(null);

		JLabel lblNewLabel_1 = new JLabel("Kunden aus\u00E4hlen:");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel_1.setBounds(10, 11, 123, 22);
		unpaidInvoicesTab.add(lblNewLabel_1);

		JLabel lblListeOffenerRechnungen = new JLabel("Liste offener Rechnungen:");
		lblListeOffenerRechnungen.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblListeOffenerRechnungen.setBounds(298, 11, 185, 22);
		unpaidInvoicesTab.add(lblListeOffenerRechnungen);

		JLabel infoLabelOpenInvoices = new JLabel("");
		infoLabelOpenInvoices.setFont(new Font("Tahoma", Font.PLAIN, 12));
		infoLabelOpenInvoices.setBounds(298, 291, 381, 14);
		unpaidInvoicesTab.add(infoLabelOpenInvoices);

		comboBoxCustomerInvoice = new JComboBox();
		comboBoxCustomerInvoice.setBounds(10, 39, 278, 20);
		unpaidInvoicesTab.add(comboBoxCustomerInvoice);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(298, 44, 381, 231);
		unpaidInvoicesTab.add(scrollPane);

		JList listForOpenInvoices = new JList();
		scrollPane.setViewportView(listForOpenInvoices);
		comboBoxSelectCustomerSubscription.setModel(new JComboBox(sys.getCustomers()).getModel());

		JButton btnNewButton_2 = new JButton("Best\u00E4tigen");
		btnNewButton_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Customer selectedCustomer = (Customer) comboBoxCustomerInvoice.getModel().getSelectedItem();
				System.out.println("RemoteID des Kunden: " + selectedCustomer.getRemoteId());

				try {
					Invoice[] invoicesArr = sys.getInvoice(selectedCustomer);
					ArrayList invoicesToStringList = new ArrayList<String>();
					for (Invoice i : invoicesArr)
						invoicesToStringList.add("InvoiceId: " + i.getInvoiceId());

					String[] invoicesStringArr = (String[]) invoicesToStringList
							.toArray(new String[invoicesToStringList.size()]);

					listForOpenInvoices.setListData(invoicesStringArr);
				} catch (NullPointerException e) {
					infoLabelOpenInvoices.setText("Es wurden keine Daten gefunden");
				} catch (IllegalArgumentException e) {
					infoLabelOpenInvoices.setText(e.getMessage());
				}
			}
		});
		btnNewButton_2.setBounds(170, 12, 118, 22);
		unpaidInvoicesTab.add(btnNewButton_2);

		listAllCustomersTab = new JList();
		listAllCustomersTab.setListData(sys.getCustomers());
		customerFunctionalityPane.addTab("Alle Kunden", null, listAllCustomersTab, null);

		JPanel productsPane = new JPanel();
		tabbedPane.addTab("Produkte", null, productsPane, null);
		productsPane.setLayout(null);

		JTabbedPane tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane_1.setBounds(10, 11, 694, 344);
		productsPane.add(tabbedPane_1);

		JList list = new JList();
		tabbedPane_1.addTab("Produkte", null, list, null);

		JList list_2 = new JList();
		tabbedPane_1.addTab("Pl\u00E4ne", null, list_2, null);

		comboBoxCustomerInvoice.setModel(new JComboBox(sys.getCustomers()).getModel());

		// ****************************Tab
		// LOGIC********************************************************

		customerFunctionalityPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (customerFunctionalityPane.getSelectedIndex() == 0) {
					confirmationLabelCreateCustomer.setText("");
					textFieldCustomerEmail.setText("");
					textFieldCustomerFirstName.setText("");
					textFieldCustomerLastName.setText("");
					textFieldCustomerPhoneNr.setText("");
				}
			}
		});

		customerFunctionalityPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (customerFunctionalityPane.getSelectedIndex() == 2) {

				}
			}
		});

		customerFunctionalityPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (customerFunctionalityPane.getSelectedIndex() == 3) {
					listForOpenInvoices.removeAll();
					infoLabelOpenInvoices.setText("");

				}
			}
		});

		customerFunctionalityPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (customerFunctionalityPane.getSelectedIndex() == 4) {
					// listAllCustomersTab.setListData(sys.getCustomers());
				}
			}
		});

	}

	@Override
	public void afterCustomerAdded(Customer customer) {
		listAllCustomersTab.setListData(sys.getCustomers());
		comboBoxCustomerInvoice.setModel(new JComboBox(sys.getCustomers()).getModel());
		comboBoxSelectCustomerSubscription.setModel(new JComboBox(sys.getCustomers()).getModel());
	}

	@Override
	public void afterCustomerChanged(Customer customer) {
		listAllCustomersTab.setListData(sys.getCustomers());
		comboBoxCustomerInvoice.setModel(new JComboBox(sys.getCustomers()).getModel());
		comboBoxSelectCustomerSubscription.setModel(new JComboBox(sys.getCustomers()).getModel());
	}
}
