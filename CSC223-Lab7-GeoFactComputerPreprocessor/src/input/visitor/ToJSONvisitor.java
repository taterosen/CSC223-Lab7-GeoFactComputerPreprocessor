/**
 * A visitor that will convert the AST of a geometry figure back into a JSONObject.
 * implements ComponentNodeVisitor
 * 
 * @author Hanna King, Tate Rosen, Regan Richardson
 * @date 10/16/2022
 */

package input.visitor;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONObject;

import input.components.FigureNode;
import input.components.point.PointNode;
import input.components.point.PointNodeDatabase;
import input.components.segment.SegmentNode;
import input.components.segment.SegmentNodeDatabase;
import input.parser.JSON_Constants;

public class ToJSONvisitor implements ComponentNodeVisitor
{
	/**
	 * visit the figure node, converting it and the entire figure (description, points, and segments)
	 * into a JSONObject
	 * @return Object JSONObject representing the entire figure
	 * @param node FigureNode
	 * @param o
	 */
	@Override
	public Object visitFigureNode(FigureNode node, Object o)
	{
		// create JSONObject
		JSONObject figure = new JSONObject();
		
		JSONObject figureVal = new JSONObject();
		
		// get each of the three parts of a figure node:
		// 	   description, points, segments
		// as JSONObjects
		figureVal.put(JSON_Constants.JSON_DESCRIPTION, node.getDescription());
		
		figureVal.put(JSON_Constants.JSON_POINT_S, visitPointNodeDatabase(node.getPointsDatabase(), figureVal));
		
		figureVal.put(JSON_Constants.JSON_SEGMENTS, visitSegmentNodeDatabase(node.getSegments(), figureVal));
		
		figure.put(JSON_Constants.JSON_FIGURE, figureVal);
		
		return figure;
	}

	/**
	 * visit the SegmentNodeDatabase node, converting it into a JSONArray
	 * @return Object JSONArray representing the data in the SegmentNodeDatabase
	 * @param node SegmentNodeDatabase to convert to JSONArray
	 * @param o
	 */
	@Override
	public Object visitSegmentNodeDatabase(SegmentNodeDatabase node, Object o)
	{
		JSONArray jsonSegments = new JSONArray();
		
		// over the list of points in the segment node database
		for (PointNode keyPoint : node.getAdjList().keySet())
		{
			// over the list of points that point is connected to 
			TreeSet<String> valuePoints = new TreeSet<String>();
			for (PointNode valuePoint : node.getAdjList().get(keyPoint))
			{
				valuePoints.add(valuePoint.getName());
			}
			
			JSONObject oneSegmentAdj = new JSONObject();
			oneSegmentAdj.put(keyPoint.getName(), valuePoints);
			
			jsonSegments.put(oneSegmentAdj);
		}
		return jsonSegments;
	}

	/**
	 * should NOT be visited, as segment data is handled in visitSegmentNodeDatabase
	 * @return null Object
	 */
	@Override
	public Object visitSegmentNode(SegmentNode node, Object o) {
		return null;
	}

	/**
	 * visit the PointNode node, converting it into a JSONObject
	 * The JSONObject contains the labels for and the three data in a PointNode: name, x, and y
	 * @return Object JSONObject representing the data in the PointNode
	 * @param node PointNode to convert to JSONObject
	 * @param o 
	 */
	@Override
	public Object visitPointNode(PointNode node, Object o)
	{
		// create JSONObject
		JSONObject point = new JSONObject();
		
		// get data from PointNode:
		//    name, x, and y
		// and fill JSONObject
		point.put(JSON_Constants.JSON_NAME, node.getName());
		point.put(JSON_Constants.JSON_X, node.getX());
		point.put(JSON_Constants.JSON_Y, node.getY());
		
		return point;
	}

	/**
	 * visit the PointNodeDatabase node, converting it into a JSONArray
	 * @return Object JSONObject representing the data in the PointNodeDatabase
	 * @param node PointNodeDatabase to convert to JSONArray
	 * @param o
	 */
	@Override
	public Object visitPointNodeDatabase(PointNodeDatabase node, Object o)
	{
		JSONArray points = new JSONArray();
		
		for (PointNode point : node.getPoints())
		{
			points.put((JSONObject) visitPointNode(point, points));
		}
		
		return points;
	}
}