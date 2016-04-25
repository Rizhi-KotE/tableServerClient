package implementation;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.apache.log4j.Logger;

import frm.SaveGraph;
import model.Book;
import remoteInterface.TableData;
import server.ViewServer;

public class RemoteLibrary extends UnicastRemoteObject implements TableData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5874943434186085346L;

	private static Logger log = Logger.getLogger(RemoteLibrary.class);
	private Library library;

	public RemoteLibrary(Library lib) throws RemoteException {
		super();
		ViewServer.appendText("create library\n");
		library = lib;
	}

	@Override
	public boolean addElement(Book element) throws RemoteException {
		ViewServer.appendText(element.toString()+"\n");
		return library.addBook(element);
	}

	@Override
	public Book[] getElements() throws RemoteException {
		List<Book> out = library.getBooks();
		ViewServer.appendText("return all books");
		return out.toArray(new Book[out.size()]);
	}

	@Override
	public void addElements(Book[] elements) throws RemoteException {
		ViewServer.appendText("add array of books");
		library.openFile(Arrays.asList(elements));
	}

	@Override
	public Book[] getElements(SerializableBookPredicate pBookdicate) throws RemoteException {
		Predicate<Book> predicate = pBookdicate.getPredicate();
		ViewServer.appendText("return filtered books: ");
		List<Book> out = library.getBooks(book -> {
			try {
				return predicate.test(book);
			} catch (Exception e) {
				log.error("error in geting books", e);
				ViewServer.appendText("error\n");
				return false;
			}
		});
		ViewServer.appendText("correct\n");
		return out.toArray(new Book[out.size()]);
	}

	@Override
	public Book[] removeElements(SerializableBookPredicate predBookate) throws RemoteException {
		Predicate<Book> predicate = predBookate.getPredicate();
		ViewServer.appendText("remove some books: ");
		List<Book> out = library.remove(book -> {
			try {
				return predicate.test(book);
			} catch (Exception e) {
				log.error("error in remove elements", e);
				ViewServer.appendText("error\n");
				//e.printStackTrace();
				return false;
			}
		});
		ViewServer.appendText("correct\n");
		return out.toArray(new Book[out.size()]);
	}

	@Override
	public Book[] openFile(File file) throws RemoteException {
		library.openFile(SaveGraph.loadLybrary(file));
		List<Book> books = library.getBooks();
		return books.toArray(new Book[books.size()]);
	}

	@Override
	public void saveFile(File file) throws RemoteException {
		SaveGraph.save(file, library.getBooks());		
	}
}
