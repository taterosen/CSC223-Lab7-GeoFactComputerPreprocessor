package preprocessor.delegates;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import geometry_objects.Segment;
import geometry_objects.delegates.intersections.IntersectionDelegate;
import geometry_objects.delegates.intersections.SegmentIntersectionDelegate;
import geometry_objects.points.Point;
import geometry_objects.points.PointDatabase;

public class ImplicitPointPreprocessor
{
	/**
	 * It is possible that some of the defined segments intersect
	 * and points that are not named; we need to capture those
	 * points and name them.
	 * 
	 * Algorithm:
	 *    TODO
	 */
	public static Set<Point> compute(PointDatabase givenPoints, List<Segment> givenSegments)
	{
		Set<Point> implicitPoints = new LinkedHashSet<Point>();


		for (Segment seg1 : givenSegments) {

			for (Segment seg2 : givenSegments) {
				
				Point p = IntersectionDelegate.segmentIntersection(seg1, seg2);

				//check that doesn't exist
				if(givenPoints.getPoint(p) == null) {
					implicitPoints.add(p);
				}
			}

		}

		return implicitPoints;
	}

}
