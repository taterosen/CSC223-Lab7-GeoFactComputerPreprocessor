package preprocessor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import geometry_objects.Segment;
import geometry_objects.points.Point;
import geometry_objects.points.PointDatabase;
import input.InputFacade;
import input.components.FigureNode;
import preprocessor.delegates.ImplicitPointPreprocessor;

class PreprocessorTest
{
	//	@Test
	//	void test_implicit_crossings()
	//	{
	//		FigureNode fig = InputFacade.extractFigure("jsonfiles/fully_connected_irregular_polygon.json");
	//
	//		Map.Entry<PointDatabase, Set<Segment>> pair = InputFacade.toGeometryRepresentation(fig);
	//		PointDatabase points = pair.getKey();
	//
	//		Set<Segment> segments = pair.getValue();
	//
	//		Preprocessor pp = new Preprocessor(points, segments);
	//
	//		// 5 new implied points inside the pentagon
	//		Set<Point> iPoints = ImplicitPointPreprocessor.compute(points, new ArrayList<Segment>(segments));
	//		assertEquals(5, iPoints.size());
	//
	//		System.out.println(iPoints);
	//
	//		//
	//		//
	//		//		               D(3, 7)
	//		//
	//		//
	//		//   E(-2,4)       D*      E*
	//		//		         C*          A*       C(6, 3)
	//		//                      B*
	//		//		       A(2,0)        B(4, 0)
	//		//
	//		//		    An irregular pentagon with 5 C 2 = 10 segments
	//
	//		Point a_star = new Point(56.0 / 15, 28.0 / 15);
	//		Point b_star = new Point(16.0 / 7, 8.0 / 7);
	//		Point c_star = new Point(8.0 / 9, 56.0 / 27);
	//		Point d_star = new Point(90.0 / 59, 210.0 / 59);
	//		Point e_star = new Point(194.0 / 55, 182.0 / 55);
	//
	//		assertTrue(iPoints.contains(a_star));
	//		
	//		//Not Equal Why???
	//		
	//		
	//		
	////		assertTrue(iPoints.contains(b_star));
	////		assertTrue(iPoints.contains(c_star));
	//		assertTrue(iPoints.contains(d_star));
	//		assertTrue(iPoints.contains(e_star));
	//
	//		//
	//		// There are 15 implied segments inside the pentagon; see figure above
	//		//
	//		Set<Segment> iSegments = pp.computeImplicitBaseSegments(iPoints);
	//		
	//		assertEquals(15, iSegments.size());
	//
	//		List<Segment> expectedISegments = new ArrayList<Segment>();
	//
	//		expectedISegments.add(new Segment(points.getPoint("A"), c_star));
	//		expectedISegments.add(new Segment(points.getPoint("A"), b_star));
	//
	//		expectedISegments.add(new Segment(points.getPoint("B"), b_star));
	//		expectedISegments.add(new Segment(points.getPoint("B"), a_star));
	//
	//		expectedISegments.add(new Segment(points.getPoint("C"), a_star));
	//		expectedISegments.add(new Segment(points.getPoint("C"), e_star));
	//
	//		expectedISegments.add(new Segment(points.getPoint("D"), d_star));
	//		expectedISegments.add(new Segment(points.getPoint("D"), e_star));
	//
	//		expectedISegments.add(new Segment(points.getPoint("E"), c_star));
	//		expectedISegments.add(new Segment(points.getPoint("E"), d_star));
	//
	//		expectedISegments.add(new Segment(c_star, b_star));
	//		expectedISegments.add(new Segment(b_star, a_star));
	//		expectedISegments.add(new Segment(a_star, e_star));
	//		expectedISegments.add(new Segment(e_star, d_star));
	//		expectedISegments.add(new Segment(d_star, c_star));
	//
	//		for (Segment iSegment : iSegments)
	//		{
	//			assertTrue(iSegments.contains(iSegment));
	//		}
	//
	//		//
	//		// Ensure we have ALL minimal segments: 20 in this figure.
	//		//
	//		List<Segment> expectedMinimalSegments = new ArrayList<Segment>(iSegments);
	//		expectedMinimalSegments.add(new Segment(points.getPoint("A"), points.getPoint("B")));
	//		expectedMinimalSegments.add(new Segment(points.getPoint("B"), points.getPoint("C")));
	//		expectedMinimalSegments.add(new Segment(points.getPoint("C"), points.getPoint("D")));
	//		expectedMinimalSegments.add(new Segment(points.getPoint("D"), points.getPoint("E")));
	//		expectedMinimalSegments.add(new Segment(points.getPoint("E"), points.getPoint("A")));
	//		
	//		Set<Segment> minimalSegments = pp.identifyAllMinimalSegments(iPoints, segments, iSegments);
	//		assertEquals(expectedMinimalSegments.size(), minimalSegments.size());
	//
	//		for (Segment minimalSeg : minimalSegments)
	//		{
	//			assertTrue(expectedMinimalSegments.contains(minimalSeg));
	//		}
	//		
	//		//
	//		// Construct ALL figure segments from the base segments
	//		//
	//		Set<Segment> computedNonMinimalSegments = pp.constructAllNonMinimalSegments(minimalSegments);
	//		
	//		//
	//		// All Segments will consist of the new 15 non-minimal segments.
	//		//
	//		assertEquals(15, computedNonMinimalSegments.size());
	//
	//		//
	//		// Ensure we have ALL minimal segments: 20 in this figure.
	//		//
	//		List<Segment> expectedNonMinimalSegments = new ArrayList<Segment>();
	//		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), d_star));
	//		expectedNonMinimalSegments.add(new Segment(points.getPoint("D"), c_star));
	//		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), points.getPoint("D")));
	//		
	//		expectedNonMinimalSegments.add(new Segment(points.getPoint("B"), c_star));
	//		expectedNonMinimalSegments.add(new Segment(points.getPoint("E"), b_star));
	//		expectedNonMinimalSegments.add(new Segment(points.getPoint("B"), points.getPoint("E")));
	//		
	//		expectedNonMinimalSegments.add(new Segment(points.getPoint("C"), d_star));
	//		expectedNonMinimalSegments.add(new Segment(points.getPoint("E"), e_star));
	//		expectedNonMinimalSegments.add(new Segment(points.getPoint("C"), points.getPoint("E")));		
	//
	//		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), a_star));
	//		expectedNonMinimalSegments.add(new Segment(points.getPoint("C"), b_star));
	//		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), points.getPoint("C")));
	//		
	//		expectedNonMinimalSegments.add(new Segment(points.getPoint("B"), e_star));
	//		expectedNonMinimalSegments.add(new Segment(points.getPoint("D"), a_star));
	//		expectedNonMinimalSegments.add(new Segment(points.getPoint("B"), points.getPoint("D")));
	//		
	//		//
	//		// Check size and content equality
	//		//
	//		assertEquals(expectedNonMinimalSegments.size(), computedNonMinimalSegments.size());
	//
	//		for (Segment computedNonMinimalSegment : computedNonMinimalSegments)
	//		{
	//			assertTrue(expectedNonMinimalSegments.contains(computedNonMinimalSegment));
	//		}
	//	}

