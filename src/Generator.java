/********************************************************
 * Author: Nick Foster
 *
 * Last edited: 10/14/2017
 ********************************************************/

public class Generator {
	/*
	 * First section is star generation
	 * 
	 */
	
	// added '-' to represent orbit zero
	private static final char[] A_BRIGHT_GIANT = { '-', 'U', 'I', 'I', 'I', 'I', 'I', 'I', 'H' };
	private static final char[] F_BRIGHT_GIANT = { '-', 'U', 'I', 'I', 'I', 'I', 'I', 'I', 'H' };
	private static final char[] G_BRIGHT_GIANT = { '-', 'U', 'I', 'I', 'I', 'I', 'I', 'I', 'H' };
	private static final char[] K_BRIGHT_GIANT = { '-', '-', 'U', 'I', 'I', 'I', 'I', 'I', 'I', 'H' };
	private static final char[] M_BRIGHT_GIANT = { '-', '-', '-', '-', '-', '-', 'I', 'I', 'I', 'I', 'I', 'H' };

	// extended 'I' to represent orbit zero
	private static final char[] A_GIANT = { 'I', 'I', 'I', 'I', 'I', 'I', 'I', 'H' };
	private static final char[] F_GIANT = { 'I', 'I', 'I', 'I', 'I', 'I', 'H' };
	private static final char[] G_GIANT = { 'I', 'I', 'I', 'I', 'I', 'I', 'I', 'H' };
	private static final char[] K_GIANT = { 'I', 'I', 'I', 'I', 'I', 'I', 'I', 'I', 'H' };
	private static final char[] M_GIANT = { 'I', 'I', 'I', 'I', 'I', 'I', 'I', 'I', 'I', 'H' };

	// already included orbit zero
	private static final char[] A_SUBGIANT = { 'I', 'I', 'I', 'I', 'I', 'I', 'H' };
	private static final char[] F_SUBGIANT = { 'I', 'I', 'I', 'I', 'I', 'H' };
	private static final char[] G_SUBGIANT = { 'I', 'I', 'I', 'I', 'I', 'H' };

	// already included orbit zero
	private static final char[] A_SEQUENCE = { 'I', 'I', 'I', 'I', 'I', 'I', 'H' };
	private static final char[] F_SEQUENCE = { 'I', 'I', 'I', 'I', 'H' };
	private static final char[] G_SEQUENCE = { 'I', 'I', 'H' };
	private static final char[] K_SEQUENCE = { 'H' };
	private static final char[] M_SEQUENCE = new char[0];

	// already included orbit zero
	private static final char[] A_DWARF = new char[0];
	private static final char[] F_DWARF = new char[0];
	private static final char[] G_DWARF = new char[0];
	private static final char[] K_DWARF = new char[0];
	private static final char[] M_DWARF = new char[0];

	// already included orbit zero
	private static final char[] F_SUBDWARF = { 'I', 'I', 'I', 'H' };
	private static final char[] G_SUBDWARF = { 'I', 'H' };
	private static final char[] K_SUBDWARF = new char[0];
	private static final char[] M_SUBDWARF = new char[0];

	public static int nature() {
		int n = Dice.deeRoll(2, 6);
		if (n <= 7)
			n = 1;
		else if (n <= 11)
			n = 2;
		else
			n = 3;

		return n;
	}

	public static char primaryType(int n) {
		char c;
		if (n <= 2)
			c = 'A';
		else if (n <= 7)
			c = 'M';
		else if (n == 8)
			c = 'K';
		else if (n == 9)
			c = 'G';
		else
			c = 'F';

		return c;
	}

	public static char companionType(int n) {
		char c;
		if (n <= 2)
			c = 'A';
		else if (n <= 4)
			c = 'F';
		else if (n <= 6)
			c = 'G';
		else if (n <= 8)
			c = 'K';
		else
			c = 'M';

		return c;
	}

