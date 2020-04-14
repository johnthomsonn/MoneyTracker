package moneytracker.ui;

/*
 * This is B interfaces
 *
 * I have used Object as the parameter to category dropdowns to get rid of the generic warnings
 * ie private JComboBox<Object> addPanelCategoryDropdown;
 * instead of private JComboBox addPanelCategoryDropdown;
 *
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import moneytracker.core.MoneyTrackerCore;
import moneytracker.core.internal.ExpenditureTransaction;
import moneytracker.core.internal.ExpenditureTransaction.DESTINATION;
import moneytracker.core.internal.IncomeTransaction;
import moneytracker.core.internal.IncomeTransaction.SOURCE;
import moneytracker.core.internal.Month;
import moneytracker.core.internal.Transaction;
import moneytracker.core.internal.User;

public class UserInterface
{
	
	private JFrame frame;
	private MoneyTrackerCore core;
	
	
	// frame stuff
	private final int WIDTH = 656;
	private final int HEIGHT = 462;
	
	// menu bar
	JMenuItem menuFileExit;
	private JTextField balanceOutput;
	
	// all panels
	JTabbedPane tabbedPane;
	JTextField output;
	JPanel outputPanel;
	JPanel AddPanel;
	
	// add panel stuff
	JComboBox<Integer> addPanelDayDropdown;
	private JLabel addIncomeAmountlbl;
	private JTextField addPanelAmount;
	private JLabel addIncomeWhereLbl;
	private JTextField addPanelWhere;
	private JLabel addPanelDayLbl;
	private JLabel addIncomeMonthLbl;
	private JLabel addPanelCategoryLbl;
	private JComboBox<Object> addPanelCategoryDropdown;
	JButton addTransactionButton;
	private JPanel removePanel;
	private JLabel removePanelTypeLbl;
	JComboBox<Transaction.TYPE> AddPanelTypeDropwdown;
	JComboBox<Month> addPanelMonthDropwdown;
	private JLabel removePanelCategoryLbl;
	JComboBox<Object> removePanelCategoryDropdwon;
	private JLabel removeIncomeTransactionsLbl;
	JButton removePanelTransactionbutton;
	private JList<Transaction> removePanelTransactionsList;
	private JScrollPane removePanelTransactionScrollPane;
	
	// view Panel stuff
	JPanel viewPanel;
	JComboBox<Object> viewPanelCategoryDropdown;
	private JTextField viewPanelIncomeOutput;
	private JTextField viewPanelExpendOutput;
	private JTextField viewPanelNetOutput;
	JList<Transaction> viewPanelTransactionsList;
	JComboBox<Transaction.TYPE> viewPanelTypeDropdown;
	JComboBox<Month> viewPanelMonthDropdown;
	String[] tableOverviewHeaders = new String[] {"Category", "Count", "£"};
	DefaultTableModel tableModel;
	
	// users panel
	JButton usersPanelLoginUserButton;
	private JLabel usersPanelCreatenameLbl;
	private JTextField usersPanelCreateName;
	private JLabel usersPanelCreateBalanceLbl;
	private JTextField usersPanelCreateBalance;
	private JLabel usersPanelCreatepasswordLbl;
	private JPasswordField usersPanelCreatepassword;
	JButton usersPanelCreateUserBtn;
	JPanel userPanel;
	private JMenuItem menuItemLogout;
	private JPasswordField usersPanelPasswordLogin;
	private JComboBox<User> usersAllUsersDropdownMenu;
	private JComboBox<Transaction.TYPE> removePanelTypeTransaction;
	private JLabel removePanelMonthDropdwonLbl;
	private JComboBox<Month> removePanelMonthDropdwon;
	private JLabel viewPanelOverviewLbl;
	private JTable viewPanelOverviewTable;
	
	/**
	 * Launch the application.
	 *
	 * @param args
	 *            arguments
	 */
	public static void main(String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				try
				{
					UserInterface window = new UserInterface();
					window.frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
	
	/**
	 * Create the application.
	 */
	public UserInterface()
	{
		this.initialize();
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		this.frame = new JFrame("Money Tracker - Final Protoype");
		this.frame.setResizable(false);
		this.frame.setBounds(screen.width / 2 - this.WIDTH / 2, screen.height / 2 - this.HEIGHT / 2, this.WIDTH, this.HEIGHT);
		this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.frame.getContentPane().setLayout(null);
		
		this.tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		this.tabbedPane.setBounds(0, 0, 650, 353);
		this.frame.getContentPane().add(this.tabbedPane);
		
		this.outputPanel = new JPanel();
		this.outputPanel.setBounds(0, 354, 650, 47);
		this.outputPanel.setBackground(Color.LIGHT_GRAY);
		this.frame.getContentPane().add(this.outputPanel);
		this.outputPanel.setLayout(null);
		
		this.output = new JTextField();
		this.output.setEditable(false);
		this.output.setBounds(10, 11, 630, 25);
		this.outputPanel.add(this.output);
		this.output.setColumns(10);
		
		this.balanceOutput = new JTextField();
		this.balanceOutput.setBounds(559, 0, 81, 20);
		this.balanceOutput.setEditable(false);
		this.frame.getContentPane().add(this.balanceOutput);
		this.balanceOutput.setColumns(10);
		
		this.core = MoneyTrackerCore.getCore();
		this.updateBalanceOutput();
		
		// initalise menu bar
		this.initialiseMenuBar();
		
		// checks if new month and if so, adds to user months CALL ONLOAD AFTER
		// SELECTING USER
		this.onLoad();
		
		
		// initailise all tabbed screens
		this.initialiseUserScreen();
		this.initialiseTabbedScreens();
		
		// show users screen, hide others
		this.showUserScreenOnly();
		
	}
	
	/**
	 * Initialises menu bar
	 */
	public void initialiseMenuBar()
	{
		JMenuBar menuBar = new JMenuBar();
		this.frame.setJMenuBar(menuBar);
		
		JMenu menuFile = new JMenu("File");
		menuBar.add(menuFile);
		
		this.menuItemLogout = new JMenuItem("Logout");
		menuFile.add(this.menuItemLogout);
		
		this.menuFileExit = new JMenuItem("Exit");
		
		menuFile.add(this.menuFileExit);
		
		this.menuBarHandlers();
	}
	
	/**
	 * Includes handlers used in menu bar
	 */
	public void menuBarHandlers()
	{
		this.menuFileExit.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				UserInterface.this.exit();
			}
		});
		
		
		this.menuItemLogout.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				UserInterface.this.logoutUser();
			}
		});
	}
	
	/**
	 * initialises all tabbed screens
	 */
	public void initialiseTabbedScreens()
	{
		
		this.addPanelInitialisation();
		this.removePanelInitialisation();
		this.viewPanelInitialisation();
	}
	
	public void initialiseUserScreen()
	{
		this.usersPanelInitialisation();
	}
	
	public void showUserScreenOnly()
	{
		this.tabbedPane.setEnabledAt(0, true);
		this.tabbedPane.setSelectedIndex(0);
		for (int i = 1; i < this.tabbedPane.getTabCount(); i++)
		{
			this.tabbedPane.setEnabledAt(i, false);
		}
	}
	
	public void hideUserScreen()
	{
		this.tabbedPane.setEnabledAt(0, false);
		for (int i = 1; i < this.tabbedPane.getTabCount(); i++)
		{
			this.tabbedPane.setEnabledAt(i, true);
		}
		this.tabbedPane.setSelectedIndex(1);
	}
	
	public void updateTitleBar(String msg)
	{
		this.frame.setTitle(msg);
	}
	
	public void loginUser(User user, char[] password)
	{
		
		if (user == null)
		{
			this.outputError("Please first select a User to login");
		}
		else
		{
			char[] storedPass = user.getpassword();
			if (!Arrays.equals(password, storedPass))
			{
				this.outputError("Sorry, the passwords do not match");
			}
			else
			{
				this.core.loginUser(user);
				this.hideUserScreen();
				this.updateBalanceOutput();
				this.updateTitleBar("Money Tracker - " + user.getName() + "'s account");
			}
		}
		this.resetUserFields();
	}
	
	public void logoutUser()
	{
		this.core.logoutUser();
		this.showUserScreenOnly();
		this.updateTitleBar("Money Tracker - Select a user");
	}
	
	// after create user if hasInit = false then inittabeedScreens
	public void createUser(String name, String balance, char[] password)
	{
		if (this.isValidInputData(balance, name))
		{
			float fBalance = Float.parseFloat(balance);
			this.core.createNewUser(name, fBalance, password);
			this.hideUserScreen();
			this.updateTitleBar("Money Tracker - " + this.core.getUser() + "'s account");
			this.updateBalanceOutput();
			this.tabbedPane.setSelectedIndex(1);
			this.resetUserFields();
		}
		else
		{
			this.outputError("Please make sure the name is not empty andit is a valad balance");
		}
	}
	
	private void resetUserFields()
	{
		this.usersPanelCreateName.setText("");
		this.usersPanelCreatepassword.setText("");
		this.usersPanelCreateBalance.setText("");
		this.usersPanelPasswordLogin.setText("");
	}
	
	// ===================================================================================================================================================
	// Users panel stuff
	// ===================================================================================================================================================
	
	public void usersPanelInitialisation()
	{
		
		this.userPanel = new JPanel();
		this.tabbedPane.addTab("Users", (Icon) null, this.userPanel, "All users in system or create a new one");
		this.userPanel.setLayout(null);
		
		JLabel UserPaneltitle = new JLabel("Login");
		UserPaneltitle.setFont(new Font("Tahoma", Font.PLAIN, 14));
		UserPaneltitle.setBounds(106, 11, 101, 31);
		this.userPanel.add(UserPaneltitle);
		
		JLabel Users = new JLabel("Users");
		Users.setFont(new Font("Tahoma", Font.PLAIN, 12));
		Users.setBounds(10, 63, 56, 14);
		this.userPanel.add(Users);
		
		JLabel lblCreateNewUser = new JLabel("Create new user");
		lblCreateNewUser.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblCreateNewUser.setBounds(408, 11, 101, 31);
		this.userPanel.add(lblCreateNewUser);
		
		JLabel usersPasswordLabel = new JLabel("Password");
		usersPasswordLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		usersPasswordLabel.setBounds(10, 111, 66, 14);
		this.userPanel.add(usersPasswordLabel);
		
		JSeparator userPanelSep = new JSeparator();
		userPanelSep.setOrientation(SwingConstants.VERTICAL);
		userPanelSep.setBounds(300, 11, 11, 314);
		this.userPanel.add(userPanelSep);
		
		this.usersPanelLoginUserButton = new JButton("login");
		this.usersPanelLoginUserButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.usersPanelLoginUserButton.setText("Login");
		this.usersPanelLoginUserButton.setBounds(87, 162, 120, 23);
		this.userPanel.add(this.usersPanelLoginUserButton);
		
		this.usersPanelCreatenameLbl = new JLabel("Name");
		this.usersPanelCreatenameLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.usersPanelCreatenameLbl.setBounds(316, 64, 46, 14);
		this.userPanel.add(this.usersPanelCreatenameLbl);
		
		this.usersPanelCreateName = new JTextField();
		this.usersPanelCreateName.setBounds(408, 61, 120, 20);
		this.userPanel.add(this.usersPanelCreateName);
		this.usersPanelCreateName.setColumns(10);
		
		this.usersPanelCreateBalanceLbl = new JLabel("Balance");
		this.usersPanelCreateBalanceLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.usersPanelCreateBalanceLbl.setBounds(316, 109, 46, 14);
		this.userPanel.add(this.usersPanelCreateBalanceLbl);
		
		this.usersPanelCreateBalance = new JTextField();
		this.usersPanelCreateBalance.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.usersPanelCreateBalance.setBounds(408, 107, 120, 20);
		this.userPanel.add(this.usersPanelCreateBalance);
		this.usersPanelCreateBalance.setColumns(10);
		
		this.usersPanelCreatepasswordLbl = new JLabel("Password");
		this.usersPanelCreatepasswordLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.usersPanelCreatepasswordLbl.setBounds(316, 156, 66, 14);
		this.userPanel.add(this.usersPanelCreatepasswordLbl);
		
		this.usersPanelCreatepassword = new JPasswordField();
		this.usersPanelCreatepassword.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.usersPanelCreatepassword.setBounds(408, 154, 120, 20);
		this.userPanel.add(this.usersPanelCreatepassword);
		
		this.usersPanelCreateUserBtn = new JButton("Create User");
		this.usersPanelCreateUserBtn.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.usersPanelCreateUserBtn.setBounds(408, 196, 120, 23);
		this.userPanel.add(this.usersPanelCreateUserBtn);
		
		this.usersPanelPasswordLogin = new JPasswordField();
		this.usersPanelPasswordLogin.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.usersPanelPasswordLogin.setBounds(87, 109, 120, 20);
		this.userPanel.add(this.usersPanelPasswordLogin);
		
		this.usersAllUsersDropdownMenu = new JComboBox<User>();
		this.usersAllUsersDropdownMenu.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.usersAllUsersDropdownMenu.setBounds(87, 61, 120, 20);
		this.userPanel.add(this.usersAllUsersDropdownMenu);
		
		this.usersPanelhandlers();
		this.usersPanelonClick();
	}
	
	public void usersPanelhandlers()
	{
		
		this.userPanel.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentShown(ComponentEvent e)
			{
				UserInterface.this.usersPanelonClick();
			}
		});
		
		this.usersPanelCreateUserBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				String name = UserInterface.this.usersPanelCreateName.getText().trim();
				String balance = UserInterface.this.usersPanelCreateBalance.getText().trim();
				char[] password = UserInterface.this.usersPanelCreatepassword.getPassword();
				
				UserInterface.this.createUser(name, balance, password);
			}
		});
		
		this.usersPanelLoginUserButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				User u = (User) UserInterface.this.usersAllUsersDropdownMenu.getSelectedItem();
				char[] password = UserInterface.this.usersPanelPasswordLogin.getPassword();
				UserInterface.this.loginUser(u, password);
			}
		});
		
	}
	
	public void usersPanelonClick()
	{
		this.resetOutputField();
		this.populateUsersDropdown();
	}
	
	public void populateUsersDropdown()
	{
		Set<User> systemUsersCollection = this.core.getAllUsers();
		if (systemUsersCollection.isEmpty())
		{
			this.outputError("No Users in System yet");
		}
		else
		{
			boolean found = false;
			for(User u: systemUsersCollection)
			{
				for(int i = 0; i < this.usersAllUsersDropdownMenu.getModel().getSize(); i++)
				{
					if(this.usersAllUsersDropdownMenu.getModel().getElementAt(i).equals(u))
						found = true;
				}
				if(!found)
				{
					this.usersAllUsersDropdownMenu.addItem(u);
					found = false;
				}
			}
		}
	}
	
	// ===================================================================================================================================================
	// Add Panel stuff Add Panel
	// ===================================================================================================================================================
	
	/**
	 * Initialises all components in the Add Panel
	 */
	public void addPanelInitialisation()
	{
		this.AddPanel = new JPanel();
		this.tabbedPane.addTab("Add Transaction", null, this.AddPanel, null);
		this.AddPanel.setLayout(null);
		JLabel addPanelMainLbl = new JLabel("Add Transactions");
		addPanelMainLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		addPanelMainLbl.setBounds(197, 11, 157, 17);
		this.AddPanel.add(addPanelMainLbl);
		
		this.addIncomeAmountlbl = new JLabel("Amount $");
		this.addIncomeAmountlbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.addIncomeAmountlbl.setBounds(122, 79, 70, 14);
		this.AddPanel.add(this.addIncomeAmountlbl);
		
		this.addPanelAmount = new JTextField();
		this.addPanelAmount.setBounds(210, 79, 127, 20);
		this.AddPanel.add(this.addPanelAmount);
		this.addPanelAmount.setColumns(10);
		
		this.addIncomeWhereLbl = new JLabel("Description");
		this.addIncomeWhereLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.addIncomeWhereLbl.setBounds(122, 119, 70, 14);
		this.AddPanel.add(this.addIncomeWhereLbl);
		
		this.addPanelWhere = new JTextField();
		this.addPanelWhere.setColumns(10);
		this.addPanelWhere.setBounds(210, 119, 127, 20);
		this.AddPanel.add(this.addPanelWhere);
		
		this.addPanelDayLbl = new JLabel("Day");
		this.addPanelDayLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.addPanelDayLbl.setBounds(122, 239, 57, 14);
		this.AddPanel.add(this.addPanelDayLbl);
		
		this.addIncomeMonthLbl = new JLabel("Month");
		this.addIncomeMonthLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.addIncomeMonthLbl.setBounds(122, 199, 57, 14);
		this.AddPanel.add(this.addIncomeMonthLbl);
		
		this.addPanelDayDropdown = new JComboBox<Integer>();
		this.addPanelDayDropdown.setBounds(210, 239, 127, 18);
		this.AddPanel.add(this.addPanelDayDropdown);
		
		this.addPanelCategoryLbl = new JLabel("Category");
		this.addPanelCategoryLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.addPanelCategoryLbl.setBounds(122, 159, 57, 14);
		this.AddPanel.add(this.addPanelCategoryLbl);
		
		this.addPanelCategoryDropdown = new JComboBox<Object>();
		this.addPanelCategoryDropdown.setBounds(210, 159, 127, 18);
		this.AddPanel.add(this.addPanelCategoryDropdown);
		
		this.addTransactionButton = new JButton("Add Transaction");
		this.addTransactionButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.addTransactionButton.setBounds(210, 279, 130, 23);
		this.AddPanel.add(this.addTransactionButton);
		
		JLabel addPanelTType = new JLabel("Type");
		addPanelTType.setFont(new Font("Tahoma", Font.PLAIN, 12));
		addPanelTType.setBounds(129, 39, 70, 20);
		this.AddPanel.add(addPanelTType);
		
		this.AddPanelTypeDropwdown = new JComboBox<Transaction.TYPE>();
		this.AddPanelTypeDropwdown.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.AddPanelTypeDropwdown.setBounds(210, 39, 127, 20);
		this.AddPanel.add(this.AddPanelTypeDropwdown);
		
		this.addPanelMonthDropwdown = new JComboBox<Month>();
		this.addPanelMonthDropwdown.setBounds(210, 199, 127, 20);
		this.AddPanel.add(this.addPanelMonthDropwdown);
		
		//popualte transaction type dropdown with Income/Expenditure
		this.populateTransactionMainTypeDrowpdown(this.AddPanelTypeDropwdown);
		
		this.addPanelOnClick();
		this.addPanelEventHandlers();
		
	}
	
	/**
	 * Includes all event handlers that are used on the Add Panel
	 */
	public void addPanelEventHandlers()
	{
		
		this.AddPanel.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentShown(ComponentEvent arg0)
			{
				UserInterface.this.addPanelOnClick();
				UserInterface.this.resetOutputField();
			}
		});
		
		this.addTransactionButton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				String amountInput = UserInterface.this.addPanelAmount.getText().trim();
				String whereInput = UserInterface.this.addPanelWhere.getText().trim();
				
				UserInterface.this.addTransaction(amountInput, whereInput);
				
			}
		});

		this.AddPanelTypeDropwdown.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				Transaction.TYPE type = (Transaction.TYPE)UserInterface.this.AddPanelTypeDropwdown.getSelectedItem();
				if(type != null)
				{
					UserInterface.this.addPanelCategoryDropdown.removeAllItems();
					UserInterface.this.populateCategoryDropdownMenu(UserInterface.this.addPanelCategoryDropdown, type);
				}
			}
		});
	}
	
	/**
	 * Handles any updating/refreshing when the Add Panel is clicked/shown
	 */
	public void addPanelOnClick()
	{
		/// add Income/expenditure to type drowpdown (Done above, in addPanelInit())
		/// populate category dropdwon depending on type
		// populate month dropdowns
		// set dropdown day to today
		
		if(this.core.getUser() != null)
		{
			//default to initializing with income types
			this.populateCategoryDropdownMenu(this.addPanelCategoryDropdown, Transaction.TYPE.INCOME);
			this.populateMonthDropdown(this.addPanelMonthDropwdown);
			this.populateDayDropdownMenu(this.addPanelDayDropdown, this.core.getCurrentMonth());
			this.setDropdownDateToToday(this.addPanelDayDropdown);
		}
		
		/*
		if (this.core.getUser() != null)
		{
			this.populateMonthJList(this.addIncomeMonthList);
			this.populateDayDropdownMenu(this.addPanelDayDropdown, this.core.getCurrentMonth());
			this.populateIncomeTypeMenu(this.addPanelCategoryDropdown);


			this.setDropdownDateToToday(this.addPanelDayDropdown);
			this.setDropdownDateToToday(this.addExpendDay);
		}
		 */
		
		
	}
	
	//populates Transaction type dropdown menu with income and expenditure
	private void populateTransactionMainTypeDrowpdown(JComboBox<Transaction.TYPE> dropdownMenu)
	{
		dropdownMenu.addItem(Transaction.TYPE.INCOME);
		dropdownMenu.addItem(Transaction.TYPE.EXPENDITURE);
		dropdownMenu.setSelectedIndex(0);
	}
	
	//populates category type dropdown with income source types
	private void populateCategoryDropdownMenu(JComboBox<Object> dropdown, Transaction.TYPE type)
	{
		
		if(type == Transaction.TYPE.INCOME)
			this.populateIncomeTypeMenu(dropdown);
		else
			this.populateExpenditureTypeMenu(dropdown);
		
	}
	
	// TODO take the output messages and update balance from the addIncome/addExpenditure methods and put them here
	//adds a transaction depending on given data from user
	public boolean addTransaction(String amount, String where)
	{
		boolean isOk = false;
		Transaction.TYPE transactionType = (Transaction.TYPE)this.AddPanelTypeDropwdown.getSelectedItem();
		Month month = (Month)this.addPanelMonthDropwdown.getSelectedItem();
		int day = this.addPanelDayDropdown.getSelectedIndex() + 1;
		
		//if no null values then proceed
		if(transactionType != null && month != null)
		{
			if(transactionType == Transaction.TYPE.INCOME)
			{
				SOURCE source = null;
				try {
					source = (SOURCE)this.addPanelCategoryDropdown.getSelectedItem();

					if(this.addIncome(amount, where, source, month, day))
						isOk = true;
				}
				catch(Exception e)
				{
					this.outputError("The category does not match the type.");
				}
			}
			else
			{
				DESTINATION dest = null;
				try {
					dest = (DESTINATION)this.addPanelCategoryDropdown.getSelectedItem();

					if(this.addExpenditure(amount, where, dest, month, day))
						isOk = true;
				}
				catch(Exception e){
					
					this.outputError("The category does not match the type.");
				}
			}
		}
		else
		{
			this.outputError("Please fill in all fields and options.");
		}
		return isOk;
	}
	
	
	public boolean addIncome(String amount, String where, SOURCE source, Month month, int day)
	{
		boolean isOk = false;
		
		
		if (this.isValidInputData(amount, where))
		{
			float floatAmount = Float.parseFloat(amount);
			this.resetInputFields();
			this.resetOutputField();
			if (this.core.addIncome(floatAmount, where, day, month, source))
			{
				this.outputSuccess("Income Transaction of $" + floatAmount + " from " + where + " on " + day + " " + month + " has been recorded. Your balance is now $" + this.core.getUser().getBalance());
				this.updateBalanceOutput();
			}
			else
			{
				this.outputError("Something went wrong in the core system.");
			}
		}
		
		return isOk;
	}
	
	public boolean addExpenditure(String amount, String where, DESTINATION dest, Month month, int day)
	{
		boolean isOk = false;
		
		if (this.isValidInputData(amount, where))
		{
			float floatAmount = Float.parseFloat(amount);
			this.resetInputFields();
			this.resetOutputField();
			if (this.core.addExpenditure(floatAmount, where, day, month, dest))
			{
				this.outputSuccess("Expenditure Transaction of $" + floatAmount + " for " + where + " on " + day + " " + month + " has been recorded. Your balance is now $" + this.core.getUser().getBalance());
				this.updateBalanceOutput();
			}
			else
			{
				this.outputError("Something went wrong in the core system.");
			}
		}
		
		return isOk;
	}
	
	// tmp helper functions for addIncome
	public boolean isValidInputData(String amount, String where)
	{
		boolean isOk = false;
		if (this.isValidAmount(amount))
		{
			if (this.isValidWhere(where))
			{
				isOk = true;
			}
			else
			{
				this.outputError("Please make sure the Where field is not empty");
			}
		}
		else
		{
			this.outputError("Please make sure the amount is not empty and numerical.");
		}
		
		return isOk;
	}
	
	public boolean isValidAmount(String amount)
	{
		boolean isOk = true;
		
		if (!amount.isEmpty())
		{
			if (!this.checkAmountPartsForCorrectDecimal(amount))
			{
				isOk = false;
			}
			else
			{
				for (int i = 0; i < amount.length(); i++)
				{
					Character x = amount.charAt(i);
					if (Character.isWhitespace(x) || Character.isLetter(x))
					{
						isOk = false;
					}
				}
			}
		}
		else
		{
			isOk = false;
		}
		
		return isOk;
	}
	
	private boolean checkAmountPartsForCorrectDecimal(String amount)
	{
		boolean isOK = true;
		String[] parts = amount.split("\\.");
		if (parts.length > 2)
		{
			isOK = false;
		}
		if (parts.length == 2)
		{
			if (parts[1].length() > 2)
			{
				isOK = false;
			}
		}
		return isOK;
	}
	
	public boolean isValidWhere(String where)
	{
		boolean isOk = false;
		if (!where.isEmpty())
		{
			isOk = true;
		}
		return isOk;
	}
	
	// ===========================================================================
	// Helper functions for Add Panel On Click
	// ===========================================================================
	
	
	private void populateMonthDropdown(JComboBox<Month> dropdown)
	{
		List<Month> months = this.core.getMonthsForList();
		Collections.sort(months);
		boolean found = false;
		for(Month m : months)
		{
			for(int i=0; i < dropdown.getModel().getSize(); i++)
			{
				if(dropdown.getModel().getElementAt(i).equals(m))
				{
					found = true;
				}
			}
			if(!found)
			{
				dropdown.addItem(m);
				found = false;
			}
		}
	}
	
	private void populateDayDropdownMenu(JComboBox<Integer> dropdownMenu, Month aMonth)
	{
		int numDays = this.core.getDaysInMonth(aMonth);
		
		int size = dropdownMenu.getModel().getSize();
		
		for (int i = 0; i < size; i++)
		{
			dropdownMenu.removeItem(i + 1);
		}
		
		for (int i = 0; i < numDays; i++)
		{
			dropdownMenu.addItem(i + 1);
		}
		dropdownMenu.setSelectedIndex(0);
	}
	
	private void populateIncomeTypeMenu(JComboBox<Object> dropdown)
	{
		int length = this.core.getIncomeTransactionTypes().length;
		IncomeTransaction.SOURCE[] sources = this.core.getIncomeTransactionTypes();
		for (int i = 0; i < length; i++)
		{
			dropdown.addItem(sources[i]);
		}
		dropdown.setSelectedIndex(0);
	}
	
	private void populateExpenditureTypeMenu(JComboBox<Object> dropdown)
	{
		int length = this.core.getExpenditureTransactionTypes().length;
		ExpenditureTransaction.DESTINATION[] dests = this.core.getExpenditureTransactionTypes();
		for (int i = 0; i < length; i++)
		{
			dropdown.addItem(dests[i]);
		}
		dropdown.setSelectedIndex(0);
	}
	
	private void setDropdownDateToToday(JComboBox<Integer> dayDropdown)
	{
		int day = LocalDate.now().getDayOfMonth();
		dayDropdown.setSelectedIndex(day - 1);
	}
	
	// ===================================================================================================================================================
	// Remove Panel stuff Remove Panel
	// ===================================================================================================================================================
	
	public void removePanelInitialisation()
	{
		this.removePanel = new JPanel();
		this.tabbedPane.addTab("Remove Transaction", null, this.removePanel, null);
		this.removePanel.setLayout(null);
		
		JLabel removeTransactionLbl = new JLabel("Remove Transactions");
		removeTransactionLbl.setFont(new Font("Tahoma", Font.PLAIN, 14));
		removeTransactionLbl.setBounds(218, 11, 157, 17);
		this.removePanel.add(removeTransactionLbl);
		
		this.removePanelTypeLbl = new JLabel("Type");
		this.removePanelTypeLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.removePanelTypeLbl.setBounds(140, 40, 68, 14);
		this.removePanel.add(this.removePanelTypeLbl);
		
		this.removePanelCategoryLbl = new JLabel("Category");
		this.removePanelCategoryLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.removePanelCategoryLbl.setBounds(140, 120, 68, 14);
		this.removePanel.add(this.removePanelCategoryLbl);
		
		this.removePanelCategoryDropdwon = new JComboBox<Object>();
		this.removePanelCategoryDropdwon.setBounds(240, 120, 127, 20);
		this.removePanel.add(this.removePanelCategoryDropdwon);
		
		this.removeIncomeTransactionsLbl = new JLabel("Transactions");
		this.removeIncomeTransactionsLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.removeIncomeTransactionsLbl.setBounds(140, 202, 80, 14);
		this.removePanel.add(this.removeIncomeTransactionsLbl);
		
		this.removePanelTransactionScrollPane = new JScrollPane();
		this.removePanelTransactionScrollPane.setBounds(240, 160, 200, 113);
		this.removePanel.add(this.removePanelTransactionScrollPane);
		
		this.removePanelTransactionsList = new JList<Transaction>();
		this.removePanelTransactionScrollPane.setColumnHeaderView(this.removePanelTransactionsList);
		
		this.removePanelTransactionbutton = new JButton("RemoveTransaciton");
		this.removePanelTransactionbutton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.removePanelTransactionbutton.setBounds(240, 291, 144, 23);
		this.removePanel.add(this.removePanelTransactionbutton);
		
		this.removePanelTypeTransaction = new JComboBox<Transaction.TYPE>();
		this.removePanelTypeTransaction.setBounds(240, 39, 127, 20);
		this.removePanel.add(this.removePanelTypeTransaction);
		
		// populate transaction type with income/expenditure as this will not change
		this.populateTransactionMainTypeDrowpdown(this.removePanelTypeTransaction);
		
		this.removePanelMonthDropdwonLbl = new JLabel("Month");
		this.removePanelMonthDropdwonLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.removePanelMonthDropdwonLbl.setBounds(140, 80, 46, 14);
		this.removePanel.add(this.removePanelMonthDropdwonLbl);
		
		this.removePanelMonthDropdwon = new JComboBox<Month>();
		this.removePanelMonthDropdwon.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.removePanelMonthDropdwon.setBounds(240, 80, 127, 20);
		this.removePanel.add(this.removePanelMonthDropdwon);
		
		this.removePanelHandlers();
		this.removePanelOnClick();
	}
	
	public void removePanelHandlers()
	{
		
		this.removePanel.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentShown(ComponentEvent arg0)
			{
				UserInterface.this.removePanelOnClick();
				UserInterface.this.resetOutputField();
			}
		});
		
		/*
		 * Main transaction type dropdown menu
		 */
		this.removePanelTypeTransaction.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				Transaction.TYPE type = (Transaction.TYPE)UserInterface.this.removePanelTypeTransaction.getSelectedItem();
				if(type != null)
				{
					UserInterface.this.removePanelCategoryDropdwon.removeAllItems();
					UserInterface.this.removePanelCategoryDropdwon.addItem("All");
					UserInterface.this.populateCategoryDropdownMenu(UserInterface.this.removePanelCategoryDropdwon, type);
					UserInterface.this.populateTransactionList(UserInterface.this.removePanelTransactionsList, UserInterface.this.removePanelMonthDropdwon, UserInterface.this.removePanelCategoryDropdwon, type);
				}
			}
		});
		
		/*
		 * Category type dropdown menu
		 */
		this.removePanelCategoryDropdwon.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				UserInterface.this.doDropdownMenuOnChange();
			}
		});
		
		/*
		 * Remove Transaction button
		 */
		this.removePanelTransactionbutton.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				UserInterface.this.removeTransaction();
			}
		});
		
		/*
		 * Month dropdown menu
		 */
		this.removePanelMonthDropdwon.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				UserInterface.this.doDropdownMenuOnChange();
			}
		});
	}
	
	private void doDropdownMenuOnChange()
	{
		Transaction.TYPE type = (Transaction.TYPE)this.removePanelTypeTransaction.getSelectedItem();
		if(type != null)
		{
			this.populateTransactionList(this.removePanelTransactionsList, this.removePanelMonthDropdwon, this.removePanelCategoryDropdwon, type);
		}
	}
	
	public void removePanelOnClick()
	{
		//populate month dropdown
		// populate category dropdown
		// populate transactions list
		if (this.core.getUser() != null)
		{
			this.populateMonthDropdown(this.removePanelMonthDropdwon);
			
			//get main transaction type
			Transaction.TYPE mainType = (Transaction.TYPE)this.removePanelTypeTransaction.getSelectedItem();
			
			this.removePanelCategoryDropdwon.removeAllItems();
			this.removePanelCategoryDropdwon.addItem("All");
			if(mainType != null)
			{
				if(mainType == Transaction.TYPE.INCOME)
					this.populateCategoryDropdownMenu(this.removePanelCategoryDropdwon, Transaction.TYPE.INCOME);
				else
					this.populateCategoryDropdownMenu(this.removePanelCategoryDropdwon, Transaction.TYPE.EXPENDITURE);
			}
			
			
			
			this.populateTransactionList(this.removePanelTransactionsList, this.removePanelMonthDropdwon, this.removePanelCategoryDropdwon, mainType);
		}
	}
	
	public void populateIncomeTransactionListFromMonth(JList<Transaction> listToPopulate, JComboBox<Month> dropdownOfMonths, JComboBox<Object> categoryDropdownMenu)
	{
		Month selectedMonth = (Month) dropdownOfMonths.getSelectedItem();
		Object o = categoryDropdownMenu.getSelectedItem();
		
		if (o == null || selectedMonth == null)
		{
			/*
			 * if(o == null) System.out.println("dropdown is null"); else if (selectedMonth
			 * == null) System.out.println("Selected Month is null");
			 */
		}
		else
		{
			if (o instanceof String)
			{
				this.populateIncomeListWithData(listToPopulate, this.core.getAllIncomeTransactionsForMonth(selectedMonth));
			}
			else
			{
				this.populateIncomeListWithData(listToPopulate, this.core.getAllIncomeTransactionsOfTypeForMonth((SOURCE) o, selectedMonth));
			}
			
		}
	}
	
	public void populateExpenditureTransactionListFromMonth(JList<Transaction> listTopPopulate, JComboBox<Month> listOfMonths, JComboBox<Object> dropdownMenu)
	{
		Month selectedMonth = (Month) listOfMonths.getSelectedItem();
		Object o = dropdownMenu.getSelectedItem();
		if (o == null || selectedMonth == null)
		{
			/*
			 * if(o == null) System.out.println("Remove Expenditure type dropdown is null");
			 * else if (selectedMonth == null)
			 * System.out.println("Selected Expenditure Month is null");
			 */
		}
		else
		{
			if (o instanceof String)
			{
				this.populateExpenditureListWithData(listTopPopulate, this.core.getAllExpenditureTransactionsForMonth(selectedMonth));
			}
			else
			{
				this.populateExpenditureListWithData(listTopPopulate, this.core.getAllExpenditureTransactionsOfTypeForMonth((DESTINATION) o, selectedMonth));
			}
		}
	}
	
	private void populateTransactionList(JList<Transaction> listToPopulate, JComboBox<Month> monthDropdown, JComboBox<Object> categoryDropdown, Transaction.TYPE mainTransactiontype)
	{
		if(mainTransactiontype == Transaction.TYPE.INCOME)
			this.populateIncomeTransactionListFromMonth(listToPopulate, monthDropdown, categoryDropdown);
		else
			this.populateExpenditureTransactionListFromMonth(listToPopulate, monthDropdown, categoryDropdown);
	}
	
	private void populateIncomeListWithData(JList<Transaction> list, List<IncomeTransaction> data)
	{
		if (data.isEmpty())
		{
			list.removeAll();
		}
		
		list.setListData(new Vector<Transaction>(data));
		list.setSelectedIndex(0);
	}
	
	private void populateExpenditureListWithData(JList<Transaction> list, List<ExpenditureTransaction> data)
	{
		if (data.isEmpty())
		{
			list.removeAll();
		}
		
		list.setListData(new Vector<Transaction>(data));
		list.setSelectedIndex(0);
		
	}
	
	private void removeTransaction()
	{
		Transaction.TYPE type = (Transaction.TYPE)this.removePanelTypeTransaction.getSelectedItem();
		if(type != null)
		{
			if(type == Transaction.TYPE.INCOME)
			{
				this.removeIncome();
			}
			else
				this.removeExpenditure();
		}
	}
	
	private void removeIncome()
	{
		IncomeTransaction it = (IncomeTransaction) this.removePanelTransactionsList.getSelectedValue();
		Month selectedMonth = (Month) this.removePanelMonthDropdwon.getSelectedItem();
		if (it == null || selectedMonth == null)
		{
			this.outputError("Sorry, either the month or transaction is null");
		}
		else
		{
			if (this.core.removeIncome(it, selectedMonth))
			{
				this.outputSuccess("£" + it.getAmount() + " from " + it.getWhere() + " has been removed from the system. Your balance is now $" + this.core.getUser().getBalance());
				this.populateIncomeTransactionListFromMonth(this.removePanelTransactionsList, this.removePanelMonthDropdwon, this.removePanelCategoryDropdwon);
				this.updateBalanceOutput();
			}
			else
			{
				this.outputError("Sorry, something went wrong in the core system. IncomeTransaction not removed");
			}
		}
	}
	private void removeExpenditure()
	{
		ExpenditureTransaction it = (ExpenditureTransaction) this.removePanelTransactionsList.getSelectedValue();
		Month selectedMonth = (Month) this.removePanelMonthDropdwon.getSelectedItem();
		if (it == null || selectedMonth == null)
		{
			this.outputError("Sorry, either the month or transaction is null");
		}
		else
		{
			if (this.core.removeExpenditure(it, selectedMonth))
			{
				this.outputSuccess("£" + it.getAmount() + " to " + it.getWhere() + " has been removed from the system. Your balance is now $" + this.core.getUser().getBalance());
				this.populateExpenditureTransactionListFromMonth(this.removePanelTransactionsList, this.removePanelMonthDropdwon, this.removePanelCategoryDropdwon);
				this.updateBalanceOutput();
			}
			else
			{
				this.outputError("Sorry, something went wrong in the core system. ExpenditureTransaction not removed");
			}
		}
	}
	
	// ===================================================================================================================================================
	// List Panel stuff List Panel
	// ===================================================================================================================================================
	public void viewPanelInitialisation()
	{
		this.viewPanel = new JPanel();
		this.tabbedPane.addTab("View Transactions", null, this.viewPanel, null);
		this.viewPanel.setLayout(null);
		
		JLabel viewPanelIncomeMonthLbl = new JLabel("Month");
		viewPanelIncomeMonthLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		viewPanelIncomeMonthLbl.setBounds(10, 11, 46, 14);
		this.viewPanel.add(viewPanelIncomeMonthLbl);
		
		JLabel viewPanelSourceLbl = new JLabel("Category");
		viewPanelSourceLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		viewPanelSourceLbl.setBounds(10, 115, 70, 14);
		this.viewPanel.add(viewPanelSourceLbl);
		
		this.viewPanelCategoryDropdown = new JComboBox<Object>();
		this.viewPanelCategoryDropdown.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.viewPanelCategoryDropdown.setBounds(90, 113, 127, 20);
		this.viewPanel.add(this.viewPanelCategoryDropdown);
		
		JLabel viewPanelAllTransactionsLbl = new JLabel("Transactions");
		viewPanelAllTransactionsLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		viewPanelAllTransactionsLbl.setBounds(10, 200, 91, 14);
		this.viewPanel.add(viewPanelAllTransactionsLbl);
		
		JScrollPane viewPanelIncomeTransactionScrollPane = new JScrollPane();
		viewPanelIncomeTransactionScrollPane.setBounds(90, 144, 200, 148);
		this.viewPanel.add(viewPanelIncomeTransactionScrollPane);
		
		this.viewPanelTransactionsList = new JList<Transaction>();
		this.viewPanelTransactionsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		viewPanelIncomeTransactionScrollPane.setViewportView(this.viewPanelTransactionsList);
		
		JLabel viewPanelIncomeTotalLbl = new JLabel("Total In");
		viewPanelIncomeTotalLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		viewPanelIncomeTotalLbl.setBounds(238, 11, 91, 14);
		this.viewPanel.add(viewPanelIncomeTotalLbl);
		
		this.viewPanelIncomeOutput = new JTextField();
		this.viewPanelIncomeOutput.setEditable(false);
		this.viewPanelIncomeOutput.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.viewPanelIncomeOutput.setBounds(309, 5, 83, 20);
		this.viewPanel.add(this.viewPanelIncomeOutput);
		this.viewPanelIncomeOutput.setColumns(10);
		
		JLabel lblTotalOut = new JLabel("Total Out");
		lblTotalOut.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblTotalOut.setBounds(238, 32, 91, 14);
		this.viewPanel.add(lblTotalOut);
		
		this.viewPanelExpendOutput = new JTextField();
		this.viewPanelExpendOutput.setEditable(false);
		this.viewPanelExpendOutput.setBounds(309, 30, 83, 20);
		this.viewPanel.add(this.viewPanelExpendOutput);
		this.viewPanelExpendOutput.setColumns(10);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(10, 61, 625, 8);
		this.viewPanel.add(separator_1);
		
		this.viewPanelNetOutput = new JTextField();
		this.viewPanelNetOutput.setEditable(false);
		this.viewPanelNetOutput.setBounds(90, 35, 127, 20);
		this.viewPanel.add(this.viewPanelNetOutput);
		this.viewPanelNetOutput.setColumns(10);
		
		JLabel viewPanelNetSpendLbl = new JLabel("Net Spend");
		viewPanelNetSpendLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		viewPanelNetSpendLbl.setBounds(10, 36, 71, 14);
		this.viewPanel.add(viewPanelNetSpendLbl);
		
		this.viewPanelMonthDropdown = new JComboBox<Month>();
		this.viewPanelMonthDropdown.setBounds(90, 9, 127, 20);
		this.viewPanel.add(this.viewPanelMonthDropdown);
		
		JLabel viewPanelTransactionTypeLbl = new JLabel("Type");
		viewPanelTransactionTypeLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		viewPanelTransactionTypeLbl.setBounds(10, 80, 46, 14);
		this.viewPanel.add(viewPanelTransactionTypeLbl);
		
		this.viewPanelTypeDropdown = new JComboBox<Transaction.TYPE>();
		this.viewPanelTypeDropdown.setBounds(90, 80, 127, 20);
		this.viewPanel.add(this.viewPanelTypeDropdown);
		
		this.populateTransactionMainTypeDrowpdown(this.viewPanelTypeDropdown);
		
		this.viewPanelOverviewLbl = new JLabel("Overview");
		this.viewPanelOverviewLbl.setFont(new Font("Tahoma", Font.PLAIN, 12));
		this.viewPanelOverviewLbl.setBounds(431, 115, 108, 14);
		this.viewPanel.add(this.viewPanelOverviewLbl);
		
		JScrollPane viwPanelOverviewScrollPane = new JScrollPane();
		viwPanelOverviewScrollPane.setBounds(328, 144, 268, 148);
		this.viewPanel.add(viwPanelOverviewScrollPane);
		
		//set up scrollpane and table for overview
		this.viewPanelOverviewTable = new JTable();
		this.viewPanelOverviewTable.setAutoCreateRowSorter(true);
		this.tableModel = new DefaultTableModel();
		this.tableModel.setColumnIdentifiers(this.tableOverviewHeaders);
		this.viewPanelOverviewTable.setModel(this.tableModel);
		
		viwPanelOverviewScrollPane.setViewportView(this.viewPanelOverviewTable);
		this.viewPanelOverviewTable.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		this.viewPanelOnClick();
		this.viewPanelHandlers();
	}
	
	public void viewPanelOnClick()
	{
		if (this.core.getUser() != null)
		{
			this.populateMonthDropdown(this.viewPanelMonthDropdown);
			
			this.viewPanelCategoryDropdown.removeItem("All");
			this.viewPanelCategoryDropdown.addItem("All");
			
			Transaction.TYPE mainType = (Transaction.TYPE)this.viewPanelTypeDropdown.getSelectedItem();
			
			this.viewPanelCategoryDropdown.removeAllItems();
			this.viewPanelCategoryDropdown.addItem("All");
			
			if(mainType != null)
			{
				if(mainType == Transaction.TYPE.INCOME)
				{
					this.populateCategoryDropdownMenu(this.viewPanelCategoryDropdown, Transaction.TYPE.INCOME);
					this.populateIncomeTransactionListFromMonth(this.viewPanelTransactionsList, this.viewPanelMonthDropdown, this.viewPanelCategoryDropdown);
				}
				else
				{
					this.populateCategoryDropdownMenu(this.viewPanelCategoryDropdown, Transaction.TYPE.EXPENDITURE);
					this.populateExpenditureTransactionListFromMonth(this.viewPanelTransactionsList, this.viewPanelMonthDropdown, this.viewPanelCategoryDropdown);
				}
				
			}
			
			//display summary table
			this.doViewPanelOverview();
			
			
			this.doViewOutputTotalsForMonth();
			//this.doViewOutputTypeTotals();
		}
	}
	
	public void viewPanelHandlers()
	{
		this.viewPanel.addComponentListener(new ComponentAdapter()
		{
			@Override
			public void componentShown(ComponentEvent arg0)
			{
				UserInterface.this.viewPanelOnClick();
				UserInterface.this.resetOutputField();
			}
		});
		
		this.viewPanelCategoryDropdown.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				UserInterface.this.doViewPanelDropdownMenuOnChange();
			}
		});
		

		this.viewPanelMonthDropdown.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent arg0) {
				UserInterface.this.doViewPanelDropdownMenuOnChange();
				UserInterface.this.doViewPanelOverview();
			}
		});
		

		this.viewPanelTypeDropdown.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				Transaction.TYPE type = (Transaction.TYPE)UserInterface.this.viewPanelTypeDropdown.getSelectedItem();
				if(type != null)
				{
					UserInterface.this.viewPanelCategoryDropdown.removeAllItems();
					UserInterface.this.viewPanelCategoryDropdown.addItem("All");
					UserInterface.this.populateCategoryDropdownMenu(UserInterface.this.viewPanelCategoryDropdown, type);
					UserInterface.this.populateTransactionList(UserInterface.this.viewPanelTransactionsList, UserInterface.this.viewPanelMonthDropdown, UserInterface.this.viewPanelCategoryDropdown, type);
					UserInterface.this.doViewPanelOverview();
				}
			}
		});
	}
	
	private void doViewPanelDropdownMenuOnChange()
	{
		Transaction.TYPE type = (Transaction.TYPE)this.viewPanelTypeDropdown.getSelectedItem();
		if(type != null)
		{
			this.populateTransactionList(this.viewPanelTransactionsList, this.viewPanelMonthDropdown, this.viewPanelCategoryDropdown, type);
		}
	}
	
	private void doViewOutputTotalsForMonth()
	{
		Month viewSelectedMonth = (Month) this.viewPanelMonthDropdown.getSelectedItem();
		
		if (viewSelectedMonth != null)
		{
			float income = this.core.getTotalIncomeForMonth(viewSelectedMonth);
			float expend = this.core.getTotalExpenditureForMonth(viewSelectedMonth);
			float diff = this.core.getNetSpendForMonth(viewSelectedMonth);
			this.viewPanelIncomeOutput.setText("£" + income);
			this.viewPanelExpendOutput.setText("£" + expend);
			this.viewPanelNetOutput.setText("£" + diff);
			if (diff > 0)
			{
				this.viewPanelNetOutput.setBackground(Color.GREEN);
			}
			else if (diff < 0)
			{
				this.viewPanelNetOutput.setBackground(Color.red);
			}
			else
			{
				this.viewPanelNetOutput.setBackground(Color.LIGHT_GRAY);
			}
			
		}
	}
	
	
	private void doViewPanelOverview()
	{
		Transaction.TYPE type = (Transaction.TYPE)this.viewPanelTypeDropdown.getSelectedItem();
		Month month = (Month)this.viewPanelMonthDropdown.getSelectedItem();
		
		if(type != null)
		{
			if(type == Transaction.TYPE.INCOME)
				this.doViewPanelIncomeOverview(month);
			else
				this.doViewPanelExpenditureOverview(month);
		}
	}
	
	//TODO remove the individual Lists and just put straight into the Object arrays
	//displays a table of each income type and number of transactions and totals
	private void doViewPanelIncomeOverview(Month month)
	{
		this.tableModel.setRowCount(0);
		
		List<IncomeTransaction> workIncome = this.core.getAllIncomeTransactionsOfTypeForMonth(SOURCE.Work, month);
		List<IncomeTransaction> interestIncome = this.core.getAllIncomeTransactionsOfTypeForMonth(SOURCE.Interest, month);
		List<IncomeTransaction> bonusIncome  = this.core.getAllIncomeTransactionsOfTypeForMonth(SOURCE.Bonus, month);
		List<IncomeTransaction> otherIncome  = this.core.getAllIncomeTransactionsOfTypeForMonth(SOURCE.Other, month);
		
		Object[] workObj = new Object[] {"Work", workIncome.size(), this.core.getTotalIncomeForIncomeTransactionType(SOURCE.Work,month)};
		Object[] interestObj = new Object[] { "Interest", interestIncome.size(), this.core.getTotalIncomeForIncomeTransactionType(SOURCE.Interest, month)};
		Object[] bonusObj = new Object[] { "Bonus", bonusIncome.size(), this.core.getTotalIncomeForIncomeTransactionType(SOURCE.Bonus, month)};
		Object[] otherObj = new Object[] { "Other", otherIncome.size(), this.core.getTotalIncomeForIncomeTransactionType(SOURCE.Other, month)};
		
		this.tableModel.addRow(workObj);
		this.tableModel.addRow(interestObj);
		this.tableModel.addRow(bonusObj);
		this.tableModel.addRow(otherObj);
	}
	
	//displays a table of each type of expenditure and # transactions and totals
	private void doViewPanelExpenditureOverview(Month month)
	{
		//Rent, Bills, Petrol, FoodShopping, Treat, Unnecessary, OtherOK
		this.tableModel.setRowCount(0);
		this.tableModel.addRow(new Object[] { "Rent", this.core.getAllExpenditureTransactionsOfTypeForMonth(DESTINATION.Rent, month).size(), this.core.getTotalExpenditureForExpenditureTransactionType(DESTINATION.Rent, month)});
		this.tableModel.addRow(new Object[] { "Bills", this.core.getAllExpenditureTransactionsOfTypeForMonth(DESTINATION.Bills, month).size(), this.core.getTotalExpenditureForExpenditureTransactionType(DESTINATION.Bills, month)});
		this.tableModel.addRow(new Object[] { "Petrol", this.core.getAllExpenditureTransactionsOfTypeForMonth(DESTINATION.Petrol, month).size(), this.core.getTotalExpenditureForExpenditureTransactionType(DESTINATION.Petrol, month)});
		this.tableModel.addRow(new Object[] { "FoodShopping", this.core.getAllExpenditureTransactionsOfTypeForMonth(DESTINATION.FoodShopping, month).size(), this.core.getTotalExpenditureForExpenditureTransactionType(DESTINATION.FoodShopping, month)});
		this.tableModel.addRow(new Object[] { "Treat", this.core.getAllExpenditureTransactionsOfTypeForMonth(DESTINATION.Treat, month).size(), this.core.getTotalExpenditureForExpenditureTransactionType(DESTINATION.Treat, month)});
		this.tableModel.addRow(new Object[] { "Unnecessary", this.core.getAllExpenditureTransactionsOfTypeForMonth(DESTINATION.Unnecessary, month).size(), this.core.getTotalExpenditureForExpenditureTransactionType(DESTINATION.Unnecessary, month)});
		this.tableModel.addRow(new Object[] { "OtherOK", this.core.getAllExpenditureTransactionsOfTypeForMonth(DESTINATION.OtherOK, month).size(), this.core.getTotalExpenditureForExpenditureTransactionType(DESTINATION.OtherOK, month)});
		
	}
	
	
	
	// ===================================================================================================================================================
	//
	// ===================================================================================================================================================
	
	/**
	 * Checks to see if we are into a new Month
	 */
	public void onLoad()
	{
		this.core.onLoad();
	}
	
	void testStuff()
	{
	}
	
	void testPrint()
	{
		System.out.println("Working!");
	}
	
	void save()
	{
		this.core.save();
		this.outputStandard("State saved");
	}
	
	private void exit()
	{
		System.exit(0);
	}
	
	// ===================================================================================================================================================
	// Output Output
	// ===================================================================================================================================================
	public void outputSuccess(String msg)
	{
		this.output.setForeground(new Color(30, 160, 67));
		this.output.setText(msg);
	}
	
	public void outputStandard(String msg)
	{
		this.output.setText(msg);
	}
	
	public void outputDebug(String msg)
	{
		this.output.setForeground(Color.BLUE);
		this.output.setText("[Debug] " + msg);
	}
	
	public void outputError(String msg)
	{
		this.output.setForeground(Color.RED);
		this.output.setText(msg);
	}
	
	public void resetOutputField()
	{
		this.output.setText("");
	}
	
	public void resetInputFields()
	{
		this.addPanelAmount.setText("");
		this.addPanelWhere.setText("");
		
		
		this.addPanelMonthDropwdown.setSelectedIndex(0);
		this.setDropdownDateToToday(this.addPanelDayDropdown);
	}
	
	void updateBalanceOutput()
	{
		User us = this.core.getUser();
		if (us == null) return;
		float u = us.getBalance();
		this.balanceOutput.setText("£" + u);
	}
}
