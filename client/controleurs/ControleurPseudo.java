package client.controleurs;

import client.reseau.ConnexionClient;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class ControleurPseudo implements Controleur {
	//Champs FXML
	@FXML
	private Button _Jouer;
	@FXML
	private Button _Quitter;
	@FXML
	private Label _Label;
	@FXML
	private TextField _Text;

	//Champs
	private final Scene scene;
	private ConnexionClient client;
	private final Stage stage;

	//Constructeur
	public ControleurPseudo(Scene s, Stage stage) {
		this.scene = s;
		this.stage = stage;
	}

	//Getter
	public Button get_Jouer() {
		return this._Jouer;
	}

	public Button get_Quitter() {
		return this._Quitter;
	}

	public Label get_Label() {
		return this._Label;
	}

	public TextField get_Text() {
		return this._Text;
	}

	public Scene getScene() {
		return this.scene;
	}

	public ConnexionClient getConnexionClient() {
		return this.client;
	}

	public Stage getStage() {
		return this.stage;
	}

	//Setter
	public void setConnexionClient(ConnexionClient connexionClient) {
		this.client = connexionClient;
	}

	//Initialize
	@FXML
	public void initialize() {
		this.get_Jouer().setOnAction(_ ->{
			String pseudo = this.get_Text().getText();
			if (pseudo.isEmpty() || pseudo.length() > 12 || !pseudo.matches("([0-9A-Za-z_]){1,12}")) {
				this.get_Label().getStyleClass().add("addOpacity");
			}
			if (pseudo.isEmpty()) {
				this.get_Label().setText("Le pseudo ne peut pas être vide.");
			}
			else if (pseudo.length() > 12) {
			this.get_Label().setText("Le pseudo ne peut pas excéder 12 caractères.");
			}
			else if (!pseudo.matches("([0-9A-Za-z_]){1,12}")) {
				this.get_Label().setText("Le pseudo ne peut contenir que des caractères alphanumériques.");
			}
			else {
				this.setConnexionClient(new ConnexionClient(this,this.getScene(), this.getStage(), pseudo));
				this.getConnexionClient().getThreadConsole().envoyerMessage("@CONNEXION " + pseudo);
			}
		});

		this.get_Quitter().setOnAction(_ ->{
			try {
				FXMLLoader quitter = new FXMLLoader(getClass().getResource("../vue/Quitter.fxml"));
				Pane root = quitter.load();
				
				Dialog<ButtonType> dialogQuitter = new Dialog<>();
				dialogQuitter.getDialogPane().setContent(root);
				dialogQuitter.initModality(Modality.APPLICATION_MODAL);
				
				ButtonType oui = new ButtonType("Oui",ButtonData.OK_DONE);
				ButtonType non = new ButtonType("Non",ButtonData.CANCEL_CLOSE);
				dialogQuitter.getDialogPane().getButtonTypes().addAll(oui,non);
				
				Optional<ButtonType> result =  dialogQuitter.showAndWait();
				if(result.get() == oui) {
					Platform.exit();
				}
			}
			catch(IOException e) {
				throw new RuntimeException(e);
			}
        });
	}
}

