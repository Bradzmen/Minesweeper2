package src_package;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

public class TopPanel extends JPanel
{
	JButton flagToggle;
	JButton analysisButton; // Have it be a toggle pressed/not pressed. Every press, flip pressedIcon and nonpressedIcon.
	JButton infoButton;
	FaceButton resetButton;
	TimerClock clock;
	MineCounter mineCount;
	ActionListener toggleListener;
	JPanel empty1;
	JPanel empty2;
	JButton settingsButton;
	static boolean flagMode;
	
	static ImageIcon settings;
	static ImageIcon info;
	static ImageIcon analysis;
	
	public TopPanel()
	{
		settings = new ImageIcon(FieldPanel.makeImage("resources/settings.png"));
		info = new ImageIcon(FieldPanel.makeImage("resources/info.png"));
		analysis = new ImageIcon(FieldPanel.makeImage("resources/analysis.png"));
		
		setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		
		int temp = FieldPanel.unpressed.getIconWidth();
		
		empty1 = new JPanel();
		add(empty1);
		
		infoButton = new JButton(info);
		infoButton.setPreferredSize(new Dimension(temp, temp));
		add(infoButton);
		
		flagToggle = new JButton(FieldPanel.mine);
		flagMode = false;
		toggleListener = new ActionListener()
		{
			@Override public void actionPerformed(ActionEvent arg0)
			{
				if (flagMode)
				{
					flagToggle.setIcon(FieldPanel.mine);
					flagMode = false;
				}
				else
				{
					flagToggle.setIcon(FieldPanel.flag);
					flagMode = true;
				}
			}
		};
		flagToggle.addActionListener(toggleListener);
		flagToggle.setRolloverEnabled(false);
		flagToggle.setPreferredSize(new Dimension(temp, temp));
		add(flagToggle);
		
		resetButton = new FaceButton();
		resetButton.setRolloverEnabled(false);
		resetButton.setPreferredSize(new Dimension(FaceButton.happy.getIconHeight(), FaceButton.happy.getIconWidth()));
		add(resetButton);
		
		settingsButton = new JButton(settings);
		settingsButton.setPreferredSize(new Dimension(temp, temp));
		add(settingsButton);
		
		analysisButton = new JButton(analysis);
		analysisButton.setPreferredSize(new Dimension(temp, temp));
		add(analysisButton);
		
		empty2 = new JPanel();
		add(empty2);
		
		clock = new TimerClock();
		add(clock);
		
		mineCount = new MineCounter();
		add(mineCount, 0);
		
		setBorder(BorderFactory.createLoweredBevelBorder());
	}
}
