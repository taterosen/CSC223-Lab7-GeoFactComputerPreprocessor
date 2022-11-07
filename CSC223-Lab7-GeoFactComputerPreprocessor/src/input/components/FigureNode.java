package input.components;


import input.components.point.PointNodeDatabase;
import input.components.segment.SegmentNodeDatabase;
import input.visitor.ComponentNodeVisitor;

/**
 * A basic figure consists of points, segments, and an optional description
 * 
 * Each figure has distinct points and segments (thus unique database objects).
 * 
 */
public class FigureNode implements ComponentNode
{
	protected String              _description;
	protected PointNodeDatabase   _points;
	protected SegmentNodeDatabase _segments;

	public String              getDescription()    { return _description; }
	public PointNodeDatabase   getPointsDatabase() { return _points; }
	public SegmentNodeDatabase getSegments()       { return _segments; }
	
	public FigureNode(String description, PointNodeDatabase points, SegmentNodeDatabase segments)
	{
		_description = description;
		_points = points;
		_segments = segments;
	}
	
	@Override
	public Object accept(ComponentNodeVisitor visitor, Object o)
	{
		return visitor.visitFigureNode(this,  o);
	}
	
}