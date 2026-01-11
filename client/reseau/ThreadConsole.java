package client.reseau;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ThreadConsole extends Thread {
    //Champs
    private final BufferedReader in;
    private final PrintWriter out;
    private final ConnexionClient connexion;
    private boolean fin = false;

    //Constructeur
    public ThreadConsole(ConnexionClient connexion, Socket socket) {
        this.connexion = connexion;
        try {
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.out = new PrintWriter(socket.getOutputStream(), true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Thread connexion créé");
        start();
    }

    //Getter
    private BufferedReader getIn() {
        return this.in;
    }

    private PrintWriter getOut() {
        return this.out;
    }

    private ConnexionClient getConnexion() {
        return this.connexion;
    }

    private boolean getFin() {
        return this.fin;
    }

    //Thread
    @Override
    public void run() {
        while (!this.getFin()) {
            try {
                String message = getIn().readLine();
                this.getConnexion().traiterMessage(message);
            } catch (IOException e) {
                this.fin = true;
            }
        }
    }

    //Méthode Technique
    public void envoyerMessage(String message) {
        getOut().println(message);
    }

    public void fin() {
        fin = true;
    }
}
