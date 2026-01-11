package client.controleurs;

import client.reseau.ConnexionClient;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ControleurScene3 implements Controleur {
    //Champs FXML
    @FXML
    private Pane _NavbarPane;
    @FXML
    private Label _LabelVictoire;
    @FXML
    private Button _RevenirEcranTitre;

    private final ConnexionClient client;
    private final Pane navbarPane;
    private final String pseudoGagnant;

    //Getter
    public Pane get_NavbarPane() {
        return this._NavbarPane;
    }

    public Label get_LabelVictoire() {
        return this._LabelVictoire;
    }

    public Button get_RevenirEcranTitre() {
        return this._RevenirEcranTitre;
    }

    public ConnexionClient getClient() {
        return this.client;
    }

    public Pane getNavbarPane() {
        return this.navbarPane;
    }

    public String getPseudoGagnant() {
        return this.pseudoGagnant;
    }

    //Constructeur
    public ControleurScene3(ConnexionClient client, Pane navbarPane, String pseudoGagnant) {
        this.navbarPane = navbarPane;
        this.client = client;
        this.pseudoGagnant = pseudoGagnant;
    }

    @FXML
    public void initialize() {
        this.get_NavbarPane().getChildren().add(this.getNavbarPane());

        this.get_LabelVictoire().setWrapText(true);

        if (this.getPseudoGagnant().equals(this.getClient().getPseudo())) {
            this.get_LabelVictoire().setText("Félicitation à toi, tu as remporté cette partie ! Es-tu prêt pour remettre ton titre en jeu ?");
        }
        else {
            this.get_LabelVictoire().setText("Dommage, tu as perdu cette partie ! C'est " + this.getPseudoGagnant() + " qui l'a remportée. Je suis sûr que tu peux faire mieux à la prochaine partie !");
        }

        this.get_RevenirEcranTitre().setOnAction(_ -> {
            try {
                FXMLLoader loaderNavbar = new FXMLLoader(getClass().getResource("../vue/Navbar.fxml"));
                ControleurNavbar controleurNavbar = new ControleurNavbar(this.getClient());
                loaderNavbar.setController(controleurNavbar);

                FXMLLoader loaderScene1 = new FXMLLoader(getClass().getResource("../vue/Scene1.fxml"));
                ControleurScene1 controleurScene1 = new ControleurScene1(this.client, loaderNavbar.load());
                loaderScene1.setController(controleurScene1);
                this.getClient().getScene().setRoot(loaderScene1.load());

                this.getClient().setControleur(controleurScene1);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
