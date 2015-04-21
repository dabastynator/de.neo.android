package de.neo.android.persistence.fields;

import java.lang.reflect.Field;

import de.neo.android.persistence.DatabaseDao;
import android.content.ContentValues;
import android.database.Cursor;

public class PersistentInteger extends PersistentField {

	public PersistentInteger(Field field, int columnIndex) {
		super(field, columnIndex);
		databaseDescription = DatabaseDao.TYPE_INTEGER_SQL;
	}

	@Override
	public void setValueToDomain(Object domain, Cursor cursor)
			throws IllegalAccessException, IllegalArgumentException {
		field.setInt(domain, cursor.getInt(columnIndex));
	}

	@Override
	public void setValueToDatabase(Object domain, ContentValues values)
			throws IllegalAccessException, IllegalArgumentException {
		values.put(columnName, field.getInt(domain));
	}

}