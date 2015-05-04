package de.neo.android.persistence;

import java.util.HashMap;
import java.util.Map;

public class DaoFactory {

	private static DaoFactory mSingelton;

	public static DaoFactory initiate(DaoBuilder builder) {
		finilize();
		mSingelton = new DaoFactory(builder);
		for (Dao<?> dao : mSingelton.mMapClassDao.values()) {
			if (dao instanceof DatabaseDao<?>) {
				((DatabaseDao<?>) dao).initDependencyFields();
			}
		}
		return mSingelton;
	}

	public static void finilize() {
		if (mSingelton != null) {
			if (mSingelton.mDatabase != null)
				mSingelton.mDatabase.close();
			mSingelton = null;
		}
	}

	public static DaoFactory getInstance() {
		if (mSingelton == null)
			throw new IllegalStateException(
					"factory has not been initiated. call initiate first");
		return mSingelton;
	}

	protected Map<Class<?>, Dao<?>> mMapClassDao;
	private NeoDataBase mDatabase;

	private DaoFactory(DaoBuilder builder) {
		mMapClassDao = new HashMap<Class<?>, Dao<?>>();
		builder.mDaoMapFilling.createDaos(mMapClassDao, builder);
	}

	@SuppressWarnings("unchecked")
	public <T> Dao<T> getDao(Class<?> domain) {
		return (Dao<T>) mMapClassDao.get(domain);
	}

	public Object getCustomDao(Class<?> domain) {
		return mMapClassDao.get(domain);
	}

}
