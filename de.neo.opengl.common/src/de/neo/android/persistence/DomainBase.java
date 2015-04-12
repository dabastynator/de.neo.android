package de.neo.android.persistence;


public class DomainBase {

	public static final int LANGUAGE_EN = 1;
	public static final int LANGUAGE_SP = 2;
	public static final int LANGUAGE_DE = 3;
	
	private long mId;
	
	public long getId() {
		return mId;
	}

	public void setId(long id) {
		mId = id;
	}

}
