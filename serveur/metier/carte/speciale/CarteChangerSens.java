package serveur.metier.carte.speciale;

import serveur.metier.carte.ECouleur;
import serveur.metier.carte.EValeur;
import serveur.metier.carte.Carte;
import serveur.metier.jeu.Jeu;

public class CarteChangerSens extends Carte {
    //Constructeur
    public CarteChangerSens(ECouleur couleur) {
        super(EValeur.CHANGEMENT_SENS, couleur);
    }

    //Méthode Métier
    @Override
    public void appliquerEffet() {
        Jeu.getInstance().changerDirectionJeu();
        Jeu.getInstance().passerAuJoueurSuivant();
    }

    @Override
    public String getMessage() {
        return "Changement de sens !";
    }
}
