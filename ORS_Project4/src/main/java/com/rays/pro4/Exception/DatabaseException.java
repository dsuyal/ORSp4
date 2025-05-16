package com.rays.pro4.Exception;

/**
 * DatabaseException is propagated by DAO classes when an unhandled Database
 * exception occurred.
 * 
 * @author DINESH SUYAL
 *
 */

public class DatabaseException extends Exception {

	public DatabaseException(String msg){
		super(msg);
	}
	
}
