package input.components.segment;

import input.components.ComponentNode;
import input.components.point.PointNode;
import input.visitor.ComponentNodeVisitor;

/**
 * A utility class only for representing ONE segment
 * 
 * @author brycenaddison
 * @date Wed Aug 31 2022
 */
public class SegmentNode implements ComponentNode{
	protected PointNode _point1;
	protected PointNode _point2;

	public PointNode getPoint1() {
		return _point1;
	}

	public PointNode getPoint2() {
		return _point2;
	}

	/**
	 * Create a new SegmentNode with the specified point nodes.
	 * 
	 * @param pt1 -- The point 1 coordinate
	 * @param pt2 -- The point 2 coordinate
	 */
	public SegmentNode(PointNode pt1, PointNode pt2) {
		_point1 = pt1;
		_point2 = pt2;
	}

	/**
	 * Compare this with a given object for equality; returns true
	 * if they are equal, false otherwise.
	 * @param obj
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof SegmentNode)) {
			return false;
		}

		SegmentNode s = (SegmentNode) obj;

		return ((this._point1.equals(s._point1) && this._point2.equals(s._point2))
				|| (this._point1.equals(s._point2) && this._point2.equals(s._point1)));
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash *= 31 * this._point1.hashCode();
		hash *= 37 * this._point2.hashCode();
		return hash;
	}

	/**
	 * Turns this segment node into a string and returns it.
	 */
	@Override
	public String toString() {
		return String.format("<%s, %s>", this._point1.toString(), this._point2.toString());
	}
	
	@Override
	public Object accept(ComponentNodeVisitor visitor, Object o)
	{
		return visitor.visitSegmentNode(this,  o);
	}

}
