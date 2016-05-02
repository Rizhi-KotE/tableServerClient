package client.view;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

import client.application.LocalizedScene;
import client.controler.TableControler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import remoteInterface.FileTreeCreator;

public class ServerFileChooser {
	private FileTreeCreator fileTreeCreator;
	private Method initNavigate;
	private TreeView<FileName> treeView;
	private File targetFile;
	private TableControler controler;
	/**
	 * @return the targetFile
	 */
	public File getTargetFile() {
		return targetFile;
	}

	public ServerFileChooser(TableControler mainControler) {
		controler = mainControler;
		try {
			fileTreeCreator = mainControler.getFileTreeCreator();
		} catch (RemoteException | NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void showSaveFileDialog() {
		try {
			initNavigate = this.getClass().getDeclaredMethod("saveButtons", new Class<?>[0]);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		showDialog();
	}

	public void showOpenFileDialog() {
		try {
			initNavigate = this.getClass().getDeclaredMethod("openButtons", new Class<?>[0]);
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		showDialog();
	}

	private void showDialog() {
		Scene scene = new LocalizedScene(initDialogView());
		Stage stage = new Stage();
		stage.setScene(scene);
		stage.show();
	}

	private Pane initDialogView() {
		try {
			treeView = createTreeView(new File(fileTreeCreator.getRoot()));
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Pane buttons = null;
		try {
			buttons = (Pane) initNavigate.invoke(this, new Object[0]);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Pane pane = new VBox();
		pane.getChildren().addAll(treeView, buttons);
		return pane;
	}

	private Pane openButtons() {
		Pane pane = new HBox();
		Button button = new Button("$open");
		button.setOnAction(e -> {
			TreeItem<FileName> item = treeView.getFocusModel().getFocusedItem();
			targetFile = item.getValue().getFile();
			controler.openFileOnServer(targetFile);
		});
		pane.getChildren().add(button);
		return pane;
	}

	private Pane saveButtons() {
		Pane pane = new HBox();
		Button button = new Button("$save");
		button.setOnAction(e -> {
			TreeItem<FileName> item = treeView.getFocusModel().getFocusedItem();
			targetFile = item.getValue().getFile();
			controler.saveFileOnServer(targetFile);
		});
		pane.getChildren().add(button);
		return pane;
	}

	private TreeView<FileName> createTreeView(File file) {
		TreeView<FileName> tree = new TreeView<>();
		TreeItem<FileName> root = new TreeItem<>(new FileName(file, file.getPath()));
		updateChilds(root);
		tree.setRoot(root);
		return tree;
	}

	private void updateChilds(TreeItem<FileName> item) {
		File root = item.getValue().getFile();
		if(!root.isDirectory())
			return;
		try {
			for (String fileName : fileTreeCreator.getFiles(root.getAbsolutePath())) {
				TreeItem<FileName> child = new TreeItem<>(new FileName(new File(fileName)));
				item.getChildren().add(child);
				updateChilds(child);
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private class FileName {
		File file;
		String string;

		public FileName(File file) {
			this.file = file;
			string = file.getName();
		}

		public FileName(File file, String name) {
			this.file = file;
			string = name;
		}

		@Override
		public String toString() {
			return string;
		}

		public File getFile() {
			return file;
		}
	}
}