	public static char primarySize(int n) {
		char c;
		if (n == 2)
			c = '2'; // bright giant
		else if (n == 3)
			c = '3'; // giant
		else if (n == 4)
			c = '4'; // sub-giant
		else if (n <= 10)
			c = '5'; // main sequence
		else if (n <= 11)
			c = '6'; // sub-dwarf
		else
			c = 'D'; // dwarf star

		return c;
	}

	public static boolean sizeTypeVerify(char a, char b) {
		boolean isVerified = true;
		if (a != 'F' && a != 'G') {
			if (a == 'A' && b == '6')
				isVerified = false;
			if (a == 'K' && b == '4')
				isVerified = false;
			if (a == 'M' && b == '4')
				isVerified = false;
		}

		return isVerified;
	}

	public static char companionSizeType(int n) {
		char c;
		if (n == 2)
			c = '2'; // bright giant
		else if (n == 3)
			c = '3'; // giant
		else if (n == 4)
			c = '4'; // sub-giant
		else if (n <= 6)
			c = 'D'; // dwarf star
		else if (n <= 8)
			c = '5'; // main sequence
		else if (n == 9)
			c = '6'; // sub-dwarf
		else
			c = 'D'; // dwarf star cont'd

		return c;
	}

	public static int companionOrbit(Star[] nature) {
		char type = nature[0].getStarType();
		char size = nature[0].getSizeType();
		char[] zones = findZone(type, size);

		int n = Dice.deeRoll(2, 6);
		if (nature.length > 2)
			n += 4;

		if (n <= 3)
			n = 0;
		else if (n <= 6)
			n -= 3;
		else if (n <= 11) {
			n -= 3;
			n += Dice.deeRoll(1, 6);
		} else {
			n = 99;
		}

		// sets orbit "inside star" instead to "close" (zero)
		if (n < zones.length && zones[n] == '-')
			n = 0;

		return n;
	}

	public static int maxOrbits(Star[] nature, int index) {
		int maxOrbits = Dice.deeRoll(2, 6);
		char type = nature[index].getStarType();
		char size = nature[index].getSizeType();

		if (size == '2')
			maxOrbits += 8;
		else if (size == '3')
			maxOrbits += 4;

		if (type == 'M')
			maxOrbits -= 4;
		else if (type == 'K')
			maxOrbits -= 2;

		// verify number of orbits between zero and fifteen
		if (maxOrbits < 0)
			maxOrbits = 0;
		if (maxOrbits > 15)
			maxOrbits = 15;

		return maxOrbits;
	}

	public static char[] findZone(char type, char size) {
		switch (size) {
		case '2':
			switch (type) {
			case 'A':
				return A_BRIGHT_GIANT;
			case 'F':
				return F_BRIGHT_GIANT;
			case 'G':
				return G_BRIGHT_GIANT;
			case 'K':
				return K_BRIGHT_GIANT;
			case 'M':
				return M_BRIGHT_GIANT;
			default:
				return new char[0];
			}
		case '3':
			switch (type) {
			case 'A':
				return A_GIANT;
			case 'F':
				return F_GIANT;
			case 'G':
				return G_GIANT;
			case 'K':
				return K_GIANT;
			case 'M':
				return M_GIANT;
			default:
				return new char[0];
			}
		case '4':
			switch (type) {
			case 'A':
				return A_SUBGIANT;
			case 'F':
				return F_SUBGIANT;
			case 'G':
				return G_SUBGIANT;
			default:
				return new char[0];
			}
		case '5':
			switch (type) {
			case 'A':
				return A_SEQUENCE;
			case 'F':
				return F_SEQUENCE;
			case 'G':
				return G_SEQUENCE;
			case 'K':
				return K_SEQUENCE;
			case 'M':
				return M_SEQUENCE;
			default:
				return new char[0];
			}
		case 'D':
			switch (type) {
			case 'A':
				return A_DWARF;
			case 'F':
				return F_DWARF;
			case 'G':
				return G_DWARF;
			case 'K':
				return K_DWARF;
			case 'M':
				return M_DWARF;
			default:
				return new char[0];
			}
		case '6':
			switch (type) {
			case 'F':
				return F_SUBDWARF;
			case 'G':
				return G_SUBDWARF;
			case 'K':
				return K_SUBDWARF;
			case 'M':
				return M_SUBDWARF;
			default:
				return new char[0];
			}
		default:
			return new char[0];
		// END OF SWITCH
		}
	}

