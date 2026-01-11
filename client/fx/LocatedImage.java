package client.fx;

import javafx.scene.image.Image;

import java.io.InputStream;

public class LocatedImage extends Image {
    //Champs
    private final String nom;

    //Constructeur
    public LocatedImage(InputStream url, String nom) {
        super(url);
        this.nom = nom;
    }

    //Getter
    public String getNom() {
        return this.nom;
    }
}
