package model;

import java.io.Serializable;

public class Book implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5647361092122034611L;
	public static final String AUTHOR_NAME = "authorName";
	public static final String BOOK_NAME = "bookName";
	public static final String CIRCULATION = "circ";
	public static final String TOME_NUMBER = "tomeNumber";
	private String authorName;
	private String bookName;
	private int circulation;
	private int tomeNumber;
	private int finalTomeNumber;
	public Book(String author, String bookName, int circ, int tomes) {
		authorName = author;
		this.bookName = bookName;
		circulation = circ;
		tomeNumber = tomes;
		finalTomeNumber = circulation * tomeNumber;
	}
	/**
	 * @return the finalTomeNumber
	 */
	public int getFinalTomeNumber() {
		return finalTomeNumber;
	}
	/**
	 * @return the authorName
	 */
	public String getAuthorName() {
		return authorName;
	}
	/**
	 * @return the bookName
	 */
	public String getBookName() {
		return bookName;
	}
	/**
	 * @return the circulation
	 */
	public int getCirculation() {
		return circulation;
	}
	/**
	 * @return the tomeNumber
	 */
	public int getTomeNumber() {
		return tomeNumber;
	}
}
