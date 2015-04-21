package de.neo.android.persistence;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

import de.neo.android.persistence.fields.PersistentBoolean;
import de.neo.android.persistence.fields.PersistentDate;
import de.neo.android.persistence.fields.PersistentDomainBase;
import de.neo.android.persistence.fields.PersistentEnum;
import de.neo.android.persistence.fields.PersistentField;
import de.neo.android.persistence.fields.PersistentInteger;
import de.neo.android.persistence.fields.PersistentText;

/**
 * The DaoBuilder manages the creation of all daos.
 * 
 * @author sebastian
 */
public class DaoBuilder {

	protected PersistentFieldBuilder mFieldBuilder;
	protected DaoMapFilling mDaoMapFilling;
	protected NeoDataBase mDatabase;

	public DaoBuilder() {
		mFieldBuilder = new PersistentDatabaseFieldBuilder();
	}

	public DaoBuilder setFieldBuilder(PersistentFieldBuilder builder) {
		mFieldBuilder = builder;
		return this;
	}

	public DaoBuilder setDaoMapFilling(DaoMapFilling filling) {
		mDaoMapFilling = filling;
		return this;
	}

	public DaoBuilder setDatabase(NeoDataBase database) {
		mDatabase = database;
		return this;
	}

	public NeoDataBase getDatabase() {
		return mDatabase;
	}

	public PersistentFieldBuilder getPersistentFieldBuilder() {
		return mFieldBuilder;
	}

	/**
	 * The PersistentFieldCreator creates a handler for a persistent database
	 * field.
	 * 
	 * @author sebastian
	 */
	public interface PersistentFieldBuilder {

		/**
		 * Create a persistent field for the specified field.
		 * 
		 * @param field
		 * @return persistentfield
		 */
		PersistentField createPersistentField(Field field, int number);

	}

	/**
	 * Fill the map with all domain classes and associated dao instance.
	 * 
	 * @param daoMap
	 */
	public interface DaoMapFilling {
		/**
		 * Fill the map with all domain classes and associated dao instance.
		 * 
		 * @param daoMap
		 */
		void createDaos(Map<Class<?>, Dao<?>> daoMap, DaoBuilder builder);
	}

	public class PersistentDatabaseFieldBuilder implements
			PersistentFieldBuilder {

		@Override
		public PersistentField createPersistentField(Field field, int number) {
			if (field.getType().equals(int.class))
				return new PersistentInteger(field, number);
			if (field.getType().equals(boolean.class))
				return new PersistentBoolean(field, number);
			if (field.getType().isEnum())
				return new PersistentEnum(field, number, field.getType());
			if (field.getType().equals(Date.class))
				return new PersistentDate(field, number);
			if (field.getType().equals(String.class))
				return new PersistentText(field, number);
			if (DomainBase.class.isAssignableFrom(field.getType()))
				return new PersistentDomainBase(field, number);
			return null;
		}

	}
}