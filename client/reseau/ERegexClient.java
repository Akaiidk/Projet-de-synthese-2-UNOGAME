package client.reseau;

public enum ERegexClient {
    //Enum
    LISTE_JOUEURS("^@LISTE_JOUEURS (\\w+;\\w+ ?)+$"), MAIN("^@MAIN (\\w+;\\w+ ?)*$"), INFO("^@INFO .*$"), JOUEURCOURANT("@JOUEURCOURANT ([0-9A-Za-z_]){1,12}$"), ERREUR("^@ERREUR .*$"), ERREURLANCEMENTPARTIE("@ERREURLANCEMENTPARTIE .*$"),TAS("^@TAS \\w+;\\w+$"), VICTOIRE("^@VICTOIRE ([0-9A-Za-z_]){1,12}$"), ERREURCONNEXION("^@ERREURCONNEXION$"), CONNEXIONREUSSI("^@CONNEXIONREUSSI$"), DEMARRAGEPARTIE("^@DEMARRAGEPARTIE$"), ERREURSTATS("^@ERREURSTATS$"), STATS("^@STATS \\w+ \\w+ ([0-9-]*) \\w+ \\w+ \\w+$");

    //Champ
    private final String regex;

    //Constructeur
    ERegexClient(String regex) {
        this.regex = regex;
    }

    //Getter
    public String getRegex() {
        return regex;
    }
}
