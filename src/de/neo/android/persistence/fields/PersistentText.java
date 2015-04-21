package de.neo.android.persistence.fields;

import java.lang.reflect.Field;

import de.neo.android.persistence.DatabaseDao;
import android.content.ContentValues;
import android.database.Cursor;

public class PersistentText extends PersistentField {

	public PersistentText(Field field, int columnIndex) {
		super(field, columnIndex);
		databaseDescription = DatabaseDao.TYPE_TEXT_SQL;
	}

	@Override
	public void setValueToDomain(Object domain, Cursor cursor)
			throws IllegalAccessException, IllegalArgumentException {
		field.set(domain, cursor.getString(columnIndex));
	}

	@Override
	public void setValueToDatabase(Object domain, ContentValues values)
			throws IllegalAccessException, IllegalArgumentException {
		String str = (String) field.get(domain);
		values.put(columnName, str);
	}

}