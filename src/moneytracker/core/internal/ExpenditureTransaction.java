package moneytracker.core.internal;

import java.time.LocalDate;

public class ExpenditureTransaction extends Transaction
{
	
	public enum DESTINATION
	{
		Rent, Bills, Petrol, FoodShopping, Treat, Unnecessary, OtherOK
	};
	
	private DESTINATION destination;
	
	/**
	 * Creates anew Expenditure Transaction with the given values
	 * @param anAmount 	the amount of the transaction
	 * @param where		where the money went to
	 * @param aDate		the date of the transaction
	 * @param aDestination the destination of the money
	 */
	public ExpenditureTransaction(float anAmount, String where, LocalDate aDate, DESTINATION aDestination)
	{
		super.amount = anAmount;
		super.date = aDate;
		super.where = where;
		this.destination = aDestination;
		super.transactionID = Transaction.globalID++;
		super.type = TYPE.EXPENDITURE;
	}
	
	/**
	 * Creates anew Expenditure Transaction with the given values
	 * @param anAmount 	the amount of the transaction
	 * @param where		where the money went to
	 * @param aDate		the date of the transaction
	 * @param aDestination the destination of the money
	 * @param anID		the uniqueID of the transaction
	 */
	public ExpenditureTransaction(float anAmount, String where, LocalDate aDate, DESTINATION aDestination, int anID)
	{
		super.amount = anAmount;
		super.date = aDate;
		super.where = where;
		this.destination = aDestination;
		super.transactionID = anID;
		Transaction.globalID = anID + 1;
		super.type = TYPE.EXPENDITURE;
	}
	
	/**
	 * Returns the destination of the transaction
	 * @return destination
	 */
	public DESTINATION getDestination()
	{
		return this.destination;
	}
	
	public static String DstinationToString(DESTINATION destination)
	{
		String s = null;
		switch(destination)
		{
			case Rent :
			{
				s = "rent";
				break;
			}
			case Bills:
			{
				s = "bills";
				break;
			}
			case Petrol:
			{
				s = "petrol";
				break;
			}
			case FoodShopping:
			{
				s = "foodshopping";
				break;
			}
			case Treat:
			{
				s = "treat";
				break;
			}
			case Unnecessary:
			{
				s = "unnecessary";
				break;
			}
			case OtherOK:
			{
				s = "other ok";
				break;
			}
		}
		return s;
	}
	
	public static DESTINATION StringToDestination(String string)
	{
		DESTINATION d = null;
		switch(string)
		{
			case "rent" :
			{
				d = DESTINATION.Rent;
				break;
			}
			case "bills":
			{
				d = DESTINATION.Bills;
				break;
			}
			case "petrol" :
			{
				d = DESTINATION.Petrol;
				break;
			}
			case "foodshopping" :
			{
				d = DESTINATION.FoodShopping;
				break;
			}
			case "unnecessary":
			{
				d = DESTINATION.Unnecessary;
				break;
			}
			case "treat":
			{
				d = DESTINATION.Treat;
				break;
			}
			case "other ok":
			{
				d = DESTINATION.OtherOK;
				break;
			}
		}
		return d;
	}

	@Override
	public String toString()
	{
		return "£" + this.getAmount() + " to " + this.getWhere() + " on the " + super.dateToString();
	}
	
	
}
