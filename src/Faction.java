/********************************************************
 * Author: Nick Foster
 *
 * Last edited: 11/22/2017
 ********************************************************/

public class Faction {
	private static final String[] TRADE_CODES = { "Ag", "As", "Ba", "De", "Fl", "Hi", "IC", "In", "Lo", "NA", "NI",
			"Po", "Ri", "Va", "Wa" };

	private static final String[] GOVERNMENTS = { "Anarchy/No Government", "Corporation", "Participating Democracy",
			"Self-Perpetuating Oligarchy", "Representative Democracy", "Feudal Technocracy", "Captive Government",
			"Balkanization", "Civil Service Government", "Impersonal Bureaucracy", "Charismatic Dictator",
			"Non-Charismatic Leader", "Charismatic Oligarchy", "Religious Dictatorship" };

	// fields
	private int government;
	private int law;
	private int techLevel;

	private char spaceport;
	private boolean[] tradeCodes;
	private int resourceWorlds;
	private Economy economy;

	// constructors
	public Faction(World world, int resourceWorlds) {
		this.resourceWorlds = resourceWorlds;
		government = Generator.mainGovernment(world);
		law = Generator.mainLaw(government);
		spaceport = Generator.mainSpaceport();

		techLevel = Generator.mainTechLevel(world, this);
		tradeCodes = Generator.mainTradeCodes(world, this);
		economy = new Economy(world, this);
	}

	// methods
	public int getGovernmentScore() {
		return government;
	}

	public String getGovernmentType() {
		return GOVERNMENTS[this.government];
	}

	public int getLawScore() {
		return law;
	}

	public int getTechScore() {
		return techLevel;
	}

	public char getSpaceport() {
		return spaceport;
	}

	public boolean[] getCodeBoolean() {
		return tradeCodes;
	}

	public String[] getTradeCodes() {
		// count number of trade codes
		int counter = 0;
		for (boolean el : tradeCodes) {
			if (el)
				++counter;
		}

		// create shortlist from trade code boolean
		String[] shortList = new String[counter];
		if (shortList.length > 0) {
			int index = 0;
			for (int i = 0; i < tradeCodes.length; ++i) {
				if (tradeCodes[i]) {
					shortList[index] = TRADE_CODES[i];
					++index;
				}
			}
		}

		return shortList;
	}

	@Override
	public String toString() {
		String s = "";

		// TODO - just another version that prints numbers instead of type
		// s = String.format("Spaceport: %c || Tech: %d || Government: %d || Law: %d",
		// spaceport, techLevel, government,
		// law);
		s = String.format("Spaceport: %c || Tech: %d || Law: %d || %s ", spaceport, techLevel, law,
				GOVERNMENTS[government]);

		String codes = String.format("%nTrade Code(s): ");
		for (int i = 0; i < tradeCodes.length; ++i) {
			if (tradeCodes[i])
				codes += String.format("%s, ", TRADE_CODES[i]);
		}

		// economy data
		String economy = this.economy.toString();

		return s + codes + economy;
	}

	private class Economy {
		private World _world;
		private int _resourceWorlds;
		private boolean[] _tradeCodes;
		private char _spaceport;
		private int _techLevel;
		private int _resources;
		private int _labor;
		private int _infrastructure;
		private int _culture;
		private int _totalDemand;
		private double _resourcesAvailable;
		private int _excessDemand;
		private int _deficitDemand;
		// GWP
		private double _tradeBenefit;
		private double _grossWorldProduct;

