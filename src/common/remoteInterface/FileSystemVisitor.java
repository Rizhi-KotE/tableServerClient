package remoteInterface;

import java.rmi.RemoteException;

public interface FileSystemVisitor {
	String getRoot();
	String[] getFiles(String file);
	String[] getFiles();
}
