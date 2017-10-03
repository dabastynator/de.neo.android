package de.neo.android.persistence;

/**
 * The DaoException describes an exception that occurs in a dao access.
 * 
 * @author sebastian
 * 
 */
public class DaoException extends Exception {

	/**
	 * generated uid
	 */
	private static final long serialVersionUID = -6799684195532905150L;

	public DaoException(String msg) {
		super(msg);
	}

}