		private final int[][] TOTAL_DEMAND = { { 0, 0, 0, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7 },
				{ 0, 0, 0, 1, 2, 2, 3, 4, 5, 5, 6, 6, 8, 8, 9, 9 },
				{ 0, 0, 1, 1, 2, 3, 4, 5, 6, 6, 7, 8, 9, 10, 11, 11 },
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

		public Economy(World world, Faction faction) {
			this._world = world;
			this._resourceWorlds = resourceWorlds;
			this._tradeCodes = faction.getCodeBoolean();
			this._spaceport = world.getSpaceport();
			this._techLevel = faction.getTechScore();
			setResources();
			this._labor = world.getPopulation() - 1;
			this._infrastructure = 0;
			setInfrastructure();
			this._culture = 0;
			setCulture();
			this._totalDemand = 0;
			this._resourcesAvailable = 0;
			this._excessDemand = 0;
			this._deficitDemand = 0;
			setPlanetaryDemand();
			this._tradeBenefit = 0;
			this._grossWorldProduct = 0;
			setGrossWorldProduct();
		}

		private void setResources() {
			int resources;
			// AG(0), AS(1), BA(2), DE(3), FL(4), HI(5), IC(6), IN(7), LO(8), NA(9), NI(10),
			// PO(11), RI(12), VA(13), WA(14);
			if (_tradeCodes[1] || _tradeCodes[2] || _tradeCodes[11]) {
				resources = Dice.deeRoll(1, 6) - 1;
			} else {
				resources = Dice.deeRoll(2, 6) - 2;
			}

			if (_tradeCodes[0])
				++resources; // AG
			if (_tradeCodes[3])
				--resources; // DE
			if (_tradeCodes[4])
				--resources; // FL
			if (_tradeCodes[5])
				++resources; // HI
			if (_tradeCodes[6])
				--resources; // IC
			if (_tradeCodes[7])
				resources += 2; // IN
			if (_tradeCodes[9])
				--resources; // NA
			if (_tradeCodes[12])
				++resources; // RI
			if (_tradeCodes[13])
				--resources; // VA

			if (_spaceport == 'A')
				resources += 2;
			else if (_spaceport == 'B')
				++resources;

			if (_techLevel >= 8) {
				// add gas giants & asteroids
				resources += _resourceWorlds;
				// don't count the main world if it's an asteroid
				if (_tradeCodes[0])
					--resources;
			}

			if (resources < 0)
				resources = 0;
			if (resources > 15)
				resources = 15;
			this._resources = resources;
		}

		private void setInfrastructure() {
			int infrastructure = Dice.deeRoll(2, 6) - 2;
			if (_tradeCodes[2]) {
				double rand = Math.random();
				if (rand < 0.34)
					infrastructure = 0;
				else if (rand < 0.84)
					infrastructure = 1;
				else
					infrastructure = 2;
			}

			if (_tradeCodes[1])
				--infrastructure;
			if (_tradeCodes[5])
				++infrastructure;
			if (_tradeCodes[7])
				infrastructure += 2;
			if (_tradeCodes[8])
				--infrastructure;
			if (_tradeCodes[10])
				--infrastructure;
			if (_tradeCodes[11])
				infrastructure -= 2;
			if (_tradeCodes[12])
				infrastructure += 2;
			if (_tradeCodes[14])
				--infrastructure;

			if (_spaceport == 'A')
				infrastructure += 4;
			else if (_spaceport == 'B')
				infrastructure += 3;
			else if (_spaceport == 'C')
				infrastructure += 2;
			else if (_spaceport == 'D')
				++infrastructure;

			if (infrastructure < 0)
				infrastructure = 0;
			this._infrastructure = infrastructure;
		}

		private void setCulture() {
			int culture = Dice.deeRoll(2, 6);
			if (_tradeCodes[2]) {
				double rand = Math.random();
				if (rand < 0.67)
					culture = 0;
				else
					culture = 1;
			}

			if (_tradeCodes[0])
				--culture;
			if (_tradeCodes[1])
				++culture;
			if (_tradeCodes[3])
				++culture;
			if (_tradeCodes[4])
				++culture;
			if (_tradeCodes[6])
				++culture;
			if (_tradeCodes[9])
				--culture;
			if (_tradeCodes[10])
				--culture;
			if (_tradeCodes[11])
				++culture;
			if (_tradeCodes[12])
				++culture;
			if (_tradeCodes[13])
				++culture;

			if (culture < 0)
				culture = 0;
			this._culture = culture;
		}

		public void setPlanetaryDemand() {
			// TODO
			int population = _world.getPopulation();
			int baseDemand = (population > 3) ? this._resources : population;
			_totalDemand = Dice.deeRoll(2, 6);

			// POPULATION modifiers on total demand
			if (population == 0 || population == 1)
				_totalDemand -= 3;
			else if (population == 2 || population == 3)
				_totalDemand -= 2;
			else if (population == 4 || population == 5)
				_totalDemand -= 1;
			else if (population == 7 || population == 8)
				_totalDemand += 1;
			else if (population == 9 || population == 10)
				_totalDemand += 2;
			else if (population > 10)
				_totalDemand += 3;

			// CULTURE modifiers on total demand
			if (_culture == 0 || _culture == 1)
				_totalDemand -= 3;
			else if (_culture == 2 || _culture == 3)
				_totalDemand -= 2;
			else if (_culture == 4 || _culture == 5)
				_totalDemand -= 1;
			else if (_culture == 8 || _culture == 9 || _culture == 10)
				_totalDemand += 1;
			else if (_culture == 11 || _culture == 12 || _culture == 13)
				_totalDemand += 2;
			else if (_culture > 13)
				_totalDemand += 3;

			// SANITY check before the big moment
			if (_totalDemand < 0)
				_totalDemand = 0;
			if (_totalDemand > 15)
				_totalDemand = 15;
			_totalDemand = TOTAL_DEMAND[_totalDemand][baseDemand];

			if (_infrastructure > _resources) {
				if (_totalDemand > _resources) {
					_deficitDemand = _totalDemand - _resources;
					_resourcesAvailable = _resources - _deficitDemand;
					_excessDemand = 0;
				} else {
					_resourcesAvailable = _resources;
					_excessDemand = _resources - _totalDemand;
					_deficitDemand = 0;
				}
			} else {
				if (_totalDemand > _resources) {
					_resourcesAvailable = _resources;
					_deficitDemand = _totalDemand - _resources;
					_excessDemand = 0;
				} else {
					_resourcesAvailable = _totalDemand;
					_excessDemand = _resources - _totalDemand;
					_deficitDemand = 0;
				}
			}

			// SANITY check
			if (_resourcesAvailable < 0)
				_resourcesAvailable = 0;
			_infrastructure -= _deficitDemand / 10;

			// Applies Excess/Deficit demand as import/export
			if (_excessDemand > 0) {
				_tradeBenefit = _excessDemand * IMPORT_BENEFIT[Dice.deeRoll(2, 6) - 2];
			} else if (_deficitDemand > 0) {
				_tradeBenefit = _deficitDemand * EXPORT_BENEFIT[Dice.deeRoll(2, 6) - 2];
			}

			// "Resource Trade Rules"
			this._resourcesAvailable += _tradeBenefit;
		}

		public void setGrossWorldProduct() {
			// RE
			double resourcesExploitable = 0;
			resourcesExploitable = _techLevel * 0.1 * _resourcesAvailable;

			// LF
			int population = _world.getPopulation();
			double laborFactor = LABOR_BASE[_labor] * population;

			_grossWorldProduct = (resourcesExploitable * laborFactor * _infrastructure) / (_culture + 1);
		}

		@Override
		public String toString() {
			String s = "";
			// s += String.format(
			// "%nResources: %d | Labor: %d | Infrastructure: %d | Culture: %d"
			// + "%nGWP: %.3f | R/A: %.1f | Excess/Deficit Demand: %d / %d",
			// _resources, _labor, _infrastructure, _culture, _grossWorldProduct,
			// _resourcesAvailable,
			// _excessDemand, _deficitDemand);

			int population = _world.getPopulation();
			s += String.format(
					"%nTL: %d * 0.1 * R/A: %.1f" + "%nLabor Base: %.3f * Population: %d * Infrastructure: %d"
							+ "%nGWP: %.3f",
					_techLevel, _resourcesAvailable, LABOR_BASE[_labor], population, _infrastructure,
					_grossWorldProduct);

			return s;
		}
	}
}