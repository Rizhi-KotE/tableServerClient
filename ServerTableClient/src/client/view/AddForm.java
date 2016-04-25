package client.view;


import client.controler.TableControler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import model.Book;

public class AddForm {
	private Pane pane;
	private TextField bookNameField = new TextField();
	private TextField athorNameField = new TextField();
	private TextField tomesNameField = new TextField();
	private TextField circulationNameField = new TextField();
	private Button addButton = new Button("add");
	
	public AddForm(TableControler controler) {
		pane = new HBox();
		
		bookNameField.setPromptText("bookName");
		athorNameField.setPromptText("authorName");
		tomesNameField.setPromptText("tomeNumber");
		circulationNameField.setPromptText("circulation");
		addButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				
				Book book;
				try {
					book = new Book(bookNameField.getText(),athorNameField.getText(),Integer.parseInt(tomesNameField.getText()),Integer.parseInt(circulationNameField.getText()));
					controler.addBookToLibrary(book);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
				pane.getChildren().addAll(bookNameField, athorNameField, tomesNameField,circulationNameField,addButton);
	}
	
	public Pane getPane(){
		return pane;
	}
}
