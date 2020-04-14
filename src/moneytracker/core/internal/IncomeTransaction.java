package moneytracker.core.internal;

import java.time.LocalDate;

public class IncomeTransaction extends Transaction
{
	
	public enum SOURCE
	{
		Work, Other, Interest, Bonus
	};
	
	private SOURCE source;
	
	/**
	 * Creates anew IncomeTransaction object with the given values
	 * @param anAmount	the amount of the transaction
	 * @param where 	where the income money came from
	 * @param aDate		the date of the transaction
	 * @param aSource	the source of the income
	 */
	public IncomeTransaction(float anAmount, String where, LocalDate aDate, SOURCE aSource)
	{
		super.amount = anAmount;
		super.where = where;
		super.date = aDate;
		this.source = aSource;
		super.transactionID = Transaction.globalID++;
		super.type = TYPE.INCOME;
	}
	
	/**
	 * Creates anew IncomeTransaction object with the given values
	 * @param anAmount	the amount of the transaction
	 * @param where 	where the income money came from
	 * @param aDate		the date of the transaction
	 * @param aSource	the source of the income
	 * @param anID		the unique ID of the transaction
	 */
	public IncomeTransaction(float anAmount, String where, LocalDate aDate, SOURCE aSource, int anID)
	{
		super.amount = anAmount;
		super.where = where;
		super.date = aDate;
		this.source = aSource;
		super.transactionID = anID;
		super.type = TYPE.INCOME;
		Transaction.globalID = anID + 1;
	}
	
	/**
	 * Returns the source of the IncomeTransaction object
	 * @return source
	 */
	public SOURCE getSource()
	{
		return this.source;
	}
	
	@Override
	public String toString()
	{
		return "£" + this.getAmount() + " from " + this.getWhere() + " on the " + super.dateToString();
	}
	
	public static String SourceToString(SOURCE source)
	{
		String s = null;
		switch(source)
		{
			case Work:
			{
				s = "work";
				break;
			}
			case Other:
			{
				s = "other";
				break;
			}
			case Interest :
			{
				s = "interest";
				break;
			}
			case Bonus:
			{
				s = "bonus";
				break;
			}
		}
		return s;
	}
	
	public static SOURCE StringToSource(String string)
	{
		SOURCE s = null;
		switch(string) {
			case "work":
			{
				s = SOURCE.Work;
				break;
			}
			case "other":
			{
				s = SOURCE.Other;
				break;
			}
			case "interest":
			{
				s = SOURCE.Interest;
				break;
			}
			case "bonus":
			{
				s = SOURCE.Bonus;
				break;
			}
		}
		return s;
	}
	
}
