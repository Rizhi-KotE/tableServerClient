package engine;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.apache.log4j.Logger;

import implementation.Library;
import implementation.RemoteLibrary;

public class Engine {
	private static final Logger log = Logger.getLogger(Engine.class);

	int port = 0;
	public Engine(int port) {
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new RMISecurityManager());
			log.debug("set RMISecurityManager");
		}
		try {
			LocateRegistry.createRegistry(port);
			log.debug("create registry whith port " + port);
			this.port = port;
		} catch (RemoteException e) {
			log.error("error in creating registry", e);
			//e.printStackTrace();
		}
	}

	public Engine() {
		this(1099);
	}
	public void addRemoteObject(String name, UnicastRemoteObject object) throws RemoteException, AlreadyBoundException {
		Registry registry = null;
		try {
			registry = LocateRegistry.getRegistry(port);
		} catch (RemoteException e) {
			throw e;
		}
		try {
			registry.bind(name, object);
		} catch (AlreadyBoundException e) {
			throw e;
		}
	}
	public void rebindObject(String name, UnicastRemoteObject object) throws RemoteException{
		Registry registry = null;
		try {
			registry = LocateRegistry.getRegistry(port);
		} catch (RemoteException e) {
			throw e;
		}
		registry.rebind(name, object);
		
	}
	public void unbindObject(String name) throws RemoteException, NotBoundException{
		Registry registry = null;
		try {
			registry = LocateRegistry.getRegistry(port);
		} catch (RemoteException e) {
			throw e;
		}
		registry.unbind(name);
		
	}
	public void start() {
		Library library = new Library();
		RemoteLibrary remoteLib = null;
		try {
			remoteLib = new RemoteLibrary(library);
		} catch (RemoteException e) {
			log.error("Fail in creating remote library", e);
		}
		try {
			addRemoteObject("Library", remoteLib);
		} catch (RemoteException e) {
			log.error("error in addeding remote library", e);
		} catch (AlreadyBoundException e) {
			log.debug("Remote library already registered");
			try {
				rebindObject("Library", remoteLib);
			} catch (RemoteException e1) {
				log.error("error in rebinding object", e);
			}
		}
	}
	public void stop() throws NotBoundException, RemoteException {
			unbindObject("Library");
	}

}
