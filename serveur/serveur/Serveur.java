package serveur.serveur;

import serveur.jdbc.JDBC;
import serveur.metier.carte.Carte;
import serveur.metier.exception.JeuException;
import serveur.metier.exception.JoueurException;
import serveur.metier.exception.PiocheException;
import serveur.metier.jeu.Jeu;
import serveur.metier.joueur.Joueur;
import serveur.reseau.ConnexionUtilisateur;
import serveur.reseau.ThreadAcceptConnexion;

import java.sql.SQLException;
import java.util.ArrayList;

public class Serveur {
    //Champs
    private final int port;
    private final ArrayList<ConnexionUtilisateur> utilisateurs = new ArrayList<>();

    //Constructeur
    public Serveur(int port) {
        this.port = port;
        new ThreadAcceptConnexion(this);
    }

    //Getter
    public int getPort() {
        return port;
    }

    public ArrayList<ConnexionUtilisateur> getUtilisateurs() {
        return this.utilisateurs;
    }

    //Méthode Technique
    public void add(ConnexionUtilisateur utilisateur) throws ServeurException {
        if (utilisateur == null)
            throw new ServeurException("La connexion utilisateur vaut null");
        this.getUtilisateurs().add(utilisateur);
    }

    public void remove(ConnexionUtilisateur utilisateur) throws ServeurException {
        if (utilisateur == null)
            throw new ServeurException("La connexion utilisateur vaut null");
        this.getUtilisateurs().remove(utilisateur);
    }

    public ConnexionUtilisateur get(String pseudo) throws ServeurException {
        for (ConnexionUtilisateur utilisateur : this.getUtilisateurs()) {
            if (pseudo.equals(utilisateur.getPseudo()))
                return utilisateur;
        }
        throw new ServeurException("L'utilisateur " + pseudo + " n'existe pas...");
    }

    public void envoyerMessageAToutLeMonde(String message) {
        if (message == null || message.isEmpty()) {
            throw new NullPointerException("Message is null or empty");
        }
        for (ConnexionUtilisateur utilisateur : this.getUtilisateurs()) {
            utilisateur.getThreadConnexion().envoyerMessage(message);
        }
    }

    private String construireMessageListeJoueurs() {
        StringBuilder message = new StringBuilder("@LISTE_JOUEURS ");
        for (Joueur joueur : Jeu.getInstance().getJoueurs())
            message.append(joueur.getNom()).append(";").append(joueur.getMain().size()).append(" ");
        return message.toString();
    }

    private String construireMessageMain(Joueur joueur) {
        if (joueur == null) {
            throw new NullPointerException("Joueur is null");
        }
        StringBuilder message = new StringBuilder("@MAIN ");
        for (Carte carte : joueur.getMain()) message.append(carte.toString()).append(" ");
        return message.toString();
    }

    private void envoyerErreurFatal(String message) {
        if (message == null || message.isEmpty()) {
            throw new NullPointerException("Message is null or empty");
        }
        Jeu.getInstance().reset();
        for (ConnexionUtilisateur user : this.getUtilisateurs()) {
            user.setEnJeu(false);
        }
        this.envoyerMessageAToutLeMonde(message);
    }

