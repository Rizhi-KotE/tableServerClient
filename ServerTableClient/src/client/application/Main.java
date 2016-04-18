package client.application;

import java.util.Locale;
import java.util.ResourceBundle;

import client.controler.TableControler;
import client.view.AddForm;
import client.view.ConnectForm;
import client.view.MainMenu;
import client.view.PagedTable;
import client.view.TableDialog;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
	private static ResourceBundle bundle;

	/**
	 * @return the bundle
	 */
	public static ResourceBundle getBundle() {
		if (bundle == null) {
			try {
				bundle = ResourceBundle.getBundle("client.application.text", new Locale("RU"));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return bundle;
	}

	/**
	 * @param bundle
	 *            the bundle to set
	 */
	public static void setBundle(Locale loc) {
		Main.bundle = ResourceBundle.getBundle("text", loc);
	}

	private static Stage primaryStage;

	public static Stage getStage() {
		return primaryStage;
	}

	@Override
	public void start(Stage primaryStage) {
		Main.primaryStage = primaryStage;

		TableControler mainControler = new TableControler();
		PagedTable mainTable = new PagedTable();

		Pane root = createMainScene(mainTable, mainControler);

		Scene scene = new LocalizedScene(root);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		primaryStage.setScene(scene);
		primaryStage.sizeToScene();
		primaryStage.setResizable(true);
		primaryStage.setMaxHeight(Double.MAX_VALUE);
		primaryStage.show();

		primaryStage.setOnCloseRequest((e) -> System.exit(0));
	}

	private Pane createMainScene(PagedTable mainTable, TableControler mainControler) {
		HBox root = new HBox();
		root.setSpacing(10);

		VBox box = new VBox();
		Pane tools = toolBar(mainControler);
		MainMenu menu = new MainMenu(mainControler);
		mainControler.registerObserverTable(mainTable);

		box.getChildren().addAll(menu.getMenuBar(), attachedPanel(mainTable));
		root.getChildren().addAll(box, tools);

		return root;
	}

	private Pane toolBar(TableControler mainControler) {
		TilePane tools = new TilePane(Orientation.VERTICAL);
		tools.setPadding(new Insets(25, 10, 50, 0));
		tools.setVgap(5);
		

		Button connectTool = new Button("connect");
		connectTool.setOnAction(connect -> {
			Stage stage = new Stage();
			Pane root = new ConnectForm(mainControler, stage).getPane();
			Scene scene = new LocalizedScene(root);
			stage.setScene(scene);
			stage.show();
			stage.setOnCloseRequest(close -> stage.close());
		});
		connectTool.setMaxWidth(Double.MAX_VALUE);

		
		Button addTool = new Button("add");
		addTool.setOnAction(add -> {
			Stage stage = new Stage();
			Pane root = new AddForm(mainControler).getPane();
			Scene scene = new LocalizedScene(root);
			stage.setScene(scene);
			stage.show();
			stage.setOnCloseRequest(close -> stage.close());
		});
		addTool.setMaxWidth(Double.MAX_VALUE);

		Button findAndRemoveTool = new Button("findAndRemove");
		findAndRemoveTool.setOnAction(action -> {
			new TableDialog(mainControler).showDeleteDialog();
		});
		findAndRemoveTool.setMaxWidth(Double.MAX_VALUE);

		tools.getChildren().addAll(connectTool, addTool, findAndRemoveTool);
		return tools;
	}

	public static Pane attachedPanel(PagedTable table) {
		VBox pane = new VBox();
		TilePane navigation = new TilePane(Orientation.HORIZONTAL);

		Button prev = new Button("previos");
		prev.setMaxWidth(Double.MAX_VALUE);
		prev.setOnAction(e -> {
			table.previos();
		});
		Button first = new Button("first");
		first.setOnAction(e -> {
			table.first();
		});
		first.setMaxWidth(Double.MAX_VALUE);
		Button next = new Button("next");
		next.setOnAction(e -> {
			table.next();
		});
		next.setMaxWidth(Double.MAX_VALUE);
		Button end = new Button("end");
		end.setOnAction(e -> {
			table.end();
		});
		end.setMaxWidth(Double.MAX_VALUE);

		HBox labels = new HBox();
		Label group = new Label(Integer.toString(table.getCurrentGroup().get() + 1));
		group.setMinSize(10, 0);
		table.getCurrentGroup().addListener((e, oldValue, newValue) -> {
			group.setText(Integer.toString(table.getCurrentGroup().get() + 1));
		});

		Label size = new Label(Integer.toString(table.getLibSize().get() / table.getGroupSize().get() + 1));
		size.setMinSize(10, 0);
		table.getLibSize().addListener((e, oldValue, newValue) -> {
			size.setText(Integer.toString(table.getLibSize().get() / table.getGroupSize().get() + 1));
		});

		table.getGroupSize().addListener((e, oldValue, newValue) -> {
			size.setText(Integer.toString(table.getLibSize().get() / table.getGroupSize().get() + 1));
			group.setText(Integer.toString(table.getCurrentGroup().get() + 1));
		});

		ComboBox<Integer> comboBox = new ComboBox<>();
		comboBox.setOnAction(e -> {
			table.setGroupSize(comboBox.getSelectionModel().getSelectedItem());
			primaryStage.sizeToScene();
		});
		comboBox.getItems().addAll(10, 15, 20);
		comboBox.getSelectionModel().select(0);

		labels.setAlignment(Pos.CENTER);
		labels.getChildren().addAll(group, size, comboBox);

		navigation.setMaxWidth(Double.MAX_VALUE);

		navigation.setHgap(10);
		navigation.setAlignment(Pos.BASELINE_CENTER);
		navigation.getChildren().addAll(first, prev, labels, next, end);
		pane.getChildren().addAll(table.getTable(), navigation);
		return pane;
	}

	public static void main(String[] args) {
		launch(args);
	}
}
