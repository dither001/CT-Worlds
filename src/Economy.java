
/*	CURRENTLY UNUSED -- TRANSFERRING BASIC ECONOMY INFORMATION HERE FROM SUB-CLASS WITHIN FACTION
 * 
 */

public class Economy {
	// fields
	private int resources;
	private int labor;
	private int infrastructure;
	private int culture;
	private int totalDemand;
	private double resourcesAvailable;
	private int excessDemand;
	private int deficitDemand;
	private double tradeBenefit;
	private double grossWorldProduct;

	private final int[][] TOTAL_DEMAND = { { 0, 0, 0, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7 },
			{ 0, 0, 0, 1, 2, 2, 3, 4, 5, 5, 6, 6, 8, 8, 9, 9 }, { 0, 0, 1, 1, 2, 3, 4, 5, 6, 6, 7, 8, 9, 10, 11, 11 },
			{ 0, 0, 1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13 },
			{ 0, 1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 },
			{ 0, 1, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14 },
			{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 },
			{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 },
			{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 },
			{ 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15 },
			{ 1, 2, 2, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16 },
			{ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 14, 15, 16, 17 },
			{ 1, 2, 3, 4, 6, 7, 8, 9, 10, 11, 12, 13, 15, 16, 17, 18 },
			{ 1, 2, 3, 5, 6, 7, 8, 9, 10, 11, 12, 13, 16, 17, 18, 19 },
			{ 1, 2, 3, 5, 7, 8, 9, 10, 11, 12, 13, 14, 17, 18, 19, 20 },
			{ 1, 2, 4, 5, 7, 8, 9, 11, 12, 13, 15, 16, 18, 19, 21, 22 } };

	private double[] EXPORT_BENEFIT = { 0.2, 0.2, 0.3, 0.3, 0.4, 0.4, 0.4, 0.4, 0.5, 0.5, 0.6 };
	private double[] IMPORT_BENEFIT = { 0.3, 0.3, 0.4, 0.4, 0.5, 0.5, 0.5, 0.5, 0.6, 0.6, 0.7 };

	private double[] LABOR_BASE = { 0.0000001, 0.000001, 0.00001, 0.0001, 0.001, 0.01, 0.1, 1, 10, 100, 1000, 10000,
			100000, 1000000, 10000000 };

	// constructors
	public Economy(World world) {
		// TODO
	}
}
