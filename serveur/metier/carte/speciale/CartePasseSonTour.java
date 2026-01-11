package serveur.metier.carte.speciale;

import serveur.metier.carte.ECouleur;
import serveur.metier.carte.EValeur;
import serveur.metier.carte.Carte;
import serveur.metier.jeu.Jeu;

public class CartePasseSonTour extends Carte {
    //Constructeur
    public CartePasseSonTour(ECouleur couleur) {
        super(EValeur.PASSE, couleur);
    }

    //Méthode Métier
    @Override
    public void appliquerEffet() {
        Jeu.getInstance().passerAuJoueurSuivantSuivant();
    }

    @Override
    public String getMessage() {
        return Jeu.getInstance().getJoueurPrecendant().getNom() + " est forcé de passer son tour !";
    }
}
