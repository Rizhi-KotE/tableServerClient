package implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import model.Book;

public class Library {
	private ObservableList<Book> books;
	private IntegerProperty size = new SimpleIntegerProperty();

	public enum booksType {
		ALL, FINDED
	};

	public Library() {
		books = FXCollections.observableArrayList();
		//books.addAll(new Book("asd", "qwe", 1, 2));
		ListChangeListener<Book> listener = (e) -> {
			size.set(books.size());
		};
		books.addListener(listener);
		size.set(books.size());
	}

	public boolean addBook(Book book) {
		return books.add(book);
	}

	public List<Book> getBooks(Predicate<Book> predicate) {
		return new ArrayList<>(books.filtered(predicate));
	}
	
	public List<Book> getBooks() {
		return new ArrayList<>(books.filtered(books->{return true;}));
	}
	
	public void openFile(Collection<Book> elements) {
		books.clear();
		books.addAll(elements);
	}

	public List<Book> remove(Predicate<Book> predicate) {
		List<Book> out = new ArrayList<>(books.filtered(predicate));
		books.removeIf(predicate);
		return out;
	}
}
