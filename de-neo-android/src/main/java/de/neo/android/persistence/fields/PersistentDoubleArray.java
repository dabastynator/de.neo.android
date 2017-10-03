package de.neo.android.persistence.fields;

import java.lang.reflect.Field;
import java.util.Arrays;

import android.content.ContentValues;
import android.database.Cursor;
import de.neo.android.persistence.DaoException;
import de.neo.android.persistence.DatabaseDao;

public class PersistentDoubleArray extends PersistentField {

	public PersistentDoubleArray(Field field, int columnIndex) {
		super(field, columnIndex);
		mDatabaseDescription = DatabaseDao.TYPE_TEXT_SQL;
	}

	@Override
	public void setValueToDomain(Object domain, Cursor cursor)
			throws IllegalAccessException, IllegalArgumentException,
			DaoException {
		double[] doubles = parseArray(cursor.getString(mColumnIndex));
		mField.set(domain, doubles);
	}

	private double[] parseArray(String string) {
		String[] split = string.replace("[", "").replace("]", "").split(",");
		double[] doubles = new double[split.length];
		for (int i = 0; i < split.length; i++)
			doubles[i] = Double.parseDouble(split[i]);
		return doubles;
	}

	@Override
	public void setValueToDatabase(Object domain, ContentValues values)
			throws IllegalAccessException, IllegalArgumentException {
		double[] doubles = (double[]) mField.get(domain);
		values.put(mColumnName, Arrays.toString(doubles));
	}

}
