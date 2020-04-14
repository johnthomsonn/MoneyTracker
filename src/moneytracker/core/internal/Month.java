package moneytracker.core.internal;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import moneytracker.core.internal.ExpenditureTransaction.DESTINATION;
import moneytracker.core.internal.IncomeTransaction.SOURCE;

public class Month implements Comparable<Month>
{
	
	private String name;
	private int number;
	private int year;
	private int monthID;
	private int globalID = 1;
	
	private List<IncomeTransaction> allIncomeTransactions;
	private List<ExpenditureTransaction> allExpenditureTransactions;
	
	/**
	 * Constructor to initailise a new Month object with given values
	 *
	 * @param aNumber
	 *            the number of the month
	 * @param aYear
	 *            the year
	 */
	public Month(int aNumber, int aYear)
	{
		this.number = aNumber;
		this.year = aYear;
		this.monthID = this.globalID++;
		this.setName();
		this.allIncomeTransactions = new ArrayList<>();
		this.allExpenditureTransactions = new ArrayList<>();
	}
	
	/**
	 * Creates a new month object with the given parameters
	 * @param aNumber the number of the month (ie 1 = January, 7 = July)
	 * @param aYear the year
	 * @param anID the months id
	 * @param incomeTransactions the list of all income transactions
	 * @param expenditureTransactions the list of all expenditure transactions
	 */
	public Month(int aNumber, int aYear, int anID, ArrayList<IncomeTransaction> incomeTransactions, ArrayList<ExpenditureTransaction> expenditureTransactions)
	{
		this.number = aNumber;
		this.year = aYear;
		this.monthID = anID;
		this.globalID = anID + 1;
		this.setName();
		this.allIncomeTransactions = incomeTransactions;
		this.allExpenditureTransactions = expenditureTransactions;
	}
	
	/**
	 * Creates a new month object with the given parameters
	 * @param aNumber the number of the month (ie 1 = January, 7 = July)
	 * @param aYear the year
	 * @param anID the months id
	 */
	public Month(int aNumber, int aYear, int anID)
	{
		this.number = aNumber;
		this.year = aYear;
		this.monthID = anID;
		this.setName();
		this.allIncomeTransactions = new ArrayList<IncomeTransaction>();
		this.allExpenditureTransactions = new ArrayList<ExpenditureTransaction>();
	}
	
	/**
	 * Creates a new Month from System time
	 */
	public Month()
	{
		LocalDate today = LocalDate.now();
		this.number = today.getMonthValue();
		this.setName();
		this.year = today.getYear();
	}
	
	public int getNumber()
	{
		return this.number;
	}
	
	/**
	 * Returns the month id
	 * @return month id
	 */
	public int getID()
	{
		return this.monthID;
	}
	
	/**
	 * Returns the year of the month
	 * @return the year
	 */
	public int getYear()
	{
		return this.year;
	}
	
	/**
	 * Sets the number of the year ie 2 for Feb, 12 for Dec
	 * @param aNumber the number of the year
	 */
	public void setNumber(int aNumber)
	{
		this.number = aNumber;
	}
	
	/**
	 * Sets the year
	 * @param aYear the year
	 */
	public void setYear(int aYear)
	{
		this.year = aYear;
	}
	
	/**
	 * Sets the name of the month by the number ie 2 = February
	 */
	private void setName()
	{
		String tmpName = "";
		switch (this.number)
		{
			case 1:
			{
				tmpName = "January";
				break;
			}
			case 2:
			{
				tmpName = "February";
				break;
			}
			case 3:
			{
				tmpName = "March";
				break;
			}
			case 4:
			{
				tmpName = "April";
				break;
			}
			case 5:
			{
				tmpName = "May";
				break;
			}
			case 6:
			{
				tmpName = "June";
				break;
			}
			case 7:
			{
				tmpName = "July";
				break;
			}
			case 8:
			{
				tmpName = "August";
				break;
			}
			case 9:
			{
				tmpName = "September";
				break;
			}
			case 10:
			{
				tmpName = "October";
				break;
			}
			case 11:
			{
				tmpName = "November";
				break;
			}
			case 12:
			{
				tmpName = "December";
				break;
			}
		}
		this.name = tmpName;
	}
	
	/**
	 * Returns the name of the month
	 *
	 * @return name
	 */
	public String getName()
	{
		return new String(this.name);
	}
	
	@Override
	public boolean equals(Object other)
	{
		Month m = (Month) other;
		boolean same = this.number == m.number && this.year == m.year;
		return same;
	}
	
	/**
	 * Returns the number of days in that month
	 *
	 * @return number of days in month
	 */
	public int getDaysInMonth()
	{
		int days = 0;
		if (this.number == 1 || this.number == 3 || this.number == 5 || this.number == 7 || this.number == 8 || this.number == 10 || this.number == 12)
		{
			days = 31;
		}
		else if (this.number == 4 || this.number == 6 || this.number == 9 || this.number == 11)
		{
			days = 30;
		}
		else
		{
			if (this.year % 4 == 0)
			{
				days = 29;
			}
			else
			{
				days = 28;
			}
		}
		return days;
	}
	
	/**
	 * Returns a list of all income transactions
	 *
	 * @return income transactions
	 */
	public List<IncomeTransaction> getIncomeTransactions()
	{
		return new ArrayList<IncomeTransaction>(this.allIncomeTransactions);
	}
	
