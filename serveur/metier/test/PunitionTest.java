package serveur.metier.test;

import serveur.metier.carte.Carte;
import serveur.metier.carte.ECouleur;
import serveur.metier.carte.EValeur;
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

class PunitionTest {
    private static final Joueur alice = new Joueur("Alice");
    private static final Joueur bob = new Joueur("Bob");
    private static final Joueur charles = new Joueur("Charles");

    private static final Pioche pioche = new Pioche();

    private static final ArrayList<Carte> mainAlice = new ArrayList<>();
    private static final ArrayList<Carte> mainBob = new ArrayList<>();
    private static final ArrayList<Carte> mainCharles = new ArrayList<>();

    private static final ArrayList<Joueur> joueurs = new ArrayList<>();
    private static final ArrayList<Carte> cartesPioche = new ArrayList<>();

    private static final Carte carte2Vert = new Carte(EValeur.DEUX, ECouleur.VERT);
    private static final Carte carte6Jaune = new Carte(EValeur.SIX, ECouleur.JAUNE);
    private static final Carte carte1Rouge = new Carte(EValeur.UN, ECouleur.ROUGE);

    private static final Carte carte2Bleu = new Carte(EValeur.DEUX, ECouleur.BLEU);
    private static final Carte carte4Jaune = new Carte(EValeur.QUATRE, ECouleur.JAUNE);
    private static final Carte carte9Rouge = new Carte(EValeur.NEUF, ECouleur.ROUGE);

    private static final Carte carte9Bleu = new Carte(EValeur.NEUF, ECouleur.BLEU);
    private static final Carte carte7Bleu = new Carte(EValeur.SEPT, ECouleur.BLEU);
    private static final Carte carte0Bleu = new Carte(EValeur.ZERO, ECouleur.BLEU);

    private static final Carte carte6Jaune2 = new Carte(EValeur.SIX, ECouleur.JAUNE);
    private static final Carte carte4Rouge = new Carte(EValeur.QUATRE, ECouleur.ROUGE);
    private static final Carte carte2Vert2 = new Carte(EValeur.DEUX, ECouleur.VERT);
    private static final Carte carte5Bleu = new Carte(EValeur.CINQ, ECouleur.BLEU);
    private static final Carte carte0Vert = new Carte(EValeur.ZERO, ECouleur.VERT);
    private static final Carte carte8Vert = new Carte(EValeur.HUIT, ECouleur.VERT);

    @BeforeAll
    static void setUp(){
        //init main alice
        mainAlice.add(carte2Vert);
        mainAlice.add(carte6Jaune);
        mainAlice.add(carte1Rouge);

        //init main bob
        mainBob.add(carte2Bleu);
        mainBob.add(carte4Jaune);
        mainBob.add(carte9Rouge);

        //init main charles
        mainCharles.add(carte9Bleu);
        mainCharles.add(carte7Bleu);
        mainCharles.add(carte0Bleu);

        //init Pioche
        cartesPioche.add(carte0Vert);
        cartesPioche.add(carte5Bleu);
        cartesPioche.add(carte2Vert2);
        cartesPioche.add(carte4Rouge);
        cartesPioche.add(carte6Jaune2);
        cartesPioche.add(carte8Vert);
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
    public void jouerCoupIllegale() throws PiocheException, JeuException {
        Jeu.getInstance().commencerPartieSansDistributionTest();
        assertThrows(JoueurException.class, () ->  alice.jouerCarte(carte6Jaune) );
        alice.piocherPunition(2, Jeu.getInstance().getPioche());
        Jeu.getInstance().passerAuJoueurSuivant();
        assertEquals(bob,Jeu.getInstance().getJoueurCourant());
        assertEquals(5,alice.getMain().size());
        assertTrue(alice.getMain().contains(carte6Jaune2));
        assertTrue(alice.getMain().contains(carte4Rouge));
        assertEquals(carte2Vert2,Jeu.getInstance().getPioche().getCartes().getLast());
    }

    @Test
    public void punisQuandPasSontTour() throws PiocheException, JeuException {
        Jeu.getInstance().commencerPartieSansDistributionTest();
        assertThrows(JoueurException.class, () -> bob.piocherCarte(Jeu.getInstance().getPioche()));
        bob.piocherPunition(2, Jeu.getInstance().getPioche());
        assertEquals(alice, Jeu.getInstance().getJoueurCourant());
        assertEquals(5,bob.getMain().size());
        assertTrue(bob.getMain().contains(carte4Rouge));
        assertTrue(bob.getMain().contains(carte6Jaune2));
        assertEquals(carte2Vert2,Jeu.getInstance().getPioche().getCartes().getLast());

    }
}