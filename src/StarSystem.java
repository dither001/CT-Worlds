
/********************************************************
* Author: Nick Foster
*
* Last edited: 10/14/2017
********************************************************/

import java.awt.Point;
import java.util.Iterator;

public class StarSystem {
	// fields
	private Point address;
	private Star[] nature;
	private int maxOrbits;
	// private Celestial[] components;
	// private boolean[] emptyOrbits;
	// private Celestial[] captured;

	private int[] populations;
	private World mainWorld;
	private Faction faction;

	// TEMPORARY
	// private int maxEmpty;
	// private boolean hasGasGiant;
	// private int numGasGiants;
	// private int numPlanets;

	// constructors
	public StarSystem() {
		// solo, binary, or trinary star system
		nature = new Star[Generator.nature()];

		// fill nature with each star (up to three)
		for (int i = 0; i < nature.length; ++i) {
			nature[i] = new Star(nature, i);
		}

		// max number of orbitals per star
		for (int i = 0; i < nature.length; ++i) {
			nature[i].setMaxOrbits(nature, i);
		}

		// determine number of empty orbits per star
		for (int i = 0; i < nature.length; ++i) {
			nature[i].setEmptyOrbits(nature, i);
		}

		// determine number of captured objects
		for (int i = 0; i < nature.length; ++i) {
			nature[i].capturedWorlds(nature, i);
		}

		// place giants, planetoids, et cetera
		for (Star el : nature) {
			el.placeComponents();
		}

		// set population numbers and select main world
		populations = countPopulations();
		mainWorld = (hasMainWorld()) ? selectMainWorld().toWorld() : null;
		if (hasMainWorld()) {
			setFaction();
		}

		// END CONSTRUCTOR
	}

	// methods
	public Point getAddress() {
		return address;
	}

	public Point setAddress(Point address) {
		return this.address = address;
	}

	public Faction getFaction() {
		return faction;
	}
	
	public Celestial getMainWorld() {
		return mainWorld;
	}

	public int getNature() {
		return nature.length;
	}

	public int getOrbits() {
		return maxOrbits;
	}

	public int totalObjects() {
		int totalObjects = 0;
		for (int i = 0; i < nature.length; ++i) {
			Celestial star = nature[i];
			if (star.getComponents() != null) {
				Celestial[] components = star.getComponents();
				totalObjects += components.length;
				for (int j = 0; j < components.length; ++j) {
					if (components[j].getSatellites() != null) {
						Celestial[] satellites = components[j].getSatellites();
						totalObjects += satellites.length;
					}
				}
			}
			if (star.getCaptured() != null) {
				Celestial[] captured = star.getCaptured();
				totalObjects += captured.length;
				for (int j = 0; j < captured.length; ++j) {
					if (captured[j].getSatellites() != null) {
						Celestial[] capSat = captured[j].getSatellites();
						totalObjects += capSat.length;
					}
				}
			}
		}

		return totalObjects;
	}

	// public boolean isInteresting() {
	// boolean isInteresting = false;
	//
	// int totalWorlds = 0;
	// for (Star el : nature) {
	// totalWorlds += el.totalWorlds();
	// }
	//
	// if (totalWorlds > 5) isInteresting = true;
	//
	// return isInteresting;
	// }

	// public String printEmpty() {
	// String s = "none";
	//
	// if (emptyOrbits.length > 0) {
	// s = "";
	// for (int i = 0; i < emptyOrbits.length; ++i) {
	// if (emptyOrbits[i]) s += String.format("%d, ", i);
	// else s += "a, ";
	// }
	// }
	//
	// return s;
	// }

	public int totalWorlds() {
		int worldCount = 0;

		for (int i = 0; i < nature.length; ++i) {
			Celestial star = nature[i];
			if (star.getComponents() != null) {
				Celestial[] components = star.getComponents();
				for (int j = 0; j < components.length; ++j) {
					if (components[j].isWorld())
						++worldCount;
					if (components[j].getSatellites() != null) {
						Celestial[] satellites = components[j].getSatellites();
						for (int k = 0; k < satellites.length; ++k) {
							if (satellites[k].isWorld())
								++worldCount;
						}
					}
				}
			}
			if (star.getCaptured() != null) {
				Celestial[] captured = star.getCaptured();
				for (int j = 0; j < captured.length; ++j) {
					if (captured[j].isWorld())
						++worldCount;
					if (captured[j].getSatellites() != null) {
						Celestial[] capSatellites = captured[j].getSatellites();
						for (int k = 0; k < capSatellites.length; ++k) {
							if (capSatellites[k].isWorld())
								++worldCount;
						}
					}
				}
			}

		}

		return worldCount;
	}

	public int[] countPopulations() {
		int[] populations = new int[11];
		Iterator<Celestial> it = getObjectIterator();

		while (it.hasNext()) {
			++populations[(it.next()).getPopulation()];
		}

		return populations;
	}

