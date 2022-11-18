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

	protected Set<Segment> computeImplicitBaseSegments(Set<Point> implicitPoints){

		Set<Segment> segments = new HashSet<Segment>();

		//loop over implicit point
		for (Point point : implicitPoints) {
			//loop over segments
			for(Segment seg :_givenSegments) {

				if(seg.pointLiesOnSegment(point)) {
					Segment newSeg1 = new Segment(point,seg.getPoint1());
					Segment newSeg2 = new Segment(point,seg.getPoint2());
					boolean isMinimal1 = true;
					boolean isMinimal2 = true;

					for(Point other: implicitPoints) {

						if(!other.equals(point) && newSeg1.pointLiesOnSegment(other))
							isMinimal1 = false;
						if(!other.equals(point) && newSeg2.pointLiesOnSegment(other))
							isMinimal2 = false;

					}
					if(isMinimal1) segments.add(newSeg1);
					if(isMinimal2) segments.add(newSeg2);
				}

				for(Point point2: implicitPoints) {
					if(!point.equals(point2)) {
						Segment subsegment = new Segment(point, point2);
						if(seg.HasSubSegment(subsegment)) {
							segments.add(subsegment);
						}

					}
				}

			}

		}



		return segments;
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


	protected Set<Segment> constructAllNonMinimalSegments(Set<Segment> minimalSegments){

		Set<Segment> segments = new HashSet<Segment>();

		for (Segment minSeg : minimalSegments) {

			for(Segment minSeg2 : minimalSegments) {

				//if(minSeg.other(minSeg2.getPoint1()).equals(segments)
				//if the first point from first segment  and first point from second segment
				if(minSeg.getPoint1().equals(minSeg2.getPoint1()) && minSeg.coincideWithoutOverlap(minSeg2)) {
					segments.add(new Segment(minSeg.getPoint2(), minSeg2.getPoint2()));	
				}
				//if the first point from first segment and second point from second segment
				if (minSeg.getPoint1().equals(minSeg2.getPoint2()) && minSeg.coincideWithoutOverlap(minSeg2)) {
					segments.add(new Segment(minSeg.getPoint2(), minSeg2.getPoint1()));	
				}
				//if the second point from first segment and first point from second segment
				if(minSeg.getPoint2().equals(minSeg2.getPoint1()) && minSeg.coincideWithoutOverlap(minSeg2)) {
					segments.add(new Segment(minSeg.getPoint1(), minSeg2.getPoint2()));	
				}
				//if the second point from second segment and second point from second segment
				if(minSeg.getPoint2().equals(minSeg2.getPoint2()) && minSeg.coincideWithoutOverlap(minSeg2)) {
					segments.add(new Segment(minSeg.getPoint1(), minSeg2.getPoint1()));	

				}
			}
		}



		return segments;
	}
}
