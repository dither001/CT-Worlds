
/*	Original code by Mr. Polywhirl
 *	via StackOverflow
 *	https://stackoverflow.com/users/1762224/mr-polywhirl
 */

import java.awt.*;

@SuppressWarnings("serial")
public class Hexagon extends Polygon {
	// fields
	private static final int SIDES = 6;
	private static final int ROTATION = 90;
	private static final int LINE_THICKNESS = 4;

	private Point[] points;
	private Point center;
	private int radius;

	// constructors
	public Hexagon(Point center, int radius) {
		points = new Point[SIDES];
		npoints = SIDES;
		xpoints = new int[SIDES];
		ypoints = new int[SIDES];

		this.center = center;
		this.radius = radius;

		updatePoints();
	}

	public Hexagon(int x, int y, int radius) {
		this(new Point(x, y), radius);
	}

	// methods
	public int getRadius() {
		return radius;
	}

	public void setRadius(int radius) {
		this.radius = radius;
		updatePoints();
	}

	public int getRotation() {
		return ROTATION;
	}

	public void setCenter(Point center) {
		this.center = center;

		updatePoints();
	}

	public void setCenter(int x, int y) {
		setCenter(new Point(x, y));
	}

	private double findAngle(double fraction) {
		return fraction * Math.PI * 2 + Math.toRadians((ROTATION + 180) % 360);
	}

	private Point findPoint(double angle) {
		int x = (int) (center.x + Math.cos(angle) * radius);
		int y = (int) (center.y + Math.sin(angle) * radius);

		return new Point(x, y);
	}

	protected void updatePoints() {
		for (int p = 0; p < SIDES; p++) {
			double angle = findAngle((double) p / SIDES);
			Point point = findPoint(angle);
			xpoints[p] = point.x;
			ypoints[p] = point.y;
			points[p] = point;
		}
	}

	public void draw(Graphics2D g, int x, int y, Color color, Faction faction) {
		// Store before changing
		Stroke tmpS = g.getStroke();
		Color tmpC = g.getColor();

		g.setColor(color);
		g.setStroke(new BasicStroke(LINE_THICKNESS, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
		g.drawPolygon(xpoints, ypoints, npoints);

		// hexagon label
		if (faction != null) {
			String text = String.format("%s", "World");
			int w = Main.metrics.stringWidth(text);
			int h = Main.metrics.getHeight();
			g.setColor(Color.WHITE);
			g.drawString(text, x - w / 2, y + h);
			
			text = "NA";
			w = Main.metrics.stringWidth(text);
			Point[] codePts = { new Point((int) (x - 0.5 * w), (int) (y - 1.75 * h)), // STARPORT
					new Point((int) (x - 2.25 * w), (int) (y - 0.75 * h)),
					new Point((int) (x - 2.25 * w), (int) (y + 1.5 * h)),
					new Point((int) (x - 0.5 * w), (int) (y + 2.5 * h)),
					new Point((int) (x + 1.25 * w), (int) (y + 1.5 * h)),
					new Point((int) (x + 1.25 * w), (int) (y - 0.75 * h))
			};

			
			char spaceport = faction.getSpaceport();
			String[] codes = faction.getTradeCodes();
			int length = codes.length;
			
			g.drawString(" " + String.valueOf(spaceport), codePts[0].x, codePts[0].y);
			for (int i = 0; i < length && length <= 5; ++i) {
				w = Main.metrics.stringWidth(text);
				h = Main.metrics.getHeight();
				g.drawString(codes[i], codePts[i+1].x, codePts[i+1].y);
			}

		}

		// Set values to previous when done.
		g.setColor(tmpC);
		g.setStroke(tmpS);
	}

	// public void draw(Graphics2D g, int x, int y, int lineThickness, int
	// colorValue, boolean filled) {
	// // Store before changing.
	// Stroke tmpS = g.getStroke();
	// Color tmpC = g.getColor();
	//
	// g.setColor(new Color(colorValue));
	// g.setStroke(new BasicStroke(lineThickness, BasicStroke.CAP_SQUARE,
	// BasicStroke.JOIN_MITER));
	//
	// if (filled)
	// g.fillPolygon(xpoints, ypoints, npoints);
	// else
	// g.drawPolygon(xpoints, ypoints, npoints);
	//
	// // Set values to previous when done.
	// g.setColor(tmpC);
	// g.setStroke(tmpS);
	// }
}
