package algoTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class TSP_DD {
	public ArrayList<Point> cities;

	public TSP_DD(ArrayList<Point> cities) {
		this.cities = new ArrayList<Point>(cities);
	}

	public ArrayList<Integer> shortestTour(ArrayList<ArrayList<Integer>> tours) {
		double minCost = 1e9;
		int ind = -1;
		for (int i = 0; i < tours.size(); ++i) {
			double cost = 0;
			ArrayList<Integer> tour = tours.get(i);
			for (int j = 0; j < tour.size(); ++j) {
				int k = (j == tour.size() - 1 ? 0 : j + 1);
				Point A = cities.get(tour.get(j));
				Point B = cities.get(tour.get(k));
				cost += A.getDist(B);
			}
			if (cost < minCost) {
				minCost = cost;
				ind = i;
			}
		}
		return (ind == -1 ? null : tours.get(ind));
	}

	public ArrayList<ArrayList<Integer>> splitCities(ArrayList<Integer> iCities) {
		double minX = 1e9, maxX = -1e9;
		double minY = 1e9, maxY = -1e9;
		for (Integer i : iCities) {
			Point p = cities.get(i);
			if (p.x < minX) {
				minX = p.x;
			}
			if (p.x > maxX) {
				maxX = p.x;
			}
			if (p.y < minY) {
				minY = p.y;
			}
			if (p.y > maxY) {
				maxY = p.y;
			}
		}

		double mulr = maxX - minX > maxY - minY ? 1 : 0;
		Collections.sort(iCities, new Comparator<Integer>() {
			public int compare(Integer ind1, Integer ind2) {
				Point A = cities.get(ind1);
				Point B = cities.get(ind2);
				return (int) Math.signum(mulr * (A.x - B.x) + (1 - mulr) * (A.y - B.y));
			}
		});
		int mid = iCities.size() / 2;
		ArrayList<ArrayList<Integer>> out = new ArrayList<ArrayList<Integer>>();
		out.add(new ArrayList<Integer>());
		out.add(new ArrayList<Integer>());

		for (int i = 0; i < mid; ++i) {
			out.get(0).add(iCities.get(i));
		}
		for (int i = mid; i < iCities.size(); ++i) {
			out.get(1).add(iCities.get(i));
		}
		return out;
	}

	public ArrayList<ArrayList<Integer>> rotations(ArrayList<Integer> tour) {
		ArrayList<ArrayList<Integer>> out = new ArrayList<ArrayList<Integer>>();
		for (int i = 0; i < tour.size(); ++i) {
			ArrayList<Integer> seq = new ArrayList<Integer>();
			for (int j = i; j < tour.size(); ++j) {
				seq.add(tour.get(j));
			}
			for (int j = 0; j < i; ++j) {
				seq.add(tour.get(j));
			}
			out.add(seq);
		}
		return out;
	}

	public ArrayList<Integer> joinTours(ArrayList<Integer> tour1, ArrayList<Integer> tour2) {
		ArrayList<ArrayList<Integer>> segs1 = rotations(tour1);
		ArrayList<ArrayList<Integer>> segs2 = rotations(tour2);
		ArrayList<ArrayList<Integer>> tours = new ArrayList<ArrayList<Integer>>();

		for (ArrayList<Integer> seg1 : segs1) {
			for (ArrayList<Integer> seg2 : segs2) {
				ArrayList<Integer> joined = new ArrayList<Integer>();
				for (Integer i : seg1) {
					joined.add(i);
				}
				for (Integer i : seg2) {
					joined.add(i);
				}
				tours.add(joined);

				joined = new ArrayList<Integer>();
				for (Integer i : seg1) {
					joined.add(i);
				}
				// add reversed tour
				for (int i = 0; i < seg2.size() / 2; ++i) {
					joined.add(seg2.get(seg2.size() - 1 - i));
				}
				for (int i = seg2.size() / 2; i < seg2.size(); ++i) {
					joined.add(seg2.get(seg2.size() - 1 - i));
				}
				tours.add(joined);
			}
		}
		return shortestTour(tours);
	}

	ArrayList<Integer> dAndD(ArrayList<Integer> iCities) {
		if (iCities.size() <= 3) {
			return iCities;
		}
		ArrayList<ArrayList<Integer>> sc = splitCities(iCities);
		return joinTours(dAndD(sc.get(0)), dAndD(sc.get(1)));
	}

	ArrayList<Integer> compute() {
		ArrayList<Integer> iCities = new ArrayList<Integer>();
		for (int i = 0; i < cities.size(); ++i) {
			iCities.add(i);
		}
		return dAndD(iCities);
	}
}
