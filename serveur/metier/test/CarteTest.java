package serveur.metier.test;


import serveur.metier.carte.Carte;
import serveur.metier.carte.ECouleur;
import serveur.metier.carte.EValeur;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CarteTest {
    @Test
    public void testCarte() {
        Carte carte1 = new Carte(EValeur.DEUX, ECouleur.ROUGE);
        Carte carte2 = new Carte(EValeur.DEUX, ECouleur.VERT);
        Carte carte3 = new Carte(EValeur.CINQ, ECouleur.ROUGE);

        assertEquals(carte1.getCouleur(), carte3.getCouleur());
        assertEquals(carte1.getValeur(), carte2.getValeur());
        assertNotEquals(carte1.getCouleur(), carte2.getCouleur());
        assertNotEquals(carte1.getValeur(), carte3.getValeur());
    }
}