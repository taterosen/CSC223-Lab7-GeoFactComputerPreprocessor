/*
 * A Builder design pattern for constructing a geometry hierarchy.
 * 
 * The default builder allows JSON file parsing without constructing
 * the corresponding hierarchy, saving effort.
 */

package input.builder;

import java.util.List;

import input.components.*;
import input.components.point.*;
import input.components.segment.SegmentNode;
import input.components.segment.SegmentNodeDatabase;


public class DefaultBuilder
{
	public DefaultBuilder() { }

    public FigureNode buildFigureNode(String description,
    		                          PointNodeDatabase points,
    		                          SegmentNodeDatabase segments)
    {
        return null;
    }
    
    public SegmentNodeDatabase buildSegmentNodeDatabase()
    {
        return null;
    }
    
    public void addSegmentToDatabase(SegmentNodeDatabase segments, PointNode from, PointNode to)
    {
    	if (segments == null) return;
    	if (from.getName().compareTo(to.getName()) < 0) segments.addDirectedEdge(from, to);
    	else segments.addDirectedEdge(to,  from);
    }
    
    public SegmentNode buildSegmentNode(PointNode pt1, PointNode pt2)
    {
        return null;
    }
    
    public PointNodeDatabase buildPointDatabaseNode(List<PointNode> points)
    {
        return null;
    }
    
    public PointNode buildPointNode(String name, double x, double y)
    {
        return null;
    }
}
