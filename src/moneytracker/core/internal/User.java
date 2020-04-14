package moneytracker.core.internal;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import moneytracker.core.internal.ExpenditureTransaction.DESTINATION;
import moneytracker.core.internal.IncomeTransaction.SOURCE;

public class User
{
	private static int globalID = 1;
	private int id;
	private String name;
	private float balance;
	private char[] password;
	
	private Set<Month> allMonths;
	
	/**
	 * Default constructor
	 */
	public User()
	{
		this.id = globalID++;
		this.name = "Default";
		this.balance = 0.0f;
	}
	
	/**
	 * Creates a new User object with the given values
	 * @param aName the name of the user
	 * @param aBalance the balance of the user
	 * @param aPassword the password of the user
	 */
	public User(String aName, float aBalance, char[] aPassword)
	{
		this.name = aName;
		this.balance = aBalance;
		this.password = aPassword;
		this.allMonths = new HashSet<Month>();
		this.id = globalID++;
	}
	
	/**
	 * Creates a new User object with the given values
	 * @param anID the user id
	 * @param aName the name of the user
	 * @param aBalance the balance of the user
	 * @param aPassword the password of the user
	 */
	public User(int anID, String aName, float aBalance, char[] aPassword)
	{
		this.id = anID;
		this.name = aName;
		this.balance = aBalance;
		this.password = aPassword;
		this.allMonths = new HashSet<Month>();
		globalID = this.id + 1;
	}
	
	/**
	 * Creates a new User object with the given values
	 * @param aName the users name
	 * @param aBalance the starting balance of the new user
	 */
	public User(String aName, float aBalance)
	{
		this.id = globalID++;
		this.name = aName;
		this.balance = aBalance;
		this.allMonths = new HashSet<Month>();
	}
	
	/**
	 * Creates a copy of the given User object
	 * @param aUser the user object to copy
	 */
	public User(User aUser)
	{
		this.id = aUser.id;
		this.name = aUser.getName();
		this.balance = aUser.getBalance();
		this.allMonths = aUser.getMonths();
	}
	
	/**
	 * Returns the users password
	 * @return password
	 */
	public char[] getpassword()
	{
		return this.password;
	}
	
	/**
	 * Returns the users name
	 * @return name
	 */
	public String getName()
	{
		return new String(this.name);
	}
	
	/**
	 * Returns the user's id
	 * @return id
	 */
	public int getID()
	{
		return this.id;
	}
	
	/**
	 * Returns the users balance
	 * @return balance
	 */
	public float getBalance()
	{
		return new Float(this.balance);
	}
	
	public Set<Month> getMonths()
	{
		//return new HashSet<Month>(this.allMonths);
		return this.allMonths;
	}
	
	/**
	 * Adds a Month to the User on the 1st day of the month
	 * @param aMonth the new month to add
	 */
	public void addMonth(Month aMonth)
	{
		this.allMonths.add(aMonth);
	}
	
	/**
	 * Checks to see if a month exists
	 * @param aMonth the month
	 * @return true if the month exists
	 */
	public boolean doesMonthExist(Month aMonth)
	{
		return this.allMonths.contains(aMonth);
	}
	
	/**
	 * Adds an Income transaction to the given month and increases the users balance
	 * @param transactionID the unique id of the transaction
	 * @param anAmount the amount of the transaction
	 * @param where where the money came from
	 * @param aDay the day of the transaction
	 * @param aMonth the month object of the transaction
	 * @param aSource the source of the amount
	 * @return true if successfully added, false otherwise
	 */
	public boolean addIncome(int transactionID, float anAmount, String where, int aDay, Month aMonth, SOURCE aSource)
	{
		boolean isOk = false;
		if (aMonth.addIncome(transactionID, anAmount, where, aDay, aSource))
		{
			this.increaseBalance(anAmount);
			isOk = true;
		}
		return isOk;
	}
	
	public boolean addIncomeFromDatabase(int transactionID, float anAmount, String where, int aDay, Month aMonth, SOURCE aSource)
	{
		boolean isOk = false;
		if (aMonth.addIncome(transactionID, anAmount, where, aDay, aSource))
		{
			isOk = true;
		}
		return isOk;
	}
	
