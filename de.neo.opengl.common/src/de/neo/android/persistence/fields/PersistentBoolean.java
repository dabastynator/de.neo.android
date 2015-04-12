package de.neo.android.persistence.fields;

import java.lang.reflect.Field;

import de.neo.android.persistence.DatabaseDao;
import android.content.ContentValues;
import android.database.Cursor;

public class PersistentBoolean extends PersistentField {

	public PersistentBoolean(Field field, int columnIndex) {
		super(field, columnIndex);
		databaseDescription = DatabaseDao.TYPE_INTEGER_SQL;
	}

	@Override
	public void setValueToDomain(Object domain, Cursor cursor)
			throws IllegalAccessException, IllegalArgumentException {
		field.setBoolean(domain, cursor.getInt(columnIndex) == 1);
	}

	@Override
	public void setValueToDatabase(Object domain, ContentValues values)
			throws IllegalAccessException, IllegalArgumentException {
		values.put(columnName, field.getBoolean(domain) ? 1 : 0);
	}

}