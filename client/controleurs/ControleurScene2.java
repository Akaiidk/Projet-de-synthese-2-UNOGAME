package client.controleurs;

import client.fx.LocatedImage;
import client.reseau.ConnexionClient;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class ControleurScene2 implements Controleur {
    //Champs FXML
    @FXML
    private Pane _NavbarPane;
    @FXML
    private GridPane _ListeJoueur;
    @FXML
    private ImageView _Tas;
    @FXML
    private StackPane _Main;
    @FXML
    private ListView<String> _Log;
    @FXML
    private Button _Jouer;
    @FXML
    private Button _DireUno;
    @FXML
    private Button _TerminerTour;
    @FXML
    private Button _Piocher;
    @FXML
    private Label _JoueurCourant;

    //Champ
    private final ConnexionClient client;
    private final Pane navbarPane;
    private String carteSelectionne;

    //Controleur
    public ControleurScene2(ConnexionClient client, Pane navbarPane) {
        this.navbarPane = navbarPane;
        this.client = client;
    }

    public Pane get_NavbarPane() {
        return this._NavbarPane;
    }

    public GridPane get_ListeJoueur() {
        return this._ListeJoueur;
    }

    public StackPane get_Main() {
        return this._Main;
    }

    public ListView<String> get_Log() {
        return this._Log;
    }

    public ImageView get_Tas() {
        return this._Tas;
    }

    public Button get_Jouer() {
        return this._Jouer;
    }

    public Button get_DireUno() {
        return this._DireUno;
    }

    public Button get_TerminerTour() {
        return this._TerminerTour;
    }

    public Button get_Piocher() {
        return this._Piocher;
    }

    public Label get_JoueurCourant() {
        return this._JoueurCourant;
    }

    public ConnexionClient getClient() {
        return this.client;
    }

    public Pane getnavbarPane() {
        return this.navbarPane;
    }

    public String getCarteSelectionne() {
        return this.carteSelectionne;
    }


    //Setter
    public void setCarteSelectionne(String carteSelectionne) {
        this.carteSelectionne = carteSelectionne;
    }

    //Initialize
    @FXML
    public void initialize() {
        this.get_NavbarPane().getChildren().add(this.getnavbarPane());

        this.get_Log().setCellFactory(_ -> new ListCell<>() {
            {
                setPrefWidth(0);
                setWrapText(true);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                } else {
                    setText(item);
                    setWrapText(true);
                }
            }
        });

        this.get_Jouer().setOnAction(_ -> {
            if (this.getCarteSelectionne() != null) {
                this.getClient().getThreadConsole().envoyerMessage("@CARTE_JOUEE "+this.getCarteSelectionne());
            }
            setCarteSelectionne(null);
        });

        this.get_Jouer().setOnDragEntered(event -> {
            Button bclic = (Button) (event.getSource());
            Tooltip toolTip = bclic.getTooltip();
            toolTip.show(bclic, bclic.localToScreen(bclic.getBoundsInLocal()).getMaxX()+10, bclic.localToScreen(bclic.getBoundsInLocal()).getMinY()+10);
        });

        this.get_Jouer().setOnDragExited(event -> {
            Button bclic = (Button) (event.getSource());
            Tooltip toolTip = bclic.getTooltip();
            toolTip.hide();
        });

        this.get_TerminerTour().setOnAction(_ -> this.getClient().getThreadConsole().envoyerMessage("@FIN_TOUR"));

        this.get_Piocher().setOnAction(_ -> this.getClient().getThreadConsole().envoyerMessage("@PIOCHE"));

        this.get_DireUno().setOnAction(_ -> this.getClient().getThreadConsole().envoyerMessage("@UNO"));
    }

    //Méthode Specifique
    public void selectionnerCarte(MouseEvent mouseEvent) {
        ObservableList<Node> buttons = this.get_Main().getChildren();
        for (Node button : buttons) {
            if (button.getStyleClass().contains("button-carte-selected")) {
                button.getStyleClass().clear();
                button.getStyleClass().add("button-invisible");
                break;
            }
        }
        Button button = (Button) mouseEvent.getSource();
        ImageView imageView = (ImageView) button.getGraphic();
        LocatedImage locatedImage = (LocatedImage) imageView.getImage();
        this.setCarteSelectionne(locatedImage.getNom());
        button.getStyleClass().removeAll();
        button.getStyleClass().add("button-carte-selected");
    }
}
