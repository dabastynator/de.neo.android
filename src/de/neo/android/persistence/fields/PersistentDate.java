package de.neo.android.persistence.fields;

import java.lang.reflect.Field;
import java.util.Date;

import de.neo.android.persistence.DatabaseDao;
import android.content.ContentValues;
import android.database.Cursor;

public class PersistentDate extends PersistentField {

	public PersistentDate(Field field, int columnIndex) {
		super(field, columnIndex);
		databaseDescription = DatabaseDao.TYPE_TEXT_SQL;
	}

	@Override
	public void setValueToDomain(Object domain, Cursor cursor)
			throws IllegalAccessException, IllegalArgumentException {
		String value = cursor.getString(columnIndex);
		if (value != null && value.length() > 0) {
			Date date = new Date(Long.parseLong(value));
			field.set(domain, date);
		} else
			field.set(domain, null);
	}

	@Override
	public void setValueToDatabase(Object domain, ContentValues values)
			throws IllegalAccessException, IllegalArgumentException {
		Date date = (Date) field.get(domain);
		if (date != null)
			values.put(columnName, date.getTime() + "");
		else
			values.put(columnName, "");
	}

}