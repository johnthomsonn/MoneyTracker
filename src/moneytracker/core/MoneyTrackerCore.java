package moneytracker.core;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import moneytracker.Util.Database;
import moneytracker.Util.File;
import moneytracker.core.internal.ExpenditureTransaction;
import moneytracker.core.internal.ExpenditureTransaction.DESTINATION;
import moneytracker.core.internal.IncomeTransaction;
import moneytracker.core.internal.IncomeTransaction.SOURCE;
import moneytracker.core.internal.Month;
import moneytracker.core.internal.Transaction;
import moneytracker.core.internal.User;

public class MoneyTrackerCore
{
	
	private static MoneyTrackerCore core = null;
	
	private List<Month> monthsInSystem;
	private User activeUser;
	private final Set<User> allUsers;
	private static final String savePath = "FinancialData.data";
	
	private MoneyTrackerCore()
	{
		this.monthsInSystem = new ArrayList<Month>();
		this.allUsers = new HashSet<User>();
		this.activeUser = null;
	}
	
	public static MoneyTrackerCore TestMoneyTrackerCore()
	{
		core = new MoneyTrackerCore();
		return core;
	}
	
	/**
	 * Tries to load the previous state of the system or returns a new core object
	 * if first time being run
	 *
	 * @return core system
	 */
	public static MoneyTrackerCore getCore()
	{
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		if (core == null)
		{
			try
			{
				fis = new FileInputStream(savePath);
				ois = new ObjectInputStream(fis);
				core = (MoneyTrackerCore) ois.readObject();
				
			}
			catch (final Exception e)
			{
				// JOptionPane.showMessageDialog(null, "No save file found. Creating new save",
				// "New Application", JOptionPane.INFORMATION_MESSAGE);
				core = new MoneyTrackerCore();
				core.save();
			}
			finally
			{
				try
				{
					if (fis != null)
					{
						fis.close();
					}
					if (ois != null)
					{
						ois.close();
					}
				}
				catch (final Exception e)
				{
					System.out.println(e.getMessage());
				}
			}
		}
		return core;
	}

	/*
	 * private List<Month> getMonths() { return this.monthsInSystem; }
	 */
	
	/**
	 * Returns a copy of the current active user
	 *
	 * @return USer
	 */
	public User getUser()
	{
		// return new User(user);
		return core.activeUser;
	}
	
	/**
	 * Returns a collection of all users in the system
	 * @return user collection
	 */
	public Set<User> getAllUsers()
	{
		this.allUsers.clear();
		try
		{
			Database.open();
			final ResultSet rs = Database.getAllUsers();
			while (rs.next())
			{
				final int id = rs.getInt("user_id");
				final String name = rs.getString("name");
				final float bal = rs.getBigDecimal("balance").floatValue();
				final String passwordString = rs.getString("password");
				final char[] pass = passwordString.toCharArray();
				final User u = new User(id, name, bal, pass);
				this.allUsers.add(u);
			}
		}
		catch (final Exception e)
		{
			System.out.println("[core.getAllUsers] " + e.getMessage());
		}
		finally
		{
			Database.close();
		}
		
		return this.allUsers;
	}
	
	public String getSavepath()
	{
		return savePath;
	}
	
	/**
	 * Saves the state of the system
	 */
	public void save()
	{ /*
	 * FileOutputStream fos = null; ObjectOutputStream oos = null; try { fos = new
	 * FileOutputStream(savePath); oos = new ObjectOutputStream(fos);
	 * oos.writeObject(this); } catch (Exception e) {
	 * JOptionPane.showMessageDialog(null,
	 * "There was an error while trying save the financial data", "Error",
	 * JOptionPane.ERROR_MESSAGE); System.exit(1); } finally { try { fos.close();
	 * oos.close(); } catch (Exception e) { System.out.println(e.getMessage()); } }
	 */
	}
	
	// ===================================================================================================================================================
	// Use Case Methods
	// ===================================================================================================================================================
	
	/**
	 * Adds an Income Transaction to the current User by the given parameters
	 *
	 * @param anAmount
	 *            the amount of the transaction
	 * @param where
	 *            where the money came from
	 * @param aDay
	 *            the day of the month
	 * @param aMonth
	 *            the month object
	 * @param aSource
	 *            the source of the income
	 * @return true if added successful, false otherwise.
	 */
	public boolean addIncome(float anAmount, String where, int aDay, Month aMonth, SOURCE aSource)
	{

		final boolean added = this.activeUser.addIncome(Database.getNumberOfTransactions(), anAmount, where, aDay, aMonth, aSource);
		if (added)
		{
			Database.insertTransaction(this.activeUser, anAmount, where, aDay, aMonth, aSource, null, Transaction.TYPE.INCOME);

			this.updateUserDatabaseBalance();
		}
		return added;
	}

