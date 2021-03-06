package de.neo.android.persistence.fields;

import java.lang.reflect.Field;
import java.util.Locale;

import android.content.ContentValues;
import android.database.Cursor;
import de.neo.android.persistence.DaoException;

public abstract class PersistentField {
	public String mDatabaseDescription;
	public String mColumnName;
	protected int mColumnIndex;
	protected Field mField;

	public PersistentField(Field field, int columnIndex) {
		this.mField = field;
		field.setAccessible(true);
		this.mColumnIndex = columnIndex;
		this.mColumnName = field.getName().toUpperCase(Locale.US);
	}

	public abstract void setValueToDomain(Object domain, Cursor cursor)
			throws IllegalAccessException, IllegalArgumentException,
			DaoException;

	public abstract void setValueToDatabase(Object domain, ContentValues values)
			throws IllegalAccessException, IllegalArgumentException;
}