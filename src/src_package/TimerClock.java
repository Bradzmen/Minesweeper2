package src_package;

import java.awt.GridLayout;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class TimerClock extends JPanel implements FieldChangeListener, NewFieldListener
{
	Timer timer;
	TimerTask task;
	int count;
	JPanel display;
	JLabel digit1;
	JLabel digit2;
	JLabel digit3;
	static boolean running = false;
	
	static ImageIcon zero;
	static ImageIcon one;
	static ImageIcon two;
	static ImageIcon three;
	static ImageIcon four;
	static ImageIcon five;
	static ImageIcon six;
	static ImageIcon seven;
	static ImageIcon eight;
	static ImageIcon nine;
	static ImageIcon nil;
	
	@Override public void newField()
	{
		digit1.setIcon(zero);
		digit2.setIcon(zero);
		digit3.setIcon(zero);
		timer.cancel();
		running = false;
	}
	
	@Override public void fieldChanged(MinesweeperGame msg)
	{
		if (!running && !MinesweeperGame.ended)
		{
			running = true;
			startTimer();
		}
		else if (MinesweeperGame.ended || msg.finished)
		{
			timer.cancel();
			timer.purge();
			running = false;
		}
	}
	
	public static ImageIcon charToIcon(char c)
	{
		if (c == '0')
		{
			return zero;
		}
		else if (c == '1')
		{
			return one;
		}
		else if (c == '2')
		{
			return two;
		}
		else if (c == '3')
		{
			return three;
		}
		else if (c == '4')
		{
			return four;
		}
		else if (c == '5')
		{
			return five;
		}
		else if (c == '6')
		{
			return six;
		}
		else if (c == '7')
		{
			return seven;
		}
		else if (c == '8')
		{
			return eight;
		}
		else if (c == '9')
		{
			return nine;
		}
		else if (c == '-')
		{
			return nil;
		}
		else
		{
			return null;
		}
	}
	
	public void startTimer()
	{
		count = 0;
		digit1.setIcon(zero);
		digit2.setIcon(zero);
		digit3.setIcon(zero);
		running = true;
		
		task = new TimerTask()
		{
			@Override public void run()
			{
				if (count < 1000)
				{
					String time = Integer.toString(count);
					
					if (time.length() == 1)
					{
						digit1.setIcon(charToIcon(time.charAt(0)));
					}
					else if (time.length() == 2)
					{
						digit2.setIcon(charToIcon(time.charAt(0)));
						digit1.setIcon(charToIcon(time.charAt(1)));
					}
					else if (time.length() == 3)
					{
						digit3.setIcon(charToIcon(time.charAt(0)));
						digit2.setIcon(charToIcon(time.charAt(1)));
						digit1.setIcon(charToIcon(time.charAt(2)));
					}
				}
				
				count++;
			}
		};
		
		timer = new Timer();
		timer.scheduleAtFixedRate(task, 0, 1000);
	}
	
	public TimerClock()
	{
		zero = new ImageIcon(FieldPanel.makeImage("resources/zero.png"));
		one = new ImageIcon(FieldPanel.makeImage("resources/one.png"));
		two = new ImageIcon(FieldPanel.makeImage("resources/two.png"));
		three = new ImageIcon(FieldPanel.makeImage("resources/three.png"));
		four = new ImageIcon(FieldPanel.makeImage("resources/four.png"));
		five = new ImageIcon(FieldPanel.makeImage("resources/five.png"));
		six = new ImageIcon(FieldPanel.makeImage("resources/six.png"));
		seven = new ImageIcon(FieldPanel.makeImage("resources/seven.png"));
		eight = new ImageIcon(FieldPanel.makeImage("resources/eight.png"));
		nine = new ImageIcon(FieldPanel.makeImage("resources/nine.png"));
		nil = new ImageIcon(FieldPanel.makeImage("resources/nil.png"));
		
		digit1 = new JLabel(zero);
		digit2 = new JLabel(zero);
		digit3 = new JLabel(zero);
		
		setLayout(new GridLayout(1, 3, 0, 0));
		add(digit3);
		add(digit2);
		add(digit1);
		
		count = 0;
		timer = new Timer();
		
		task = new TimerTask()
		{
			@Override public void run()
			{
				if (count < 1000)
				{
					String time = Integer.toString(count);
					
					if (time.length() == 1)
					{
						digit1.setIcon(charToIcon(time.charAt(0)));
					}
					else if (time.length() == 2)
					{
						digit2.setIcon(charToIcon(time.charAt(0)));
						digit1.setIcon(charToIcon(time.charAt(1)));
					}
					else if (time.length() == 3)
					{
						digit3.setIcon(charToIcon(time.charAt(0)));
						digit2.setIcon(charToIcon(time.charAt(1)));
						digit1.setIcon(charToIcon(time.charAt(2)));
					}
				}
				
				count++;
			}
		};
	}
	
	@Override public void fieldChangedWithXY(int x, int y)
	{
		// TODO Auto-generated method stub
		
	}
}