    //Méthode Métier
    public void appliquerCommencerPartie(ConnexionUtilisateur coUser) {
        if (!Jeu.getInstance().getJoueurs().isEmpty()) {
            coUser.getThreadConnexion().envoyerMessage("@ERREURLANCEMENTPARTIE Une partie est déjà en cours.");
        }
        else if (!coUser.getPret()) {
            coUser.getThreadConnexion().envoyerMessage("@ERREURLANCEMENTPARTIE Vous ne vous êtes pas mis prêt.");
        }
        else {
            boolean commencer = true;
            for (ConnexionUtilisateur utilisateur : this.getUtilisateurs()) {
                if (!utilisateur.getPret()) {
                    commencer = false;
                    break;
                }
            }
            if (!commencer) {
                coUser.getThreadConnexion().envoyerMessage("@ERREURLANCEMENTPARTIE Un joueur n'est pas encore prêt.");
            } else {
                for (ConnexionUtilisateur user : this.getUtilisateurs()) {
                    Jeu.getInstance().getJoueurs().add(new Joueur(user.getPseudo()));
                }
                try {
                    Jeu.getInstance().commencerPartie();
                    for (ConnexionUtilisateur user : this.getUtilisateurs()) {
                        user.setPret(false);
                        user.setEnJeu(true);
                    }
                    this.envoyerMessageAToutLeMonde("@DEMARRAGEPARTIE");
                    Thread.sleep(1000);
                    this.envoyerMessageAToutLeMonde("@INFO La partie a commencé. C'est à " + Jeu.getInstance().getJoueurCourant().getNom() + " de commencer.");
                    this.envoyerMessageAToutLeMonde("@JOUEURCOURANT " + Jeu.getInstance().getJoueurCourant().getNom());
                    for (Joueur joueur : Jeu.getInstance().getJoueurs()) {
                        this.get(joueur.getNom()).getThreadConnexion().envoyerMessage(this.construireMessageMain(joueur));
                    }
                    this.envoyerMessageAToutLeMonde(this.construireMessageListeJoueurs());
                    this.envoyerMessageAToutLeMonde("@TAS " + Jeu.getInstance().getDefausse().getLast().toString());
                } catch (JeuException e) {
                    this.envoyerMessageAToutLeMonde("@ERREURLANCEMENTPARTIE " + e.getMessage());
                } catch (PiocheException _) {
                    this.envoyerErreurFatal("@ERREUR Un problème lié à la pioche a été detecté. Contactez le support.");
                } catch (InterruptedException | ServeurException _) {
                    this.envoyerErreurFatal("@ERREUR Un problème lié au serveur a été detecté. Contactez le support.");
                }
            }
        }
    }

    public void appliquerJouerCarte(ConnexionUtilisateur utilisateur, String couleur, String valeur) {
        if (utilisateur == null) {
            throw new NullPointerException("La connexion utilisateur vaut null");
        }
        if (couleur == null || couleur.isEmpty()) {
            throw new NullPointerException("La connexion couleur vaut null");
        }
        if (valeur == null || valeur.isEmpty()) {
            throw new NullPointerException("La connexion valeur vaut null");
        }
        try {
            Joueur joueur = Jeu.getInstance().getSpecificJoueur(utilisateur.getPseudo());
            Carte carte = joueur.getSpecificCarte(couleur, valeur);
            try {
                joueur.jouerCarte(carte);
                this.envoyerMessageAToutLeMonde("@INFO " + joueur.getNom() + " a joué une carte.");
                utilisateur.getThreadConnexion().envoyerMessage(construireMessageMain(joueur));
                this.envoyerMessageAToutLeMonde(construireMessageListeJoueurs());
                this.envoyerMessageAToutLeMonde("@TAS " + Jeu.getInstance().getDefausse().getLast().toString());
            } catch (JoueurException e) {
                this.envoyerMessageAToutLeMonde("@INFO " + e.getMessage());
                this.appliquerPunition(e.getJoueur(), utilisateur);
                e.getJoueur().setPeutJouerUneCarte(true);
            }
        } catch (JoueurException _) {
            this.envoyerErreurFatal("@ERREUR Un problème lié au joueur a été detecté. Contactez le support.");
        } catch (JeuException _) {
            this.envoyerErreurFatal("@ERREUR Un problème lié au jeu a été detecté. Contactez le support.");
        }
    }

