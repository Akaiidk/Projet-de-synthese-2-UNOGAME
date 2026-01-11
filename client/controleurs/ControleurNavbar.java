package client.controleurs;

import client.reseau.ConnexionClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;

import java.io.IOException;
import java.util.Optional;

public class ControleurNavbar implements Controleur {
    //Champs FXML
    @FXML
    private Button _Statistique;
    @FXML
    private Button _Aide;
    @FXML
    private Button _Quitter;

    //Champ
    private final ConnexionClient client;

    //Getter
    public ControleurNavbar(ConnexionClient client) {
        this.client = client;
    }

    public ConnexionClient getClient() {
        return this.client;
    }

    public Button get_Statistique() {
        return this._Statistique;
    }

    public Button get_Aide() {
        return this._Aide;
    }

    public Button get_Quitter() {
        return this._Quitter;
    }


    @FXML
    void initialize() {
        this.get_Quitter().setOnAction(_ -> {
            try {
                FXMLLoader quitter = new FXMLLoader(getClass().getResource("../vue/Quitter.fxml"));
                Pane root = quitter.load();

                Dialog<ButtonType> dialogQuitter = new Dialog<>();
                dialogQuitter.getDialogPane().setContent(root);
                dialogQuitter.initModality(Modality.APPLICATION_MODAL);

                ButtonType oui = new ButtonType("Oui", ButtonBar.ButtonData.OK_DONE);
                ButtonType non = new ButtonType("Non", ButtonBar.ButtonData.CANCEL_CLOSE);
                dialogQuitter.getDialogPane().getButtonTypes().addAll(oui,non);

                Optional<ButtonType> result =  dialogQuitter.showAndWait();
                if(result.get() == oui) {
                    this.getClient().traiterDeconnexion();
                    Platform.exit();
                }
            }
            catch(IOException e) {
                throw new RuntimeException(e);
            }
        });

        this.get_Aide().setOnAction(_ -> {
            try {
                FXMLLoader aide = new FXMLLoader(getClass().getResource("../vue/Aide.fxml"));

                Dialog<ButtonType> dialogAide = new Dialog<>();
                dialogAide.getDialogPane().setContent(aide.load());
                dialogAide.initModality(Modality.APPLICATION_MODAL);

                dialogAide.getDialogPane().getButtonTypes().addAll(new ButtonType("Fermer la fenêtre", ButtonBar.ButtonData.OK_DONE));
                dialogAide.showAndWait();
            }
            catch(IOException e) {
                throw new RuntimeException(e);
            }
        });

        this.get_Statistique().setOnAction(_ -> this.getClient().getThreadConsole().envoyerMessage("@STATS " + this.getClient().getPseudo()));
    }

    @FXML
    private void devoilerBulle(MouseEvent event) {
        Button bclic = (Button) (event.getSource());
        Tooltip toolTip = bclic.getTooltip();
        toolTip.show(bclic, bclic.localToScreen(bclic.getBoundsInLocal()).getMaxX()+10, bclic.localToScreen(bclic.getBoundsInLocal()).getMinY()+10);
    }

    @FXML
    private void cacherBulle(MouseEvent event) {
        Button bclic = (Button) (event.getSource());
        Tooltip toolTip = bclic.getTooltip();
        toolTip.hide();
    }
}
