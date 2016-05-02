package remoteInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileTreeCreator extends Remote{
	String getRoot() throws RemoteException;
	String[] getFiles(String file) throws RemoteException;
	String[] getFiles() throws RemoteException;
}
