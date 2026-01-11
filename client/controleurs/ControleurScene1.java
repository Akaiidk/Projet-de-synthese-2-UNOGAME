package client.controleurs;

import client.reseau.ConnexionClient;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.Objects;

public class ControleurScene1 implements Controleur {
    //Champs FXML
    @FXML
    private Button _Pret;
    @FXML
    private Button _Jouer;
    @FXML
    private Pane _NavbarPane;
    @FXML
    private Label _LabelErreur;

    //Champs
    private final GridPane navbar;
    private final ConnexionClient client;

    //Constructeur
    public ControleurScene1(ConnexionClient c, GridPane navbar) {
        this.client = c;
        this.navbar = navbar;
    }

    //Getter
    public Button get_Pret() {
        return this._Pret;
    }

    public Button get_Jouer() {
        return this._Jouer;
    }

    public Pane get_NavbarPane() {
        return this._NavbarPane;
    }

    public Label get_LabelErreur() {
        return this._LabelErreur;
    }

    public GridPane getNavbar() {
        return this.navbar;
    }

    public ConnexionClient getClient(){
        return this.client;
    }

    //Initialize
    @FXML
    public void initialize() {
        this.get_NavbarPane().getChildren().add(this.getNavbar());

        this.get_LabelErreur().setWrapText(true);

        this.get_Pret().setOnAction(_ -> {
            this.getClient().getThreadConsole().envoyerMessage("@PRET");
            if(this.get_Pret().getGraphic() == null){
                this.get_Pret().setGraphic(new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("../ressource/images/checkmark.png")))));
            }
            else{
                this.get_Pret().setGraphic(null);
            }
        });

        this.get_Jouer().setOnAction(_ -> this.getClient().getThreadConsole().envoyerMessage("@DEMARRER_PARTIE"));
    }
}
