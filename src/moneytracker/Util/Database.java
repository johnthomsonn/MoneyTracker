package moneytracker.Util;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;

import moneytracker.core.internal.ExpenditureTransaction;
import moneytracker.core.internal.ExpenditureTransaction.DESTINATION;
import moneytracker.core.internal.IncomeTransaction;
import moneytracker.core.internal.IncomeTransaction.SOURCE;
import moneytracker.core.internal.Month;
import moneytracker.core.internal.Transaction;
import moneytracker.core.internal.User;

public class Database
{
	private static Connection connect = null;
	private static Statement statement = null;
	private static PreparedStatement preparedStatement = null;
	private static ResultSet resultSet = null;
	
	public static void open()
	{
		
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			connect = DriverManager.getConnection("jdbc:mysql://localhost/tm470?" + "user=root&password=Mestalla1");
		}
		catch (SQLException | ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testReadFromTest() throws Exception
	{
		try
		{
			
			statement = connect.createStatement();
			
			resultSet = statement.executeQuery("select * from test");
			
			writeResultSetForTest(resultSet);
		}
		catch (final Exception e)
		{
			System.out.println(e.getMessage());
		}
		finally
		{
			
		}
	}
	
	public static void writeResultSetForTest(ResultSet rs) throws SQLException
	{
		while (rs.next())
		{
			final String n = rs.getString("name");
			System.out.println(n);
		}
	}
	
	/**
	 * Closes the conncetion to the database
	 */
	public static void close()
	{
		try
		{
			if (resultSet != null)
			{
				resultSet.close();
			}
			
			if (statement != null)
			{
				statement.close();
			}
			
			if (connect != null)
			{
				connect.close();
			}
		}
		catch (final Exception e)
		{
			System.out.println("[Database.close] " + e.getMessage());
		}
	}
	
	/**
	 * Inserts a new user into the database
	 * @param newUser the newly created user
	 */
	public static void insertNewUser(User newUser)
	{
		try
		{
			open();
			preparedStatement = connect.prepareStatement("INSERT INTO users (user_id, name, balance, password) VALUES (?,?,?,?)");
			
			preparedStatement.setInt(1, newUser.getID());
			preparedStatement.setString(2, newUser.getName());
			preparedStatement.setString(4, String.copyValueOf(newUser.getpassword()));
			preparedStatement.setBigDecimal(3, BigDecimal.valueOf(newUser.getBalance()));
			
			preparedStatement.execute();
			
		}
		catch (final SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			close();
		}
	}
	
	/**
	 * Returns a ResultSet containing all the users i in the database
	 * @return all users
	 */
	public static ResultSet getAllUsers()
	{
		resultSet = null;
		try
		{
			statement = connect.createStatement();
			resultSet = statement.executeQuery("Select * FROM users");
		}
		catch (final Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		finally
		{
			
		}
		return resultSet;
	}
	
	public static ResultSet getAllMonths()
	{
		resultSet = null;
		try
		{
			statement = connect.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM months");
		}
		catch (final Exception e)
		{
			System.out.println("[Database.getAllMonths] " + e.getMessage());
		}
		return resultSet;
	}
	
	public static void addMonth(Month aMonth)
	{
		try
		{
			preparedStatement = connect.prepareStatement("INSERT INTO months (id,name,number,year) VALUES (?,?,?,?)");
			preparedStatement.setString(2, aMonth.getName());
			preparedStatement.setInt(1, aMonth.getID());
			preparedStatement.setInt(3, aMonth.getNumber());
			preparedStatement.setInt(4, aMonth.getYear());
			preparedStatement.execute();
		}
		catch (final SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Inserts a  transaction into the database
	 * @param aUser the user that made the transaction
	 * @param anAmount the amount of the transaction
	 * @param where where the money came or went to
	 * @param aDay the day of the month of the transaction
	 * @param aMonth the month of the transaction
	 * @param aSOURCE the SOURCE of the transaction if it is an Income Transaction
	 * @param aDesintaion the DESTINATION of the transaction if expenditure transaction
	 * @param transactionType the type whether it is Income or Expenditure
	 */
	public static void insertTransaction(User aUser, float anAmount, String where, int aDay, Month aMonth, SOURCE aSource, DESTINATION aDestination, Transaction.TYPE transactionType)
	{
		open();
		preparedStatement = null;
		resultSet = null;
		final String type = Transaction.TransactionTypeToString(transactionType);
		String source = null;
		String destination = null;
		String stringDate = dateToString(aDay, aMonth);
		if(transactionType == Transaction.TYPE.INCOME) {
			source = IncomeTransaction.SourceToString(aSource);
		}
		else if(transactionType == Transaction.TYPE.EXPENDITURE)
		{
			destination = ExpenditureTransaction.DstinationToString(aDestination);
		}
		// final java.sql.Date sqlDate = new java.sql.Date(aMonth.getYear(),
		// aMonth.getNumber(), aDay);
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(aMonth.getYear(), aMonth.getNumber(), aDay, 1, 1, 1);
		try
		{
			
			preparedStatement = connect.prepareStatement("INSERT INTO transactions (user_id, amount, date,where_string,type,source,destination,month_id) VALUES (?,?,?,?,?,?,?,?)");
			
			preparedStatement.setInt(1, aUser.getID());
			preparedStatement.setFloat(2, anAmount);
			preparedStatement.setString(3, stringDate);
			preparedStatement.setString(4, where);
			preparedStatement.setString(5, type);
			preparedStatement.setString(6, source);
			preparedStatement.setString(7, destination);
			preparedStatement.setInt(8, getMonthID(aMonth));
			preparedStatement.executeUpdate();
			
		}
		catch (final SQLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Debug("Database.insertTransaction error: " + e.getMessage());
		}
		finally
		{
			close();
		}
	}
	
	public static int getMonthID(Month aMonth)
	{
		int id = -1;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Connection c = null;
		try
		{
			c = DriverManager.getConnection("jdbc:mysql://localhost/tm470?" + "user=root&password=Mestalla1");
			ps = c.prepareStatement("SELECT * FROM months WHERE number = ? AND year = ?");
			ps.setInt(1, aMonth.getNumber());
			ps.setInt(2, aMonth.getYear());
			rs = ps.executeQuery();
			rs.next();
			id = rs.getInt("id");
		}
		catch(final Exception e)
		{
			System.out.println("[database.getMonthID] " + e.getMessage());
		}
		return id;
	}
	
	/**
	 * Fetches and returns all transactions for the given user
	 * @param aUser the user
	 * @return all transactions
	 */
	public static ResultSet getAllTransactionsForUser(User aUser)
	{
		resultSet = null;
		try
		{
			preparedStatement = connect.prepareStatement("SELECT * FROM transactions WHERE user_id = ?");
			preparedStatement.setInt(1, aUser.getID());
			resultSet = preparedStatement.executeQuery();
		}
		catch (final Exception e)
		{
			System.out.println("[database.getAlltransactionsForUser] " + e.getMessage());
			File.logError("Database", "getAllTransactionsForUser", e.getMessage());
		}
		return resultSet;
	}
	
	public static ResultSet getMonthDataFromID(int id)
	{
		ResultSet resultSett = null;
		PreparedStatement ps = null;
		Connection c = null;
		try
		{
			c = DriverManager.getConnection("jdbc:mysql://localhost/tm470?" + "user=root&password=Mestalla1");
			ps = c.prepareStatement("SELECT * FROM months WHERE id = ?");
			ps.setInt(1, id);
			resultSett = ps.executeQuery();
		}
		catch (final Exception e)
		{
			System.out.println("database.getMonthFromID " + e.getMessage());
		}
		finally
		{
		}
		return resultSett;
	}

	public static void truncateUsersTable()
	{
		open();
		// TODO Auto-generated method stub
		try
		{
			statement = connect.createStatement();
			statement.execute("TRUNCATE users");
		}
		catch(final Exception e)
		{
			System.out.println(e.getMessage());
		}
		finally
		{
			close();
		}
	}
	
	public static void Debug(String msg)
	{
		System.out.println(msg);
	}
	
	public static void updateUserBalance(User aUser)
	{
		open();
		try
		{
			preparedStatement = connect.prepareStatement("UPDATE users SET balance = ? WHERE user_id = ?");
			preparedStatement.setFloat(1,aUser.getBalance());
			preparedStatement.setInt(2, aUser.getID());
			preparedStatement.execute();
		}
		catch (Exception e)
		{
			System.out.println("[database.updateUserBalance] " + e.getMessage());
		}
		finally
		{
			close();
		}
	}

	public static void removeExpenditureTransaction(ExpenditureTransaction t, User u, Month m)
	{
		removeTransaction(t.getTransactionID(), u.getID(), getMonthID(m));
	}
	
	public static void removeIncomeTransaction(IncomeTransaction t, User u, Month m)
	{
		removeTransaction(t.getTransactionID(), u.getID(), getMonthID(m));
	}

	private static void removeTransaction(int transactionID, int userID, int monthID)
	{
		try
		{
			open();
			preparedStatement = connect.prepareStatement("DELETE FROM transactions WHERE id = ? AND user_id = ? AND month_id = ?");
			preparedStatement.setInt(1, transactionID);
			preparedStatement.setInt(2, userID);
			preparedStatement.setInt(3, monthID);
			preparedStatement.execute();
		}
		catch (Exception e)
		{
			System.out.println("Database.removeTransaction" + e.getMessage());
			File.logError("Database", "removeTransaction", e.getMessage());
		}
		finally
		{
			try
			{
				close();
			}
			catch (Exception ex)
			{
				System.out.println("Database.removeTransaction.exError:" + ex.getMessage());
			}
		}
	}
	
	public static int getNumberOfTransactions()
	{
		open();
		int rows = 0;
		try
		{
			preparedStatement = connect.prepareStatement("SELECT COUNT(*) FROM transactions");
			resultSet = preparedStatement.executeQuery();
			resultSet.next();
			rows = resultSet.getInt(1);
		}
		catch (Exception e)
		{
			System.out.println("Database.getNumberOfTransactions " + e.getMessage());
			File.logError("Database", "getNumberOfTransactions", e.getMessage());
		}
		finally
		{
			close();
		}
		return rows;
	}

	private static String dateToString(int aDay, Month aMonth)
	{
		return aMonth.getYear() + "-" + aMonth.getNumber() + "-" + aDay;
	}
	
}
