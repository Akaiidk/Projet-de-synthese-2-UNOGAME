package serveur.jdbc;

public enum ESQLQuery {
    //Enum
    InsererPartie("INSERT INTO partie VALUES ()"),
    InsererJoueur("INSERT INTO joueur (pseudo_joueur) VALUES (?)"),
    InsererParticipe("INSERT INTO participe (num_partie, num_joueur, score) VALUES (?,?,?)"),
    ObtenirCleJoueur("SELECT num_joueur FROM joueur WHERE pseudo_joueur LIKE ?"),
    ObtenirTotalPartie("SELECT COUNT(num_partie) FROM partie"),
    ObtenirTotalJoueur("SELECT COUNT(num_joueur) FROM joueur"),
    ObtenirPremierePartieDuJoueur("SELECT MIN(date_partie) FROM joueur JOIN participe USING (num_joueur) JOIN partie USING (num_partie) WHERE pseudo_joueur LIKE ?"),
    ObtenirTotalPartieDuJoueur("SELECT count(num_partie) FROM joueur JOIN participe USING (num_joueur) WHERE pseudo_joueur LIKE ?"),
    ObtenirTotalPartieGagneeDuJoueur("SELECT count(num_partie) FROM joueur JOIN participe USING (num_joueur) WHERE pseudo_joueur LIKE ? AND score = 0"),
    ObtenirScoreMoyenDuJoueur("SELECT avg(score) FROM joueur JOIN participe USING (num_joueur) WHERE pseudo_joueur LIKE ?");

    //Champ
    private final String query;

    ESQLQuery(String query) {
        this.query = query;
    }

    public String getQuery() {
        return this.query;
    }
}
