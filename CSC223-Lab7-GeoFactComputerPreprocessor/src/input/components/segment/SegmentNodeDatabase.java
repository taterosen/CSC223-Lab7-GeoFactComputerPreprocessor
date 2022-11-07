package input.components.segment;

import input.components.ComponentNode;
import input.components.point.PointNode;
import input.visitor.ComponentNodeVisitor;

import java.util.*;
import java.util.Map.Entry;

/**
 * Stores line segments as an adjacency list.
 *
 * @author brycenaddison, taterosen, georgelamb
 * @date Wed Aug 31 2022
 */
public class SegmentNodeDatabase implements ComponentNode
{
    protected Map<PointNode, Set<PointNode>> _adjLists;

    /**
     * Create a new empty SegmentNodeDatabase.
     */
    public SegmentNodeDatabase() {
        this._adjLists = new TreeMap<>();
    }
    
    public Map<PointNode, Set<PointNode>> getAdjList() { return _adjLists; }

    /**
     * Create a new SegmentNode database from a map of points and adjacency lists.
     *
     * @param adjLists A map of adjacency lists to create the database from
     */
    public SegmentNodeDatabase(Map<PointNode, Set<PointNode>> adjLists)
    {
        this._adjLists = adjLists;
        //sortByKey();
    }

    /**
     * @return the number of edges in the SegmentNodeDatabase
     */
    public int numUndirectedEdges() {
        int count = 0;
        for (Set<PointNode> adjList : this._adjLists.values()) {
            count += adjList.size();
        }
        return count / 2;
    }

    /**
     * Add an edge going in one direction. Ex: For a segment AB, add the vector AB and
     * not the vector BA.
     * 
     * 10/18 changed for ToJSONvisitor
     *
     * @param a the key point
     * @param b the point to add to the key point's adjacency list
     */
    public void addDirectedEdge(PointNode a, PointNode b) {
        Set<PointNode> adjList = this._adjLists.computeIfAbsent(a, k -> new TreeSet<>());
        adjList.add(b);
        //sortByKey();
    }

    /**
     * Add an edge going in both directions. Ex: For a segment AB, add both the vectors
     * AB and BA.
     *
     * @param a the first point in the edge
     * @param b the other point in the edge
     */
    public void addUndirectedEdge(PointNode a, PointNode b) {
        this.addDirectedEdge(a, b);
        this.addDirectedEdge(b, a);
    }

    /**
     * Adds segments to the database based on an adjacency list
     *
     * @param p    a point to add
     * @param list a list of points to create segments with
     */
    public void addAdjacencyList(PointNode p, List<PointNode> list) {
        for (PointNode q : list) {
            this.addUndirectedEdge(p, q);
        }
    }

    /**
     * Returns a list of SegmentNode objects based on the connections in the
     * database. Segments in both directions are returned: say for a connection AB,
     * both SegmentNode AB and BA are returned in the list.
     *
     * @return a list of SegmentNode objects created from the adjacency lists in the
     * database.
     */
    public List<SegmentNode> asSegmentList() {
        ArrayList<SegmentNode> list = new ArrayList<>();
        for (Entry<PointNode, Set<PointNode>> entry : this._adjLists.entrySet()) {
            PointNode a = entry.getKey();
            for (PointNode b : entry.getValue()) {
                list.add(new SegmentNode(a, b));

            }
        }
        return list;
    }

    /**
     * Returns a list of SegmentNode objects based on the connections in the
     * database. Segments in one direction are returned: say for a connection AB,
     * only one of segment AB or BA are returned.
     *
     * @return a list of SegmentNode objects created from the adjacency lists in the
     * database.
     */
    public List<SegmentNode> asUniqueSegmentList() {
        Set<SegmentNode> set = new TreeSet<>(this.asSegmentList());
        return new ArrayList<>(set);
    }
	
	@Override
	public Object accept(ComponentNodeVisitor visitor, Object o)
	{
		return visitor.visitSegmentNodeDatabase(this,  o);
	}
}
