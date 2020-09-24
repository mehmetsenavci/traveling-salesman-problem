package algoTest;

import java.util.ArrayList;

public class TSP_NN {

	private int coordinates[][];

	ArrayList<Integer> visitedCities;
	ArrayList<Integer> bestTourOrder;
	private int[][] tourMatrix;
	int tourCost;
	int costOfbestTourOrder;
	int tourSize = 48;

	public TSP_NN(int[][] coordinates) {
		this.coordinates = coordinates;

		visitedCities = new ArrayList<Integer>();
		bestTourOrder = new ArrayList<Integer>();

		tourCost = 0;
		costOfbestTourOrder = 1000000000;

		tourMatrix = calculateDistanceMatrix(coordinates);
//		printMatrix(tourMatrix);

	};

	public ArrayList<Integer> nearestNeighbor(int city) {
		shortestTour(city);

		System.out.println(costOfbestTourOrder);
		return bestTourOrder;
	}

	public ArrayList<Integer> repNearestNeighbor() {

		for (int city = 0; city < tourSize; city++) {
			shortestTour(city);
		}
		System.out.println(costOfbestTourOrder);
		return bestTourOrder;
	}

	private void shortestTour(int point) {
		int startingPoint = point;

		ArrayList<Integer> tmpSolution = new ArrayList<Integer>();

		visitedCities.clear();

		tourCost = 0;

		tmpSolution.add(point);
		visitedCities.add(point);

		while (visitedCities.size() < tourSize) {
			point = getNearestpoint(point);
			visitedCities.add(point);
			tmpSolution.add(point);
		}

		tourCost += tourMatrix[point][startingPoint];

		if (tourCost < costOfbestTourOrder) {
			costOfbestTourOrder = tourCost;
			bestTourOrder = (ArrayList<Integer>) tmpSolution.clone();
		}
	}

	private int getNearestpoint(int curPoint) {
		double edge = -1.0;
		int nearestpoint = -1;

		for (int i = (tourSize - 1); i > -1; i--) {
			if (isMarkedVisited(i)) {
				continue;
			}

			if (-1 == tourMatrix[curPoint][i]) {
				continue;
			}

			if ((-1.0 == edge) && (-1 != tourMatrix[curPoint][i])) {
				edge = tourMatrix[curPoint][i];
			}

			if ((tourMatrix[curPoint][i] <= edge)) {
				edge = tourMatrix[curPoint][i];
				nearestpoint = i;
			}
		}
		tourCost += tourMatrix[curPoint][nearestpoint];
		return nearestpoint;
	}

	private boolean isMarkedVisited(int point) {
		boolean found = false;

		for (int i = 0; i < visitedCities.size(); i++) {
			if (point == visitedCities.get(i)) {
				found = true;
				break;
			}
		}
		return found;
	}

	private int[][] calculateDistanceMatrix(int[][] coordinates) {
		int[][] distArr = new int[tourSize][tourSize];
		// CREATING THE DISTANCE MATRIX
		for (int i = 0; i < coordinates.length; i++) {
			for (int j = 0; j < distArr.length; j++) {
				int xdist = (coordinates[i][0] - coordinates[j][0]) * (coordinates[i][0] - coordinates[j][0]);

				int ydist = (coordinates[i][1] - coordinates[j][1]) * (coordinates[i][1] - coordinates[j][1]);

				int totalDist = (int) Math.sqrt(xdist + ydist);
				if (totalDist == 0) {
					distArr[i][j] = -1;
				} else {
					distArr[i][j] = (int) Math.sqrt(xdist + ydist);
				}

			}
		}
		return distArr;

	}

	private void printMatrix(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix.length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println("");
		}
	}

}
