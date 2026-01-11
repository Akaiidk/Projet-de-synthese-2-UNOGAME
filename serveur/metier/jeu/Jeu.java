package serveur.metier.jeu;

import serveur.metier.carte.Carte;
import serveur.metier.carte.ECouleur;
import serveur.metier.carte.EValeur;
import serveur.metier.carte.speciale.CarteChangerSens;
import serveur.metier.carte.speciale.CartePasseSonTour;
import serveur.metier.carte.speciale.CartePlusDeux;
import serveur.metier.exception.JeuException;
import serveur.metier.exception.PiocheException;
import serveur.metier.joueur.Joueur;
import serveur.metier.pioche.Pioche;

import java.util.ArrayList;
import java.util.Collections;

public final class Jeu {
    //Champs
    private static Jeu instance;

    public final static int HORAIRE = 1;
    public final static int ANTIHORAIRE = -1;

    private final ArrayList<Joueur> joueurs = new ArrayList<>();
    private Pioche pioche = new Pioche();
    private final ArrayList<Carte> defausse = new ArrayList<>();
    private Joueur joueurCourant;
    private ECouleur couleurCourante;
    private EValeur valeurCourante;
    private int sens = HORAIRE; // 1 pour sens horaire -1 pour anti horaire

    //Constructeur
    private Jeu() {}

    //Instance
    public static Jeu getInstance() {
        if (instance == null) {
            instance = new Jeu();
        }

        return instance;
    }

    //Reset
    public void reset() {
        this.getJoueurs().clear();
        this.pioche = null;
        this.joueurCourant = null;
        this.couleurCourante = null;
        this.valeurCourante = null;
        this.getDefausse().clear();
        this.sens = HORAIRE;
    }

    //Getter
    public ArrayList<Joueur> getJoueurs() {
        return this.joueurs;
    }

    public int getSizeJoueurs() {
        return this.getJoueurs().size();
    }

    public Pioche getPioche() {
        return this.pioche;
    }

    public int getSens() {
        return this.sens;
    }

    public Joueur getJoueurCourant() {
        return this.joueurCourant;
    }

    public Joueur getJoueurPrecendant() {
        int indexSuivant = this.getJoueurs().indexOf(getJoueurCourant()) - this.getSens();
        if (indexSuivant == this.getSizeJoueurs()){
            indexSuivant = 0;
        }
        if (indexSuivant < 0 ){
            indexSuivant = this.getSizeJoueurs() - 1;
        }
        return this.getJoueurs().get(indexSuivant);
    }

    public Joueur getJoueurSuivant() {
        int indexSuivant = this.getJoueurs().indexOf(getJoueurCourant()) + this.getSens();
        if (indexSuivant == this.getSizeJoueurs()){
            indexSuivant = 0;
        }
        if (indexSuivant < 0 ){
            indexSuivant = this.getSizeJoueurs() - 1;
        }
        return this.getJoueurs().get(indexSuivant);
    }

    public EValeur getValeurCourante() {
        return this.valeurCourante;
    }

    public ECouleur getCouleurCourante() {
        return this.couleurCourante;
    }

    public ArrayList<Carte> getDefausse() {
        return this.defausse;
    }

    //
    public Joueur getSpecificJoueur(String nom) throws JeuException {
        for (Joueur joueur : this.getJoueurs()) {
            if (joueur.getNom().equals(nom)) {
                return joueur;
            }
        }
        throw new JeuException(this);
    }

    //Setter
    public void setJoueurs(ArrayList<Joueur> joueurss) {
        joueurs.clear();
        joueurs.addAll(joueurss);
    }

    public void setPioche(Pioche pioche) {
        if (pioche == null) {
            throw new NullPointerException("pioche est vide");
        }
        this.pioche = pioche;
    }

    public void setJoueurCourant(Joueur joueurCourant) {
        if (joueurCourant == null) {
            throw new NullPointerException("joueur courant est null");
        }
        this.joueurCourant = joueurCourant;
    }

    public void setValeurCourante(EValeur valeurCourante) {
        if (valeurCourante == null) {
            throw new NullPointerException("valeur courante est null");
        }
        this.valeurCourante = valeurCourante;
    }

    public void setCouleurCourante(ECouleur couleurCourante) {
        if (couleurCourante == null) {
            throw new NullPointerException("couleur courante est null");
        }
        this.couleurCourante = couleurCourante;
    }

    public void setSens(int sens) {
        if (sens != HORAIRE && sens != ANTIHORAIRE) {
            throw new IllegalArgumentException("sens est incorrect");
        }
        this.sens = sens;
    }