	/**
	 * Adds a list of months to the user if they are not already in the system
	 * @param monthsToAdd the list of months to add to the user
	 */
	public void addMonth(List<Month> monthsToAdd)
	{
		for(Month m : monthsToAdd)
		{
			if(!this.allMonths.contains(m))
				this.allMonths.add(m);
		}
	}
	
	/**
	 * Adds an Expenditure Transaction to the given month and decreases the users balance
	 * @param transactionID the unique id of the transaction
	 * @param anAmount the amount of the transaction
	 * @param where where the money went
	 * @param aDay the day of the month
	 * @param aMonth the month object
	 * @param dest the destination enum
	 * @return true if successfully added
	 */
	public boolean addExpenditure(int transactionID, float anAmount, String where, int aDay, Month aMonth, DESTINATION dest)
	{
		boolean isOk = false;
		if (aMonth.addExpenditure(transactionID, anAmount, where, aDay, dest))
		{
			this.decreaseBalance(anAmount);
			isOk = true;
		}
		return isOk;
	}

	public boolean addExpenditureFromDatabase(int transactionID, float anAmount, String where, int aDay, Month aMonth, DESTINATION dest)
	{
		boolean isOk = false;
		if (aMonth.addExpenditure(transactionID, anAmount, where, aDay, dest))
		{
			isOk = true;
		}
		return isOk;
	}
	
	private void increaseBalance(float anAmount)
	{
		this.balance += anAmount;
	}
	
	private void decreaseBalance(float anAmount)
	{
		this.balance -= anAmount;
	}
	
	/**
	 * Returns a list of Income transactions for the given month
	 * @param aMonth the month you want to find
	 * @return income transactions
	 */
	public List<IncomeTransaction> getIncomeTransactionsForMonth(Month aMonth)
	{
		return aMonth.getIncomeTransactions();
	}
	
	/**
	 * Returns a list of Expenditure transactions for the given month
	 * @param aMonth the month you want to find
	 * @return expenditure transactions for month
	 */
	public List<ExpenditureTransaction> getExpenditureTransactionsForMonth(Month aMonth)
	{
		return aMonth.getExpenditureTransactions();
	}
	
	/**
	 * Removes the given Income transaction from the given month
	 * @param anIncomeTransaction the income transaction
	 * @param aMonth the month
	 * @return true if successful
	 */
	public boolean removeIncome(IncomeTransaction anIncomeTransaction, Month aMonth)
	{
		boolean removed = false;
		if (aMonth.removeIncome(anIncomeTransaction))
		{
			this.decreaseBalance(anIncomeTransaction.getAmount());
			removed = true;
		}
		return removed;
	}
	
	/**
	 * Removes the given expenditure transaction from the given month
	 * @param anExpenditureTransaction the expenditure transaction to remove
	 * @param aMonth the given month
	 * @return true if successful
	 */
	public boolean removeExpenditure(ExpenditureTransaction anExpenditureTransaction, Month aMonth)
	{
		boolean removed = false;
		if (aMonth.removeExpenditure(anExpenditureTransaction))
		{
			this.increaseBalance(anExpenditureTransaction.getAmount());
			removed = true;
		}
		return removed;
	}
	
	/**
	 * Checks to see if the given password matches the users password
	 * @param other the other password to test
	 * @return true if they match
	 */
	public boolean doesPasswordsMatch(String other)
	{
		return this.password.equals(other.toCharArray());
	}
	
	@Override
	public String toString()
	{
		return this.getName();
	}
	
	@Override
	public boolean equals(Object other)
	{
		User u = (User) other;
		return u.id == this.id;
	}
	
	@Override
	public int hashCode()
	{
		return this.id;
	}
	
	public Month getMonth(Month aMonth)
	{
		Month m = null;
		for(Month mon : this.allMonths)
		{
			if(mon.equals(aMonth))
				m = mon;
		}
		return m;
	}
	
}