	private void addIncomeFromDatabase(int transactionID, float anAmount, String where, int aDay, Month aMonth, SOURCE aSource)
	{
		this.activeUser.addIncomeFromDatabase(transactionID, anAmount, where, aDay, aMonth, aSource);
	}
	
	/**
	 * Adds an expenditure Transaction to the current user by the given parameters
	 *
	 * @param anAmount
	 *            the amount of the transaction
	 * @param where
	 *            where the money was paid to
	 * @param day
	 *            the day of the month
	 * @param month
	 *            the month object
	 * @param dest
	 *            the destination enum
	 * @return true if added successfully
	 */
	public boolean addExpenditure(float anAmount, String where, int day, Month month, DESTINATION dest)
	{
		final boolean added = this.activeUser.addExpenditure(Database.getNumberOfTransactions(), anAmount, where, day, month, dest);
		if (added)
		{
			Database.insertTransaction(this.activeUser, anAmount, where, day, month, null, dest, Transaction.TYPE.EXPENDITURE);
			this.updateUserDatabaseBalance();
		}
		return added;
	}

	private void addExpenditureFromDatabase(int transactionID, float anAmount, String where, int day, Month month, DESTINATION dest)
	{
		this.activeUser.addExpenditureFromDatabase(transactionID, anAmount, where, day, month, dest);
	}
	
	private void updateUserDatabaseBalance()
	{
		Database.updateUserBalance(this.activeUser);
	}
	
	/**
	 * Removes the given Income Transaction from the system
	 *
	 * @param anIncomeTransaction
	 *            the income transaction to remove
	 * @param aMonth
	 *            the month where the transaction took place
	 * @return true if successful
	 */
	public boolean removeIncome(IncomeTransaction anIncomeTransaction, Month aMonth)
	{
		boolean removed = this.activeUser.removeIncome(anIncomeTransaction, aMonth);
		if (removed)
		{
			Database.removeIncomeTransaction(anIncomeTransaction, this.getUser(), aMonth);
			this.updateUserDatabaseBalance();
		}
		return removed;
	}
	
	/**
	 * Removes the given expenditure transaction from the given month
	 *
	 * @param anExpenditureTransaction
	 *            the expenditure transaction to remove
	 * @param aMonth
	 *            the month to remove the transaction from
	 * @return true if successful.
	 */
	public boolean removeExpenditure(ExpenditureTransaction anExpenditureTransaction, Month aMonth)
	{
		boolean removed = this.activeUser.removeExpenditure(anExpenditureTransaction, aMonth);
		if (removed)
		{
			Database.removeExpenditureTransaction(anExpenditureTransaction, this.getUser(), aMonth);
			this.updateUserDatabaseBalance();
		}
		return removed;
	}
	
	/**
	 * Returns the total income for the given month
	 *
	 * @param aMonth
	 *            the month you want to get the income for
	 * @return the total income
	 */
	public float getTotalIncomeForMonth(Month aMonth)
	{
		return aMonth.getTotalIncome();
	}
	
	/**
	 * Returns the total expenditure for that month
	 *
	 * @param aMonth
	 *            the month you want to get the expenditure for
	 * @return the total expenditure
	 */
	public float getTotalExpenditureForMonth(Month aMonth)
	{
		return aMonth.getTotalExpenditure();
	}
	
	/**
	 * Returns the net spend for the given month
	 *
	 * @param aMonth
	 *            the month you want to get the net spend for
	 * @return the income - expenditure
	 */
	public float getNetSpendForMonth(Month aMonth)
	{
		return aMonth.getNetSpend();
	}
	
	/**
	 * Creates a new User with the given values and sets the active user to it
	 * @param aName the users name
	 * @param aBalance the users balance
	 * @param aPassword the users password
	 * @return true if successful
	 */
	public boolean createNewUser(String aName, float aBalance, char[] aPassword)
	{
		final User newUser = new User(aName, aBalance, aPassword);
		this.addUser(newUser);
		this.userloggedOn(newUser);
		
		Database.insertNewUser(newUser);
		
		return true;
	}
	
