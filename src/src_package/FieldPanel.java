package src_package;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class FieldPanel extends JPanel implements NewFieldListener
{
	MinesweeperGameWithAI mineGame;
	ModButton[][] buttonField;
	int fieldX;
	int fieldY;
	int difficulty;
	
	// French numbers refer to the numbers in the field, while English numbers refer to the time display.
	static ImageIcon unpressed;
	static ImageIcon rien;
	static ImageIcon un;
	static ImageIcon deux;
	static ImageIcon trois;
	static ImageIcon quatre;
	static ImageIcon cinq;
	static ImageIcon six_2;
	static ImageIcon sept;
	static ImageIcon huit;
	static ImageIcon flag;
	static ImageIcon flag_wrong;
	static ImageIcon wrong_guess;
	static ImageIcon mine;
	static ImageIcon undiscovered;
	static ImageIcon reset;
	
	@Override public void newField()
	{
		mineGame.makeNewField(fieldX, fieldY, difficulty);
		
		for (int i = 0; i < buttonField.length; i++)
		{
			for (int n = 0; n < buttonField[0].length; n++)
			{
				if (FieldPanel.charToImageIcon(buttonField[i][n].game.revealedField[i][n]) != buttonField[i][n].getIcon())
				{
					buttonField[i][n].setIcon(FieldPanel.charToImageIcon(buttonField[i][n].game.revealedField[i][n]));
				}
			}
		}
	}
	
	public static Image makeImage(String path)
	{
		Image img = null;
		try
		{
			img = ImageIO.read(new File(path));
		}
		catch (IOException e)
		{
			System.out.println("Error");
		}
		
		return img;
	}
	
	public static ImageIcon charToImageIcon(char in)
	{
		ImageIcon out = null;
		
		if (in == '0' || in == '.')
		{
			out = rien;
		}
		else if (in == '1')
		{
			out = un;
		}
		else if (in == '2')
		{
			out = deux;
		}
		else if (in == '3')
		{
			out = trois;
		}
		else if (in == '4')
		{
			out = quatre;
		}
		else if (in == '5')
		{
			out = cinq;
		}
		else if (in == '6')
		{
			out = six_2;
		}
		else if (in == '7')
		{
			out = sept;
		}
		else if (in == '8')
		{
			out = huit;
		}
		else if (in == 'X')
		{
			out = wrong_guess;
		}
		else if (in == 'x')
		{
			out = flag_wrong;
		}
		else if (in == 'F')
		{
			out = flag;
		}
		else if (in == 'M')
		{
			out = mine;
		}
		else if (in == '?')
		{
			out = undiscovered;
		}
		else
		{
			out = null;
		}
		
		return out;
	}
	
	public Color multiplyColors(Color color1, Color color2)
	{
		float[] color1Components = color1.getRGBComponents(null);
		float[] color2Components = color2.getRGBColorComponents(null);
		float[] newComponents = new float[3];
		
		for (int i = 0; i < 3; i++)
			newComponents[i] = color1Components[i] * color2Components[i];
		
		return new Color(newComponents[0], newComponents[1], newComponents[2], color1Components[3]);
	}
	
	public Color probToColor(double prob)
	{
		if (prob >= 0.0)
		{
			int r = 0;
			int g = 0;
			int b = 0;
			double scalar = 0.0;
			
			if (prob == 1.0)
			{
				r = 255;
				g = 255;
				b = 255;
			}
			else if (prob > 0.0 && prob < 0.166667)
			{
				b = 255;
				scalar = -prob / 0.166667 + 1;
				r = (int) (255.0 * scalar);
			}
			else if (prob >= 0.166667 && prob < 0.333333)
			{
				b = 255;
				scalar = (prob - 0.166667) / 0.166667;
				g = (int) (255 * scalar);
			}
			else if (prob >= 0.333333 && prob < 0.5)
			{
				g = 255;
				scalar = -prob / 0.166667 + 3;
				b = (int) (255.0 * scalar);
			}
			else if (prob >= 0.5 && prob < 0.666667)
			{
				g = 255;
				scalar = (prob - 0.5) / 0.166667;
				r = (int) (255 * scalar);
			}
			else if (prob >= 0.666667 && prob < 0.833333)
			{
				r = 255;
				scalar = -prob / 0.166667 + 5;
				g = (int) (255.0 * scalar);
			}
			else if (prob >= 0.833333 && prob < 1.0)
			{
				r = 255;
				scalar = (prob - 0.833333) / 0.166667;
				g = (int) (255.0 * scalar);
				b = (int) (255.0 * scalar);
			}
			
			Color c = new Color(r, g, b);
			return c;
		}
		else
		{
			return null;
		}
	}
	
	public void colorField()
	{
		for (int i = 0; i < fieldY; i++)
		{
			for (int n = 0; n < fieldX; n++)
			{
				BufferedImage image = new BufferedImage(buttonField[i][n].getIcon().getIconWidth(), buttonField[i][n].getIcon().getIconHeight(), BufferedImage.TYPE_INT_RGB);
				Graphics gr = image.createGraphics();
				// paint the Icon to the BufferedImage.
				buttonField[i][n].getIcon().paintIcon(null, gr, 0, 0);
				double prob = mineGame.probField[i][n];
				gr.dispose();
				
				if (prob >= 0.0)
				{
					Color c = probToColor(prob);
					
					for (int y = 0; y < image.getHeight(); y++)
					{
						for (int x = 0; x < image.getWidth(); x++)
						{
							Color imageColor = new Color(image.getRGB(x, y));
							Color c2 = multiplyColors(c, imageColor);
							image.setRGB(x, y, c2.getRGB());
						}
					}
					
					ImageIcon newIcon = new ImageIcon(image);
					buttonField[i][n].setIcon(newIcon);
				}
			}
		}
	}
	
	public FieldPanel(int fieldx, int fieldy, int diff)
	{
		fieldX = fieldx;
		fieldY = fieldy;
		difficulty = diff;
		
		mineGame = new MinesweeperGameWithAI();
		mineGame.makeNewField(fieldx, fieldy, difficulty);
		buttonField = new ModButton[mineGame.masterField.length][mineGame.masterField[0].length];
		
		unpressed = new ImageIcon(makeImage("resources/undiscovered.png"));
		rien = new ImageIcon(makeImage("resources/rien.png"));
		un = new ImageIcon(makeImage("resources/un.png"));
		deux = new ImageIcon(makeImage("resources/deux.png"));
		trois = new ImageIcon(makeImage("resources/trois.png"));
		quatre = new ImageIcon(makeImage("resources/quatre.png"));
		cinq = new ImageIcon(makeImage("resources/cinq.png"));
		six_2 = new ImageIcon(makeImage("resources/six_2.png"));
		sept = new ImageIcon(makeImage("resources/sept.png"));
		huit = new ImageIcon(makeImage("resources/huit.png"));
		flag = new ImageIcon(makeImage("resources/flagged.png"));
		flag_wrong = new ImageIcon(makeImage("resources/flagged_wrong.png"));
		wrong_guess = new ImageIcon(makeImage("resources/wrong_guess.png"));
		mine = new ImageIcon(makeImage("resources/mine.png"));
		undiscovered = new ImageIcon(makeImage("resources/undiscovered.png"));
		reset = new ImageIcon(makeImage("resources/reset.png"));
		
		setLayout(new GridLayout(mineGame.masterField.length, mineGame.masterField[0].length));
		
		for (int i = 0; i < mineGame.masterField.length; i++)
		{
			for (int n = 0; n < mineGame.masterField[0].length; n++)
			{
				buttonField[i][n] = new ModButton(n, i, mineGame.masterField.length, mineGame);
				buttonField[i][n].setIcon(unpressed);
				buttonField[i][n].setPreferredSize(new Dimension(unpressed.getIconWidth(), unpressed.getIconHeight()));
				mineGame.addFieldChangeListener(buttonField[i][n]);
				add(buttonField[i][n]);
			}
		}
		setMaximumSize(new Dimension(unpressed.getIconWidth() * fieldx, unpressed.getIconHeight() * fieldy));
		setMinimumSize(new Dimension(unpressed.getIconWidth() * fieldx, unpressed.getIconHeight() * fieldy));
		
		setBorder(BorderFactory.createLoweredBevelBorder());
	}
}
