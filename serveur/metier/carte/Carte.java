package serveur.metier.carte;

import serveur.metier.exception.PiocheException;
import serveur.metier.jeu.Jeu;

public class Carte {
    //Champs
    private ECouleur couleur;
    private EValeur valeur;

    //Constructeur
    public Carte(EValeur valeur, ECouleur couleur) {
        this.setCouleur(couleur);
        this.setValeur(valeur);
    }

    //Getter
    public ECouleur getCouleur() {
        return couleur;
    }

    public EValeur getValeur() {
        return this.valeur;
    }

    //Setter
    private void setCouleur(ECouleur couleur) {
        if (couleur == null) {
            throw new NullPointerException("couleur est vide");
        }
        this.couleur = couleur;
    }

    private void setValeur(EValeur valeur) {
        if (valeur == null) {
            throw new NullPointerException("valeur est vide");
        }
        this.valeur = valeur;
    }


    //Méthode Metier
    public boolean estJouable() {
        return this.getCouleur().equals(Jeu.getInstance().getCouleurCourante()) || this.getValeur().equals(Jeu.getInstance().getValeurCourante());
    }

    public void appliquerEffet() throws PiocheException {
        Jeu.getInstance().passerAuJoueurSuivant();
    }

    public String getMessage() {
        return "";
    }

    //Equals
    public boolean equals(Carte carte) {
        return (carte != null) && (this.getCouleur() == carte.getCouleur()) && (this.getValeur() == carte.getValeur());
    }

    //toString
    @Override
    public String toString() {
        return this.getCouleur().toString()+";"+this.getValeur().toString();
    }
}