	/**
	 * Logs the user out and returns to login screen
	 * @return true
	 */
	public boolean logoutUser()
	{
		this.activeUser = null;
		
		return true;
	}
	
	/**
	 * Logs the given user in
	 * @param aUser the user to login
	 * @return true
	 */
	public boolean loginUser(User aUser)
	{
		this.clearPreviousUser();
		this.userloggedOn(aUser);
		return true;
	}

	private void clearPreviousUser()
	{
		this.onLoad();
	}
	
	//TODO not realy a todo but if transactions dont seem to be saving then remove the user.addMonth and uncomment the addLatestMonthToUser
	private void userloggedOn(User user)
	{
		this.setActiveUser(user);
		this.gettAllActiveUserData();
		user.addMonth(this.monthsInSystem);
		//this.addLatestMonthToUser();
		// this.checkIfNewMonth(); shouldn't need this
	}
	
	// get all data from transaction database
	// populate system info with this
	// Don't think this will work since the month objects might be different in memory
	//might work now as i've added getMonth in User
	//if it doesn't work then look at creating the map with monthID keys and months as value then need to write new addMonths in user
	//if it does work then look at refactoring
	void gettAllActiveUserData()
	{
		Database.open();
		final ResultSet rs = Database.getAllTransactionsForUser(this.activeUser);


		
		try
		{
			while (rs.next())
			{
				final int monthID = rs.getInt("month_id");
				final Month monthDB = this.createMonthFromDatabase(monthID);
				Month month = null;
				for(final Month m : this.monthsInSystem)
				{
					if (m == null || monthDB == null)
					{
					}
					else if (m.equals(monthDB))
					{
						month = m;
					}
				}

				final int transactionID = rs.getInt("id");
				final float amount = rs.getFloat("amount");
				final String where = rs.getString("where_string");
				final String typeString = rs.getString("type");
				final Transaction.TYPE type = Transaction.StringToTransactionType(typeString);
				
				final String date = rs.getString("date");
				//final Calendar calendar = Calendar.getInstance();
				//calendar.setTime(date);
				//final int day = calendar.get(Calendar.DAY_OF_MONTH);
				final int day = Integer.parseInt(date.split("-")[2]);
				
				
				final String sourceString = rs.getString("source");
				final String destString = rs.getString("destination");
				SOURCE source = null;
				DESTINATION destination = null;
				if(type == Transaction.TYPE.INCOME) {
					source = IncomeTransaction.StringToSource(sourceString);
				}
				else if(type == Transaction.TYPE.EXPENDITURE)
				{
					destination = ExpenditureTransaction.StringToDestination(destString);
				}
				// if already created month
				if (this.activeUser.doesMonthExist(month))
				{
					if(type == Transaction.TYPE.INCOME)
					{
						this.addIncomeFromDatabase(transactionID, amount, where, day, this.activeUser.getMonth(month), source);
					}
					else if(type == Transaction.TYPE.EXPENDITURE)
					{
						this.addExpenditureFromDatabase(transactionID, amount, where, day, this.activeUser.getMonth(month), destination);
					}
				}
				else // else month doesn't exist so create it and add
				{
					this.activeUser.addMonth(month);
					if(type == Transaction.TYPE.INCOME)
					{
						Month m = this.activeUser.getMonth(month);
						this.addIncomeFromDatabase(transactionID, amount, where, day, m, source);
					}
					else if(type == Transaction.TYPE.EXPENDITURE)
					{
						this.addExpenditureFromDatabase(transactionID, amount, where, day, this.activeUser.getMonth(month), destination);
					}
				}
				
			}

		}
		catch (final SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			File.logError("Core", "getAllActiveUserData", e.getMessage());
		}
		finally
		{
			Database.close();
		}
		
	}
	
	private Month createMonthFromDatabase(int id)
	{
		final ResultSet rs = Database.getMonthDataFromID(id);
		Month m = null;
		try
		{
			while (rs.next())
			{
				final int number = rs.getInt("number");
				final int year = rs.getInt("year");
				m = new Month(number, year);
			}
		}
		catch (final Exception e)
		{
			System.out.println("core.createMonthFromDatabase " + e.getMessage());
		}
		return m;
	}
	
	/**
	 * Sets the active user to the given user
	 * @param aUser the new user to be active
	 * @return true if successfully set
	 */
	public boolean setActiveUser(User aUser)
	{
		this.activeUser = aUser;
		return true;
	}
	
