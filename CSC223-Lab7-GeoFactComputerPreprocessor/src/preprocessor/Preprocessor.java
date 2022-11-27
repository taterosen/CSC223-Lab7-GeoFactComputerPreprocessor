package preprocessor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import geometry_objects.points.Point;
import geometry_objects.points.PointDatabase;
import preprocessor.delegates.ImplicitPointPreprocessor;
import geometry_objects.Segment;

public class Preprocessor
{
	// The explicit points provided to us by the user.
	// This database will also be modified to include the implicit
	// points (i.e., all points in the figure).
	protected PointDatabase _pointDatabase;

	// Minimal ('Base') segments provided by the user
	protected Set<Segment> _givenSegments;

	// The set of implicitly defined points caused by segments
	// at implicit points.
	protected Set<Point> _implicitPoints;

	// The set of implicitly defined segments resulting from implicit points.
	protected Set<Segment> _implicitSegments;

	// Given all explicit and implicit points, we have a set of
	// segments that contain no other subsegments; these are minimal ('base') segments
	// That is, minimal segments uniquely define the figure.
	protected Set<Segment> _allMinimalSegments;

	// A collection of non-basic segments
	protected Set<Segment> _nonMinimalSegments;

	// A collection of all possible segments: maximal, minimal, and everything in between
	// For lookup capability, we use a map; each <key, value> has the same segment object
	// That is, key == value. 
	protected Map<Segment, Segment> _segmentDatabase;
	public Map<Segment, Segment> getAllSegments() { return _segmentDatabase; }

	public Preprocessor(PointDatabase points, Set<Segment> segments)
	{
		_pointDatabase  = points;
		_givenSegments = segments;

		_segmentDatabase = new HashMap<Segment, Segment>();

		analyze();
	}

	/**
	 * Invoke the precomputation procedure.
	 */
	public void analyze()
	{
		//
		// Implicit Points
		//
		_implicitPoints = ImplicitPointPreprocessor.compute(_pointDatabase, new ArrayList<Segment>(_givenSegments));

		//
		// Implicit Segments attributed to implicit points
		//
		_implicitSegments = computeImplicitBaseSegments(_implicitPoints);

		//
		// Combine the given minimal segments and implicit segments into a true set of minimal segments
		//     *givenSegments may not be minimal
		//     * implicitSegmen
		//
		_allMinimalSegments = identifyAllMinimalSegments(_implicitPoints, _givenSegments, _implicitSegments);

		//
		// Construct all segments inductively from the base segments
		//
		_nonMinimalSegments = constructAllNonMinimalSegments(_allMinimalSegments);

		//
		// Combine minimal and non-minimal into one package: our database
		//
		_allMinimalSegments.forEach((segment) -> _segmentDatabase.put(segment, segment));
		_nonMinimalSegments.forEach((segment) -> _segmentDatabase.put(segment, segment));
	}

	/**
	 * Gets all the implicit base segments in a figure based on given implicit points
	 * ex:
	 * 		A---------(X)---------E -> returned implicit base segments: AX, XE
	 * 
	 * 
	 * @param implicitPoints
	 * @return a set of implicit base segments
	 */
	protected Set<Segment> computeImplicitBaseSegments(Set<Point> implicitPoints){

		Set<Segment> segments = new LinkedHashSet<Segment>();

		for(Segment seg: _givenSegments) {
			//get the internal points on current segment and add the endpoints
			SortedSet<Point> pointsOn = seg.collectOrderedPointsOnSegment(implicitPoints);
			pointsOn.add(seg.getPoint1());
			pointsOn.add(seg.getPoint2());

			//get each implicit segment using set of points that lie on the current segment
			Set<Segment> segmentsToAdd = getAllSegments(pointsOn);
			segments.addAll(segmentsToAdd);
		}

		return segments;
	}

	/**
	 * Creates a list of all segments using a given set of points
	 * if there are more than just the two endpoints.
	 * ex:
	 * 		points: A,X,E -> returned segments: AX, XE
	 * @param points
	 * @return a set of segments
	 */
	protected Set<Segment> getAllSegments(SortedSet<Point> points){
		if(points.size() <= 2) return new LinkedHashSet<Segment>();

		//put points in a list and create set of segments
		List<Point> pointsAsList = new ArrayList<Point>(points);
		
		//loop through each point and create segment with its neighbor
		Set<Segment> segmentsToReturn = new LinkedHashSet<Segment>();

		for(int index = 0; index < pointsAsList.size() - 1; index++) {
			Point p1 = pointsAsList.get(index);
			Point p2 = pointsAsList.get(index + 1);
			segmentsToReturn.add(new Segment(p1,p2));

		}

		return segmentsToReturn;
	}

	/**
	 * Adds all implicit segments in then decides from given segments which are not already included 
	 * @param implicitPoints
	 * @param givenSegments
	 * @param implicitSegments
	 * @return set of all minimal segments in the given figure
	 */
	protected Set<Segment> identifyAllMinimalSegments(Set<Point> implicitPoints, Set<Segment> givenSegments, Set<Segment> implicitSegments){

		Set<Segment> minSegments = new LinkedHashSet<Segment>();

		//all implicit segments will be in the Minimal Segments
		minSegments.addAll(implicitSegments);

		Set<Point> points = new HashSet<Point>();

		points.addAll(_pointDatabase.getPoints());
		points.addAll(implicitPoints);


		for (Segment seg : givenSegments) {
			//check all the points for the givenSegments to make sure we have added 
			//all valid minimalSegments
			SortedSet <Point> segmentPoints = seg.collectOrderedPointsOnSegment(points);

			//shouldn't be more than 2 points on a segment
			if(segmentPoints.size() <= 2)
			{
				//add checks to see if it is already in set
				minSegments.add(seg);
			}

		}

		return minSegments;

	}

	//rewrite: add minimal segments onto things:
	// 		while loop based on a queue
	//		then for loop adding a segment each time
	// 		do test on A------B----C----D----E  (transitive closure)
	
	//		ex: 1seg + 1seg = 2seg
	//			2seg + 1seg = 3seg
	//			3seg + 1seg = 4seg, etc 
	protected Set<Segment> constructAllNonMinimalSegments(Set<Segment> minimalSegments){

		Set<Segment> segments = new HashSet<Segment>();

		for(Segment minSeg1: minimalSegments) {
			//test all minimal segments against each other
			for(Segment minSeg2: minimalSegments) {
				if(!minSeg1.equals(minSeg2) && minSeg1.coincideWithoutOverlap(minSeg2)) {
					//find point that is shared by both segments
					Point sharedPoint = minSeg1.sharedVertex(minSeg2);

					if(sharedPoint != null) {
						//create segment of the points that are not shared (non-minimal)
						Segment nonMinSeg = new Segment(minSeg1.other(sharedPoint), minSeg2.other(sharedPoint));
						segments.add(nonMinSeg);
					}	
				}
			}

			//also must check the given segments against the minimal segments
			for(Segment s: _givenSegments) {
				if(!s.equals(minSeg1) && s.HasSubSegment(minSeg1))
					segments.add(s); 
			}
		}
		return segments;
	}
}
