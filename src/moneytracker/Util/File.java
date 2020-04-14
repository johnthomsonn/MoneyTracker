package moneytracker.Util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class File
{
	private static final String errorFolder = "Output";
	private static final String errorOutputFile = "Output/ErrorLog.txt";
	private static BufferedWriter bufferedWriter = null;
	
	private static java.io.File errorLogFile;
	
	private static Date date = null;
	
	private static SimpleDateFormat simpleDateFormat = null;
	
	public static void CreateOutputErrorFile()
	{
		simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		checkIfOutputFolderExists(errorFolder);
		checkOutputFileExists(errorOutputFile);
		errorLogFile = new java.io.File(errorOutputFile);
	}
	
	/**
	 * Outputs an error message and source of the error to the error log
	 * @param errorPath the path eg ClassName.Method
	 * @param msg the error message
	 */
	public static void logError(String className, String methodName, String errorMessage)
	{
		try
		{
			date = new Date(System.currentTimeMillis());
			String formattedDate = simpleDateFormat.format(date);
			bufferedWriter = new BufferedWriter(new FileWriter(errorLogFile, true));
			bufferedWriter.write("[" + formattedDate + "]" + "[" + className + "::" + methodName + "] " + errorMessage);
			bufferedWriter.newLine();
		}
		catch (Exception e)
		{
			System.out.println(e.getMessage());
		}
		finally
		{
			try
			{
				bufferedWriter.close();
			}
			catch (Exception e)
			{
				System.out.println(e.getMessage());
			}
		}
	}
	
	private static boolean checkIfOutputFolderExists(String folderPath)
	{
		java.io.File folder = new java.io.File(errorFolder);
		boolean exists = false;
		if (folder.exists())
		{
			exists = true;
		}
		else
		{
			folder.mkdir();
			exists = true;
		}
		return exists;
	}
	
	private static boolean checkOutputFileExists(String path)
	{
		java.io.File theFile = new java.io.File(path);
		boolean exists = false;
		if (theFile.exists())
		{
			exists = true;
			theFile.delete();
		}
		try
		{
			theFile.createNewFile();
			exists = true;
		}
		catch (IOException e)
		{
			System.out.println("Failed to create the File: " + path + ". ERROR: ");
			e.printStackTrace();
			exists = false;
		}

		return exists;
	}
	
}
