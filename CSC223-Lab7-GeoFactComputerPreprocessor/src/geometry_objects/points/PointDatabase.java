package geometry_objects.points;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import utilities.math.MathUtilities;

/**
 * This class represents a bi-directional database of points.
 * 
 * We can lookup a point given:
 *   (a) coordinates --> name
 *   (b) name --> coordinates
 * 
 * This is a Decorator class with the Point Factory in the background
 * 
 * @author xxx
 */
public class PointDatabase
{
	// The factory is the central means of representing all
    // points in a figure
    protected PointNamingFactory _factory;

    public Set<Point> getPoints() { return _factory.getAllPoints(); }
    
	public PointDatabase()
	{
        this(new ArrayList<Point>());
	}

	public PointDatabase(List<Point> points)
	{
        this._factory = new PointNamingFactory(points);
	}

	public int size() { return _factory.size(); }
	
	/**
	 * Add this point to the database.
	 *   If the point with the given coordinates are not in the database, we add it (as is)
	 *   If the point is in the database:
	 *      * If the new point defines a name (and old is unnamed), we overwrite
	 *      * If the new point is unnamed, we do not overwrite
     *        the name (this is a do-nothing operation)
	 *     
	 * @param pt 
	 */
	public void put(String name, double x, double y)
	{
		if(name != null) this._factory.put(name, x, y);
	}

	/**
	 * Given raw coordinates of a point, determine if it is named.
	 * 
	 * @param x,y -- doubles defining a point (x,y)
	 * @return a string corresponding to that point, if it is named.
	 */
	public String getName(double x, double y)
	{
		return this.getName(new Point(x , y));
	}
	
	public String getName(Point pt)
	{
		return this._factory.get(pt).getName();
	}

	/**
	 * Given the name of a point, determine the coordinates.
	 * 
	 * @param name -- a String name
	 * @return an (x,y) corresponding to name, if it has been defined.
	 */
	public Point getPoint(String name)
	{
		Set<Point> points = this._factory.getAllPoints();
		
		for (Point p : points) {
			if(name.equals(p.getName())) return p;
		}
		
		return null;	
	}

	/**
	 * Given a point, acquire the stored database object; facilitates
	 * singular objects and mitigates lingering copies of points.
	 * 
	 * @param pt -- a basic point
	 * @return the database entry for the point
	 */
	public Point getPoint(Point pt)
	{
		return this._factory.get(pt);
	}

	/**
	 * Given a raw point (x, y), acquire the stored database object .
	 * 
	 * @param x,y -- doubles defining a point (x,y)
	 * @return a string corresponding to that point, if it is named.
	 */
	public Point getPoint(double x, double y)
	{
		return this.getPoint(new Point(x,y));
	}

	@Override
	public String toString() {
		return this._factory.toString();
	}
}