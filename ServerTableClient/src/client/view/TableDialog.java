package client.view;

import java.util.function.Predicate;

import client.application.LocalizedScene;
import client.application.Main;
import client.controler.TableControler;
import implementation.SerializableBookPredicate;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TableDialog {
	private Stage stage;

	/**
	 * @return the stage
	 */
	public void showFindDialog() {
		Pane main = new VBox();
		PagedTable table = new PagedTable();
		main.getChildren().add(Main.attachedPanel(table));
		main.getChildren().add(findElements(table));
		stage.setScene(new LocalizedScene(main));
		stage.show();
	}

	public void showDeleteDialog() {
		Pane main = new VBox();
		PagedTable table = new PagedTable();
		main.getChildren().add(Main.attachedPanel(table));
		main.getChildren().add(removeElements(table));
		stage.setScene(new LocalizedScene(main));
		stage.show();
	}

	private TableControler controler;

	private TextField bookNameField = new TextField();
	private TextField authorNameField = new TextField();
	private TextField tomesNumberField = new TextField();
	private TextField circulationField = new TextField();
	private TextField finalTomesNumberField = new TextField();

	Predicate<String> bookNamePred = (name) -> {
		return true;
	};
	Predicate<String> authorNamePred = (name) -> {
		return true;
	};
	Predicate<Integer> circulationPred = (name) -> {
		return true;
	};
	Predicate<Integer> tomesNumbPred = (name) -> {
		return true;
	};
	Predicate<Integer> finalTomesNumbPred = (name) -> {
		return true;
	};

	private ToggleGroup finalTomesNumberToogle;

	private ToggleGroup circulationToogle;

	public TableDialog(TableControler contr) {
		stage = new Stage();
		controler = contr;
		stage.setOnCloseRequest((e) -> {
			stage.close();
		});
	}

	private Pane findElements(PagedTable table) {
		Pane box = new VBox();
		bookNameField.setPromptText("bookName");
		authorNameField.setPromptText("authorName");
		tomesNumberField.setPromptText("tomeNumber");
		circulationField.setPromptText("circulation");
		finalTomesNumberField.setPromptText("finalTomeNumber");
		Button findButton = new Button("find");
		findButton.setOnAction((e) -> {
			table.setBooks(controler.getBooks(calcResultPredicate()));
		});
		box.getChildren().addAll(bookNameField, authorNameField, circulationCheckBox(), tomesNumberField,
				finalNumberOfTomesCheckBox(), findButton);
		return box;
	}

	private Pane removeElements(PagedTable table) {
		Pane box = new VBox();
		bookNameField.setPromptText("bookName");
		authorNameField.setPromptText("authorName");
		tomesNumberField.setPromptText("tomeNumber");
		circulationField.setPromptText("circulation");
		finalTomesNumberField.setPromptText("finalTomeNumber");
		Button findButton = new Button("find");
		findButton.setOnAction((e) -> {
			table.setBooks(controler.getBooks(calcResultPredicate()));
		});
		Button removeButton = new Button("remove");
		removeButton.setOnAction((e) -> {
			table.setBooks(controler.removeBooks(calcResultPredicate()));
		});
		box.getChildren().addAll(bookNameField, authorNameField, circulationCheckBox(), tomesNumberField,
				finalNumberOfTomesCheckBox(), findButton, removeButton);
		return box;
	}

	private SerializableBookPredicate calcResultPredicate() {
		SerializableBookPredicate predicate = new SerializableBookPredicate();
		String bookName = bookNameField.getText();
		if (!"".equals(bookName)) {
			predicate.setBookName(bookName, SerializableBookPredicate.ComparatorType.stringEquals);
		} else {
			predicate.setBookName(bookName, SerializableBookPredicate.ComparatorType.all);
		}
		String authorName = authorNameField.getText();
		if (!"".equals(authorName)) {
			predicate.setAuthorName(authorName, SerializableBookPredicate.ComparatorType.stringEquals);
		} else {
			predicate.setAuthorName(authorName, SerializableBookPredicate.ComparatorType.all);
		}
		try {
			int circulation = Integer.parseInt(circulationField.getText());
			switch ((String) circulationToogle.getSelectedToggle().getUserData()) {
			case "less":
				predicate.setCirculation(circulation, SerializableBookPredicate.ComparatorType.less);
				break;
			case "more":
				predicate.setCirculation(circulation, SerializableBookPredicate.ComparatorType.more);
				break;
			}
		} catch (Exception e1) {
			predicate.setCirculation(0, SerializableBookPredicate.ComparatorType.all);
		}
		try {
			int finalTomesNumber = Integer.parseInt(finalTomesNumberField.getText());
			switch ((String) finalTomesNumberToogle.getSelectedToggle().getUserData()) {
			case "less":
				predicate.setFinalBookAmount(finalTomesNumber, SerializableBookPredicate.ComparatorType.less);
				break;
			case "more":
				predicate.setFinalBookAmount(finalTomesNumber, SerializableBookPredicate.ComparatorType.more);
				break;
			}
		} catch (Exception e) {
			predicate.setFinalBookAmount(0, SerializableBookPredicate.ComparatorType.all);
		}
		return predicate;
	}

	private Pane circulationCheckBox() {
		Pane pane = new HBox();
		pane.getChildren().add(circulationField);
		circulationToogle = new ToggleGroup();

		RadioButton less = new RadioButton("less");
		less.setUserData("less");
		less.setSelected(true);
		less.setToggleGroup(circulationToogle);

		RadioButton more = new RadioButton("more");
		more.setUserData("more");
		more.setToggleGroup(circulationToogle);

		pane.getChildren().addAll(more, less);
		return pane;
	}

	private Pane finalNumberOfTomesCheckBox() {
		Pane pane = new HBox();
		pane.getChildren().add(finalTomesNumberField);
		finalTomesNumberToogle = new ToggleGroup();

		RadioButton less = new RadioButton("less");
		less.setUserData("less");
		less.setSelected(true);
		less.setToggleGroup(finalTomesNumberToogle);

		RadioButton more = new RadioButton("more");
		more.setUserData("more");
		more.setToggleGroup(finalTomesNumberToogle);

		pane.getChildren().addAll(more, less);
		return pane;
	}
}
