package src_package;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.SwingWorker;

public class BatchTester
{
	ArrayList<PrintStream> outputList;
	ArrayList<SwingWorker<Void, Integer>> workerList;
	int count;
	int batchSize = 80;
	
	public class ChainTestWorker extends SwingWorker<Void, Integer>
	{
		int streamNum;
		String date;
		
		@Override protected Void doInBackground() throws Exception
		{
			System.out.println("Worker #" + streamNum + " is working.");
			
			for (int i = 0; i < batchSize; i++)
			{
				File f = new File("tests/chain_tests/" + date + "/" + count + ".txt");
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
				outputList.add(out);
				System.setOut(out);
				
				Chain.main(null);
				count++;
			}
			
			return null;
		}
		
		@Override protected void process(List<Integer> chunks)
		{
			
		}
		
		@Override protected void done()
		{
			System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
			System.out.println("Worker #" + streamNum + " is done.");
		}
		
		public ChainTestWorker(int n, String d)
		{
			streamNum = n;
			System.out.println("Worker #" + n + " was made.");
			date = d;
		}
	}
	
	public void runChainTests() throws IOException
	{
		Date now = new Date();
		String d = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.ENGLISH).format(now);
		
		ChainTestWorker ctw = new ChainTestWorker(0, d);
		workerList.add(ctw);
		workerList.get(0).execute();
	}
	
	public BatchTester()
	{
		outputList = new ArrayList<PrintStream>();
		workerList = new ArrayList<SwingWorker<Void, Integer>>();
	}
	
	public static void main(String[] args) throws Exception
	{
		BatchTester bt = new BatchTester();
		bt.runChainTests();
		
		try
		{
			int sleepTime = (int) (bt.batchSize / 20) * 10000;
			Thread.sleep(sleepTime);
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}
}
