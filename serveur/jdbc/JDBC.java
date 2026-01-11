package serveur.jdbc;

import java.sql.*;

public class JDBC {
    //Champs
    private static Connection connection = null;

    //Méthode BDD
    public static void connecter() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/bdduno", "user_lambda", "lambda");
        connection.setAutoCommit(false);
    }

    public static void deconnecter() throws SQLException {
        connection.close();
    }

    public static int insererPartie() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(ESQLQuery.InsererPartie.getQuery(), PreparedStatement.RETURN_GENERATED_KEYS);
        int affectedRows = statement.executeUpdate();
        if (affectedRows != 1) {
            throw new SQLException();
        }
        ResultSet resultat = statement.getGeneratedKeys();
        resultat.next();
        int key = resultat.getInt(1);
        statement.close();
        resultat.close();
        return key;
    }

    public static int insererJoueur(String pseudo) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(ESQLQuery.InsererJoueur.getQuery(), PreparedStatement.RETURN_GENERATED_KEYS);
        statement.setString(1, pseudo);
        int affectedRows = statement.executeUpdate();
        if (affectedRows != 1) {
            throw new SQLException();
        }
        ResultSet resultat = statement.getGeneratedKeys();
        resultat.next();
        int key = resultat.getInt(1);
        statement.close();
        resultat.close();
        return key;

    }

    public static void insererParticipe(int clePartie, int cleJoueur, int score) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(ESQLQuery.InsererParticipe.getQuery());
        statement.setInt(1, clePartie);
        statement.setInt(2, cleJoueur);
        statement.setInt(3, score);
        int affectedRows = statement.executeUpdate();
        if (affectedRows != 1) {
            throw new SQLException();
        }
        statement.close();
    }

    public static int obtenirCleJoueur(String joueur) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(ESQLQuery.ObtenirCleJoueur.getQuery());
        statement.setString(1, joueur);
        ResultSet resultat = statement.executeQuery();
        resultat.next();
        int key = resultat.getInt(1);
        statement.close();
        resultat.close();
        return key;
    }

    public static int obtenirTotalPartie() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(ESQLQuery.ObtenirTotalPartie.getQuery());
        ResultSet resultat = statement.executeQuery();
        resultat.next();
        int nbPartie = resultat.getInt(1);
        statement.close();
        resultat.close();
        return nbPartie;
    }

    public static int obtenirTotalJoueur() throws SQLException {
        PreparedStatement statement = connection.prepareStatement(ESQLQuery.ObtenirTotalJoueur.getQuery());
        ResultSet resultat = statement.executeQuery();
        resultat.next();
        int nbJoueur = resultat.getInt(1);
        statement.close();
        resultat.close();
        return nbJoueur;
    }

    public static String obtenirPremierePartieDuJoueur(String pseudo) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(ESQLQuery.ObtenirPremierePartieDuJoueur.getQuery());
        statement.setString(1,pseudo);
        ResultSet resultat = statement.executeQuery();
        resultat.next();
        String date = resultat.getString(1);
        statement.close();
        resultat.close();
        return date;
    }

    public static int obtenirTotalPartieDuJoueur(String pseudo) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(ESQLQuery.ObtenirTotalPartieDuJoueur.getQuery());
        statement.setString(1,pseudo);
        ResultSet resultat = statement.executeQuery();
        resultat.next();
        int nbPartie = resultat.getInt(1);
        statement.close();
        resultat.close();
        return nbPartie;
    }


    public static int obtenirTotalPartieGagneeDuJoueur(String pseudo) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(ESQLQuery.ObtenirTotalPartieGagneeDuJoueur.getQuery());
        statement.setString(1,pseudo);
        ResultSet resultat = statement.executeQuery();
        resultat.next();
        int nbPartie = resultat.getInt(1);
        statement.close();
        resultat.close();
        return nbPartie;
    }

    public static int obtenirScoreMoyenDuJoueur(String pseudo) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(ESQLQuery.ObtenirScoreMoyenDuJoueur.getQuery());
        statement.setString(1,pseudo);
        ResultSet resultat = statement.executeQuery();
        resultat.next();
        int score = resultat.getInt(1);
        statement.close();
        resultat.close();
        return score;
    }
}
