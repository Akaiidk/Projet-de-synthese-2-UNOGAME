package client.reseau;


import client.controleurs.*;
import client.fx.LocatedImage;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.Socket;
import java.util.Objects;
import java.util.regex.Pattern;

public class ConnexionClient {
    //Champs
    private static final String SERVEUR = "localhost";
    private static final int PORT = 4567;

    private final ThreadConsole threadConsole;
    private final Socket socket;
    private Controleur controleur;
    private final Scene scene;
    private final Stage stage;
    private final String pseudo;
    //Constructeur
    public ConnexionClient(ControleurPseudo controleur, Scene scene, Stage stage, String pseudo) {
        try {
            this.socket = new Socket(SERVEUR, PORT);
            this.threadConsole = new ThreadConsole(this, this.getSocket());
            this.setControleur(controleur);
            this.scene = scene;
            this.stage = stage;
            this.pseudo = pseudo;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Getter
    public ThreadConsole getThreadConsole() {
        return this.threadConsole;
    }

    public Socket getSocket() {
        return this.socket;
    }

    public Controleur getControleur() {
        return this.controleur;
    }

    public Scene getScene() {
        return this.scene;
    }

    public Stage getStage() {
        return this.stage;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    //Setter
    public void setControleur(Controleur controleur) {
        this.controleur = controleur;
    }

    //Fonction Reseau
    public void traiterMessage(String message) {
        if (!verifieProtocole(message)) {
            this.getThreadConsole().envoyerMessage("@ERREUR ce que vous dites n'a aucun sens. Votre message est ignoré");
            return;
        }

        String typeMessage = message.split(" ")[0];
        switch (typeMessage) {
            case "@CONNEXIONREUSSI" -> traiterConnexionReussi();
            case "@DEMARRAGEPARTIE" -> traiterDemarragePartie();
            case "@TAS" -> traiterTas(message);
            case "@INFO" -> traiterInfo(message);
            case "@LISTE_JOUEURS" -> traiterListeJoueurs(message);
            case "@MAIN" -> traiterMain(message);
            case "@VICTOIRE" -> traiterVictoire(message);
            case "@JOUEURCOURANT" -> traiterJoueurCourant(message);
            case "@STATS" -> traiterStats(message);
            case "@ERREURCONNEXION" -> traiterErreurConexion();
            case "@ERREURLANCEMENTPARTIE" -> traiterErreurLancementPartie(message);
            case "@ERREURSTATS" -> traiterErreurStats();
            case "@ERREUR" -> traiterErreur(message);
            default -> System.err.println("Ce type de message n'existe pas : "+typeMessage);
        }
    }

    private void traiterConnexionReussi() {
        if (this.getControleur() instanceof ControleurPseudo) {
            Platform.runLater(() -> {
                try {
                    FXMLLoader loaderNavbar = new FXMLLoader(getClass().getResource("../vue/Navbar.fxml"));
                    ControleurNavbar controleurNavbar = new ControleurNavbar(this);
                    loaderNavbar.setController(controleurNavbar);

                    FXMLLoader loaderScene1 = new FXMLLoader(getClass().getResource("../vue/Scene1.fxml"));
                    ControleurScene1 controleurScene1 = new ControleurScene1(this, loaderNavbar.load());
                    loaderScene1.setController(controleurScene1);
                    this.getScene().setRoot(loaderScene1.load());
                    this.setControleur(controleurScene1);

                    this.getStage().setTitle("Uno - "+this.getPseudo());
                    this.getStage().setOnCloseRequest(_ -> {
                        this.traiterDeconnexion();
                        Platform.exit();
                    });
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void traiterDemarragePartie(){
        if (this.getControleur() instanceof ControleurScene1){
            Platform.runLater(() -> {
                try {
                    FXMLLoader loaderNavbar = new FXMLLoader(getClass().getResource("../vue/Navbar.fxml"));
                    ControleurNavbar controleurNavbar = new ControleurNavbar(this);
                    loaderNavbar.setController(controleurNavbar);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../vue/Scene2.fxml"));
                    ControleurScene2 controleurScene2 = new ControleurScene2(this, loaderNavbar.load());
                    loader.setController(controleurScene2);
                    this.getScene().setRoot(loader.load());

                    this.setControleur(controleurScene2);
                }catch (IOException e){
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void traiterTas(String message) {
        if (this.getControleur() instanceof ControleurScene2 controleurScene2) {
            message = message.substring(message.indexOf(" ") + 1);
            String[] motsTas = message.split(";");
            Platform.runLater(() -> {
                try {
                    controleurScene2.get_Tas().setImage(new Image(Objects.requireNonNull(getClass().getResourceAsStream("../ressource/images/cartes/" + motsTas[0] + "_" + motsTas[1] + ".png"))));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void traiterInfo(String message) {
        if (this.getControleur() instanceof ControleurScene2 controleurScene2) {
            Platform.runLater(() -> {
                controleurScene2.get_Log().getItems().add(message.substring(message.indexOf(" ") + 1));
                controleurScene2.get_Log().scrollTo(controleurScene2.get_Log().getItems().size() - 1);
            });
        }
    }

    private void traiterListeJoueurs(String message) {
        if (this.getControleur() instanceof ControleurScene2 controleurScene2) {
            message = message.substring(message.indexOf(" ") + 1);
            if (!message.isEmpty()) {
                String[] motsJoueurs = message.split(" ");
                Platform.runLater(() -> {
                    controleurScene2.get_ListeJoueur().getChildren().clear();
                    for (int i = 0; i < motsJoueurs.length; i++) {
                        String[] motJoueur = motsJoueurs[i].split(";");
                        controleurScene2.get_ListeJoueur().add(new Label(motJoueur[0]), 0, i);
                        controleurScene2.get_ListeJoueur().add(new Label(motJoueur[1]), 1, i);
                    }
                });
            }
        }
    }

    private void traiterMain(String message) {
        if (this.getControleur() instanceof ControleurScene2 controleurScene2) {
            String finalMessage = message.substring(message.indexOf(" ") + 1);
            Platform.runLater(() -> {
                controleurScene2.get_Main().getChildren().clear();
                if (!finalMessage.isEmpty()) {
                    String[] motsMain = finalMessage.split(" ");
                    double sizeStackPane = controleurScene2.get_Main().getWidth();
                    double offSetCarte = 90;
                    if (offSetCarte * motsMain.length > sizeStackPane) {
                        offSetCarte = (sizeStackPane - 70) / motsMain.length;
                    }
                    double offSetMain = offSetCarte * (motsMain.length - 1) / 2;
                    int i = 0;
                    for (String s : motsMain) {
                        String[] motMain = s.split(";");
                        Button button = new Button();
                        button.getStyleClass().add("button-invisible");
                        Image image = new LocatedImage(getClass().getResourceAsStream("../ressource/images/cartes/" + motMain[0] + "_" + motMain[1] + ".png"), motMain[0] + " " + motMain[1]);
                        button.setGraphic(new ImageView(image));
                        button.addEventHandler(MouseEvent.MOUSE_CLICKED, controleurScene2::selectionnerCarte);
                        button.setTranslateX(offSetCarte * i - offSetMain);
                        i++;
                        controleurScene2.get_Main().getChildren().add(button);
                    }
                }
            });
        }
    }

    private void traiterJoueurCourant(String message) {
        if (this.getControleur() instanceof ControleurScene2 controleurScene2) {
            Platform.runLater(() -> controleurScene2.get_JoueurCourant().setText(message.substring(message.indexOf(" ") + 1)));
        }
    }

    private void traiterVictoire(String message) {
        if (this.getControleur() instanceof ControleurScene2) {
            Platform.runLater(() -> {
                try {
                    FXMLLoader loaderNavbar = new FXMLLoader(getClass().getResource("../vue/Navbar.fxml"));
                    ControleurNavbar controleurNavbar = new ControleurNavbar(this);
                    loaderNavbar.setController(controleurNavbar);

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../vue/Scene3.fxml"));
                    ControleurScene3 controleurScene3 =  new ControleurScene3(this, loaderNavbar.load(), message.substring(message.indexOf(" ") + 1));
                    loader.setController(controleurScene3);
                    this.getScene().setRoot(loader.load());

                    this.setControleur(controleurScene3);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private void traiterStats(String message) {
        String[] motsStats = message.split(" ");
        Platform.runLater(() -> {
            try {
                FXMLLoader stats = new FXMLLoader(getClass().getResource("../vue/Stats.fxml"));
                ControleurStats controleurStats = new ControleurStats(motsStats[1], motsStats[2], motsStats[3], motsStats[4], motsStats[5], motsStats[6]);
                stats.setController(controleurStats);

                Dialog<ButtonType> dialogStats = new Dialog<>();
                dialogStats.getDialogPane().setContent(stats.load());
                dialogStats.initModality(Modality.APPLICATION_MODAL);

                dialogStats.getDialogPane().getButtonTypes().addAll(new ButtonType("Fermer la fenêtre", ButtonBar.ButtonData.OK_DONE));
                dialogStats.showAndWait();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void traiterErreurConexion() {
        if (this.getControleur() instanceof ControleurPseudo controleurPseudo) {
            Platform.runLater(()-> controleurPseudo.get_Label().setText("Erreur de connexion - votre pseudo est déjà utilisé"));
            this.traiterDeconnexion();
        }
    }

    private void traiterErreurLancementPartie(String message) {
        if (this.getControleur() instanceof ControleurScene1 controleurScene1) {
            Platform.runLater(() -> {
                controleurScene1.get_LabelErreur().getStyleClass().add("addOpacity");
                controleurScene1.get_LabelErreur().setText(message.substring(message.indexOf(" ") + 1));
            });
        }
    }

    private void traiterErreurStats() {
        Platform.runLater(() -> {
            try {
                FXMLLoader statsVide = new FXMLLoader(getClass().getResource("../vue/StatsVide.fxml"));

                Dialog<ButtonType> dialogStatsVide = new Dialog<>();
                dialogStatsVide.getDialogPane().setContent(statsVide.load());
                dialogStatsVide.initModality(Modality.APPLICATION_MODAL);

                dialogStatsVide.getDialogPane().getButtonTypes().addAll(new ButtonType("Fermer la fenêtre", ButtonBar.ButtonData.OK_DONE));
                dialogStatsVide.showAndWait();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void traiterErreur(String message) {
        if (this.getControleur() instanceof ControleurScene2) {
            Platform.runLater(() -> {
                try {
                    FXMLLoader loaderNavbar = new FXMLLoader(getClass().getResource("../vue/Navbar.fxml"));
                    ControleurNavbar controleurNavbar = new ControleurNavbar(this);
                    loaderNavbar.setController(controleurNavbar);

                    FXMLLoader loaderScene1 = new FXMLLoader(getClass().getResource("../vue/Scene1.fxml"));
                    ControleurScene1 controleurScene1 = new ControleurScene1(this, loaderNavbar.load());
                    loaderScene1.setController(controleurScene1);
                    this.getScene().setRoot(loaderScene1.load());
                    this.setControleur(controleurScene1);
                    controleurScene1.get_LabelErreur().getStyleClass().add("addOpacity");
                    controleurScene1.get_LabelErreur().setText(message.substring(message.indexOf(" ") + 1));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void traiterDeconnexion(){
        try {
            this.getThreadConsole().envoyerMessage("@DECONNEXION");
            this.getSocket().close();
            this.getThreadConsole().fin();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    //Fonction Traitement Protocole
    public boolean verifieProtocole(String message) {
        for (ERegexClient phraseDuProtocole : ERegexClient.values()) {
            if (this.verifiePhraseDuProtocole(message, phraseDuProtocole.getRegex())) {
                return true;
            }
        }
        return false;
    }

    public boolean verifiePhraseDuProtocole(String message, String phrase) {
        Pattern pattern = Pattern.compile(phrase, Pattern.CASE_INSENSITIVE);
        return pattern.matcher(message).matches();
    }

}
