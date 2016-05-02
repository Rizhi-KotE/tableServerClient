package client.view;

import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import client.application.Main;
import client.controler.TableControler;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class ConnectForm {
	private static Logger log = Logger.getLogger(ConnectForm.class);
	private Stage ownStage;

	/**
	 * @param ownStage
	 *            the ownStage to set
	 */
	public void setOwnStage(Stage ownStage) {
		this.ownStage = ownStage;
	}

	private TilePane pane;
	private TextField ipField = new TextField("127.0.0.1");
	private TextField portField = new TextField("1099");
	private Button connectButton = new Button("connect");

	private final String ipPattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";

	public ConnectForm(TableControler controler, Stage stage) {
		ownStage = stage;
		pane = new TilePane(Orientation.VERTICAL);
		pane.setVgap(10);

		ipField.setMaxWidth(Double.MAX_VALUE);
		portField.setMaxWidth(Double.MAX_VALUE);
		connectButton.setMaxWidth(Double.MAX_VALUE);

		connectButton.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				String IP = "127.0.0.1";
				IP = ipField.getText();
				if (Pattern.compile(ipPattern).matcher(IP) == null) {
					Alert dialog = new Alert(AlertType.ERROR);
					dialog.setTitle(Main.getBundle().getString("$ip_incorrect_format_title"));
					dialog.setHeaderText(Main.getBundle().getString("$ip_incorrect_format_header"));
					dialog.setContentText(Main.getBundle().getString("$ip_incorrect_format_text"));

					dialog.showAndWait();
					return;
				}
				int port = 1099;

				try {
					port = Integer.parseInt(portField.getText());
				} catch (NumberFormatException e) {
					if (Pattern.compile(ipPattern).matcher(IP) == null) {
						Alert dialog = new Alert(AlertType.ERROR);
						dialog.setTitle(Main.getBundle().getString("$port_incorrect_format_title"));
						dialog.setHeaderText(Main.getBundle().getString("$port_incorrect_format_header"));
						dialog.setContentText(Main.getBundle().getString("$port_incorrect_format_message"));
						dialog.showAndWait();
						return;
					}
				}
				try {
					controler.getRemoteLibrary(IP, port);
					ownStage.close();
				} catch (AccessException e) {
					log.error(e);
					Alert dialog = new Alert(AlertType.ERROR);
					dialog.setTitle(Main.getBundle().getString("$acces_error_title"));
					dialog.setHeaderText(Main.getBundle().getString("$acces_error_header"));
					dialog.setContentText(Main.getBundle().getString("$port_error_message"));
					dialog.showAndWait();

				} catch (RemoteException e) {
					log.error(e);
					Alert dialog = new Alert(AlertType.ERROR);
					dialog.setTitle(Main.getBundle().getString("$remote_error_title"));
					dialog.setHeaderText(Main.getBundle().getString("$remote_error_header"));
					dialog.setContentText(Main.getBundle().getString("$remote_error_message"));
					dialog.showAndWait();

				} catch (NotBoundException e) {
					log.error(e);
					Alert dialog = new Alert(AlertType.ERROR);
					dialog.setTitle(Main.getBundle().getString("$NotBound_error_title"));
					dialog.setHeaderText(Main.getBundle().getString("$NotBound_error_headre"));
					dialog.setContentText(Main.getBundle().getString("$NotBound_error_message"));
					dialog.showAndWait();
					return;
				}
			}
		});
		pane.getChildren().addAll(ipField, portField, connectButton);
	}

	public Pane getPane() {
		return pane;
	}
}