	public static int maxEmptyOrbits(Star[] nature, int index) {
		char size = nature[index].getSizeType();
		// TODO this is a shortcut - 'actual' empty orbits is slightly different
		return numCapturedWorlds(size);
	}

	public static int availableOrbits(boolean[] orbits) {
		int n = 0;
		for (boolean el : orbits) {
			if (!el)
				++n;
		}

		return n;
	}

	public static int findAvailableOrbit(boolean[] options, int choice, int its) {
		if (its > 15 && availableOrbits(options) > 0) {
			for (int i = 0; i < options.length; ++i) {
				if (!options[i])
					return i;
			}
		} else if (options[choice]) {
			choice = findAvailableOrbit(options, Dice.deeRoll(1, options.length) - 1, its + 1);
		} else {
			return choice;
		}

		return choice;
	}

	public static boolean[] placeEmptyOrbits(Star[] nature, int index, int maxOrbits, int toBeEmptied) {
		boolean[] emptyOrbits = new boolean[maxOrbits];
		char type = nature[index].getStarType();
		char size = nature[index].getSizeType();
		char[] zones = findZone(type, size);

		// initialize Empty Orbits
		if (emptyOrbits.length > 0) {
			for (int i = 0; i < emptyOrbits.length; ++i) {
				emptyOrbits[i] = false;
			}
		}

		// empty orbits based on zones
		if (zones.length > 0 && emptyOrbits.length > 0) {
			for (int i = 0; i < zones.length && i < emptyOrbits.length; ++i) {
				if (zones[i] != 'I' && zones[i] != 'H')
					emptyOrbits[i] = true;
			}
		}

		// empty companion orbit, et cetera
		if (index == 0 && nature.length > 1) {
			int companionOrbit = nature[1].getOrbit();
			int companionMin = (int) (companionOrbit * 0.5);
			int companionMax = ((companionOrbit + 3) > maxOrbits) ? maxOrbits : companionOrbit + 3;

			for (int i = companionMin; i < companionOrbit && i < emptyOrbits.length; ++i) {
				emptyOrbits[i] = true;
			}
			for (int i = companionOrbit; i < companionMax && i < emptyOrbits.length; ++i) {
				emptyOrbits[i] = true;
			}

			// for trinary systems
			if (index == 0 && nature.length > 2) {
				companionOrbit = nature[2].getOrbit();
				companionMin = (int) (companionOrbit * 0.5);
				companionMax = ((companionOrbit + 3) > maxOrbits) ? maxOrbits : companionOrbit + 3;

				for (int i = companionMin; i < companionOrbit && i < emptyOrbits.length; ++i) {
					emptyOrbits[i] = true;
				}
				for (int i = companionOrbit; i < companionMax && i < emptyOrbits.length; ++i) {
					emptyOrbits[i] = true;
				}
			}
		}

		// if remaining orbits <= orbits To Be Emptied, return early
		if (toBeEmptied >= availableOrbits(emptyOrbits)) {
			for (int i = 0; i < emptyOrbits.length; ++i) {
				emptyOrbits[i] = true;
			}
			// early exit
			return emptyOrbits;
		}

		while (toBeEmptied > 0 && availableOrbits(emptyOrbits) > 1) {
			emptyOrbits[findAvailableOrbit(emptyOrbits, Dice.deeRoll(1, emptyOrbits.length - 1), 1)] = true;
			--toBeEmptied;
		}

		return emptyOrbits;
	}

	public static int numCapturedWorlds(char size) {
		int i = (size != 'A') ? 0 : 1;
		int capturedWorlds = (Dice.deeRoll(1, 6) + i < 5) ? 0 : Dice.deeRoll(1, 3);

		return capturedWorlds;
	}

