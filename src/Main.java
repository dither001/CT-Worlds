
/*	Original code by Mr. Polywhirl
 *	via StackOverflow
 *	https://stackoverflow.com/users/1762224/mr-polywhirl
 */

import java.awt.*;
import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class Main extends JPanel {
	private static final Point ORIGIN = new Point(50, 56);
	private final int WIDTH = 1080;
	private final int HEIGHT = 720;

	// remember this font nonsense for when I need it again someday
	private Font font = new Font("Arial", Font.BOLD, 12);
	static FontMetrics metrics;
	
	// subsector stuff
	private static StarSystem[] worlds; 

	// stuff for printwriter
	private static Date myDate = new Date();
	private static PrintWriter myWriter = null;
	private static String[] header = { "hex", "size", "atmo", "hydro", "pop", "gov't", "law", "tech", "port", "Ag",
			"As", "Ba", "De", "Fl", "Ga", "Hi", "IC", "In", "Lo", "NA", "NI", "Po", "Ri", "Va", "Wa", "Gov't",
			"giant" };

	public Main() {
		setPreferredSize(new Dimension(WIDTH, HEIGHT));
	}

	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		Point origin = new Point(WIDTH / 2, HEIGHT / 2);

		g2d.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
		g2d.setFont(font);
		metrics = g.getFontMetrics();

		g2d.setColor(Color.BLACK);
		g2d.fillRect(0, 0, WIDTH, HEIGHT);

		drawSubsectorMap(g2d);
	}

	private static StarSystem[] generateSubsector() {
		StarSystem[] mySystem = new StarSystem[80];
		int systemIndex = 0;

		for (int row = 1; row <= 8; row++) {
			for (int col = 1; col <= 10; col++) {
				if (Math.random() < 0.5) {
					do {
						mySystem[systemIndex] = new StarSystem();
					} while (mySystem[systemIndex].hasMainWorld() != true);
					mySystem[systemIndex].setAddress(new Point(row, col));
				}
				++systemIndex;
			}
		}

		return mySystem;
	}

	public static void printSubsector() {
		for (int i = 0; i < header.length; ++i) {
			myWriter.printf("%s,", header[i]);
		}
		myWriter.printf("%n");

		for (int i = 1; i <= 8; ++i) {
			for (int j = 1; j <= 10; ++j) {
				if (j < 10)
					myWriter.printf("'0%d0%d,", i, j);
				else
					myWriter.printf("'0%d%d,", i, j);
				// rest of world
				// if (worlds[counter] != null)
				// printWorld();

				myWriter.printf("%n");
			}
		}

		myWriter.close();
	}

	private void drawSubsectorMap(Graphics graphics) {
		Point point;
		int x, y, radius, padding, worldIndex;
		radius = 50;
		padding = 8;
		worldIndex = 0;

		double ang30 = Math.toRadians(30);
		double xOff = Math.cos(ang30) * (radius + padding);
		double yOff = Math.sin(ang30) * (radius + padding);

		for (int row = 1; row <= 8; row++) {
			for (int col = 1; col <= 10; col++) {
				if (row % 2 == 0) {
					x = (int) (ORIGIN.x + xOff * (col - 0.5) * 2);
					y = (int) (ORIGIN.y + yOff * (row - 1) * 3);
				} else {
					x = (int) ((ORIGIN.x + xOff) * (col - 0.5));
					y = (int) (ORIGIN.y + yOff * (row - 1) * 3);
				}

				point = new Point(x,y);

				if (worlds[worldIndex] != null) {
					drawHex(graphics, row, col, point, radius, worlds[worldIndex].getFaction());
				} else {
					drawHex(graphics, row, col, point, radius);
				}
				
				++worldIndex;
			}
		}
	}

	private void drawHex(Graphics g, int posX, int posY, Point point, int r) {
		drawHex(g, posX, posY, point, r, null);
	}

	private void drawHex(Graphics g, int posX, int posY, Point point, int r, Faction faction) {
		Graphics2D g2d = (Graphics2D) g;
		Hexagon hex = new Hexagon(point, r);
		hex.draw(g2d, point.x, point.y, Color.BLUE, faction);

		// hexagon label
		if (faction == null) {
			String text = String.format("%s, %s", coord(posX), coord(posY));
			int w = metrics.stringWidth(text);
			int h = metrics.getHeight();
			g.setColor(Color.WHITE);
			g.drawString(text, point.x - w / 2, point.y + h / 2 - 6);
		}
	}

	private String coord(int value) {
		return ((value > 0) ? "" : "") + Integer.toString(value);
	}

	public static void main(String[] args) throws IOException {
//		try {
//			myWriter = new PrintWriter(myDate.getTime() + ".csv");
//		} catch (IOException error) {
//			error.printStackTrace();
//		}

		worlds = generateSubsector();
		 for (StarSystem el : worlds) {
		 if (el != null) System.out.println(el.printMain());
		 }

		JFrame frame = new JFrame();
		Main p = new Main();

		frame.setContentPane(p);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
}
