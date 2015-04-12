package de.neo.android.persistence;

import java.util.Map;

/**
 * The DaoCreator creates the dao objects.
 * 
 * @author sebastian
 */
public interface DaoCreator {

	/**
	 * Fill the map with all domain classes and associated dao instance.
	 * 
	 * @param daoMap
	 */
	void createDaos(Map<Class<?>, Dao<?>> daoMap);
}