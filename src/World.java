/********************************************************
 * Author: Nick Foster
 *
 * Last edited: 11/3/2017
 ********************************************************/

public class World implements Celestial {
	// fields
	private StarSystem system;
	private Star star;
	private boolean isEmpty;
	private boolean isGiant;
	private boolean isPlanetoid;
	private boolean isCaptured;
	private boolean isSatellite;
	private boolean isRing;

	private int orbit;
	private int size;
	private int atmosphere;
	private int hydrosphere;
	private int population;
	private int government;
	private int lawLevel;

	private Celestial[] satellites;

	private Faction faction;

	// constructors
	public World(Star star, int orbit) {
		this(star, orbit, '0');
	}

	public World(Star star, int orbit, char worldType) {
		this.star = star;
		this.orbit = orbit;
		isEmpty = false;
		isGiant = false;
		isPlanetoid = false;
		isCaptured = false;
		isSatellite = false;
		isRing = false;

		int numSatellites = 0;

		// all world types are mutually exclusive
		if (worldType != '0') {
			if (worldType == 'E')
				isEmpty = true;
			if (worldType == 'G')
				isGiant = true;
			if (worldType == 'P')
				isPlanetoid = true;
			if (worldType == 'C')
				isCaptured = true;
		}

		if (isEmpty || isPlanetoid) {
			return;
		} else if (isGiant) {
			if (Dice.deeRoll(1, 2) == 1) {
				size = 50;
				numSatellites = Dice.deeRoll(2, 6) - 4;
			} else {
				size = 99;
				numSatellites = Dice.deeRoll(2, 6);
			}
		} else {
			size = Generator.worldSize(star, this);
			atmosphere = Generator.worldAtmosphere(star, this);
			hydrosphere = Generator.worldHydrosphere(star, this);
			population = Generator.worldPopulation(star, this);
			numSatellites = (size > 0) ? Dice.deeRoll(1, 6) - 3 : 0;
		}

		// last step is satellite generation
		if (size > 0 && numSatellites > 0) {
			satellites = new World[numSatellites];
			for (int i = 0; i < satellites.length; ++i) {
				satellites[i] = new World(this);
			}
		}

	}

	// satellite generation
	public World(World world) {
		// star and orbit belong to
		this.star = world.getStar();
		this.orbit = world.getOrbit();
		isEmpty = false;
		isGiant = false;
		isPlanetoid = false;
		isCaptured = false;
		isSatellite = true;
		isRing = false;

		// satellite size
		size = Generator.satelliteSize(world);
		if (size == 0)
			isRing = true;
		else if (size < 0)
			size = 0;

		atmosphere = Generator.satelliteAtmosphere(world, this);
		hydrosphere = Generator.satelliteHydrosphere(world, this);
		population = Generator.satellitePopulation(star, this);

	}

	// methods
	public boolean isEmpty() {
		return isEmpty;
	}

	public boolean isStar() {
		return false;
	}

	public boolean isGiant() {
		return isGiant;
	}

	public boolean isPlanetoid() {
		return isPlanetoid;
	}

	public boolean isCaptured() {
		return isCaptured;
	}

	public boolean isSatellite() {
		return isSatellite;
	}

	public boolean isRing() {
		return isRing;
	}

	public boolean isWorld() {
		boolean isWorld = false;
		if (this.isEmpty() != true && this.isGiant() != true && this.isPlanetoid() != true && this.isRing() != true) {
			isWorld = true;
		}

		return isWorld;
	}

	public Celestial[] getComponents() {
		return null;
	}

	public Celestial[] getCaptured() {
		return null;
	}

	public Star getStar() {
		return star;
	}

	public int getOrbit() {
		return orbit;
	}

	public int getSize() {
		return size;
	}

	public int getAtmosphere() {
		return atmosphere;
	}

	public int getHydrosphere() {
		return hydrosphere;
	}

	public int getPopulation() {
		return population;
	}
	
	public int getGovernment() {
		return government;
	}
	
	public int getLawLevel() {
		return lawLevel;
	}

	public Celestial[] getSatellites() {
		if (satellites == null)
			return new Celestial[0];
		else
			return satellites;
	}

//	public int getGovernment() {
//		if (faction != null)
//			return faction.getGovernmentScore();
//		else
//			return 0;
//	}
//
//	public int getLawLevel() {
//		if (faction != null)
//			return faction.getLawScore();
//		else
//			return 0;
//	}
//
//	public int getTechlevel() {
//		if (faction != null)
//			return faction.getTechScore();
//		else
//			return 0;
//	}
//
//	public char getSpaceport() {
//		if (faction != null)
//			return faction.getSpaceport();
//		else
//			return 'X';
//	}
//
//	public String[] getTradeCodes() {
//		if (faction != null)
//			return faction.getTradeCodes();
//		else
//			return new String[0];
//	}
//
//	public boolean[] getCodeBoolean() {
//		if (faction != null)
//			return faction.getCodeBoolean();
//		else
//			return new boolean[0];
//	}
//	
//	public void setFaction(boolean isMainWorld) {
//		faction = new Faction(this);
//	}
//
//	public void setSystem(StarSystem system) {
//		this.system = system;
//	}
//	
//	public String printFaction() {
//		if (faction == null) return "";
//		else return String.format("%n%s", faction.toString());
//	}

	@Override
	public String toString() {
		String s = "";
		String moons = "";
		int totalMoons = 0;
		if (satellites != null && satellites.length > 0) {
			totalMoons = satellites.length;
			for (Celestial el : satellites) {
				moons += String.format("%n");
				moons += el.toString();
			}
		}

		if (isEmpty) {
			s = "Empty";
		} else if (isGiant) {
			if (size == 50)
				s = String.format("Small Giant || Moon(s): %d", totalMoons);
			else if (size == 99)
				s = String.format("Large Giant || Moon(s): %d", totalMoons);
		} else if (isPlanetoid) {
			s = "Planetoid";
		} else if (isCaptured) {
			s = "Captured || ";
			s += String.format("Orbit: %2d | Size: %2d | Atmo: %2d | Hydro: %2d | Pop: %2d | Moon(s): %d", orbit, size,
					atmosphere, hydrosphere, population, totalMoons);
		} else if (isSatellite) {
			s = "Satellite || ";
			s += String.format("Size: %2d | Atmo: %2d | Hydro: %2d | Pop: %2d", size, atmosphere, hydrosphere,
					population);
			if (isRing)
				s = "Ring";
		} else {
			s = "World || ";
			s += String.format("Orbit: %2d | Size: %2d | Atmo: %2d | Hydro: %2d | Pop: %2d | Moon(s): %d", orbit, size,
					atmosphere, hydrosphere, population, totalMoons);
		}
		
		return s + moons;
	}

	@Override
	public char getSpaceport() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String[] getTradeCodes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public World toWorld() {
		return this;
	}
}