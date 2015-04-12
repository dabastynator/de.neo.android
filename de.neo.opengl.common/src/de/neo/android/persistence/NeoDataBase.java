package de.neo.android.persistence;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class NeoDataBase extends SQLiteOpenHelper {

	private boolean mCreate = false;
	private boolean mDelete = false;
	private List<DatabaseDao<?>> mDaoList;

	public NeoDataBase(Context context, String name, int version) {
		super(context, name, null, version);
		mDaoList = new ArrayList<DatabaseDao<?>>();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		mCreate = true;
		for (DatabaseDao<?> dao : mDaoList) {
			dao.createTable(db);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		mDelete = true;
		mCreate = true;
		for (DatabaseDao<?> dao : mDaoList) {
			dao.deleteTable(db);
			dao.createTable(db);
		}
	}

	public List<DatabaseDao<?>> getDaoList() {
		return mDaoList;
	}

	public boolean doCreate() {
		return mCreate;
	}

	public boolean doDelete() {
		return mDelete;
	}

}
