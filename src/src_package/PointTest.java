package src_package;

import java.awt.Point;
import java.util.ArrayList;

public class PointTest
{
	Point p1;
	Point p2;
	
	public PointTest()
	{
		p1 = new Point(1, 1);
		p2 = new Point(1, 1);
	}
	
	public static void main(String[] args)
	{
		ArrayList<Point> plist = new ArrayList<Point>();
		PointTest pt = new PointTest();
		plist.add(pt.p1);
		System.out.println(plist.contains(pt.p2));
	}
}