	public static Celestial[] placeComponents(Star star) {
		Celestial[] components = new Celestial[star.getMaxOrbits()];
		/*
		 * if components is empty, GTFO
		 */
		if (components.length < 1) {
			return components;
		} else {
			// initialize empty orbits
			boolean[] empties = star.getEmptyOrbits();
			for (int i = 0; i < components.length; ++i) {
				if (empties[i])
					components[i] = new World(star, i, 'E');
			}
		}

		// determine number of giants, then planetoids - then place both
		int numGiants = (hasGiants()) ? maxGiants() : 0;
		if (numGiants > star.getMaxOrbits())
			numGiants = star.getMaxOrbits();
		if (numGiants > 0 && availableHabitable(star, components) < 1) {
			int i = components.length - 1;
			components[i] = new World(star, i, 'G');
			numGiants = 1;
		}
		int numPlanetoids = (hasPlanetoids(numGiants)) ? maxPlanetoids(numGiants) : 0;

		// place giants, then planetoids
		int offset = startOfHabitableZone(star);
		int range = components.length - offset;
		int choice = 0;
		while (numGiants > 0 && availableHabitable(star, components) > 1) {
			choice = Dice.deeRoll(1, range) - 1 + offset;
			int i = findHabitable(components, range, offset, choice, 1);
			components[i] = new World(star, i, 'G');
			--numGiants;
		}

		while (numPlanetoids > 0 && availableOrbits(components) > 1) {
			choice = components.length - 1;
			int i = placePlanetoid(components, choice, 1);
			components[i] = new World(star, i, 'P');
			--numPlanetoids;
		}

		// fill in all remaining available orbits with worlds
		for (int i = 0; i < components.length; ++i) {
			if (components[i] == null)
				components[i] = new World(star, i);
		}

		return components;
	}

	private static int availableOrbits(Celestial[] components) {
		int availableOrbits = 0;
		for (Celestial el : components) {
			if (el != null)
				++availableOrbits;
		}

		return availableOrbits;
	}

	private static boolean hasGiants() {
		boolean hasGiants = (Dice.deeRoll(2, 6) < 10) ? true : false;
		return hasGiants;
	}

	private static int maxGiants() {
		int n = Dice.deeRoll(2, 6);
		int numGiants = 0;

		if (n <= 3)
			numGiants = 1;
		else if (n <= 5)
			numGiants = 2;
		else if (n <= 7)
			numGiants = 3;
		else if (n <= 10)
			numGiants = 4;
		else if (n >= 11)
			numGiants = 5;

		return numGiants;
	}

	private static boolean hasHabitableZone(Star star) {
		char[] zones = findZone(star.getStarType(), star.getSizeType());
		for (char el : zones) {
			if (el == 'H')
				return true;
		}
		return false;
	}

	private static int startOfHabitableZone(Star star) {
		if (!(hasHabitableZone(star)))
			return 0;
		else {
			char[] zones = findZone(star.getStarType(), star.getSizeType());
			int startOfHabitableZone = 0;
			for (int i = 0; i < zones.length; ++i) {
				if (zones[i] == 'H') {
					startOfHabitableZone = i;
					break;
				}
			}

			return startOfHabitableZone;
		}
	}

	public static int availableHabitable(Star star, Celestial[] components) {
		int offset = startOfHabitableZone(star);
		int n = 0;

		for (int i = offset; i < components.length; ++i) {
			if (!(components[i] != null))
				++n;
		}

		return n;
	}

	private static int findHabitable(Celestial[] components, int range, int offset, int choice, int its) {
		if (its > range) {
			for (int i = offset; i < components.length; ++i) {
				if (components[i] == null)
					return i;
			}
		} else if (components[choice] == null) {
			return choice;
		} else if (components[choice] != null) {
			choice = Dice.deeRoll(1, range) - 1 + offset;
			choice = findHabitable(components, range, offset, choice, its + 1);
		}

		return choice;
	}

