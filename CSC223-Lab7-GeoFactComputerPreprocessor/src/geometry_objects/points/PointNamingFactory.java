package geometry_objects.points;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/*
 * Given a pair of coordinates; generate a unique name for it;
 * return that point object.
 *
 * Names go from A..Z..AA..ZZ..AAA...ZZZ
 */
public class PointNamingFactory
{
	private static final String _PREFIX = "*_"; // Distinguishes generated names

	private static final char START_LETTER = 'A';
	private static final char END_LETTER = 'Z';

	private String _currentName = "A";
	private int _numLetters = 1;

	public static final String _ANONYMOUS = "__UNNAMED";

	//
	// A hashed container for the database of points;
	// This requires the Point class implement equals
	// based solely on the individual values and not a name
	// We need a get() method; HashSet doesn't offer one.
	// Each entry is a <Key, Value> pair where Key == Value
	//
	protected Map<Point, Point> _database;

	public PointNamingFactory()
	{
		this._database = new LinkedHashMap<Point,Point>();
	}

	/**
	 * 
	 * @param points -- a list of points, named or not named
	 */
	public PointNamingFactory(List<Point> points)
	{
		this();
		
		for(Point point: points) {
			this.put(point.getName(), point.getX(), point.getY());
		}

	}

	/**
	 * @param pt -- (x, y) coordinate pair object
	 * @return a point (if it already exists) or a completely new point that
	 *         has been added to the database
	 */
	public Point put(Point pt)
	{
		return this.lookupExisting(pt.getName(), pt.getX(), pt.getY());
	}

	/**
	 * @param x -- single coordinate
	 * @param y -- single coordinate
	 * @return a point (if it already exists) or a completely new point that has been added to the database
	 */
	public Point put(double x, double y)
	{
		return this.put(new Point(x,y));
	}

	/**
	 * @param name -- the name of the point 
	 * @param x -- single coordinate
	 * @param y -- single coordinate
	 * @return a point (if it already exists) or a completely new point that
	 *         has been added to the database.
	 *         
	 *         If the point is in the database and the name differs from what
	 *         is given, nothing in the database will be changed; essentially
	 *         this means we use the first name given for a point.
	 *         
	 *         The exception is that a valid name can overwrite an unnamed point.
	 */
	public Point put(String name, double x, double y)
	{
		return this.put(new Point(name,x,y));
	}    

	/**
	 * Strict access (read-only of the database)
	 * 
	 * @param x
	 * @param y
	 * @return stored database Object corresponding to (x, y) 
	 */
	public Point get(double x, double y)
	{
		return get(new Point(x,y));
	}	
	public Point get(Point pt)
	{
		return _database.get(pt);
	}

	/**
	 * @param name -- the name of the point 
	 * @param x -- single coordinate
	 * @param y -- single coordinate
	 * @return a point (if it already exists) or a completely new point that
	 *         has been added to the database.
	 *         
	 *         If the point is in the database and the name differs from what
	 *         is given, nothing in the database will be changed; essentially
	 *         this means we use the first name given for a point.
	 *         
	 *         The exception is that a valid name can overwrite an unnamed point.
	 */
	protected Point lookupExisting(String name, double x, double y)
	{
		Point existingPoint = this.get(x, y);
		// point doesn't exist yet
		if(existingPoint == null) 
		{
			if(name.equals(_ANONYMOUS)) name = getCurrentName();
			return createNewPoint(name,x,y);
		}

		// point already has non-automatic name
		if(!existingPoint.getName().startsWith(_PREFIX)) return existingPoint;

		// point has auto name, but no replacement name given
		if(name.equals(_ANONYMOUS)) return existingPoint;
		
		// point has auto name and replacement was given
		return createNewPoint(name,x,y);
	}  

	/**
	 * @param name -- the name of the point 
	 * @param x -- single coordinate
	 * @param y -- single coordinate
	 * @return a point (if it already exists) or a completely new point that
	 *         has been added to the database.
	 * 
	 *         Called by lookupExisting and only creates and puts a point
	 */
	protected Point createNewPoint(String name, double x, double y)
	{
		Point point = new Point(name,x,y);

		_database.put(point, point);

		return point;
	}

	/**
	 * @param x -- single coordinate
	 * @param y -- single coordinate
	 * @return simple containment; no updating
	 */
	public boolean contains(double x, double y) {

		return this.contains(new Point(x,y));
	}
	public boolean contains(Point p) {

		return this.get(p) != null;
	}
	/**
	 * @return acquires and returns the next name in sequence; also
	 * generates the next name in a 'lazy list' manner 
	 */
	protected String getCurrentName()
	{
		String name = this._currentName;
		this.updateName();
		return _PREFIX + name;
	}

	/**
	 * Advances the current generated name to the next letter in the alphabet:
	 * 'A' -> 'B' -> 'C' -> 'Z' --> 'AA' -> 'BB'
	 */
	protected void updateName()
	{
		//gets only first character and integer representation of it
		char firstChar = this._currentName.charAt(0);
		
		int intRep = firstChar;

		//stores next integer representation of letter
		int nextIntRep = intRep + 1;

		//if current letter is Z then it wraps around to the front again
		if (intRep == END_LETTER) {
			nextIntRep = 'A';
			_numLetters++;
		}
		//if only 1 letter then just regular next letter
		if(_numLetters==1) {
			
			_currentName = Character.toString((char) nextIntRep);
			
		}
		//if more than one letter in the currentName then it loops through that num of times
		else {
			_currentName = "";
			for (int i = 0; i < _numLetters; i++) {
				
				_currentName = _currentName+Character.toString((char) nextIntRep);
			}
		}
		

	}

	/**
	 * @return The entire database of points.
	 */
	public Set<Point> getAllPoints()
	{

		Set<Point> points = new HashSet<Point>();

		//loop through entire database to return set of points
		for(Entry<Point,Point> entry: _database.entrySet()) {
			points.add(entry.getKey());
		}

		return points;
	}

	public void clear() { _database.clear(); }
	
	public int size() { return _database.size(); }

	@Override
	public String toString()
	{

		Set<Point> points = this.getAllPoints();
		String returnString = "";

		for(Point point: points) {
			returnString += point.toString() + ", ";
		}

		returnString = returnString.substring(0, returnString.length()-2);

		return returnString;
	}
}