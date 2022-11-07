/**
 * Populates a StringBuilder from AST nodes
 * 
 * @author Regan Richardson, Tate Rosen, Hanna King
 * @date 10/12/2022
 */
package input.visitor;

import java.util.AbstractMap;

import input.components.*;
import input.components.point.*;
import input.components.segment.SegmentNode;
import input.components.segment.SegmentNodeDatabase;
import utilities.io.StringUtilities;

public class UnparseVisitor implements ComponentNodeVisitor
{
	/**
	 * Populates string object to unparse FigureNodes as we visit
	 */
	@Override
	public Object visitFigureNode(FigureNode node, Object o)
	{
		// Unpack the input object containing a Stringbuilder and an indentation level
		@SuppressWarnings("unchecked")
		AbstractMap.SimpleEntry<StringBuilder, Integer> pair = (AbstractMap.SimpleEntry<StringBuilder, Integer>)(o);
		StringBuilder sb = pair.getKey();
		int level = pair.getValue();
		
		//create an indent for figure
		sb.append(StringUtilities.indent(level) + "Figure \n");

		sb.append(StringUtilities.indent(level) + "{\n");

		//create a new object that takes string builder object that is 1 level in
		AbstractMap.SimpleEntry<StringBuilder, Integer> newPair = new AbstractMap.SimpleEntry<>(sb, level +1);
		
        // unparse description
		sb.append(StringUtilities.indent(level +1));
		sb.append("Description : \"" + node.getDescription() + "\"");
		
		//indent
		sb.append(StringUtilities.indent(level) + "\n");
		
		//calls visitPointsDatabase
		node.getPointsDatabase().accept(this, newPair);
		
		//calls visitSegments
		node.getSegments().accept(this, newPair);
		
		sb.append(StringUtilities.indent(level) + "}\n");
		
		return null;
       
	}

	/**
	 * Populates string builder with segment information
	 */
	@Override
	public Object visitSegmentNodeDatabase(SegmentNodeDatabase node, Object o)
	{
		// Unpack the input object containing a Stringbuilder and an indentation level
		@SuppressWarnings("unchecked")
		AbstractMap.SimpleEntry<StringBuilder, Integer> pair = (AbstractMap.SimpleEntry<StringBuilder, Integer>)(o);
		StringBuilder sb = pair.getKey();
		int level = pair.getValue();
				
		sb.append(StringUtilities.indent(level) + "Segments:\n" + StringUtilities.indent(level) + "{\n");
		
		//for each node, get the nodes that form segments with it
		for(PointNode p : node.getAdjList().keySet())
		{
			sb.append(StringUtilities.indent(level + 1) + p.getName() + " : ");
				
			//append all the nodes that form a segment with p to string builder
			for(PointNode val : node.getAdjList().get(p))
			{
					sb.append(val.getName() + "  ");
			}
				
			sb.append("\n");
		}

		sb.append(StringUtilities.indent(level) + "}\n");
		
        return null;
	}

	/**
	 * This method should NOT be called since the segment database
	 * uses the Adjacency list representation
	 */
	@Override
	public Object visitSegmentNode(SegmentNode node, Object o) { return null; }

	/**
	 * Indent's and adds "points" to string
	 */
	@Override
	public Object visitPointNodeDatabase(PointNodeDatabase node, Object o)
	{
		// Unpack the input object containing a Stringbuilder and an indentation level
		@SuppressWarnings("unchecked")
		AbstractMap.SimpleEntry<StringBuilder, Integer> pair = (AbstractMap.SimpleEntry<StringBuilder, Integer>)(o);
		StringBuilder sb = pair.getKey();
		int level = pair.getValue();
		
		sb.append(StringUtilities.indent(level) + "Points:\n" + StringUtilities.indent(level) +  "{\n");
		
		//Create new string builder 1 indent in to send to visitPointNode
		AbstractMap.SimpleEntry<StringBuilder, Integer> newPair = new AbstractMap.SimpleEntry<>(sb, level +1);
		
		for(PointNode p : node.getPoints())
		{
			p.accept(this, newPair);
		}
		
		sb.append(StringUtilities.indent(level) + "}\n");
		
        return null;
	}
	
	/**
	 * Adds points to string
	 */
	@Override
	public Object visitPointNode(PointNode node, Object o)
	{
		// Unpack the input object containing a Stringbuilder and an indentation level
		@SuppressWarnings("unchecked")
		AbstractMap.SimpleEntry<StringBuilder, Integer> pair = (AbstractMap.SimpleEntry<StringBuilder, Integer>)(o);
		StringBuilder sb = pair.getKey();
		int level = pair.getValue();
				
		sb.append(StringUtilities.indent(level) +"Point(" + node.getName() + ")(" + node.getX() + 
		          ", " + node.getY() + ")\n");
        
        return null;
	}
}