    public void appliquerPiocherCarte(ConnexionUtilisateur utilisateur) {
        if (utilisateur == null) {
            throw new NullPointerException("La connexion utilisateur vaut null");
        }
        try {
            Joueur joueur = Jeu.getInstance().getSpecificJoueur(utilisateur.getPseudo());
            try {
                joueur.piocherCarte(Jeu.getInstance().getPioche());
                this.envoyerMessageAToutLeMonde("@INFO " + joueur.getNom() + " a pioché une carte.");
                utilisateur.getThreadConnexion().envoyerMessage(construireMessageMain(joueur));
                this.envoyerMessageAToutLeMonde(construireMessageListeJoueurs());
            }
            catch (JoueurException e) {
                this.envoyerMessageAToutLeMonde("@INFO " + e.getMessage());
                this.appliquerPunition(e.getJoueur(), utilisateur);
                e.getJoueur().setPeutPiocher(true);
            }
            catch (PiocheException _) {
                this.envoyerErreurFatal("@ERREUR Un problème lié à la pioche a été detecté. Contactez le support.");
            }
        }
        catch (JeuException _) {
            this.envoyerErreurFatal("@ERREUR Une erreur lié au joueur a été detecté. Contactez le support.");
        }
    }

    public void appliquerFinDuTour(ConnexionUtilisateur utilisateur) {
        if (utilisateur == null) {
            throw new NullPointerException("La connexion utilisateur vaut null");
        }
        try {
            Joueur joueur = Jeu.getInstance().getSpecificJoueur(utilisateur.getPseudo());
            try {
                boolean aJoue = joueur.mettreFinASonTour();
                this.envoyerMessageAToutLeMonde("@INFO " + joueur.getNom() + " a fini son tour.");
                if (joueur.getMain().isEmpty()) {
                    this.envoyerMessageAToutLeMonde("@VICTOIRE "+ joueur.getNom());
                    this.appliquerFinDePartie();
                    return;
                }
                if (aJoue && !Jeu.getInstance().getDefausse().getLast().getMessage().isEmpty()) {
                    this.envoyerMessageAToutLeMonde("@INFO " + Jeu.getInstance().getDefausse().getLast().getMessage());
                }
                for (Joueur joueurr : Jeu.getInstance().getJoueurs()) {
                    this.get(joueurr.getNom()).getThreadConnexion().envoyerMessage(this.construireMessageMain(joueurr));
                }
                this.envoyerMessageAToutLeMonde("@INFO C'est au tour de " + Jeu.getInstance().getJoueurCourant().getNom() + " de jouer.");
                this.envoyerMessageAToutLeMonde("@JOUEURCOURANT " + Jeu.getInstance().getJoueurCourant().getNom());
            }
            catch (JoueurException e) {
                this.envoyerMessageAToutLeMonde("@INFO " + e.getMessage());
                appliquerPunition(e.getJoueur(), utilisateur);
            }
            catch (PiocheException _) {
                this.envoyerErreurFatal("@ERREUR Un problème lié à la pioche a été detecté. Contactez le support.");
            } catch (ServeurException _) {
                this.envoyerErreurFatal("@ERREUR Un problème lié au serveur a été detecté. Contactez le support.");
            }
        } catch (JeuException _) {
            this.envoyerErreurFatal("@ERREUR Une erreur lié au joueur a été detecté. Contactez le support.");
        }
    }

    public void appliquerUno(ConnexionUtilisateur utilisateur) {
        if (utilisateur == null) {
            throw new NullPointerException("La connexion utilisateur vaut null");
        }
        try {
            Joueur joueur = Jeu.getInstance().getSpecificJoueur(utilisateur.getPseudo());
            try {
                joueur.direUno();
                this.envoyerMessageAToutLeMonde("@INFO " + joueur.getNom()+ " a dit UNO !");
            }
            catch (JoueurException e) {
                this.envoyerMessageAToutLeMonde("@INFO " + e.getMessage());
                appliquerPunition(e.getJoueur(), utilisateur);
            }
        } catch (JeuException _) {
            this.envoyerErreurFatal("@ERREUR Une erreur lié au joueur a été detecté. Contactez le support.");
        }
    }

