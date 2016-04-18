package remoteInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

import implementation.SerializableBookPredicate;
import model.Book;

public interface TableData extends Remote {
	boolean addElement(Book lement) throws RemoteException;

	Book[] getElements() throws RemoteException;
	
	Book[] getElements(SerializableBookPredicate pBookdicate) throws RemoteException;
	
	void addElements(Book[] eBookments) throws RemoteException;
	
	Book[] removeElements(SerializableBookPredicate predBookate) throws RemoteException;

}