	private static boolean hasPlanetoids(int giants) {
		boolean hasPlanetoids = ((Dice.deeRoll(2, 6) - giants) < 7) ? true : false;
		return hasPlanetoids;
	}

	private static int maxPlanetoids(int giants) {
		int n = Dice.deeRoll(2, 6) - giants;
		int numPlanetoids = 1;

		if (n <= 1)
			numPlanetoids = 3;
		else if (n <= 6)
			numPlanetoids = 2;

		return numPlanetoids;
	}

	private static int placePlanetoid(Celestial[] components, int choice, int its) {
		if (its > components.length) {
			for (int i = components.length - 1; i > 0; --i) {
				if (components[i] == null)
					return i;
			}
		} else if (components[choice] == null) {
			return choice;
		} else if (components[choice] != null) {
			choice = Dice.deeRoll(1, components.length) - 1;
			choice = placePlanetoid(components, choice, its + 1);
		}

		return choice;
	}

	public static Celestial[] placeCapturedWorlds(Star star) {
		char size = star.getSizeType();
		Celestial[] capturedWorlds = new Celestial[numCapturedWorlds(size)];
		char[] zones = star.getZones();
		int maxOrbits = star.getMaxOrbits();

		// if there are unihabitable zones, we create an offset
		int offset = 0;
		if (zones != null && zones.length > 0) {
			for (char el : zones) {
				if (el == '-' || el == 'U')
					++offset;
			}
		}
		// it's important that range is greater/equal to one
		int range = (maxOrbits > offset) ? maxOrbits - offset : 1;
		// set orbit, which is the lesser of offset and maxOrbits
		int orbit = (offset < maxOrbits) ? offset : maxOrbits;

		for (int i = 0; i < capturedWorlds.length; ++i) {
			orbit = Dice.deeRoll(1, range) - 1;
			capturedWorlds[i] = new World(star, orbit, 'C');
		}

		return capturedWorlds;
	}

	public static int worldSize(Star star, World world) {
		int worldSize = Dice.deeRoll(2, 6) - 2;
		int orbit = world.getOrbit();

		// closer orbits produce smaller worlds
		if (orbit == 0)
			worldSize -= 5;
		else if (orbit == 1)
			worldSize -= 4;
		else if (orbit == 2)
			worldSize -= 3;

		// M-class stars produce smaller worlds
		char type = star.getStarType();
		if (type == 'M')
			worldSize -= 2;

		/*
		 * size '0' was "reserved" for asteroid worlds from the basic generation rules,
		 * but forget that noise -- I'm going to ignore the 'S' class worlds and use
		 * zero
		 */
		if (worldSize < 1)
			worldSize = 0;
		return worldSize;
	}

	public static int worldAtmosphere(Star star, World world) {
		int atmosphere = Dice.deeRoll(2, 6) - 7 + world.getSize();
		char[] zones = star.getZones();
		int orbit = world.getOrbit();

		// Inner zone -2 || Outer zone -4
		if (orbit < zones.length) {
			if (zones[orbit] == 'I')
				atmosphere -= 2;
		} else
			atmosphere -= 4;

		if (orbit - zones.length > 1) {
			// unique case produces "exotic" atmosphere in Outer zone
			atmosphere = (Dice.deeRoll(2, 6) == 12) ? 10 : atmosphere;
		}

		if (atmosphere < 0)
			atmosphere = 0;
		if (atmosphere > 15)
			atmosphere = 15;
		return atmosphere;
	}

