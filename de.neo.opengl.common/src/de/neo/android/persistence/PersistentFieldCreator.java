package de.neo.android.persistence;

import java.lang.reflect.Field;

import de.neo.android.persistence.fields.PersistentField;

/**
 * The PersistentFieldCreator creates a handler for a persistent database field.
 * 
 * @author sebastian
 */
public interface PersistentFieldCreator {

	/**
	 * Create a persistent field for the specified field.
	 * 
	 * @param field
	 * @return persistentfield
	 */
	PersistentField createPersistentField(Field field, int number);

}
