package model;

import java.io.Serializable;

public class Author extends Human implements Serializable{
	public Author(String fName, String sName) {
		super(fName, sName);
	}
}