	/**
	 * Adds a user to the list of all users
	 * @param user the user to add to the system
	 */
	public void addUser(User user)
	{
		this.allUsers.add(user);
	}
	
	// ===================================================================================================================================================
	// Helper Functions for UI
	// ===================================================================================================================================================
	
	public void onLoad()
	{
		Database.open();
		File.CreateOutputErrorFile();
		int numMonths = this.setMonthsInSystemFromDatabase();
		this.checkIfNewMonth(numMonths);
		Database.close();
	}
	
	/*
	 * private void addLatestMonthToUser() { Collections.sort(this.monthsInSystem);
	 * final boolean contain =
	 * this.activeUser.doesMonthExist(this.getSystemCurrentMonth());
	 *
	 * if (!contain) { this.activeUser.addMonth(this.getSystemCurrentMonth()); } }
	 */
	
	/*
	 * // TODO in August check if this still shows properly private Month
	 * getSystemCurrentMonth() { return
	 * this.monthsInSystem.get(this.monthsInSystem.size() -
	 * this.monthsInSystem.size()); }
	 */
	
	private int setMonthsInSystemFromDatabase()
	{
		final ResultSet rs = Database.getAllMonths();
		final List<Month> tmp = new ArrayList<>();
		int num = 0;
		if (rs == null)
		{
			this.monthsInSystem = tmp;
		}
		else
		{
			try
			{
				while (rs.next())
				{
					final int id = rs.getInt("id");
					rs.getString("name");
					final int number = rs.getInt("number");
					final int year = rs.getInt("year");
					final Month m = new Month(number, year, id);
					tmp.add(m);
					num = id;
				}
				this.monthsInSystem = tmp;
			}
			catch (final SQLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
				File.logError("Core", "setMonthsinSystemFromDatabase", e.getMessage());
			}
		}
		return num;
	}
	
	/*
	 * Dont think we need to add month to user month but will keep here anyway /**
	 * Checks if we are into a new month, if so, creates and adds new Month to User
	 */ /*
	 * public void checkIfNewMonth() { if (core.getUser() != null) { LocalDate today
	 * = LocalDate.now(); Month tmp = new Month(today.getMonthValue(),
	 * today.getYear()); // first time run, this will add a new month to user if
	 * (this.activeUser.getMonths().size() == 0) { this.activeUser.addMonth(tmp); }
	 * // check if current Month does not exists if
	 * (!this.activeUser.doesMonthExist(tmp)) { this.activeUser.addMonth(tmp); } } }
	 */
	
	private void checkIfNewMonth(int numMonths)
	{
		final LocalDate today = LocalDate.now();
		final Month tmp = new Month(today.getMonthValue(), today.getYear(), numMonths + 1);
		// monthsInSystem should be up to date so if this month isn't in there then add
		// it and add to database
		if (!this.monthsInSystem.contains(tmp))
		{
			this.monthsInSystem.add(tmp);
			Database.addMonth(tmp);
		}
	}
	
	/**
	 * Gets the previous three months from the system to display on the UI
	 *
	 * @return previous 3 months
	 */
	public List<Month> getMonthsForList()
	{
		/*
		if (this.activeUser != null)
		{
			List<Month> months = new ArrayList<>();
			Set<Month> userMonths = this.activeUser.getMonths();
			List<Month> monthsList = new ArrayList<>(userMonths);

			Collections.sort(monthsList);
			months.add(monthsList.get(monthsList.size() - 1));
			if (monthsList.size() > 2)
			{
				months.add(monthsList.get(monthsList.size() - 3));
				months.add(monthsList.get(monthsList.size() - 2));
			}

			Collections.sort(monthsInSystem);
			Month latest = monthsInSystem.get(monthsInSystem.size()-1);
			if(!months.contains(latest));
				months.add(latest);

			return months;
		}
		return null;
		 */
		if(this.activeUser != null)
		{
			final List<Month> monthsToUse = new ArrayList<>();
			Collections.sort(this.monthsInSystem);
			final Set<Month> userMonthsSet = this.activeUser.getMonths();
			final List<Month> userMonthsList = new ArrayList<>(userMonthsSet);
			final int userMonthsCount = userMonthsList.size();
			if(userMonthsCount <= 2)
			{
				monthsToUse.addAll(userMonthsList);
			}
			else
			{
				monthsToUse.add(userMonthsList.get(userMonthsCount-1));
				monthsToUse.add(userMonthsList.get(userMonthsCount-2));
			}
			return monthsToUse;
		}
		return null;
	}
	
