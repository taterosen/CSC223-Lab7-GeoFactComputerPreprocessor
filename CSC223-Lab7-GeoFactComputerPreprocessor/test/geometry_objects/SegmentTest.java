package geometry_objects;

import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.Test;

import geometry_objects.points.Point;

class SegmentTest {
	
	@Test
	void testHasSubSegment() {
		Segment seg = new Segment(new Point(0,0), new Point(15,15));
		assertTrue(seg.HasSubSegment(seg));
		
		Segment subSeg = new Segment(new Point(1,1), new Point(10,10));
		assertTrue(seg.HasSubSegment(subSeg));
		
		Segment difSeg = new Segment(new Point(56.0 / 15, 28.0 / 15), new Point(-194.0 / 55, 182.0 / 55));
		assertFalse(seg.HasSubSegment(difSeg));
		
		Segment sameEndpointSeg = new Segment(new Point(0,0), new Point(10,10));
		assertTrue(seg.HasSubSegment(sameEndpointSeg));
		
	}
	
	
	@Test
	void testCoincideWithoutOverlap() {
		Segment seg = new Segment(new Point(0,0), new Point(15,15));
		Segment sameSeg = new Segment(new Point(0,0), new Point(15,15));
		assertFalse(seg.coincideWithoutOverlap(sameSeg));
		
		Segment difSeg = new Segment(new Point(56.0 / 15, 28.0 / 15), new Point(-194.0 / 55, 182.0 / 55));
		assertFalse(seg.coincideWithoutOverlap(difSeg));
		
//		Segment difCollinearSeg = new Segment(new Point(20,20), new Point(30,30));
//		assertTrue(seg.coincideWithoutOverlap(difCollinearSeg));
		
		Segment sharedEndPointSeg = new Segment(new Point(15,15), new Point(30,30));
		assertTrue(seg.coincideWithoutOverlap(sharedEndPointSeg));
		
		Segment ptWithinEndPointsSeg = new Segment(new Point(14,14), new Point(30,30));
		assertFalse(seg.coincideWithoutOverlap(ptWithinEndPointsSeg));
		
		Segment verticalSeg1 = new Segment(new Point(0,0), new Point(0,15));
		Segment verticalSeg2 = new Segment(new Point(0,20), new Point(0,30));
		assertTrue(verticalSeg1.coincideWithoutOverlap(verticalSeg2));
		
		Segment horizontalSeg1 = new Segment(new Point(0,0), new Point(0,15));
		Segment horizontalSeg2 = new Segment(new Point(0,20), new Point(0,30));
		assertTrue(horizontalSeg1.coincideWithoutOverlap(horizontalSeg2));
	}
	
	@Test
	void testCollectOrderedPointsOnSegment() {
		Set<Point> points = new HashSet<Point>();
		points.add(new Point("2", 2, 2));
		points.add(new Point("5", 5, 5));
		points.add(new Point("1", 1, 1));
		points.add(new Point("3", 3, 3));
		points.add(new Point("10", 10, 10));
		
		Segment seg = new Segment(new Point(0,0), new Point(15,15));
		
		points = seg.collectOrderedPointsOnSegment(points);
		
		Set<Point> comparePoints = new HashSet<Point>();
		comparePoints.add(new Point("1", 1, 1));
		comparePoints.add(new Point("2", 2, 2));
		comparePoints.add(new Point("3", 3, 3));
		comparePoints.add(new Point("5", 5, 5));
		comparePoints.add(new Point("10", 10, 10));
		
		assertEquals(comparePoints, points);
		
		points.clear();
		comparePoints.clear();
		points = seg.collectOrderedPointsOnSegment(points);
		
		assertEquals(comparePoints, points);
	}

	
}