	public static int worldHydrosphere(Star star, World world) {
		char[] zones = star.getZones();
		int orbit = world.getOrbit();
		int size = world.getSize();
		int atmo = world.getAtmosphere();

		int hydrosphere = Dice.deeRoll(2, 6) - 7 + size;

		// Atmosphere too thin/dense means less water
		if (atmo < 2 || atmo > 9)
			hydrosphere -= 4;

		// Inner zone evaporates water || Outer zones freeze
		if (orbit < zones.length) {
			if (zones[orbit] == 'I')
				hydrosphere = 0;
		} else if (orbit - zones.length > 1)
			hydrosphere -= 2;

		// Small worlds can't hold water
		if (size < 2)
			hydrosphere = 0;

		if (hydrosphere < 0)
			hydrosphere = 0;
		if (hydrosphere > 10)
			hydrosphere = 10;
		return hydrosphere;
	}

	public static int worldPopulation(Star star, World world) {
		int population = Dice.deeRoll(2, 6) - 2;
		int[] viableAtmo = { 0, 5, 6, 8 };

		char[] zones = star.getZones();
		int orbit = world.getOrbit();
		int atmo = world.getAtmosphere();

		// Inner zone is too hot, Outer zone is too cold
		if (orbit < zones.length) {
			if (zones[orbit] == 'I')
				population -= 5;
		} else if (orbit - zones.length >= 0)
			population -= 3;

		// compares atmosphere to set of viable atmospheres
		boolean viable = false;
		for (int el : viableAtmo) {
			if (atmo == el)
				viable = true;
		}
		if (!viable)
			population -= 2;

		if (population < 0)
			population = 0;
		return population;
	}

	public static int satelliteSize(World world) {
		int size = world.getSize() - Dice.deeRoll(1, 6);

		if (world.isGiant() && world.getSize() == 99) {
			size = Dice.deeRoll(2, 6) - 4;
		} else if (world.isGiant() && world.getSize() == 50) {
			size = Dice.deeRoll(2, 6) - 6;
		}

		return size;
	}

	public static int satelliteAtmosphere(World world, World satellite) {
		int size = satellite.getSize();
		int atmosphere = Dice.deeRoll(2, 6) - 7 + size;

		// inner zone burns atmosphere || outer freezes
		char[] zones = (world.getStar()).getZones();
		int orbit = world.getOrbit();
		if (orbit < zones.length) {
			if (zones[orbit] == 'I')
				atmosphere = 0;
		} else if (orbit - zones.length >= 0)
			atmosphere -= 4;

		// atmosphere can't initially be more than 9
		if (atmosphere > 9)
			atmosphere = 9;

		// chance for exotic atmosphere
		if (orbit - zones.length > 1) {
			if (Dice.deeRoll(2, 6) == 12)
				atmosphere = 10;
		}

		// tiny satellite has no atmosphere
		if (size < 2)
			atmosphere = 0;

		if (atmosphere < 0)
			atmosphere = 0;
		return atmosphere;
	}

	public static int satelliteHydrosphere(World world, World satellite) {
		int size = satellite.getSize();
		int atmosphere = satellite.getAtmosphere();
		int hydrosphere = Dice.deeRoll(2, 6) - 7 + size;

		// inner zone evaporates water || outer freezes
		char[] zones = (world.getStar()).getZones();
		int orbit = world.getOrbit();
		if (orbit < zones.length) {
			if (zones[orbit] == 'I')
				hydrosphere = 0;
		} else if (orbit - zones.length >= 0)
			hydrosphere -= 4;

		// too much or too little atmosphere hurts water
		if (atmosphere < 2 || atmosphere > 9)
			hydrosphere -= 4;

		// tiny rock can't hold water
		if (size < 1)
			hydrosphere = 0;

		if (hydrosphere < 0)
			hydrosphere = 0;
		return hydrosphere;
	}

	public static int satellitePopulation(Star star, World world) {
		int population = Dice.deeRoll(2, 6) - 2;
		int[] viableAtmo = { 5, 6, 8 };

		char[] zones = star.getZones();
		int orbit = world.getOrbit();
		int atmo = world.getAtmosphere();

		// Inner zone is too hot, Outer zone is too cold
		if (orbit < zones.length) {
			if (zones[orbit] == 'I')
				population -= 5;
		} else if (orbit - zones.length >= 0)
			population -= 4;

		// compares atmosphere to set of viable atmospheres
		boolean viable = false;
		for (int el : viableAtmo) {
			if (atmo == el)
				viable = true;
		}
		if (!viable)
			population -= 2;

		if (world.isRing())
			population = 0;

		if (population < 0)
			population = 0;
		return population;
	}

