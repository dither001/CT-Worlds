/********************************************************
 * Author: Nick Foster
 *
 * Last edited: 11/3/2017
 ********************************************************/

public class Star implements Celestial {
	// fields
	private Star[] nature;
	private int index;

	private int type;
	private char starType;
	private int size;
	private char sizeType;

	private int maxOrbits;
	private Celestial[] components;
	private Celestial[] captured;

	// if not primary, then random orbit >= 0
	private boolean isPrimary;
	private int orbit;

	/*
	 * TEMPORARY
	 */
	private boolean[] emptyOrbits;
	// private int gasGiants;
	// private int planetoids;

	// constructors
	public Star(Star[] nature, int index) {
		isPrimary = true;
		this.nature = nature;

		if (index < 1) {
			// primary star
			do {
				type = Dice.deeRoll(2, 6);
				starType = Generator.primaryType(type);
				size = Dice.deeRoll(2, 6);

				sizeType = Generator.primarySize(size);
			} while (!Generator.sizeTypeVerify(starType, sizeType));

			orbit = 0;

		} else {
			// secondary, tertiary star
			isPrimary = false;
			char primaryType = nature[0].getStarType();
			char primarySize = nature[0].getSizeType();
			int zones = (Generator.findZone(primaryType, primarySize)).length;

			do {
				type = Dice.deeRoll(2, 6) + Generator.primaryType(primaryType);
				starType = Generator.companionType(type);

				size = Dice.deeRoll(2, 6) + Generator.primarySize(primarySize);
				sizeType = Generator.companionSizeType(size);
			} while (!Generator.sizeTypeVerify(starType, sizeType));

			orbit = Generator.companionOrbit(nature);
		}
		// END OF CONSTRUCTOR
	}

	// methods
	public boolean isEmpty() {
		return false;
	}

	public boolean isStar() {
		return true;
	}

	public boolean isGiant() {
		return false;
	}

	public boolean isPlanetoid() {
		return false;
	}

	public boolean isCaptured() {
		return false;
	}

	public boolean isWorld() {
		return false;
	}

	public int getPopulation() {
		return 0;
	}

	public World[] getSatellites() {
		return null;
	}

	public void setFaction(boolean isMainWorld) {
		return;
	}

	public void printFaction() {
		return;
	}

	//
	public int getType() {
		return type;
	}

	public char getStarType() {
		return starType;
	}

	public int getSize() {
		return size;
	}

	public char getSizeType() {
		return sizeType;
	}

	public int getMaxOrbits() {
		return maxOrbits;
	}

	public boolean[] getEmptyOrbits() {
		return emptyOrbits;
	}

	public Celestial[] getComponents() {
		if (components == null)
			return new Celestial[0];
		else
			return components;
	}

	public Celestial[] getCaptured() {
		if (captured == null)
			return new Celestial[0];
		else
			return captured;
	}

	public void setMaxOrbits(Star[] nature, int index) {
		// max number of orbitals
		maxOrbits = Generator.maxOrbits(nature, index);
	}

	public void setEmptyOrbits(Star[] nature, int index) {
		// determine number of empty orbits
		emptyOrbits = new boolean[maxOrbits];
		int maxEmpty = Generator.maxEmptyOrbits(nature, index);

		if (maxEmpty > maxOrbits)
			maxEmpty = 0;
		emptyOrbits = Generator.placeEmptyOrbits(nature, index, maxOrbits, maxEmpty);
	}

	public void capturedWorlds(Star[] nature, int index) {
		// determine number and placement of captured worlds
		captured = Generator.placeCapturedWorlds(this);
	}

	public void placeComponents() {
		// determines number and placement of gas giants and planetoids
		components = Generator.placeComponents(this);
	}

	public int getOrbit() {
		return orbit;
	}

	public char[] getZones() {
		char[] zones = Generator.findZone(starType, sizeType);
		return zones;
	}

	public String printEmpty() {
		String s = "none";

		if (emptyOrbits.length > 0) {
			s = "";
			for (int i = 0; i < emptyOrbits.length; ++i) {
				if (emptyOrbits[i])
					s += String.format("%d, ", i);
				else
					s += "a, ";
			}
		}

		return s;
	}

	public String printComponents() {
		String s = "none";
		if (components.length > 0) {
			s = String.format("%n");
			for (int i = 0; i < components.length; ++i) {
				if (i > 0)
					s += String.format("%n");
				s += components[i].toString();
			}
		}

		return s;
	}

	public String printCaptured() {
		String s = "";
		if (captured.length > 0) {
			s = String.format("%n");
			for (int i = 0; i < captured.length; ++i) {
				if (i > 0)
					s += String.format("%n");
				s += captured[i].toString();
			}
		}

		return s;
	}

	@Override
	public String toString() {
		int outside = maxOrbits - (getZones()).length;
		String capturedObjects = "";

		String star = (!isPrimary) ? String.format("%n") : "";
		String zone = String.valueOf(Generator.findZone(starType, sizeType));

		star += String.format("%c%c || Position: %d | Orbital(s): %d", starType, sizeType, orbit, maxOrbits);
		String componentObjects = String.format("%nComponents: %s", printComponents());

		if (captured != null && captured.length > 0) {
			capturedObjects = String.format("%nCaptured: %s", printCaptured());
		}

		if (zone.length() > 0)
			star += String.format("%n%s", zone);

		return star + componentObjects + capturedObjects;
	}

	@Override
	public String[] getTradeCodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public char getSpaceport() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public World toWorld() {
		// TODO Auto-generated method stub
		return null;
	}
}