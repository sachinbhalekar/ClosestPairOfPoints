import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

/**
 * @author sachin Bhalekar
 * 
 *         Given  an  instance  ofClosestPairOfPointsdetermine  minimum  distance  between  any  2 
 *         points  with  aO(nlg(n)) time algorithm.
 *         Input Format:The input file will be called input2.txtnand be in the same directory as the java and class files. 
 *         Each line will be two integer values 
 *         separated by whitespace which represents the X and Y values of the points.
 *         Output: A single number which is the square of the distance between the closest pair of points.
 */

public class Project2 {


	
	/**
	 * @author sachin
	 *
	 */
	
	public static class Point{
		int x,y;

		/**
		 * @param x
		 * @param y
		 */
		public Point(int x, int y)
		{
			this.x=x;
			this.y=y;
		}

		/**
		 * @param distFromPoint
		 * @return
		 */
		public long getDistanceSqr(Point distFromPoint)
		{
			long distSqr= (this.x- distFromPoint.x)*(this.x- distFromPoint.x) + 
					(this.y- distFromPoint.y)*(this.y- distFromPoint.y);

			return distSqr;
		}


	}


	// Class to compare Point by X
	public static class SortByX implements Comparator<Point>
	{


		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(Point arg0, Point arg1) {
			// TODO Auto-generated method stub
			return arg0.x-arg1.x;
		}
	}

	// Class to compare Point by y
	public static class SortByY implements Comparator<Point>
	{


		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		@Override
		public int compare(Point arg0, Point arg1) {
			// TODO Auto-generated method stub
			return arg0.y-arg1.y;
		}
	}



	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Point> arrPointsSortedX=new ArrayList<>();
		ArrayList<Point> arrPointsSortedY=new ArrayList<>();
		try {
			File file = new File("input2.txt");
			Scanner sc = new Scanner(file);
			
			//Fetch all points and store in arrPoints Arraylist
		
			
			while(sc.hasNext())
			{
				Point currentPoint=new Project2.Point(sc.nextInt(),sc.nextInt());
				arrPointsSortedX.add(currentPoint);
				arrPointsSortedY.add(currentPoint);

			}

			Collections.sort(arrPointsSortedX,new Project2.SortByX());
			Collections.sort(arrPointsSortedY,new Project2.SortByY());

			//Print all the points received as input
			//printPoints(arrPointsSortedX);
			//printPoints(arrPointsSortedY);

			//find closest pair distance
			long closestPairDist=closestPairDistanceRecursive(arrPointsSortedX,arrPointsSortedY,0,arrPointsSortedX.size()-1);
			System.out.println(closestPairDist);
			// Close the input Scanner
			sc.close();


		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}


	
	/**
	 * @param arrPointsSortedX
	 * @param arrPointsSortedY
	 * @param first
	 * @param last
	 * @return
	 * This recursive method divides the points into two halves and recursively calculate distance between closest set 
	 * of points in each half.
	 */
	public static long closestPairDistanceRecursive(ArrayList<Point> arrPointsSortedX,ArrayList<Point> arrPointsSortedY, int first, int last )
	{
		long closestPairDist=0;

		if(first==last)
		{
			closestPairDist=Long.MAX_VALUE;

		}
		else if(first==last-1)
		{
			closestPairDist= arrPointsSortedX.get(first).getDistanceSqr(arrPointsSortedX.get(last));

		}
		else
		{

			int divideIndex=(first+last)/2;

			long distLeft=closestPairDistanceRecursive(arrPointsSortedX,arrPointsSortedY,first,divideIndex);

			long distRight=closestPairDistanceRecursive(arrPointsSortedX,arrPointsSortedY,divideIndex+1,last);

			closestPairDist=distLeft<distRight?distLeft:distRight;


			closestPairDist=getClosestPairDistInStrip(arrPointsSortedX,arrPointsSortedY,closestPairDist,divideIndex);

		}


		return closestPairDist;

	}

	/**
	 * @param arrPointsSortedX
	 * @param arrPointsSortedY
	 * @param currentClosestDist
	 * @param indexStripMid
	 * @return
	 * This method finds the minimum distance betwwen points that line in the strip between two regions
	 */
	public static long getClosestPairDistInStrip(ArrayList<Point> arrPointsSortedX,ArrayList<Point> arrPointsSortedY,long currentClosestDist ,int indexStripMid)
	{

		ArrayList<Point> arrPointsInStripSortedY=new ArrayList<>();
		Point midPoint=arrPointsSortedX.get(indexStripMid);

		// Find points at distance currentClosestDist from the midPoint forming a strip. Store all such points in arrPointsInStripSortedY.
		//These points in arrPointsInStripSortedY will be sorted on Y coordinates since the loop is running on points in
		//arrPointsSortedY which is already sorted on Y
		for(int i=0;i<arrPointsSortedY.size();i++)
		{
			int xDist=Math.abs(midPoint.x-arrPointsSortedY.get(i).x);
			if(xDist<Math.sqrt(currentClosestDist))
			{
				arrPointsInStripSortedY.add(arrPointsSortedY.get(i));
			}
		}



		//Compare every point currPoint1 in strip with other points currPoint2 such that the other point i.e. currPoint2
		// has Y coordinate with a difference of less than currentClosestDist from the Y coordinate of currPoint1 
		for(int i=0;i<arrPointsInStripSortedY.size();i++)
		{
			Point currPoint1=arrPointsInStripSortedY.get(i);
			//This loop will not be executed more than constant (7) number of times
			for(int j=i+1;j<arrPointsInStripSortedY.size()&& (arrPointsInStripSortedY.get(j).y- currPoint1.y)<Math.sqrt(currentClosestDist);j++)
			{
				Point currPoint2=arrPointsInStripSortedY.get(j);
				if(currPoint1.getDistanceSqr(currPoint2)<currentClosestDist)
				{
					currentClosestDist=currPoint1.getDistanceSqr(currPoint2);
				}
			}
		}


		return currentClosestDist;
	}


	//Method to print all the point passed in ArrayList arrPoints
	/**
	 * @param arrPoints
	 */
	public static void printPoints(ArrayList<Point> arrPoints)
	{
		for(int i=0;i<arrPoints.size();i++)
		{
			Point currPoint=arrPoints.get(i);
			System.out.println("The POINT is ("+currPoint.x+" , "+currPoint.y+")");
		}
	}


}
