package src_package;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

public class FileTest
{
	public static void main(String[] args) throws Exception
	{
		int c = 1;
		Date d = new Date();
		String date = "" + d.getTime();
		File f = new File("tests/chain_tests/" + date + "/" + c + ".txt");
		f.getParentFile().mkdirs();
		try
		{
			f.createNewFile();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		PrintStream out = new PrintStream(new FileOutputStream(f));
		System.setOut(out);
		System.out.println("This is a test.");
		
		/*
		 * PrintWriter writer = new PrintWriter(f); writer.println("The first line"); writer.println("The second line"); writer.close();
		 */
	}
	
}
