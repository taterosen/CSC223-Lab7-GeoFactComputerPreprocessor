package input;

//import static org.junit.jupiter.api.Assertions.*; ????

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import geometry_objects.points.Point;
import geometry_objects.points.PointDatabase;
import geometry_objects.points.PointNamingFactory;
import geometry_objects.Segment;
import input.builder.GeometryBuilder;
import input.components.ComponentNode;
import input.components.FigureNode;
import input.components.point.PointNode;
import input.components.segment.SegmentNode;
import input.parser.JSONParser;

public class InputFacade
{
	/**
	 * Acquire a figure from the given JSON file.
     *
	 * @param filename -- the name of a file
	 * @return a FigureNode object corresponding to the input file.
	 */
	public static FigureNode extractFigure(String filename)
	{
		JSONParser parser = new JSONParser();
		return (FigureNode)parser.parse(filename.toString());
	}
	
	/**
	 * 1) Read in a figure from a JSON file.
	 * 2) Convert the PointNode and SegmentNode objects to a Point and Segment objects 
	 *    (those classes have more meaningful, geometric functionality).
     *
	 * @param filename
	 * @return a pair <set of points as a database, set of segments>
	 */
	public static Map.Entry<PointDatabase, Set<Segment>> toGeometryRepresentation(String filename)
	{
		FigureNode figure = extractFigure(filename);
		
		//change PointNodes in PointNodeDatabase into Points, then add to PointDatabase
		PointDatabase pointData = new PointDatabase();
		
		for(PointNode p: figure.getPointsDatabase().getPoints()) {
			pointData.put(p.getName(),p.getX(),p.getY());
		}
		
		//change SegmentNodes into Segments, then add to a LinkedHashSet
		Set<Segment> segments = new LinkedHashSet<Segment>();
		for(SegmentNode seg: figure.getSegments().asSegmentList()) {
			PointNode pointNode1 = seg.getPoint1();
			PointNode pointNode2 = seg.getPoint2();
			
			Point point1 = new Point(pointNode1.getName(),pointNode1.getX(),pointNode1.getY());
			Point point2 = new Point(pointNode2.getName(),pointNode2.getX(),pointNode2.getY());
			
			Segment segment = new Segment(point1,point2);
			segments.add(segment);
		}
		
		return new AbstractMap.SimpleEntry<PointDatabase, Set<Segment>>(pointData, segments);

	}
}