	/**
	 * Gets the current month
	 *
	 * @return current the current month
	 */
	public Month getCurrentMonth()
	{
		final Set<Month> setMonths = this.activeUser.getMonths();
		final List<Month> allMonths = new ArrayList<Month>(setMonths);
		Collections.sort(allMonths);
		return allMonths.get(allMonths.size() - 1);
	}
	
	/**
	 * Returns the number of days in the given month
	 *
	 * @param aMonth
	 *            the month you want to get days for
	 * @return number of days as an integer
	 */
	public int getDaysInMonth(Month aMonth)
	{
		return aMonth.getDaysInMonth();
	}
	
	/**
	 * Returns the SOURCE enum values of IncomeTransaction
	 *
	 * @return SOURCE enum values
	 */
	public SOURCE[] getIncomeTransactionTypes()
	{
		return IncomeTransaction.SOURCE.values();
	}
	
	/**
	 * Returns the DESTINATION enum values of IncomeTransaction
	 *
	 * @return DESTINATION enum values
	 */
	public DESTINATION[] getExpenditureTransactionTypes()
	{
		return ExpenditureTransaction.DESTINATION.values();
	}
	
	/**
	 * Gets the number of Income transactions for the given month
	 *
	 * @param aMonth
	 *            the month
	 * @return the number of income transactions
	 */
	public int getNumIncomeTransactionsForMonth(Month aMonth)
	{
		return this.activeUser.getIncomeTransactionsForMonth(aMonth).size();
	}
	
	/**
	 * Returns the number of expenditure transactions for the given month
	 *
	 * @param aMonth
	 *            the month
	 * @return the number of expenditure transactions
	 */
	public int getNumExpenditureTransactionsForMonth(Month aMonth)
	{
		return this.activeUser.getExpenditureTransactionsForMonth(aMonth).size();
	}
	
	/**
	 * Returns a list of all income transactions of given month
	 *
	 * @param aMonth
	 *            the month you want transactions for
	 * @return List of income transactions
	 */
	public List<IncomeTransaction> getAllIncomeTransactionsForMonth(Month aMonth)
	{
		final List<IncomeTransaction> incomes = this.activeUser.getIncomeTransactionsForMonth(aMonth);
		return incomes;
	}
	
	/**
	 * Returns a list of all expenditure transactions of the given month
	 *
	 * @param aMonth
	 *            the month
	 * @return list of expenditure transactions
	 */
	public List<ExpenditureTransaction> getAllExpenditureTransactionsForMonth(Month aMonth)
	{
		return this.activeUser.getExpenditureTransactionsForMonth(aMonth);
	}
	
	/**
	 * Returns a list of all income transactions of a given type for a given month
	 *
	 * @param source
	 *            the source of the transaction
	 * @param aMonth
	 *            the month
	 * @return a list
	 */
	public List<IncomeTransaction> getAllIncomeTransactionsOfTypeForMonth(SOURCE source, Month aMonth)
	{
		return aMonth.getIncomeTransactionsOfType(source);
	}
	
	/**
	 * Returns a list of all expenditure transactions of a given type for a given
	 * month
	 *
	 * @param destination the type of transaction
	 * @param aMonth the month
	 * @return a list of all relevant transactions
	 */
	public List<ExpenditureTransaction> getAllExpenditureTransactionsOfTypeForMonth(DESTINATION destination, Month aMonth)
	{
		return aMonth.getExpenditureTransactionsOfType(destination);
	}
	
	/**
	 * Returns the total money spent for the transaction type of the given month
	 * @param destination the destination of the transactions you want the total for
	 * @param aMonth the month
	 * @return total money spent
	 */
	public float getTotalExpenditureForExpenditureTransactionType(DESTINATION destination, Month aMonth)
	{
		return aMonth.getTotalExpenditureForExpenditureTransactionType(destination);
	}
	
	/**
	 * Returns the total money gained for the transaction type of the given month
	 *
	 * @param aMonth the month
	 * @param source the source enum for transaction
	 * @return total money gained
	 */
	public float getTotalIncomeForIncomeTransactionType(SOURCE source, Month aMonth)
	{
		return aMonth.getTotalIncomeForIncomeTransactionType(source);
	}

	
}