	@Test
	void test_implicit_shape() {
		FigureNode fig = InputFacade.extractFigure("jsonfiles/crossing_symmetric_triangle.json");

		Map.Entry<PointDatabase, Set<Segment>> pair = InputFacade.toGeometryRepresentation(fig);
		PointDatabase points = pair.getKey();

		Set<Segment> segments = pair.getValue();

		Preprocessor pp = new Preprocessor(points, segments);

		Set<Point> iPoints = ImplicitPointPreprocessor.compute(points, new ArrayList<Segment>(segments));

		assertEquals(1, iPoints.size());

		Point x_star = new Point(3.0,3.0);

		Point a_star = new Point("A*", 56.0 / 15, 28.0 / 15);
		Point b_star = new Point("B*",16.0 / 7, 8.0 / 7);
		Point c_star = new Point("C*",8.0 / 9, 56.0 / 27);
		Point d_star = new Point("D*",90.0 / 59, 210.0 / 59);
		Point e_star = new Point("E*",194.0 / 55, 182.0 / 55);



		assertTrue(iPoints.contains(x_star));

		Set<Segment> iSegments = pp.computeImplicitBaseSegments(iPoints);

		assertEquals(15, iSegments.size());


		assertEquals(10, iSegments.size());

		List<Segment> expectedISegments = new ArrayList<Segment>();

		expectedISegments.add(new Segment(points.getPoint("A"), points.getPoint("B")));
		expectedISegments.add(new Segment(points.getPoint("A"), points.getPoint("C")));

		expectedISegments.add(new Segment(points.getPoint("B"), points.getPoint("C")));
		expectedISegments.add(new Segment(points.getPoint("B"), points.getPoint("D")));
		expectedISegments.add(new Segment(points.getPoint("B"), x_star));

		expectedISegments.add(new Segment(points.getPoint("C"), x_star));	
		expectedISegments.add(new Segment(points.getPoint("C"), points.getPoint("E")));

		expectedISegments.add(new Segment(points.getPoint("D"), points.getPoint(x_star)));	
		expectedISegments.add(new Segment(points.getPoint("D"), points.getPoint("E")));

		expectedISegments.add(new Segment(points.getPoint(x_star), points.getPoint("E")));

		List<Segment> expectedMinimalSegments = new ArrayList<Segment>(iSegments);

		Set<Segment> minimalSegments = pp.identifyAllMinimalSegments(iPoints, segments, iSegments);
		
		assertEquals(expectedMinimalSegments.size(), minimalSegments.size());

		for (Segment minimalSeg : minimalSegments)
		{
			assertTrue(expectedMinimalSegments.contains(minimalSeg));
		}

		//
		// Construct ALL figure segments from the base segments
		//
		Set<Segment> computedNonMinimalSegments = pp.constructAllNonMinimalSegments(minimalSegments);

		//
		// All Segments will consist of the new 15 non-minimal segments.
		//
		assertEquals(10, computedNonMinimalSegments.size());

		List<Segment> expectedNonMinimalSegments = new ArrayList<Segment>();
//		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), d_star));
//		expectedNonMinimalSegments.add(new Segment(points.getPoint("D"), c_star));
//		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), points.getPoint("D")));
//
//		expectedNonMinimalSegments.add(new Segment(points.getPoint("B"), c_star));
//		expectedNonMinimalSegments.add(new Segment(points.getPoint("E"), b_star));
//		expectedNonMinimalSegments.add(new Segment(points.getPoint("B"), points.getPoint("E")));
//
//		expectedNonMinimalSegments.add(new Segment(points.getPoint("C"), d_star));
//		expectedNonMinimalSegments.add(new Segment(points.getPoint("E"), e_star));
//		expectedNonMinimalSegments.add(new Segment(points.getPoint("C"), points.getPoint("E")));		
//
//		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), a_star));
//		expectedNonMinimalSegments.add(new Segment(points.getPoint("C"), b_star));
//		expectedNonMinimalSegments.add(new Segment(points.getPoint("A"), points.getPoint("C")));
//
//		expectedNonMinimalSegments.add(new Segment(points.getPoint("B"), e_star));
//		expectedNonMinimalSegments.add(new Segment(points.getPoint("D"), a_star));
//		expectedNonMinimalSegments.add(new Segment(points.getPoint("B"), points.getPoint("D")));

		//
		// Check size and content equality
		//
		assertEquals(expectedNonMinimalSegments.size(), computedNonMinimalSegments.size());

		for (Segment computedNonMinimalSegment : computedNonMinimalSegments)
		{
			assertTrue(expectedNonMinimalSegments.contains(computedNonMinimalSegment));
		}
	}

}