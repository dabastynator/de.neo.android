package de.neo.android.persistence.fields;

import java.lang.reflect.Field;
import java.util.Arrays;

import android.content.ContentValues;
import android.database.Cursor;
import de.neo.android.persistence.DaoException;
import de.neo.android.persistence.DatabaseDao;

public class PersistentLongArray extends PersistentField {

	public PersistentLongArray(Field field, int columnIndex) {
		super(field, columnIndex);
		mDatabaseDescription = DatabaseDao.TYPE_TEXT_SQL;
	}

	@Override
	public void setValueToDomain(Object domain, Cursor cursor)
			throws IllegalAccessException, IllegalArgumentException,
			DaoException {
		long[] longs = parseArray(cursor.getString(mColumnIndex));
		mField.set(domain, longs);
	}

	private long[] parseArray(String string) {
		if ("null".equals(string))
			return null;
		String[] split = string.replace("[", "").replace("]", "").split(",");
		long[] longs = new long[split.length];
		for (int i = 0; i < split.length; i++)
			longs[i] = Long.parseLong(split[i]);
		return longs;
	}

	@Override
	public void setValueToDatabase(Object domain, ContentValues values)
			throws IllegalAccessException, IllegalArgumentException {
		long[] longs = (long[]) mField.get(domain);
		values.put(mColumnName, Arrays.toString(longs));
	}

}