	/**
	 * Returns a list of all expenditure transactions
	 *
	 * @return expenditure transactions
	 */
	public List<ExpenditureTransaction> getExpenditureTransactions()
	{
		return new ArrayList<ExpenditureTransaction>(this.allExpenditureTransactions);
		// return this.allExpenditureTransactions;
	}
	
	@Override
	public int hashCode()
	{
		return this.year * this.number;
	}
	
	@Override
	public int compareTo(Month m)
	{
		int tmp;
		tmp = m.number - this.number;
		if (tmp == 0)
		{
			tmp = m.year - this.year;
		}
		return tmp;
	}
	
	/**
	 * Returns a human readable representation of the Month
	 */
	@Override
	public String toString()
	{
		return this.getName() + " " + this.year;
	}
	
	boolean addIncome(int transactionID, float anAmount, String where, int aDay, SOURCE aSource)
	{
		LocalDate ld = LocalDate.of(this.year, this.number, aDay);
		return this.addIncomeTransaction(new IncomeTransaction(anAmount, where, ld, aSource, transactionID));
	}
	
	boolean addExpenditure(int transactionID, float anAmount, String where, int aDay, DESTINATION dest)
	{
		LocalDate ld = LocalDate.of(this.year, this.number, aDay);
		return this.addExpenditureTransaction(new ExpenditureTransaction(anAmount, where, ld, dest, transactionID));
	}
	
	
	private boolean addIncomeTransaction(IncomeTransaction anIncomeTransaction)
	{
		return this.allIncomeTransactions.add(anIncomeTransaction);
	}
	
	private boolean addExpenditureTransaction(ExpenditureTransaction anExpenditureTransaction)
	{
		return this.allExpenditureTransactions.add(anExpenditureTransaction);
	}
	
	/**
	 * Returns a list of all income transactions of the given type in the month
	 *
	 * @param type
	 *            the type of income transaction
	 * @return list of all income transactions
	 */
	public List<IncomeTransaction> getIncomeTransactionsOfType(SOURCE type)
	{
		List<IncomeTransaction> transactions = new ArrayList<>();
		for (IncomeTransaction t : this.allIncomeTransactions)
		{
			if (t.getSource().equals(type))
			{
				transactions.add(t);
			}
		}
		return transactions;
	}
	
	/**
	 * Returns a list of all expenditure transactions of the given type for the
	 * month
	 *
	 * @param dest the destination enum for the transaction type you want to find
	 * @return a list of all relevant expenditure transactions
	 */
	public List<ExpenditureTransaction> getExpenditureTransactionsOfType(DESTINATION dest)
	{
		List<ExpenditureTransaction> transactions = new ArrayList<>();
		for (ExpenditureTransaction t : this.allExpenditureTransactions)
		{
			if (t.getDestination().equals(dest))
			{
				transactions.add(t);
			}
		}
		return transactions;
	}
	
	/**
	 * Removes the given income transaction from the month
	 *
	 * @param anIncomeTransaction
	 *            the income transaction to remove
	 * @return true if successful
	 */
	public boolean removeIncome(IncomeTransaction anIncomeTransaction)
	{
		return this.allIncomeTransactions.remove(anIncomeTransaction);
	}
	
	/**
	 * Removes the given expenditure transaction from the month
	 *
	 * @param anExpenditureTransaction
	 *            the expenditure transaction to remove
	 * @return true if successful.
	 */
	public boolean removeExpenditure(ExpenditureTransaction anExpenditureTransaction)
	{
		return this.allExpenditureTransactions.remove(anExpenditureTransaction);
	}
	
	/**
	 * Returns the total income for the month
	 *
	 * @return total income
	 */
	public float getTotalIncome()
	{
		float totals = 0.0f;
		for (IncomeTransaction t : this.allIncomeTransactions)
		{
			totals += t.getAmount();
		}
		return totals;
	}
	
	/**
	 * Returns the total expenditure for the month
	 *
	 * @return the expenditure
	 */
	public float getTotalExpenditure()
	{
		float totals = 0.0f;
		for (Transaction t : this.allExpenditureTransactions)
		{
			totals += t.getAmount();
		}
		return totals;
	}
	
	/**
	 * Returns the net spend for the month
	 *
	 * @return the net spend
	 */
	public float getNetSpend()
	{
		return this.getTotalIncome() - this.getTotalExpenditure();
	}
	
	/**
	 * Returns the total amount of money for each transaction type for that month
	 *
	 * @param source
	 *            the source type of the transaction
	 * @return total money gained
	 */
	public float getTotalIncomeForIncomeTransactionType(SOURCE source)
	{
		List<IncomeTransaction> incomes = this.getIncomeTransactionsOfType(source);
		float total = 0.0f;
		for (Transaction t : incomes)
		{
			total += t.getAmount();
		}
		return total;
	}
	
	/**
	 * Returns the total money spent in the month for the given destination
	 *
	 * @param destination
	 *            the destination of the transaction type you want to find money out
	 *            for
	 * @return the total money spent
	 */
	public float getTotalExpenditureForExpenditureTransactionType(DESTINATION destination)
	{
		List<ExpenditureTransaction> expends = this.getExpenditureTransactionsOfType(destination);
		float total = 0.0f;
		for (Transaction t : expends)
		{
			total += t.getAmount();
		}
		return total;
	}
	
}