    //Initialisation
    public void commencerPartie() throws JeuException, PiocheException {
        if (getJoueurs().size() < 2) {
            throw new JeuException(this, "Vous ne pouvez pas commencer la partie car vous êtes seul.");
        }
        if (getJoueurs().size() > 8) {
            throw new JeuException(this, "Vous ne pouvez pas lancer la partie car vous êtes trop nombreux (maximum : 8 joueurs).");
        }
        Collections.shuffle(getJoueurs());
        this.initialisationPioche();
        this.distribuerCarte();
        Carte carte = getPioche().depiler();
        this.setCouleurCourante(carte.getCouleur());
        this.setValeurCourante(carte.getValeur());
        this.getDefausse().add(carte);
        this.setJoueurCourant(this.getJoueurs().getFirst());
        this.getJoueurCourant().setASonTour(true);
    }

    public void initialisationPioche() {
        this.setPioche(new Pioche());
        for (int i = 1; i <= 2; i++) {
            for (ECouleur couleur : ECouleur.values()) {
                if (!couleur.toString().equals("NOIRE")) {
                    this.getPioche().getCartes().add(new Carte(EValeur.ZERO, couleur));
                    this.getPioche().getCartes().add(new Carte(EValeur.UN, couleur));
                    this.getPioche().getCartes().add(new Carte(EValeur.DEUX, couleur));
                    this.getPioche().getCartes().add(new Carte(EValeur.TROIS, couleur));
                    this.getPioche().getCartes().add(new Carte(EValeur.QUATRE, couleur));
                    this.getPioche().getCartes().add(new Carte(EValeur.CINQ, couleur));
                    this.getPioche().getCartes().add(new Carte(EValeur.SIX, couleur));
                    this.getPioche().getCartes().add(new Carte(EValeur.SEPT, couleur));
                    this.getPioche().getCartes().add(new Carte(EValeur.HUIT, couleur));
                    this.getPioche().getCartes().add(new Carte(EValeur.NEUF, couleur));
                    this.getPioche().getCartes().add(new CartePasseSonTour(couleur));
                    this.getPioche().getCartes().add(new CartePlusDeux(couleur));
                    this.getPioche().getCartes().add(new CarteChangerSens(couleur));
                }
            }
        }
        Collections.shuffle(this.getPioche().getCartes());
    }

    public void distribuerCarte() throws PiocheException {
        for (int i = 0; i < getSizeJoueurs(); i++) {
            for (int j = 0; j < 7; j++) {
                this.getJoueurs().get(i).getMain().add(this.getPioche().depiler());
            }
        }
    }

    //Méthode Métier
    public void ajouterCarteDefausse(Carte carte) {
        if (carte == null) {
            throw new NullPointerException("carte est vide");
        }
        this.getDefausse().add(carte);
    }

    public void passerAuJoueurSuivant() {
        this.getJoueurCourant().setPeutJouerUneCarte(true);
        this.getJoueurCourant().setPeutPiocher(true);
        this.getJoueurCourant().setPeutDireUno(true);
        this.getJoueurCourant().setASonTour(false);
        this.setJoueurCourant(this.getJoueurSuivant());
        this.getJoueurCourant().setASonTour(true);
    }

    public void passerAuJoueurSuivantSuivant() {
        this.getJoueurCourant().setPeutPiocher(true);
        this.getJoueurCourant().setPeutJouerUneCarte(true);
        this.getJoueurCourant().setPeutDireUno(true);
        this.getJoueurCourant().setASonTour(false);
        this.setJoueurCourant(this.getJoueurSuivant());
        this.setJoueurCourant(this.getJoueurSuivant());
        this.getJoueurCourant().setASonTour(true);
    }

    public void changerDirectionJeu() {
        if (this.getSens() == HORAIRE) {
            this.setSens(ANTIHORAIRE);
        }
        else {
            this.setSens(HORAIRE);
        }
    }

    public void reconstituerPioche() {
        this.getPioche().setCartes(this.getDefausse());
        this.getDefausse().clear();
        this.getDefausse().add(this.getPioche().getCartes().getLast());
        this.getPioche().getCartes().removeLast();
    }

    //toString
    @Override
    public String toString() {
        return "Jeu{" +
                "joueurs=" + this.getJoueurs() +
                ", pioche=" + this.getPioche() +
                ", defausse=" + this.getDefausse() +
                ", joueurCourant=" + this.getJoueurCourant() +
                ", couleurCourante=" + this.getCouleurCourante() +
                ", valeurCourante=" + this.getValeurCourante() +
                ", direction=" + this.getSens() +
                '}';
    }

    //Existe seulement pour les tests
    public void commencerPartieSansDistributionTest() throws JeuException, PiocheException {
        if (this.getJoueurs().size() < 2 || this.getJoueurs().size() > 8) {
            throw new JeuException(this);
        }
        // Initialisation du jeu
        Carte carte = this.getPioche().depiler();
        this.setCouleurCourante(carte.getCouleur());
        this.setValeurCourante(carte.getValeur());
        this.getDefausse().add(carte);
        this.setJoueurCourant(this.getJoueurs().getFirst());
        this.getJoueurCourant().setASonTour(true);
    }
}
