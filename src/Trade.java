
/*
 * This is an unused class - my plan is to eventually refactor Faction/Economy
 * to utilize enums for trade codes, and create enumSet for trade code lists
 * that are used throughout the program;
 */

/*
 * To my understanding, there is one _main world_ per star system (but up to 3
 * stars), one _faction_ per main world/system, and one _economy_ per system,
 * et cetera... so all these things can be correlated to "one" system
 */

import java.util.EnumSet;

public enum Trade {
	AG, AS, BA, DE, FL, HI, IC, IN, LO, NA, NI, PO, RI, VA, WA;

	public EnumSet<Trade> getTradeCodes(World world) {
		EnumSet<Trade> tradeCodes = EnumSet.noneOf(Trade.class);

		int size = world.getSize();
		int atmosphere = world.getAtmosphere();
		int hydrosphere = world.getHydrosphere();
		int population = world.getPopulation();
		int government = world.getGovernment();
		int law = world.getLawLevel();

		// Agriculture
		if ((atmosphere >= 4 && atmosphere <= 9) && (hydrosphere >= 4 && hydrosphere <= 8)
				&& (population >= 5 && population <= 9))
			tradeCodes.add(AG);

		// Asteroid
		if (size == 0 && atmosphere == 0 && hydrosphere == 0)
			tradeCodes.add(AS);

		// Barren
		if (population == 0 && government == 0 && law == 0)
			tradeCodes.add(BA);

		// Desert
		if (atmosphere >= 2 && hydrosphere == 0)
			tradeCodes.add(DE);

		// Fluid Oceans
		if (atmosphere >= 10 && hydrosphere >= 1)
			tradeCodes.add(FL);

		// High Population
		if (population >= 9)
			tradeCodes.add(HI);

		// Ice-Capped
		if (atmosphere <= 1 && hydrosphere >= 1)
			tradeCodes.add(IC);

		// Industrial
		if ((atmosphere <= 2 || atmosphere == 4 || atmosphere == 7 || atmosphere == 9) && population >= 9)
			tradeCodes.add(IN);

		// Low Population
		if (population <= 3)
			tradeCodes.add(LO);

		// Non-Agriculatural
		if (atmosphere <= 3 && hydrosphere <= 3 && population >= 6)
			tradeCodes.add(NA);

		// Non-Industrial
		if (population <= 6)
			tradeCodes.add(NI);

		// Poor
		if ((atmosphere >= 2 && atmosphere <= 5) && hydrosphere <= 3)
			tradeCodes.add(PO);

		// Rich
		if ((atmosphere == 6 && atmosphere == 8) && (population >= 6 && population <= 8)
				&& (government >= 4 && government <= 9))
			tradeCodes.add(RI);

		// Vacuum
		if (atmosphere == 0)
			tradeCodes.add(VA);

		// Water World
		if (hydrosphere == 10)
			tradeCodes.add(WA);

		return tradeCodes;
	}
}