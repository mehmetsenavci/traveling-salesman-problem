import java.util.Random;
import java.util.ArrayList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileReader;

public class Main {
	public static class InitSize {
		public static double width = 600;
		public static double height = 600;
	}
	final static int lat_scale = 69;
	final static int long_scale = -48;
	
	public static class Visualizer extends JComponent{
		
		ArrayList<Point> data;
		ArrayList<Integer> tour;
		public Visualizer(ArrayList<Point> cities, ArrayList<Integer> tour) {
			data = new ArrayList<Point>(cities);
			this.tour = tour;
			
			// remap/rescale given coordinates to the screen coordinates
			double minX = 1e9, maxX = -1e9;
			double minY = 1e9, maxY = -1e9;
			for(Point p : data) {
				if(p.x < minX) {
					minX = p.x;
				}
				if(p.x > maxX) {
					maxX = p.x;
				}
				if(p.y < minY) {
					minY = p.y;
				}
				if(p.y > maxY) {
					maxY = p.y;
				}
			}
			double ratio = (maxX - minX) / (maxY - minY);
			InitSize.width = InitSize.height * ratio;
			double marginX = 10 / ratio;
			double marginY = 10 * ratio;
			for(Point p : data) {
				p.x = (p.x - minX) * (InitSize.width - 2*marginX)  / (maxX - minX) + marginX;
				p.y = (p.y - minY) * (InitSize.height - 2*marginY) / (maxY - minY) + marginY;
			}
		}
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setColor(Color.BLUE);
			for (Point p : data) {
				g.fillOval((int)(p.x), (int)(InitSize.height - p.y), 5, 5);
			}
			for(int i = 0; i < tour.size(); ++i) {
				int j = (i == tour.size() - 1 ? 0 : i + 1);
				int A = tour.get(i);
				int B = tour.get(j);
				g.drawLine((int)data.get(A).x, (int)(InitSize.height - data.get(A).y), (int)data.get(B).x, (int)(InitSize.height - data.get(B).y));
			}
		}
	}
	public static void main(String[] args) {
		int arr[][] = { { 6734, 1453 }, { 2233, 10 }, { 5530, 1424 }, { 401, 841 }, { 3082, 1644 }, { 7608, 4458 },
				{ 7573, 3716 }, { 7265, 1268 }, { 6898, 1885 }, { 1112, 2049 }, { 5468, 2606 }, { 5989, 2873 },
				{ 4706, 2674 }, { 4612, 2035 }, { 6347, 2683 }, { 6107, 669 }, { 7611, 5184 }, { 7462, 3590 },
				{ 7732, 4723 }, { 5900, 3561 }, { 4483, 3369 }, { 6101, 1110 }, { 5199, 2182 }, { 633, 2809 },
				{ 4307, 2322 }, { 675, 1006 }, { 7555, 4819 }, { 7541, 3981 }, { 3177, 756 }, { 7352, 4506 },
				{ 7545, 2801 }, { 3245, 3305 }, { 6426, 3173 }, { 4608, 1198 }, { 23, 2216 }, { 7248, 3779 },
				{ 7762, 4595 }, { 7392, 2244 }, { 3484, 2829 }, { 6271, 2135 }, { 4985, 140 }, { 1916, 1569 },
				{ 7280, 4899 }, { 7509, 3239 }, { 10, 2676 }, { 6807, 2993 }, { 5185, 3258 }, { 3023, 1942 } };
		
		ArrayList<Point> cities = new ArrayList<Point>();
		
		try {
			String line;
			 // open input stream test.txt for reading purpose.
			BufferedReader br = new BufferedReader(new FileReader("input.txt"));			 
			while ((line = br.readLine()) != null) {
				String[] tokens = line.split("\\s *");
				//System.out.println(tokens[1] + " " + tokens[2]);
				//System.out.println(tokens.length);
				Point p = new Point(Double.parseDouble(tokens[2]) * long_scale, Double.parseDouble(tokens[1]) * lat_scale);
				// if you want to test the map of the USA from the link un-comment the line below
				//cities.add(p);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		
		for(int i = 0; i < arr.length; ++i) {
			Point p = new Point(arr[i][0], arr[i][1]);
			cities.add(p);
		}
		
		TSP_alt_greedy algo1 = new TSP_alt_greedy(cities);
		TSP_DD algo2 = new TSP_DD(cities);
		
		//ArrayList<Integer> tour = algo1.compute();
		ArrayList<Integer> tour = algo2.compute();
		
		double total = 0;
		for(int i = 0; i < tour.size(); ++i) {
			int j = (i == tour.size() - 1 ? 0 : i + 1);
			int A = tour.get(i);
			int B = tour.get(j);
			total += cities.get(A).getDist(cities.get(B));
		}
		System.out.println("total cost: " + total);
		System.out.print("tour: ");
		for(Integer i : tour) {
			System.out.print(i + " ");
		}
		System.out.println();
		
		JFrame testFrame = new JFrame();
		testFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		final Visualizer comp = new Visualizer(cities, tour);
		comp.setPreferredSize(new Dimension((int)InitSize.width, (int)InitSize.height));
		testFrame.getContentPane().add(comp, BorderLayout.CENTER);
		testFrame.pack();
		testFrame.setVisible(true);
	}
}