package serveur.metier.carte;

public enum EValeur {
    //Enum
    ZERO(1),UN(2),DEUX(3),TROIS(4),QUATRE(5),CINQ(6),SIX(7),SEPT(8),HUIT(9),NEUF(10),PLUS_2(20),PASSE(20),CHANGEMENT_SENS(20);

    //Champs
    private final int valeur;

    //Constructeur
    EValeur(int valeur) {
        this.valeur = valeur;
    }

    //Getter
    public int getValeur() {
        return valeur;
    }
}