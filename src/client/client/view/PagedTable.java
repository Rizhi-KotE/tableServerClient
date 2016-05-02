package client.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Book;

public class PagedTable {
	private ObservableList<Book> books;

	private IntegerProperty libSize;
	private TableView<Book> table;
	private IntegerProperty currentGroup;
	private IntegerProperty groupSize;

	public PagedTable() {
		libSize = new SimpleIntegerProperty();
		groupSize = new SimpleIntegerProperty();
		currentGroup = new SimpleIntegerProperty(0);
		table = new TableView<>();
		table = initTable(table);
		List<Book> list = new ArrayList<>(10);
		setBooks(list);
		setGroupSize(10);
	}

	public void setGroupSize(int size) {
		groupSize.set(size);
		table.minHeight(25+size*25);
		table.setMaxHeight(25+size*25);
		table.prefHeight(25+size*25);
		if(currentGroup.get()*size>libSize.get()){
			currentGroup.set(libSize.get()/groupSize.get());
		}
		setCurrentGroup();
	}

	public void setBooks(List<Book> lib) {
		books = FXCollections.observableArrayList(lib);
		libSize.set(books.size());
		ListChangeListener<Book> listener = e -> {
			libSize.set(books.size());
		};
		currentGroup.set(0);
	books.addListener(listener);

	setCurrentGroup();

	}

	private void setCurrentGroup() {
		List<Book> subList = new ArrayList<Book>(groupSize.get());
		Iterator<Book> allBooksIt = books.listIterator(currentGroup.get() * groupSize.get());
		while (allBooksIt.hasNext() && subList.size() < groupSize.get()) {
			subList.add(allBooksIt.next());
		}
		setItems(subList);
	}

	public void end() {
		if (libSize.get() > groupSize.get()) {
			currentGroup.set(libSize.get() / groupSize.get());
			setCurrentGroup();
		}
	}

	public void first() {
		if (currentGroup.get() != 0) {
			currentGroup.set(0);
			setCurrentGroup();
		}
	}

	/**
	 * @return the libSize
	 */
	public IntegerProperty getLibSize() {
		return libSize;
	}

	
	/**
	 * @return the pane
	 */

	/**
	 * @return the currentGroup
	 */
	public IntegerProperty getCurrentGroup() {
		return currentGroup;
	}

	/**
	 * @return the groupSize
	 */
	public IntegerProperty getGroupSize() {
		return groupSize;
	}

	/**
	 * @return the table
	 */
	public TableView<Book> getTable() {
		return table;
	}

	private TableView<Book> initTable(TableView<Book> table) {
		TableColumn<Book, String> bookNameCol = new TableColumn<Book, String>("bookName");
		bookNameCol.setMinWidth(100);
		bookNameCol.setCellValueFactory(new PropertyValueFactory<Book, String>("bookName"));
		
		TableColumn<Book, String> authorCol = new TableColumn<Book, String>("authorName");
		authorCol.setMinWidth(100);
		authorCol.setCellValueFactory(new PropertyValueFactory<Book, String>("authorName"));

		TableColumn<Book, String> circulationCol = new TableColumn<Book, String>("circulation");
		circulationCol.setMinWidth(100);
		circulationCol.setCellValueFactory(new PropertyValueFactory<Book, String>("circulation"));

		TableColumn<Book, String> tomesNumCol = new TableColumn<Book, String>("tomeNumber");
		tomesNumCol.setMinWidth(100);
		tomesNumCol.setCellValueFactory(new PropertyValueFactory<Book, String>("tomeNumber"));

		TableColumn<Book, String> finalTomesNumCol = new TableColumn<Book, String>("finalTomeNumber");
		finalTomesNumCol.setMinWidth(100);
		finalTomesNumCol.setCellValueFactory(new PropertyValueFactory<Book, String>("finalTomeNumber"));

		table.getColumns().addAll(bookNameCol, authorCol, circulationCol, tomesNumCol, finalTomesNumCol);
		return table;
	}

	public void next() {
		if (libSize.get() > currentGroup.get() * groupSize.get() + groupSize.get()) {
			currentGroup.set(currentGroup.get() + 1);
			setCurrentGroup();
		}
	}

	public void previos() {
		if (currentGroup.get() > 0) {
			currentGroup.set(currentGroup.get() - 1);
			setCurrentGroup();
		}
	}

	private void setItems(List<Book> find) {
		table.setItems(FXCollections.observableArrayList(find));
	}

	public void addBook(Book book) {
		books.add(book);
		setCurrentGroup();
	}
	
}
