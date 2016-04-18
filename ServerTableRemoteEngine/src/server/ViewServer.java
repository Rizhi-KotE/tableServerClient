package server;

import java.io.BufferedReader;

import javax.swing.JFrame;

import engine.Engine;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ViewServer extends Application {
	JFrame frame;

	TextArea text;
	@Override
	public void start(Stage primaryStage) throws Exception {
		Pane root = new VBox();
		Engine engine = new Engine();
		text = new TextArea();
		Button start = new Button("start");
		start.setOnAction(e->engine.start());
		Button stop = new Button("stop");
		
		stop.setOnAction(e->{try {
			engine.stop();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}});
		TextFieldLogger.setArea(text);

		root.getChildren().addAll(text, start, stop);
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
		primaryStage.setOnCloseRequest(e->System.exit(0));
	}

	public static void main(String[] args){
		launch(args);
	}
}
