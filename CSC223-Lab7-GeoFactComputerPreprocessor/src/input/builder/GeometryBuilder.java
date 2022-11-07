/**
 * Performs the construction of the AST nodes from the information in the json for use by JSONParser.
 * 
 * @author taterosen
 * @date 10/11/2022
 */

package input.builder; 

import java.util.List;

import input.components.FigureNode;
import input.components.point.PointNode;
import input.components.point.PointNodeDatabase;
import input.components.segment.SegmentNode;
import input.components.segment.SegmentNodeDatabase;

public class GeometryBuilder extends DefaultBuilder
{
	@Override
	public FigureNode buildFigureNode(String description,
									  PointNodeDatabase points,
									  SegmentNodeDatabase segments)
	{ return new FigureNode(description, points, segments);	}
	
	@Override
	public SegmentNodeDatabase buildSegmentNodeDatabase()
    { return new SegmentNodeDatabase(); }
	
	@Override
	public void addSegmentToDatabase(SegmentNodeDatabase segments,
									 PointNode from,
									 PointNode to)
    { super.addSegmentToDatabase(segments, from, to); }
	
	@Override
	public SegmentNode buildSegmentNode(PointNode pt1, PointNode pt2)
    { return new SegmentNode(pt1, pt2); }

	@Override
	public PointNodeDatabase buildPointDatabaseNode(List<PointNode> points)
    { return new PointNodeDatabase(points); }
    
	@Override
    public PointNode buildPointNode(String name, double x, double y)
    { return new PointNode(name, x, y); }
}