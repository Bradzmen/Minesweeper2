package src_package;

import javax.swing.JFrame;

public class ColorTest extends JFrame
{
	public static void main(String[] args)
	{
		ColorTest ct = new ColorTest();
		FieldPanel fp = new FieldPanel(20, 20, 2);
		ct.add(fp);
		/*
		 * Random r = new Random();
		 * for (int i = 0; i < fp.fieldY; i++)
		 * {
		 * for (int n = 0; n < fp.fieldX; n++)
		 * {
		 * double d = r.nextDouble();
		 * fp.mineGame.probField[i][n] = d;
		 * }
		 * }
		 */
		
		int counter = 0;
		
		for (int i = 0; i < fp.fieldY; i++)
		{
			for (int n = 0; n < fp.fieldX; n++)
			{
				double d = (double) counter / 400;
				System.out.println(d + " @ (" + n + ", " + i + ")");
				fp.mineGame.probField[i][n] = d;
				counter++;
			}
		}
		
		fp.colorField();
		ct.pack();
		ct.setLocationRelativeTo(null);
		ct.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ct.setTitle("ColorTest");
		ct.setVisible(true);
	}
}
