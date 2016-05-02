package model;

import java.io.Serializable;

public class Human implements Serializable{
	private String firstName;
	private String surName;
	public Human(String fName, String sName) {
		firstName=fName;
		surName=sName;
	}
	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}
	/**
	 * @return the surName
	 */
	public String getSurName() {
		return surName;
	}
}
