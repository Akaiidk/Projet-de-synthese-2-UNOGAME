package client.application;

import client.controleurs.ControleurPseudo;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class AppClient extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			Scene scenePseudo = new Scene(new Pane(),1000,600);

			ControleurPseudo cPseudo = new ControleurPseudo(scenePseudo, primaryStage);
			FXMLLoader loaderPseudo = new FXMLLoader(getClass().getResource("../vue/Pseudo.fxml"));
			loaderPseudo.setController(cPseudo);
			scenePseudo.setRoot(loaderPseudo.load());
			
			primaryStage.setScene(scenePseudo);
			primaryStage.setTitle("Uno");
			primaryStage.setResizable(false);
			primaryStage.setOnCloseRequest(_ -> Platform.exit());
			primaryStage.show();
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
