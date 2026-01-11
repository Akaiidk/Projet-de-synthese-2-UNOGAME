package client.controleurs;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class ControleurStats {
    //Champs FXML
    @FXML
    private GridPane _StatsJeu;
    @FXML
    private GridPane _StatsJoueur;

    //Champs
    private final String nbTotalPartie;
    private final String nbTotalJoueur;
    private final String premierPartieDuJoueur;
    private final String nbTotalPartieDuJoueur;
    private final String nbTotalPartieGagneeDuJoueur;
    private final String scoreMoyenDuJoueur;

    //Getter
    public GridPane get_StatsJeu() {
        return this._StatsJeu;
    }

    public GridPane get_StatsJoueur() {
        return this._StatsJoueur;
    }

    public String getNbTotalPartie() {
        return this.nbTotalPartie;
    }

    public String getNbTotalJoueur() {
        return this.nbTotalJoueur;
    }

    public String getPremierPartieDuJoueur() {
        return this.premierPartieDuJoueur;
    }

    public String getNbTotalPartieDuJoueur() {
        return this.nbTotalPartieDuJoueur;
    }

    public String getNbTotalPartieGagneeDuJoueur() {
        return this.nbTotalPartieGagneeDuJoueur;
    }

    public String getScoreMoyenDuJoueur() {
        return this.scoreMoyenDuJoueur;
    }

    //Constructeur
    public ControleurStats(String nbTotalPartie, String nbTotalJoueur, String premierPartieDuJoueur, String nbTotalPartieDuJoueur, String nbTotalPartieGagneeDuJoueur, String scoreMoyenDuJoueur) {
        this.nbTotalPartie = nbTotalPartie;
        this.nbTotalJoueur = nbTotalJoueur;
        this.premierPartieDuJoueur = premierPartieDuJoueur;
        this.nbTotalPartieDuJoueur = nbTotalPartieDuJoueur;
        this.nbTotalPartieGagneeDuJoueur = nbTotalPartieGagneeDuJoueur;
        this.scoreMoyenDuJoueur = scoreMoyenDuJoueur;
    }

    @FXML public void initialize() {
        String nbTotalPartiePerduDuJoueur = String.valueOf(Integer.parseInt(this.getNbTotalPartieDuJoueur()) - Integer.parseInt(this.getNbTotalPartieGagneeDuJoueur()));
        String winrateDuJoueur = Integer.parseInt(this.getNbTotalPartieGagneeDuJoueur()) * 100 / Integer.parseInt(this.getNbTotalPartieDuJoueur()) + "%";
        this.get_StatsJeu().add(new Label(this.getNbTotalPartie()),1,0);
        this.get_StatsJeu().add(new Label(this.getNbTotalJoueur()),1,1);

        this.get_StatsJoueur().add(new Label(this.getPremierPartieDuJoueur()),1,0);
        this.get_StatsJoueur().add(new Label(this.getNbTotalPartieDuJoueur()),1,1);
        this.get_StatsJoueur().add(new Label(this.getNbTotalPartieGagneeDuJoueur()),1,2);
        this.get_StatsJoueur().add(new Label(nbTotalPartiePerduDuJoueur),1,3);
        this.get_StatsJoueur().add(new Label(winrateDuJoueur),1,4);
        this.get_StatsJoueur().add(new Label(this.getScoreMoyenDuJoueur()),1,5);
    }
}
