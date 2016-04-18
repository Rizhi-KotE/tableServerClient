package client.view;

import java.io.File;
import java.util.Locale;

import client.application.Main;
import client.controler.TableControler;
import frm.SaveGraph;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;

public class MainMenu {
	private MenuBar menuBar;

	private TableControler controler;

	public MenuBar getMenuBar() {
		return menuBar;
	}

	public MainMenu(TableControler contr) {
		controler = contr;
		menuBar = new MenuBar();

		Menu menu = new Menu("file");
		menuBar.getMenus().add(menu);

		MenuItem item = new MenuItem("open");
		item.setOnAction((e) -> {
			FileChooser chooser = new FileChooser();
			File fileName = chooser.showOpenDialog(Main.getStage());
			controler.openFile(SaveGraph.loadLybrary(fileName));
		});

		MenuItem save = new MenuItem("save");
		save.setOnAction((e) -> {
			FileChooser chooser = new FileChooser();
			File fileName = chooser.showSaveDialog(Main.getStage());
			SaveGraph.save(fileName, controler.getBooks());
		});
		menu.getItems().addAll(item, save);

		menu = new Menu("edit");
		menuBar.getMenus().add(menu);

		item = new MenuItem("find");
		item.setOnAction((e) -> {
			new TableDialog(controler).showFindDialog();
		});
		menu.getItems().add(item);
		item = new MenuItem("delete");
		item.setOnAction((e) -> {
			new TableDialog(controler).showDeleteDialog();
		});
		menu.getItems().add(item);
		menu = new Menu("help");
		menuBar.getMenus().add(menu);

		Menu changeLanguage = new Menu("changeLanguage");
		menu.getItems().add(changeLanguage);

		MenuItem ru = new MenuItem("RU");
		ru.setOnAction((e) -> {
			Main.setBundle(new Locale("RU"));
		});
		MenuItem eng = new MenuItem("ENG");
		eng.setOnAction((e) -> {
			Main.setBundle(new Locale("ENG"));
		});
		changeLanguage.getItems().addAll(ru, eng);
	}
}
