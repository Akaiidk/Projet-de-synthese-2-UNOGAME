package serveur.metier.test;

import serveur.metier.carte.Carte;
import serveur.metier.carte.ECouleur;
import serveur.metier.carte.EValeur;
import serveur.metier.carte.speciale.CartePasseSonTour;
import serveur.metier.exception.JeuException;
import serveur.metier.exception.JoueurException;
import serveur.metier.exception.PiocheException;
import serveur.metier.jeu.Jeu;
import serveur.metier.joueur.Joueur;
import serveur.metier.pioche.Pioche;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class CartePasseSonTourTest {
    private static final Joueur alice = new Joueur("Alice");
    private static final Joueur bob = new Joueur("Bob");
    private static final Joueur charles = new Joueur("Charles");

    private static final Pioche pioche = new Pioche();

    private static final ArrayList<Carte> mainAlice = new ArrayList<>();
    private static final ArrayList<Carte> mainBob = new ArrayList<>();
    private static final ArrayList<Carte> mainCharles = new ArrayList<>();
    
    private static final ArrayList<Joueur> joueurs = new ArrayList<>();
    private static final ArrayList<Carte> cartesPioche = new ArrayList<>();

    private static final Carte cartePasseRouge = new CartePasseSonTour(ECouleur.ROUGE);
    private static final Carte carte9Bleu = new Carte(EValeur.NEUF, ECouleur.BLEU);
    private static final Carte carte4Jaune = new Carte(EValeur.QUATRE, ECouleur.JAUNE);

    private static final Carte carte6Jaune = new Carte(EValeur.SIX, ECouleur.JAUNE);
    private static final Carte carte6Vert = new Carte(EValeur.SIX, ECouleur.VERT);
    private static final Carte carte7Bleu = new Carte(EValeur.SEPT, ECouleur.BLEU);

    private static final Carte carte1Bleu = new Carte(EValeur.UN, ECouleur.BLEU);
    private static final Carte cartePasseVert = new CartePasseSonTour(ECouleur.VERT);
    private static final Carte carte1Rouge = new Carte(EValeur.UN, ECouleur.ROUGE);

    private static final Carte carte0Bleu = new Carte(EValeur.ZERO, ECouleur.BLEU);
    private static final Carte carte8Vert = new Carte(EValeur.HUIT, ECouleur.VERT);
    private static final Carte carte2Vert = new Carte(EValeur.DEUX, ECouleur.VERT);
    private static final Carte carte4Rouge = new Carte(EValeur.QUATRE, ECouleur.ROUGE);
    private static final Carte carte2Vert2 = new Carte(EValeur.DEUX, ECouleur.VERT);
    private static final Carte carte9Rouge = new Carte(EValeur.NEUF, ECouleur.ROUGE);
    @BeforeAll
    static void setUp(){
        //init main alice
        mainAlice.add(cartePasseRouge);
        mainAlice.add(carte9Bleu);
        mainAlice.add(carte4Jaune);

        //init main bob
        mainBob.add(carte6Jaune);
        mainBob.add(carte6Vert);
        mainBob.add(carte7Bleu);

        //init main charles
        mainCharles.add(carte1Bleu);
        mainCharles.add(cartePasseVert);
        mainCharles.add(carte1Rouge);

        //init Pioche
        cartesPioche.add(carte2Vert2);
        cartesPioche.add(carte4Rouge);
        cartesPioche.add(carte2Vert);
        cartesPioche.add(carte8Vert);
        cartesPioche.add(carte0Bleu);
        cartesPioche.add(carte9Rouge);
    }

    @BeforeEach
    public void initialiser() {
        pioche.setCartes(cartesPioche);
        alice.setMain(mainAlice);
        bob.setMain(mainBob);
        charles.setMain(mainCharles);

        joueurs.clear();
        joueurs.add(alice);
        joueurs.add(bob);
        joueurs.add(charles);

        Jeu.getInstance().reset();
        Jeu.getInstance().setJoueurs(joueurs);
        Jeu.getInstance().setPioche(pioche);
    }

    @Test
    public void coupLegalCartePasseTonTour() throws PiocheException, JeuException, JoueurException {
        Jeu.getInstance().commencerPartieSansDistributionTest();
        assertEquals(alice, Jeu.getInstance().getJoueurCourant());
        alice.jouerCarte(cartePasseRouge);
        Jeu.getInstance().getDefausse().getLast().appliquerEffet();
        assertEquals(charles, Jeu.getInstance().getJoueurCourant());
        assertEquals(cartePasseRouge, Jeu.getInstance().getDefausse().getLast());

        charles.jouerCarte(cartePasseVert);
        Jeu.getInstance().getDefausse().getLast().appliquerEffet();
        assertEquals(bob, Jeu.getInstance().getJoueurCourant());
        assertEquals(cartePasseVert, Jeu.getInstance().getDefausse().getLast());

        bob.jouerCarte(carte6Vert);
        Jeu.getInstance().getDefausse().getLast().appliquerEffet();
        assertEquals(charles, Jeu.getInstance().getJoueurCourant());
        assertEquals(carte6Vert, Jeu.getInstance().getDefausse().getLast());
    }

    @Test
    public void coupeIllegalSurPasseTonTour() throws PiocheException, JeuException, JoueurException {
        Jeu.getInstance().commencerPartieSansDistributionTest();
        assertEquals(alice, Jeu.getInstance().getJoueurCourant());
        alice.jouerCarte(cartePasseRouge);
        Jeu.getInstance().getDefausse().getLast().appliquerEffet();
        assertEquals(charles, Jeu.getInstance().getJoueurCourant());
        assertEquals(3, charles.getMain().size());
        try {
            charles.jouerCarte(carte1Bleu);
        }
        catch (JoueurException e) {
            assertEquals(3, e.getNbCartes());
        }
    }

    @Test
    public void coupeIllegalACausePasseTonTour() throws PiocheException, JeuException, JoueurException {
        Jeu.getInstance().commencerPartieSansDistributionTest();
        assertEquals(alice, Jeu.getInstance().getJoueurCourant());
        alice.jouerCarte(carte9Bleu);
        Jeu.getInstance().getDefausse().getLast().appliquerEffet();
        assertEquals(bob, Jeu.getInstance().getJoueurCourant());
        bob.jouerCarte(carte7Bleu);
        Jeu.getInstance().getDefausse().getLast().appliquerEffet();
        assertEquals(charles, Jeu.getInstance().getJoueurCourant());
        assertEquals(3, charles.getMain().size());
        try {
            charles.jouerCarte(cartePasseVert);
        }
        catch (JoueurException e) {
            assertEquals(3, e.getNbCartes());
        }
    }
}