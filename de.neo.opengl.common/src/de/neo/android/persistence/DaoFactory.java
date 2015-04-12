package de.neo.android.persistence;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

public class DaoFactory {

	private static DaoFactory mSingelton;

	public static DaoFactory initiate(Context context, DaoCreator creator) {
		if (mSingelton == null) {
			mSingelton = new DaoFactory(context, creator);
			for (Dao<?> dao : mSingelton.mMapClassDao.values()) {
				if (dao instanceof DatabaseDao<?>) {
					((DatabaseDao<?>) dao).initDependencyFields();
				}
			}
		}
		return mSingelton;
	}

	public static void finilize() {
		if (mSingelton != null) {
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

	private DaoFactory(Context context, DaoCreator creator) {
		mMapClassDao = new HashMap<Class<?>, Dao<?>>();
		creator.createDaos(mMapClassDao);
	}

	@SuppressWarnings("unchecked")
	public <T> Dao<T> getDao(Class<?> domain) {
		return (Dao<T>) mMapClassDao.get(domain);
	}

}
