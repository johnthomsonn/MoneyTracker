package moneytracker.core.internal;

import java.time.LocalDate;

public abstract class Transaction
{
	public static enum TYPE
	{
		INCOME, EXPENDITURE
	};

	float amount;
	// Date date;
	LocalDate date;
	String where;
	protected int transactionID;
	int userID;
	TYPE type;

	protected static int globalID = 1;

	/**
	 * Returns the amount of the transaction
	 * @return amount
	 */
	public float getAmount()
	{
		return new Float(this.amount);
	}

	/**
	 * Returns the date of the transaction
	 * @return date
	 */
	public LocalDate getDate()
	{
		return LocalDate.of(this.date.getYear(), this.date.getMonth(), this.date.getDayOfMonth());
	}

	/**
	 * Returns where the transaction came from or went to
	 * @return where the money came or went
	 */
	public String getWhere()
	{
		return new String(this.where);
	}

	@Override
	public String toString()
	{
		return "$$" + this.getAmount() + "  " + this.getWhere() + " on " + this.getDate().getDayOfMonth() + this.getSuffixForDate();
	}

	public String getSuffixForDate()
	{
		String tmp = "";
		int day = this.getDate().getDayOfMonth();
		if (day == 1 || day == 21 || day == 31)
			tmp = "st";
		else if (day == 2 || day == 22)
			tmp = "nd";
		else if (day == 3 || day == 23)
			tmp = "rd";
		else
			tmp = "th";
		return tmp;
	}

	public static String TransactionTypeToString(TYPE type)
	{
		String s = null;
		switch (type)
		{
			case INCOME:
			{
				s = "income";
				break;
			}
			case EXPENDITURE:
			{
				s = "expenditure";
				break;
			}
		}
		return s;
	}

	public static TYPE StringToTransactionType(String string)
	{
		TYPE t = null;
		switch (string)
		{
			case "income":
			{
				t = TYPE.INCOME;
				break;
			}
			case "expenditure":
			{
				t = TYPE.EXPENDITURE;
				break;
			}
		}
		return t;
	}
	
	public void setID(int anID)
	{
		this.transactionID = anID;
	}

	public int getTransactionID()
	{
		return this.transactionID;
	}

	protected String dateToString()
	{
		return this.date.getDayOfMonth() + this.getSuffixForDate();
	}
	
}
