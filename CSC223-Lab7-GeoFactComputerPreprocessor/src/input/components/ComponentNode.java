package input.components;

import input.visitor.ComponentNodeVisitor;

public interface ComponentNode
{
	/**
	 * General accept method to be overridden.
	 * @param visitor
	 * @param o
	 * @return Object
	 */
	Object accept(ComponentNodeVisitor visitor, Object o);
	
	/**
	 * Indents the number of times specified by a given int
	 * @param level the number of indents to have
	 * @return String of indentations
	 */
	default String indent(int level)
	{
		String indents = "";
		for(int i = 0; i < level; i++) 
		{
			indents = indents + "\t";
		}
		return indents;
	}
}