	public static int mainGovernment(World world) {
		int population = world.getPopulation();
		int government = Dice.deeRoll(2, 6) - 7 + population;

		if (government < 0)
			government = 0;
		return government;
	}

	public static int mainLaw(int government) {
		int law = Dice.deeRoll(2, 6) - 7 + government;

		if (law < 0)
			law = 0;
		return law;
	}

	public static char mainSpaceport() {
		char[] types = { 'A', 'A', 'A', 'B', 'B', 'C', 'C', 'D', 'E', 'E', 'X' };
		char spaceport = types[Dice.deeRoll(2, 6) - 2];

		return spaceport;
	}

	public static int mainTechLevel(World world, Faction faction) {
		char spaceport = faction.getSpaceport();
		int government = faction.getGovernmentScore();
		int techLevel = Dice.deeRoll(1, 6);

		if (spaceport == 'A')
			techLevel += 6;
		else if (spaceport == 'B')
			techLevel += 4;
		else if (spaceport == 'C')
			techLevel += 2;
		else if (spaceport == 'X')
			techLevel -= 4;

		int[] size = { 2, 2, 1, 1, 1, 0, 0, 0, 0, 0, 0 };
		int[] atmo = { 1, 1, 1, 1, 0, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 0 };
		int[] hydr = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 2 };
		int[] pops = { 0, 1, 1, 1, 1, 1, 0, 0, 0, 2, 4 };
		int[] govt = { 1, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, -2, 0, 0 };

		techLevel += (world.getSize() < size.length) ? size[world.getSize()] : 0;
		techLevel += (world.getAtmosphere() < atmo.length) ? atmo[world.getAtmosphere()] : 0;
		techLevel += (world.getHydrosphere() < hydr.length) ? hydr[world.getHydrosphere()] : 0;
		techLevel += (world.getPopulation() < pops.length) ? pops[world.getPopulation()] : 0;
		techLevel += (government < govt.length) ? govt[government] : 0;

