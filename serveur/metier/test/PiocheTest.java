package serveur.metier.test;

import serveur.metier.carte.Carte;
import serveur.metier.carte.ECouleur;
import serveur.metier.carte.EValeur;
import serveur.metier.exception.PiocheException;
import serveur.metier.exception.UnoException;
import serveur.metier.pioche.Pioche;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PiocheTest {
    @Test
    public void testDepilerQuandLaPiocheEstVide() {
        Pioche pioche = new Pioche();
        assertThrows(UnoException.class, pioche::depiler);
    }

    @Test
    public void testDepilerUneCarte() throws PiocheException {
        Carte carte1 = new Carte(EValeur.SIX, ECouleur.JAUNE);
        Carte carte2 = new Carte(EValeur.QUATRE, ECouleur.ROUGE);
        Carte carte3 = new Carte(EValeur.DEUX, ECouleur.VERT);
        Carte carte4 = new Carte(EValeur.CINQ, ECouleur.BLEU);
        Carte carte5 = new Carte(EValeur.ZERO, ECouleur.VERT);

        ArrayList<Carte> cartes = new ArrayList<>();
        cartes.add(carte1);
        cartes.add(carte2);
        cartes.add(carte3);
        cartes.add(carte4);
        cartes.add(carte5);

        Pioche pioche = new Pioche(cartes);
        assertEquals(pioche.getCarteFromIndex(0), carte1);
        assertEquals(pioche.getCarteFromIndex(1), carte2);
        assertEquals(pioche.getCarteFromIndex(2), carte3);
        assertEquals(pioche.getCarteFromIndex(3), carte4);
        assertEquals(pioche.getCarteFromIndex(4), carte5);
        Carte carte = pioche.depiler();
        assertEquals(carte5, carte);
        assertEquals(4, pioche.getCartes().size());
        assertEquals(pioche.getCarteFromIndex(0), carte1);
        assertEquals(pioche.getCarteFromIndex(1), carte2);
        assertEquals(pioche.getCarteFromIndex(2), carte3);
        assertEquals(pioche.getCarteFromIndex(3), carte4);
    }

    @Test
    public void testDepilerLaDerniereCarte() throws PiocheException {
        Carte carte1 = new Carte(EValeur.SIX, ECouleur.JAUNE);
        Carte carte2 = new Carte(EValeur.QUATRE, ECouleur.ROUGE);
        Carte carte3 = new Carte(EValeur.DEUX, ECouleur.VERT);
        Carte carte4 = new Carte(EValeur.CINQ, ECouleur.BLEU);
        Carte carte5 = new Carte(EValeur.ZERO, ECouleur.VERT);

        ArrayList<Carte> cartes = new ArrayList<>();
        cartes.add(carte1);
        cartes.add(carte2);
        cartes.add(carte3);
        cartes.add(carte4);
        cartes.add(carte5);

        Pioche pioche = new Pioche(cartes);
        pioche.depiler();
        pioche.depiler();
        pioche.depiler();
        pioche.depiler();
        pioche.depiler();
        assertEquals(0, pioche.getCartes().size());
    }
}