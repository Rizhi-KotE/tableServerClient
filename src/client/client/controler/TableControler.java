package client.controler;

import java.io.File;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import client.view.PagedTable;
import exceptions.GetRegistryException;
import implementation.SerializableBookPredicate;
import model.Book;
import remoteInterface.FileTreeCreator;
import remoteInterface.TableData;

public class TableControler {
	private final static Logger log = Logger.getLogger(TableControler.class);
	private TableData libraryStub;
	private Map<PagedTable, SerializableBookPredicate> observer;

	String IP;
	int port;
	public TableControler() {
		observer = new HashMap<>();
		if(System.getSecurityManager()==null){
			System.setSecurityManager(new RMISecurityManager());
			log.debug("security manager is added");
		}
	}

	public TableControler(String IP, int port)
			throws RemoteException, GetRegistryException, NotBoundException, ClassCastException {
		this();
		this.IP = IP;
		this.port = port;
		Registry registry = null;
		try {
			registry = LocateRegistry.getRegistry(IP, port);
		} catch (RemoteException e) {
			log.error(e);
			e.printStackTrace();
			throw new GetRegistryException();
		}

		try {
			libraryStub = (TableData) registry.lookup("Library");
		} catch (NotBoundException e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} catch (ClassCastException e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
	}

	public void getRemoteLibrary(String IP, int port) throws NotBoundException, AccessException, RemoteException {
		Registry registry = null;
		try {
			registry = LocateRegistry.getRegistry(IP, port);
		} catch (RemoteException e) {
			log.error(e);
			e.printStackTrace();
			throw new GetRegistryException();
		}

		try {
			libraryStub = (TableData) registry.lookup("Library");
		} catch (NotBoundException e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} catch (ClassCastException e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
	}

	public boolean addBookToLibrary(Book book) throws NullPointerException {
		boolean isAdded = false;
		try {
			isAdded = libraryStub.addElement(book);
		} catch (RemoteException e) {
			log.error(e);
			e.printStackTrace();
		} catch (NullPointerException e) {
			log.error(e);
			e.printStackTrace();
		}
		if (isAdded) {
			fireModelAddBook(book);
		}
		return isAdded;
	}

	private void fireModelAddBook(Book book) {
		for (PagedTable table : observer.keySet()) {
			if (observer.get(table).getPredicate().test(book)) {
				table.addBook(book);
			}
		}
	}

	private void fireModelChange() {
		for (PagedTable table : observer.keySet()) {
			try {
				table.setBooks(Arrays.asList(libraryStub.getElements(observer.get(table))));
			} catch (RemoteException e) {
				log.error(e);
				e.printStackTrace();
			} catch (NullPointerException e) {
				log.error(e);
				e.printStackTrace();
			}
		}
	}

	public void openFile(List<Book> list) throws NullPointerException {
		try {
			libraryStub.addElements(list.toArray(new Book[list.size()]));
		} catch (RemoteException e) {
			log.error(e);e.printStackTrace();
		} catch (NullPointerException e) {
			log.error(e);
			e.printStackTrace();
		}
		fireModelChange();
	}

	public List<Book> getBooks() throws NullPointerException {
		try {
			return Arrays.asList(libraryStub.getElements());
		} catch (RemoteException e) {
			log.error(e);
			e.printStackTrace();
			return new ArrayList<>();
		} catch (NullPointerException e) {
			log.error(e);
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public List<Book> getBooks(SerializableBookPredicate predicate) throws NullPointerException {
		try {
			return Arrays.asList(libraryStub.getElements(predicate));
		} catch (RemoteException e) {
			log.error(e);
			e.printStackTrace();
			return new ArrayList<>();
		} catch (NullPointerException e) {
			log.error(e);
			e.printStackTrace();
			return new ArrayList<>();
		}
	}

	public void registerObserverTable(PagedTable table) {
		registerObserverTable(table, new SerializableBookPredicate());
	}

	public void registerObserverTable(PagedTable table, SerializableBookPredicate predicate) {
		observer.put(table, predicate);
	}

	public List<Book> removeBooks(SerializableBookPredicate predicate) throws NullPointerException {
		List<Book> out;
		try {
			out = Arrays.asList(libraryStub.removeElements(predicate));
		} catch (RemoteException e) {
			log.error(e);
			e.printStackTrace();
			out = new ArrayList<>();
		} catch (NullPointerException e) {
			log.error(e);
			e.printStackTrace();
			out = new ArrayList<>();
		}
		fireModelChange();
		return out;
	}
	
	public FileTreeCreator getFileTreeCreator() throws AccessException, RemoteException, NotBoundException{
		Registry registry = null;
		try {
			registry = LocateRegistry.getRegistry(IP, port);
		} catch (RemoteException e) {
			log.error(e);
			e.printStackTrace();
			throw new GetRegistryException();
		}

		try {
			return (FileTreeCreator) registry.lookup("fileTreeCreator");
		} catch (NotBoundException e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		} catch (ClassCastException e) {
			log.error(e);
			e.printStackTrace();
			throw e;
		}
	}
	
	public void saveFileOnServer(File file){
		try {
			libraryStub.saveFile(file);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void openFileOnServer(File file){
		try {
			Arrays.asList(libraryStub.openFile(file));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fireModelChange();
	}
}
