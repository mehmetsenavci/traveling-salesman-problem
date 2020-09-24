package algoTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class TSP_alt_greedy {
	public static class Pair {
		public int a, b;

		public Pair(int a, int b) {
			this.a = a;
			this.b = b;
		}
	}

	public ArrayList<Point> cities;
	public ArrayList<Pair> edges;
	public HashMap<Integer, ArrayList<Integer>> endpoints;

	public TSP_alt_greedy(ArrayList<Point> cities) {
		this.cities = new ArrayList<Point>(cities);
		edges = new ArrayList<Pair>();
		endpoints = new HashMap<Integer, ArrayList<Integer>>();
	}

	public void shortestEdgesSorted() {
		for (int i = 0; i < cities.size(); ++i) {
			for (int j = i + 1; j < cities.size(); ++j) {
				Pair p = new Pair(i, j);
				edges.add(p);
			}
		}
		Collections.sort(edges, new Comparator<Pair>() {
			public int compare(Pair p1, Pair p2) {
				Point A = cities.get(p1.a);
				Point B = cities.get(p1.b);
				Point C = cities.get(p2.a);
				Point D = cities.get(p2.b);
				double d1 = A.getDist(B);
				double d2 = C.getDist(D);
				if (d1 < d2) {
					return -1;
				}
				if (d1 == d2) {
					return 0;
				}
				return 1;
				// return (int)Math.signum(-A.getDist(B) + C.getDist(D));
			}
		});
	}

	public ArrayList<Integer> joinEndpoints(Integer a, Integer b, ArrayList<Integer> seg1, ArrayList<Integer> seg2) {
		ArrayList<Integer> newSegment = new ArrayList<Integer>(seg1);
		if (newSegment.get(newSegment.size() - 1) != a) {
			Collections.reverse(newSegment);
		}
		if (seg2.get(0) != b) {
			Collections.reverse(seg2);
		}
		for (Integer i : seg2) {
			newSegment.add(i);
		}
		endpoints.remove(a);
		endpoints.remove(b);
		endpoints.put(newSegment.get(0), newSegment);
		endpoints.put(newSegment.get(newSegment.size() - 1), newSegment);
		return newSegment;
	}

	public ArrayList<Integer> compute() {

		shortestEdgesSorted();

		for (int i = 0; i < cities.size(); ++i) {
			ArrayList<Integer> segs = new ArrayList<Integer>();
			segs.add(i);
			endpoints.put(i, segs);
		}

		for (Pair p : edges) {
			ArrayList<Integer> seg1 = endpoints.get(p.a);
			ArrayList<Integer> seg2 = endpoints.get(p.b);
			if (seg1 != null && seg2 != null && seg1 != seg2) {
				ArrayList<Integer> newSegment = joinEndpoints(p.a, p.b, seg1, seg2);
				// System.out.print(p.a + " " + p.b + ": ");
				// for(Integer i : newSegment) {
				// System.out.print(i + " ");
				// }
				// System.out.println();
				if (newSegment.size() == cities.size()) {
					return newSegment;
				}
			} else {
				// System.out.println(p.a + " or " + p.b + " null");
			}
		}
		return null;
	}
}