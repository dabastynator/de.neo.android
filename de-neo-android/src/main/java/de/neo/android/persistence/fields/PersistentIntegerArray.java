package de.neo.android.persistence.fields;

import java.lang.reflect.Field;
import java.util.Arrays;

import android.content.ContentValues;
import android.database.Cursor;
import de.neo.android.persistence.DaoException;
import de.neo.android.persistence.DatabaseDao;

public class PersistentIntegerArray extends PersistentField {

	public PersistentIntegerArray(Field field, int columnIndex) {
		super(field, columnIndex);
		mDatabaseDescription = DatabaseDao.TYPE_TEXT_SQL;
	}

	@Override
	public void setValueToDomain(Object domain, Cursor cursor)
			throws IllegalAccessException, IllegalArgumentException,
			DaoException {
		int[] integer = parseArray(cursor.getString(mColumnIndex));
		mField.set(domain, integer);
	}

	private int[] parseArray(String string) {
		if ("null".equals(string))
			return null;
		String[] split = string.replace("[", "").replace("]", "").split(",");
		int[] integer = new int[split.length];
		for (int i = 0; i < split.length; i++)
			integer[i] = Integer.parseInt(split[i]);
		return integer;
	}

	@Override
	public void setValueToDatabase(Object domain, ContentValues values)
			throws IllegalAccessException, IllegalArgumentException {
		int[] integer = (int[]) mField.get(domain);
		values.put(mColumnName, Arrays.toString(integer));
	}
}
