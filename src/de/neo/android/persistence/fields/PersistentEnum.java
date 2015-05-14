package de.neo.android.persistence.fields;

import java.lang.reflect.Field;

import de.neo.android.persistence.DatabaseDao;
import android.content.ContentValues;
import android.database.Cursor;

public class PersistentEnum extends PersistentField {

	private Class<?> enumeration;

	public PersistentEnum(Field field, int columnIndex, Class<?> enumeration) {
		super(field, columnIndex);
		if (!enumeration.isEnum())
			throw new IllegalArgumentException("class must be enumeration.");
		mDatabaseDescription = DatabaseDao.TYPE_INTEGER_SQL;
		this.enumeration = enumeration;
	}

	@Override
	public void setValueToDomain(Object domain, Cursor cursor)
			throws IllegalAccessException, IllegalArgumentException {
		int ordinal = cursor.getInt(mColumnIndex);
		Object value = null;
		if (ordinal > -1)
			value = enumeration.getEnumConstants()[ordinal];
		mField.set(domain, value);
	}

	@Override
	public void setValueToDatabase(Object domain, ContentValues values)
			throws IllegalAccessException, IllegalArgumentException {
		Object value = mField.get(domain);
		int ordinal = -1;
		if (value != null)
			ordinal = ((Enum<?>) value).ordinal();
		values.put(mColumnName, ordinal);
	}
}