	public int highestPopulation() {
		int highestPopulation = 0;
		int index = populations.length - 1;

		for (int i = index; i > 0; --i) {
			if (populations[i] > 0) {
				highestPopulation = i;
				break;
			}
		}

		return highestPopulation;
	}

	public boolean hasMainWorld() {
		return (highestPopulation() > 0);
	}

	public Celestial selectMainWorld() {
		int highestPopulation = highestPopulation();
		int numCandidates = populations[highestPopulation];
		Celestial[] candidates = new Celestial[numCandidates];
		Iterator<Celestial> it = getObjectIterator();

		Celestial mainWorld;
		int i = 0;
		do {
			mainWorld = it.next();
			if (mainWorld.getPopulation() == highestPopulation)
				candidates[i++] = mainWorld;
		} while (it.hasNext());

		// TODO write process to select the main world from candidates
		mainWorld = candidates[0];
		return mainWorld;
	}

	public int getGovernment() {
		if (faction != null)
			return faction.getGovernmentScore();
		else
			return 0;
	}

	public int getLawLevel() {
		if (faction != null)
			return faction.getLawScore();
		else
			return 0;
	}

	public int getTechlevel() {
		if (faction != null)
			return faction.getTechScore();
		else
			return 0;
	}

	public char getSpaceport() {
		if (faction != null)
			return faction.getSpaceport();
		else
			return 'X';
	}

	public String[] getTradeCodes() {
		if (faction != null)
			return faction.getTradeCodes();
		else
			return new String[0];
	}

	public boolean[] getCodeBoolean() {
		if (faction != null)
			return faction.getCodeBoolean();
		else
			return new boolean[0];
	}

	public void setFaction() {
		int resourceWorlds = 0;
		Iterator<Celestial> it = getObjectIterator();

		Celestial world;
		do {
			world = it.next();
			if (world.isGiant() || world.isPlanetoid())
				++resourceWorlds;
		} while (it.hasNext());
		
		
		faction = new Faction(this.mainWorld, resourceWorlds);
	}

//	public int countResourceWorlds() {
//		int resourceWorlds = 0;
//		Iterator<Celestial> it = getObjectIterator();
//
//		Celestial world;
//		do {
//			world = it.next();
//			if (world.isGiant() || world.isPlanetoid())
//				++resourceWorlds;
//		} while (it.hasNext());
//
//		return resourceWorlds;
//	}

	public Iterator<Celestial> getObjectIterator() {
		return new ObjectIterator();
	}

	private class ObjectIterator implements Iterator<Celestial> {
		private Celestial[] elements;
		private int next;

		private ObjectIterator() {
			elements = new Celestial[totalObjects()];
			next = 0;

			orderObjects(nature);

			next = 0;
		}

		private void orderObjects(Celestial[] nature) {
			int i = 0;
			for (int j = 0; j < nature.length; ++j) {
				Celestial star = nature[j];
				if (star.getComponents() != null) {
					Celestial[] components = star.getComponents();
					for (int k = 0; k < components.length; ++k) {
						elements[i++] = components[k];
						if (components[k].getSatellites() != null) {
							Celestial[] satellites = components[k].getSatellites();
							for (int m = 0; m < satellites.length; ++m) {
								elements[i++] = satellites[m];
							}
						}
					}
				}
				if (star.getCaptured() != null) {
					Celestial[] captured = star.getCaptured();
					for (int k = 0; k < captured.length; ++k) {
						elements[i++] = captured[k];
						if (captured[k].getSatellites() != null) {
							Celestial[] capSat = captured[k].getSatellites();
							for (int m = 0; m < capSat.length; ++m) {
								elements[i++] = capSat[m];
							}
						}
					}
				}

			}
		}

		public boolean hasNext() {
			return (next < elements.length);
		}

		public Celestial next() {
			return elements[next++];
		}
	}

	public String printPopulations() {
		String s = String.format("%n");

		for (int el : populations) {
			s += String.format("%d, ", el);
		}

		return s;
	}

	public String printMain() {
		String main = String.format("%nMain world: %s", mainWorld);

		// faction data
		String faction = printFaction();

		return main + faction;
	}

	public String printFaction() {
		if (faction == null) return "";
		else return String.format("%n%s", faction.toString());
	}

	@Override
	public String toString() {
		// system data
		// String line = String.format("System Name");
		String stars = String.format("%n");

		// star data
		for (Star el : nature)
			stars += el.toString();

		// population data
		String printPops = printPopulations();
		int pop = highestPopulation();
		String popData = String.format("%nHighest population: %d", pop);

		// main world data
		String main = String.format("%nMain world: %s", mainWorld);

		// faction data
		String faction = printFaction();
		
		return main + stars + printPops + popData + faction;
	}

	// END OF CODE
}