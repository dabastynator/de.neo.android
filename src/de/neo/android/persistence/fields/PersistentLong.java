package de.neo.android.persistence.fields;

import java.lang.reflect.Field;

import android.content.ContentValues;
import android.database.Cursor;
import de.neo.android.persistence.DaoException;
import de.neo.android.persistence.DatabaseDao;

public class PersistentLong extends PersistentField {

	public PersistentLong(Field field, int columnIndex) {
		super(field, columnIndex);
		mDatabaseDescription = DatabaseDao.TYPE_INTEGER_SQL;
	}

	@Override
	public void setValueToDomain(Object domain, Cursor cursor)
			throws IllegalAccessException, IllegalArgumentException,
			DaoException {
		mField.setLong(domain, cursor.getLong(mColumnIndex));
	}

	@Override
	public void setValueToDatabase(Object domain, ContentValues values)
			throws IllegalAccessException, IllegalArgumentException {
		values.put(mColumnName, mField.getLong(domain));
	}

}
