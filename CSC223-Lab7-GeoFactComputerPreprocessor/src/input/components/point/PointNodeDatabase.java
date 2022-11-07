package input.components.point;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import input.components.ComponentNode;
import input.visitor.ComponentNodeVisitor;
import utilities.math.MathUtilities;

/**
 * Stores Point Nodes in LinkedHashSet
 * @author georgelamb, taterosen, brycenaddison
 * @date Wed September 7 2022
 */
public class PointNodeDatabase implements ComponentNode {
	protected Set<PointNode> _points;

	/**
	 * create empty PointNodeDataBase
	 */
	public PointNodeDatabase() {

		_points = new TreeSet<PointNode>();

	}
	
	public Set<PointNode> getPoints(){
		return _points;
	}
	
	/**
	 * create PointNodeDatabase with list of point nodes in it
	 * @param list of PointNodes 
	 */
	public PointNodeDatabase(List<PointNode> list) {

		_points = new TreeSet<PointNode>(list);

	}
	
	/**
	 * @param node to put into database
	 */
	public void put(PointNode node) {
		_points.add(node);
	}

	/**
	 * 
	 * @param node to check 
	 * @return if node is contained
	 */
	public boolean contains(PointNode node) {
		return this.getPoint(node) != null;

	}
	
	/**
	 * 
	 * @param x value of node
	 * @param y value of node
	 * @return if node is contained
	 */
	public boolean contains(double x, double y) {
		return this.getPoint(x, y) != null;
	}
	
	/**
	 * 
	 * @param node to get name of
	 * @return name of node
	 */
	public String getName(PointNode node) {
		return this.getPoint(node).getName();
	}
	
	/**
	 * 
	 * @param x value of node to get name of 
	 * @param y value of node to get name of 
	 * @return name of node
	 */
	public String getName(double x, double y) {
		return this.getPoint(x, y).getName();

	}
	
	/**
	 * 
	 * @param node to get node of 
	 * @return node to get
	 */
	public PointNode getPoint(PointNode node) {
		return this.getPoint(node.getX(), node.getY());
	}

	/**
	 * 
	 * @param x value to get node of
	 * @param y value to get node of
	 * @return node to get
	 */
	public PointNode getPoint(double x, double y) {
		for (PointNode p: this._points) {
			if (MathUtilities.doubleEquals(x,p.getX()) && MathUtilities.doubleEquals(y, p.getY())) {
				return p;
			}
		}
		return null;

	}
	
	/**
	 * 
	 * @param x value to get node of
	 * @param y value to get node of
	 * @return node to get
	 */
	public PointNode getPoint(String name) {
		for (PointNode p: this._points) {
			if (p._name.equals(name)) return p;
		}
		return null;

	}

	@Override
	public Object accept(ComponentNodeVisitor visitor, Object o)
	{
		return visitor.visitPointNodeDatabase(this,  o);
	}

}
