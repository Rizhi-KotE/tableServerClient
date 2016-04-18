package implementation;

import java.io.Serializable;
import java.util.function.Predicate;

import org.apache.log4j.Logger;

import model.Book;

public class SerializableBookPredicate implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8433064802284075041L;

	private static final Logger log = Logger.getLogger(SerializableBookPredicate.class);
	private String authorName = "";
	private ComparatorType authorNameComparatorType = ComparatorType.all;
	private String bookName = "";
	private ComparatorType bookNameComparatorType = ComparatorType.all;
	private int circulation;
	private ComparatorType circulationComparatorType = ComparatorType.all;
	private int tomeNumber;
	private ComparatorType tomeNumberComparatorType = ComparatorType.all;
	private int finalBookAmount;
	private ComparatorType finalBookAmountComparatorType = ComparatorType.all;

	/**
	 * @param authorName
	 *            the authorName to set
	 */
	public void setAuthorName(String authorName, ComparatorType type) {
		this.authorName = authorName;
		authorNameComparatorType = type;
	}

	/**
	 * @param bookName
	 *            the bookName to set
	 */
	public void setBookName(String bookName, ComparatorType type) {
		this.bookName = bookName;
		bookNameComparatorType = type;
	}

	/**
	 * @param circulation
	 *            the circulation to set
	 */
	public void setCirculation(int circulation, ComparatorType type) {
		this.circulation = circulation;
		circulationComparatorType = type;
	}

	/**
	 * @param tomeNumber
	 *            the tomeNumber to set
	 */
	public void setTomeNumber(int tomeNumber, ComparatorType type) {
		this.tomeNumber = tomeNumber;
		tomeNumberComparatorType = type;
	}

	/**
	 * @param finalBookAmount
	 *            the finalBookAmount to set
	 */
	public void setFinalBookAmount(int finalBookAmount, ComparatorType type) {
		this.finalBookAmount = finalBookAmount;
		finalBookAmountComparatorType = type;
	}

	public static enum ComparatorType {
		all, more, less, stringEquals, numberEquals
	}

	public SerializableBookPredicate() {

	}

	public Predicate<Book> getPredicate() {
		Predicate<String> authorNamePredicate = getPredicate(authorName, authorNameComparatorType);
		Predicate<String> bookNamePredicate = getPredicate(bookName, bookNameComparatorType);
		Predicate<Integer> circulationPredicate = getPredicate(circulation, circulationComparatorType);
		Predicate<Integer> tomeNumberPredicate = getPredicate(tomeNumber, tomeNumberComparatorType);
		Predicate<Integer> finalBookAmountPredicate = getPredicate(finalBookAmount, finalBookAmountComparatorType);
		return (Book e) -> {
			return bookNamePredicate.test(e.getBookName()) && authorNamePredicate.test(e.getAuthorName())
					&& circulationPredicate.test(e.getCirculation()) && tomeNumberPredicate.test(e.getTomeNumber())
					&& finalBookAmountPredicate.test(e.getFinalTomeNumber());
		};
	}

	private Predicate<String> getPredicate(String value, ComparatorType type) {
		Predicate<String> predicate = e -> {
			return true;
		};
		switch (type) {
		case stringEquals: {
			predicate = e -> {
				return value.equals(e);
			};
		}
			break;
		case all: {
			predicate = e -> {
				return true;
			};
			break;
		}
		default:
			log.error("Something wrong whith getStringPredicate");
			predicate = e -> {
				return false;
			};
		}
		return predicate;
	}

	private Predicate<Integer> getPredicate(Integer value, ComparatorType type) {
		Predicate<Integer> predicate = e -> {
			return true;
		};
		switch (type) {
		case less: {
			predicate = e -> {
				return e < value;
			};
		}
			break;
		case more: {
			predicate = e -> {
				return e > value;
			};
			break;
		}
		case numberEquals: {
			predicate = e -> {
				return value.equals(e);
			};
		}
			break;
		case all: {
			predicate = e -> {
				return true;
			};
		}
			break;
		default:
			log.error("Something wrong whith getIntegerPredicate");
			predicate = e -> {
				return false;
			};
		}
		return predicate;
	}
}