		if (techLevel < 0)
			techLevel = 0;
		return techLevel;
	}

	public static boolean[] mainTradeCodes(World world, Faction faction) {
		boolean[] tradeCodes = new boolean[15];

		tradeCodes[0] = hasAgriculture(world);
		tradeCodes[1] = isAsteroid(world);
		tradeCodes[2] = isBarren(faction, world);
		tradeCodes[3] = isDesert(world);
		tradeCodes[4] = hasFluidOceans(world);
		tradeCodes[5] = hasHighPopulation(world);
		tradeCodes[6] = isIceCapped(world);
		tradeCodes[7] = isIndustrial(world);
		tradeCodes[8] = hasLowPopulation(world);
		tradeCodes[9] = isNonAgricultural(world);
		tradeCodes[10] = isNonIndustrial(world);
		tradeCodes[11] = isPoor(world);
		tradeCodes[12] = isRich(faction, world);
		tradeCodes[13] = isVacuum(world);
		tradeCodes[14] = isWaterWorld(world);

		return tradeCodes;
	}

	private static boolean hasAgriculture(World world) {
		boolean hasAgriculture = false;
		//
		boolean hasAtmo = false;
		boolean hasHydro = false;
		boolean hasPop = false;

		int atmosphere = world.getAtmosphere();
		int hydrosphere = world.getHydrosphere();
		int population = world.getPopulation();

		if (atmosphere >= 4 && atmosphere <= 9)
			hasAtmo = true;
		if (hydrosphere >= 4 && hydrosphere <= 8)
			hasHydro = true;
		if (population >= 5 && population <= 9)
			hasPop = true;

		if (hasAtmo && hasHydro && hasPop)
			hasAgriculture = true;

		return hasAgriculture;
	}

	private static boolean isAsteroid(World world) {
		boolean isAsteroid = false;

		int size = world.getSize();
		int atmo = world.getAtmosphere();
		int hydro = world.getHydrosphere();

		if (size == 0 && atmo == 0 && hydro == 0)
			isAsteroid = true;

		return isAsteroid;
	}

	private static boolean isBarren(Faction faction, World world) {
		boolean isBarren = false;

		int pop = world.getPopulation();
		int gov = faction.getGovernmentScore();
		int law = faction.getLawScore();

		if (pop == 0 && gov == 0 && law == 0)
			isBarren = true;

		return isBarren;
	}

	private static boolean isDesert(World world) {
		boolean isDesert = false;

		int atmo = world.getAtmosphere();
		int hydro = world.getHydrosphere();

		if (atmo >= 2 && hydro == 0)
			isDesert = true;

		return isDesert;
	}

	private static boolean hasFluidOceans(World world) {
		boolean hasFluidOceans = false;

		int atmo = world.getAtmosphere();
		int hydro = world.getHydrosphere();

		if (atmo >= 10 && hydro >= 1)
			hasFluidOceans = true;

		return hasFluidOceans;
	}

	private static boolean hasHighPopulation(World world) {
		boolean hasHighPopulation = false;

		int population = world.getPopulation();
		if (population >= 9)
			hasHighPopulation = true;

		return hasHighPopulation;
	}

	private static boolean isIceCapped(World world) {
		boolean isIceCapped = false;

		int atmo = world.getAtmosphere();
		int hydro = world.getHydrosphere();

		if (atmo <= 1 && hydro >= 1)
			isIceCapped = true;

		return isIceCapped;
	}

	private static boolean isIndustrial(World world) {
		boolean isIndustrial = false;

		int atmo = world.getAtmosphere();
		int pop = world.getPopulation();

		if ((atmo <= 2 || atmo == 4 || atmo == 7 || atmo == 9) && pop >= 9)
			isIndustrial = true;

		return isIndustrial;
	}

	private static boolean hasLowPopulation(World world) {
		boolean hasLowPopulation = false;

		int pop = world.getPopulation();
		if (pop <= 3)
			hasLowPopulation = true;

		return hasLowPopulation;
	}

	private static boolean isNonAgricultural(World world) {
		boolean isNonAgricultural = false;

		int atmo = world.getAtmosphere();
		int hydro = world.getHydrosphere();
		int pop = world.getPopulation();

		if (atmo <= 3 && hydro <= 3 && pop >= 6)
			isNonAgricultural = true;

		return isNonAgricultural;
	}

	private static boolean isNonIndustrial(World world) {
		boolean isNonIndustrial = false;

		int pop = world.getPopulation();
		if (pop <= 6)
			isNonIndustrial = true;

		return isNonIndustrial;
	}

	private static boolean isPoor(World world) {
		boolean isPoor = false;

		int atmo = world.getAtmosphere();
		int hydro = world.getHydrosphere();

		if ((atmo >= 2 && atmo <= 5) && hydro <= 3)
			isPoor = true;

		return isPoor;
	}

	private static boolean isRich(Faction faction, World world) {
		boolean isRich = false;

		int atmo = world.getAtmosphere();
		int pop = world.getPopulation();
		int gov = faction.getGovernmentScore();

		if ((atmo == 6 && atmo == 8) && (pop >= 6 && pop <= 8) && (gov >= 4 && gov <= 9))
			isRich = true;

		return isRich;
	}

	private static boolean isVacuum(World world) {
		boolean isVacuum = false;

		int atmo = world.getAtmosphere();
		if (atmo == 0)
			isVacuum = true;

		return isVacuum;
	}

	private static boolean isWaterWorld(World world) {
		boolean isWaterWorld = false;

		int hydro = world.getHydrosphere();
		if (hydro == 10)
			isWaterWorld = true;

		return isWaterWorld;
	}

	// END OF CODE
}