package implementation;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import remoteInterface.FileTreeCreator;

public class RemoteFileTreeCreator extends UnicastRemoteObject implements FileTreeCreator{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3121789571219913730L;
	File root;
	public RemoteFileTreeCreator(File root) throws RemoteException {
		super();
		this.root = root;
	}

	@Override
	public String[] getFiles(String file) throws RemoteException {
		return new File(file).list();
	}

	@Override
	public String[] getFiles() throws RemoteException {
		return getFiles(root.getAbsolutePath());
	}

	@Override
	public String getRoot() throws RemoteException {
		return root.getPath();
	}

}
