package de.neo.android.persistence;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import de.neo.android.persistence.fields.PersistentDomainBase;
import de.neo.android.persistence.fields.PersistentField;

/**
 * The AbstractDatabaseDao implements the basic dao functionality.
 * 
 * @author sebastian
 * 
 * @param <T>
 */
public class DatabaseDao<T extends DomainBase> implements Dao<T> {

	public static final String FIELD_ID = "ID";
	public static final String WHERE_ID = FIELD_ID + "=?";

	public static final String TYPE_TEXT_SQL = "TEXT";
	public static final String TYPE_REAL_SQL = "REAL";
	public static final String TYPE_INTEGER_SQL = "INTEGER";
	public static final String TYPE_BLOB_SQL = "BLOB";

	public static final int TYPE_TEXT = 0;
	public static final int TYPE_REAL = 1;
	public static final int TYPE_INTEGER = 2;
	public static final int TYPE_BLOB = 3;

	protected NeoDataBase mSqlLite;
	protected String mTableName;
	protected String[] mFieldsPlusId;
	protected Class<T> mDomainClass;
	protected String mCreateTable;
	protected List<PersistentField> mPersistentFields;
	protected Map<Long, T> mCache;

	@SuppressLint("UseSparseArrays")
	public DatabaseDao(NeoDataBase sqlLite, Class<T> domainClass,
			PersistentFieldCreator fieldCreator) {
		mPersistentFields = new ArrayList<PersistentField>();
		mCache = new HashMap<Long, T>();
		mSqlLite = sqlLite;
		mDomainClass = domainClass;
		mTableName = domainClass.getSimpleName().toUpperCase(Locale.US);
		checkDomainClass();
		initFields(fieldCreator);
		if (sqlLite.doDelete()) {
			SQLiteDatabase db = sqlLite.getWritableDatabase();
			deleteTable(db);
			db.close();
		}
		if (sqlLite.doCreate()) {
			SQLiteDatabase db = sqlLite.getWritableDatabase();
			createTable(db);
			db.close();
		}
		sqlLite.getDaoList().add(this);
	}

	private void checkDomainClass() {
		try {
			try {
				T dummyDomain = (T) mDomainClass.newInstance();
				dummyDomain.getClass();
			} catch (ClassCastException e) {
				throw new IllegalStateException(
						"Domain class must have public constructor without parameter");
			}
		} catch (Exception e) {
			throw new IllegalStateException(
					"Domain class must have public constructor without parameter");
		}
	}

	private void initFields(PersistentFieldCreator fieldCreator) {
		mPersistentFields.clear();
		for (Field field : mDomainClass.getDeclaredFields()) {
			PersistentField pField = fieldCreator.createPersistentField(field,
					mPersistentFields.size() + 1);
			if (field.getAnnotation(Persistent.class) != null) {
				if (pField == null)
					throw new IllegalArgumentException(
							"Unknown persistent field: "
									+ field.getType().getSimpleName());
				mPersistentFields.add(pField);
			}
		}
		String createFields = "";
		mFieldsPlusId = new String[mPersistentFields.size() + 1];
		mFieldsPlusId[0] = FIELD_ID;
		for (int i = 0; i < mPersistentFields.size(); i++) {
			PersistentField field = mPersistentFields.get(i);
			mFieldsPlusId[i + 1] = field.columnName;
			if (i > 0)
				createFields += ", ";
			createFields += field.columnName + " " + field.databaseDescription;
		}
		mCreateTable = "CREATE TABLE " + mTableName + " ( " + FIELD_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + createFields + " )";
	}

	protected void initDependencyFields() {
		for (PersistentField field : mPersistentFields) {
			if (PersistentDomainBase.class.isAssignableFrom(field.getClass()))
				((PersistentDomainBase) field).init();
		}
	}

	public void deleteTable(SQLiteDatabase db) {
		db.execSQL("DROP TABLE IF EXISTS " + mTableName);
	}

	public void createTable(SQLiteDatabase db) {
		db.execSQL(mCreateTable);
	}

	protected T loadItemByCurser(Cursor cursor) throws DaoException {
		try {
			T domain = (T) mDomainClass.newInstance();
			for (PersistentField field : mPersistentFields) {
				field.setValueToDomain(domain, cursor);
			}
			return domain;
		} catch (Exception e) {
			throw new DaoException(e.getClass().getSimpleName() + ":"
					+ e.getMessage());
		}
	}

	@Override
	public List<T> loadAll() throws DaoException {
		return loadByWhereClausel(null, null, null, null);
	}

	protected List<T> loadByWhereClausel(String where, String[] values,
			String orderBy, String limit) throws DaoException {
		SQLiteDatabase db = mSqlLite.getReadableDatabase();
		List<T> items = new ArrayList<T>();
		Cursor cursor = db.query(mTableName, mFieldsPlusId, where, values,
				null, null, orderBy, limit);
		while (cursor.moveToNext()) {
			int id = cursor.getInt(0);
			if (mCache.containsKey(id))
				items.add(mCache.get(id));
			else {
				T item = loadItemByCurser(cursor);
				item.setId(id);
				items.add(item);
				mCache.put((long) id, item);
			}
		}
		db.close();
		return items;
	}

	@Override
	public T loadById(long id) throws DaoException {
		if (mCache.containsKey(id))
			return mCache.get(id);
		SQLiteDatabase db = mSqlLite.getReadableDatabase();
		Cursor cursor = db.query(mTableName, mFieldsPlusId, WHERE_ID,
				new String[] { id + "" }, null, null, null);
		T item = null;
		if (cursor.moveToNext()) {
			item = loadItemByCurser(cursor);
			item.setId(id);
		}
		db.close();
		return item;
	}

	protected ContentValues getContentValues(T item) throws DaoException {
		try {
			ContentValues values = new ContentValues();
			for (PersistentField field : mPersistentFields) {
				field.setValueToDatabase(item, values);
			}
			return values;
		} catch (Exception e) {
			throw new DaoException(e.getClass().getSimpleName() + ":"
					+ e.getMessage());
		}
	}

	@Override
	public long save(T item) throws DaoException {
		SQLiteDatabase db = mSqlLite.getWritableDatabase();
		long id = db.insert(mTableName, null, getContentValues(item));
		db.close();
		item.setId(id);
		mCache.put(id, item);
		return id;
	}

	@Override
	public void update(T item) throws DaoException {
		SQLiteDatabase db = mSqlLite.getWritableDatabase();
		db.update(mTableName, getContentValues(item), WHERE_ID,
				new String[] { item.getId() + "" });
		mCache.put(item.getId(), item);
		db.close();
	}

	@Override
	public void delete(long id) throws DaoException {
		SQLiteDatabase db = mSqlLite.getWritableDatabase();
		db.delete(mTableName, WHERE_ID, new String[] { id + "" });
		db.close();
		mCache.remove(id);
	}

	@Override
	public void deleteAll() throws DaoException {
		SQLiteDatabase db = mSqlLite.getWritableDatabase();
		db.delete(mTableName, null, null);
		db.close();
		mCache.clear();
	}

}