    private void appliquerPunition(Joueur joueur, ConnexionUtilisateur utilisateur) {
        if (joueur == null) {
            throw new NullPointerException("La connexion utilisateur vaut null");
        }
        if (utilisateur == null) {
            throw new NullPointerException("La connexion utilisateur vaut null");
        }
        try {
            joueur.piocherPunition(2, Jeu.getInstance().getPioche());
            this.envoyerMessageAToutLeMonde("@INFO " + joueur.getNom() + " a été puni.");
            if (!joueur.getPeutJouerUneCarte()) {
                joueur.getMain().add(Jeu.getInstance().getDefausse().getLast());
                Jeu.getInstance().getDefausse().removeLast();
                Jeu.getInstance().setCouleurCourante(Jeu.getInstance().getDefausse().getLast().getCouleur());
                Jeu.getInstance().setValeurCourante(Jeu.getInstance().getDefausse().getLast().getValeur());
                this.envoyerMessageAToutLeMonde("@TAS " + Jeu.getInstance().getDefausse().getLast().toString());
            }
            utilisateur.getThreadConnexion().envoyerMessage(construireMessageMain(joueur));
            this.envoyerMessageAToutLeMonde(construireMessageListeJoueurs());
            if (joueur.getASonTour()) {
                Jeu.getInstance().passerAuJoueurSuivant();
            }
            this.envoyerMessageAToutLeMonde("@INFO C'est à " + Jeu.getInstance().getJoueurCourant().getNom() + " de jouer.");
            this.envoyerMessageAToutLeMonde("@JOUEURCOURANT " + Jeu.getInstance().getJoueurCourant().getNom());
        }
        catch (PiocheException _) {
            this.envoyerErreurFatal("@ERREUR Un problème lié à la pioche a été detecté. Contactez le support.");
        }
    }

    private void appliquerFinDePartie() {
        try {
            for (ConnexionUtilisateur user : this.getUtilisateurs()) {
                user.setEnJeu(false);
            }
            JDBC.connecter();
            int clePartie = JDBC.insererPartie();
            for (ConnexionUtilisateur user : this.getUtilisateurs()) {
                Joueur joueur = Jeu.getInstance().getSpecificJoueur(user.getPseudo());
                int cleJoueur;
                try {
                    cleJoueur = JDBC.obtenirCleJoueur(user.getPseudo());
                }
                catch (Exception _) {
                    cleJoueur = JDBC.insererJoueur(user.getPseudo());
                }
                JDBC.insererParticipe(clePartie, cleJoueur, joueur.getScore());
            }
            JDBC.deconnecter();
            Jeu.getInstance().reset();
        }
        catch (JeuException _) {
            this.envoyerErreurFatal("@ERREUR Un problème lié au jeu a été detecté. Contactez le support.");
        }
        catch (ClassNotFoundException | SQLException _) {
            this.envoyerErreurFatal("@ERREUR Un problème lié à la base de données a été detecté. Contactez le support.");
        }
    }

    public void appliquerStats(ConnexionUtilisateur user, String pseudo) {
        try {
            JDBC.connecter();
            try {
                JDBC.obtenirCleJoueur(user.getPseudo());
                int nbTotalPartie = JDBC.obtenirTotalPartie();
                int nbTotalJoueur = JDBC.obtenirTotalJoueur();
                String premierePartieDuJoueur = JDBC.obtenirPremierePartieDuJoueur(pseudo).substring(0,JDBC.obtenirPremierePartieDuJoueur(pseudo).indexOf(" ")-1);
                int nbTotalPartieDuJoueur = JDBC.obtenirTotalPartieDuJoueur(pseudo);
                int nbTotalPartieGagneeDuJoueur = JDBC.obtenirTotalPartieGagneeDuJoueur(pseudo);
                int scoreMoyenDuJoueur = JDBC.obtenirScoreMoyenDuJoueur(pseudo);
                user.getThreadConnexion().envoyerMessage("@STATS "+nbTotalPartie+" "+nbTotalJoueur+" "+premierePartieDuJoueur+" "+nbTotalPartieDuJoueur+" "+nbTotalPartieGagneeDuJoueur+" "+scoreMoyenDuJoueur);
                JDBC.deconnecter();
            }
            catch (Exception _) {
                user.getThreadConnexion().envoyerMessage("@ERREURSTATS");
                JDBC.deconnecter();
            }
        }
        catch (ClassNotFoundException | SQLException _) {
            this.envoyerErreurFatal("@ERREUR Un problème lié à la base de données a été detecté. Contactez le support.");
        }
    }
}