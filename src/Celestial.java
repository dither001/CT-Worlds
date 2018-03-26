/********************************************************
 * Author: Nick Foster
 *
 * Last edited: 10/15/2017
 ********************************************************/

public interface Celestial {
	// booleans
	public boolean isEmpty();

	public boolean isStar();

	public boolean isGiant();

	public boolean isPlanetoid();

	public boolean isCaptured();

	public boolean isWorld();

	// getters
	public Celestial[] getComponents();

	public Celestial[] getCaptured();

	public int getPopulation();

	public Celestial[] getSatellites();

//	public void setFaction(boolean isMainWorld);
	
//	public void setSystem(StarSystem system);

	//public void printFaction();

	public char getSpaceport();
	
	public String[] getTradeCodes();
	
	public World toWorld();
}