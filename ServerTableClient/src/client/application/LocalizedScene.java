package client.application;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.Map;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Labeled;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class LocalizedScene extends Scene {

	public LocalizedScene(Parent root) {
		super(root);
		localizeLabeled(root);
	}

	public LocalizedScene(Parent root, double width, double height) {
		super(root, width, height);
		localizeLabeled(root);
	}

	Map<Class<?>, Labeled> labeled;

	private void localizeLabeled(Parent root) {
		Iterator<Node> it = root.getChildrenUnmodifiable().iterator();
		while (it.hasNext()) {
			Node node = it.next();

			Method method = null;
			try {
				method = node.getClass().getMethod("setText", String.class);
			} catch (NoSuchMethodException e) {

			} catch (SecurityException e) {

			}
			if (node instanceof TextField) {
				localiseNode("PromptText", node);
			} else if (method != null) {
				localiseNode("Text", node);
			} else if (node instanceof MenuBar) {
				localiseMenuBar((MenuBar) node);
			} else if (node instanceof TableView<?>) {
				localiseTable((TableView) node);
			} else if (node instanceof Parent) {
				if (((Parent) node).getChildrenUnmodifiable() != null) {
					localizeLabeled((Parent) node);
				}
			}
		}
	}

	private void localiseMenuBar(MenuBar node) {
		for (Menu menu : node.getMenus()) {
			localiseNode("Text", menu);
			for (MenuItem item : menu.getItems()) {
				localiseNode("Text", item);
			}
		}
	}

	private void localiseTable(TableView node) {
		Iterator<TableColumn<?, ?>> it = node.getColumns().iterator();
		while (it.hasNext()) {
			TableColumn<?, ?> column = it.next();
			localiseNode("Text", column);
		}
	}

	private void localiseNode(String name, Object node) {
		Method set = null;
		Method get = null;
		try {
			set = node.getClass().getMethod("set" + name, String.class);
			get = node.getClass().getMethod("get" + name, new Class[0]);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		String currentText = null;
		try {
			currentText = (String) get.invoke(node, new Object[0]);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}

		if (currentText != null) {
			String newText = null;
			try {
				newText = Main.getBundle().getString(currentText);
			} catch (Exception e) {
				newText = currentText;
			}
			try {
				set.invoke(node, newText);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}

	}
}
