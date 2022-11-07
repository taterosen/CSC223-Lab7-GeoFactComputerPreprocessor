package input.components.point;

import org.json.JSONException;

import input.components.ComponentNode;
import input.exception.ParseException;
import input.parser.JSON_Constants;
import input.visitor.ComponentNodeVisitor;
import utilities.math.MathUtilities;

/**
 * A 2D Point (x, y).
 * 
 * @author taterosen
 * @date 08/31/2022
 */

public class PointNode implements ComponentNode, Comparable
{
	
	protected static final String ANONYMOUS = "__UNNAMED";

	protected double _x;
	protected double _y;
	protected String _name;

	public double getX() {
		return this._x;
	}
	
	public double getY() {
		return this._y;
	}
	
	public String getName() {
		return _name;
	}

	/**
	 * Create a new Point with the specified coordinates.
	 * 
	 * @param x The X coordinate
	 * @param y The Y coordinate
	 */
	public PointNode(double x, double y) {
		this(ANONYMOUS, x, y);
	}
	

	/**
	 * Create a new Point with the specified coordinates.
	 * 
	 * @param name -- The name of the point. (Assigned by the UI)
	 * @param x -- The X coordinate
	 * @param y -- The Y coordinate
	 */
	public PointNode(String name, double x, double y) {
		this._name = name;
		this._x = x;
		this._y = y;
	}

	@Override
	public int hashCode() {
		final int prime = 37;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(MathUtilities.removeLessEpsilon(this.getX()));
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(MathUtilities.removeLessEpsilon(this.getY()));
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	/**
	 * Compare this with a given object for equality; returns true
	 * if they are equal, false otherwise.
	 * @param obj
	 */
	@Override
	public boolean equals(Object obj) {
		//check if obj is null or of different class
		if (obj == null) return false;
		if (obj.getClass() != this.getClass()) return false;
		
		PointNode objAsPointNode = (PointNode) obj;
		//check obj address and coordinates
		if (this == obj) return true; 
		if (MathUtilities.doubleEquals(this.getX(),objAsPointNode.getX()) && 
			MathUtilities.doubleEquals(this.getY(),objAsPointNode.getY())) return true;
		return false;
	}

	/**
	 * Turns this point node into a string and returns it.
	 */
	@Override
	public String toString() {
		return this.getName() + "(" + this.getX() + ", " + this.getY() + ")";
	}
	
	@Override
	public Object accept(ComponentNodeVisitor visitor, Object o)
	{
		return visitor.visitPointNode(this,  o);
	}

	@Override
	public int compareTo(Object o)
	{	
		return this._name.compareTo(((PointNode) o).getName());
	}
}