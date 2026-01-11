package serveur.metier.carte.speciale;

import serveur.metier.carte.ECouleur;
import serveur.metier.carte.EValeur;
import serveur.metier.carte.Carte;
import serveur.metier.exception.PiocheException;
import serveur.metier.jeu.Jeu;

public class CartePlusDeux extends Carte {
    //Constructeur
    public CartePlusDeux(ECouleur couleur) {
        super(EValeur.PLUS_2, couleur);
    }

    //Méthode Métier
    @Override
    public void appliquerEffet() throws PiocheException {
        Jeu.getInstance().getJoueurSuivant().piocherPunition(2, Jeu.getInstance().getPioche());
        Jeu.getInstance().passerAuJoueurSuivantSuivant();
    }

    @Override
    public String getMessage() {
        return Jeu.getInstance().getJoueurPrecendant().getNom() + " doit piocher 2 cartes et passer son tour !";
    }
}
