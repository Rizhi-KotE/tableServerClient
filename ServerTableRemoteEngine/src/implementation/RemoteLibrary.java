package implementation;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.apache.log4j.Logger;

import model.Book;
import remoteInterface.TableData;

public class RemoteLibrary extends UnicastRemoteObject implements TableData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5874943434186085346L;

	private static Logger log = Logger.getLogger(RemoteLibrary.class);
	private Library library;

	public RemoteLibrary(Library lib) throws RemoteException {
		super();
		library = lib;
	}

	@Override
	public boolean addElement(Book element) throws RemoteException {
		return library.addBook(element);
	}

	@Override
	public Book[] getElements() throws RemoteException {
		List<Book> out = library.getBooks();
		return out.toArray(new Book[out.size()]);
	}

	@Override
	public void addElements(Book[] elements) throws RemoteException {
		library.openFile(Arrays.asList(elements));
	}

	public List<Book> removeElements(Predicate<Book> predicate) throws RemoteException {
		return library.remove(predicate);
	}

	@Override
	public Book[] getElements(SerializableBookPredicate pBookdicate) throws RemoteException {
		Predicate<Book> predicate = pBookdicate.getPredicate();
		List<Book> out = library.getBooks(book -> {
			try {
				return predicate.test(book);
			} catch (Exception e) {
				log.error(e);
				return false;
			}
		});
		return out.toArray(new Book[out.size()]);
	}

	@Override
	public Book[] removeElements(SerializableBookPredicate predBookate) throws RemoteException {
		Predicate<Book> predicate = predBookate.getPredicate();
		List<Book> out = library.remove(book -> {
			try {
				return predicate.test(book);
			} catch (Exception e) {
				log.error(e);
				e.printStackTrace();
				return false;
			}
		});
		return out.toArray(new Book[out.size()]);
	}
}
