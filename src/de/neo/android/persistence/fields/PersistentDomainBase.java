package de.neo.android.persistence.fields;

import java.lang.reflect.Field;

import android.content.ContentValues;
import android.database.Cursor;
import de.neo.android.persistence.DatabaseDao;
import de.neo.android.persistence.Dao;
import de.neo.android.persistence.DaoException;
import de.neo.android.persistence.DaoFactory;
import de.neo.android.persistence.DomainBase;

public class PersistentDomainBase extends PersistentField {

	private Dao<?> dao;

	public PersistentDomainBase(Field field, int columnIndex) {
		super(field, columnIndex);
		mDatabaseDescription = DatabaseDao.TYPE_INTEGER_SQL;
	}

	public void init() {
		dao = DaoFactory.getInstance().getDao(mField.getType());
		if (dao == null)
			throw new IllegalArgumentException(
					"Field type must has a dao in the daofactory.");
	}

	@Override
	public void setValueToDomain(Object domain, Cursor cursor)
			throws IllegalAccessException, IllegalArgumentException,
			DaoException {
		int id = cursor.getInt(mColumnIndex);
		Object domainObject = dao.loadById(id);
		mField.set(domain, domainObject);
	}

	@Override
	public void setValueToDatabase(Object domain, ContentValues values)
			throws IllegalAccessException, IllegalArgumentException {
		DomainBase domainObject = (DomainBase) mField.get(domain);
		if (domainObject != null)
			values.put(mColumnName, domainObject.getId());
		else
			values.put(mColumnName, -1);
	}

}