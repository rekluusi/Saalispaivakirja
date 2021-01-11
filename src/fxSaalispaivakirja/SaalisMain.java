package fxSaalispaivakirja;
	
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import saalispaivakirja.Saalispaivakirja;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;

/**
 * 
 * @author Riku
 * @version 1.4.2020
 *
 * Pääohjelma, joka avaa Saalispäiväkirjan
 * 
 */ 
public class SaalisMain extends Application {
	@Override
    public void start(Stage primaryStage) {
        try {
            final FXMLLoader ldr = new FXMLLoader(getClass().getResource("SaalisGUIView.fxml"));
            final Pane root = (Pane)ldr.load();
            final SaalisGUIController saalispaivakirjaCtrl = (SaalisGUIController)ldr.getController();

            final Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("saalis.css").toExternalForm());
            primaryStage.setScene(scene);
            primaryStage.setTitle("Saalispäiväkirja");

            primaryStage.setOnCloseRequest((event) -> {
                    if ( !saalispaivakirjaCtrl.voikoSulkea() ) event.consume();
                });
            
            Saalispaivakirja saalispaivakirja = new Saalispaivakirja();  
            saalispaivakirjaCtrl.setSaalispaivakirja(saalispaivakirja); 
            
            primaryStage.show();           
            Application.Parameters params = getParameters(); 
            if ( params.getRaw().size() > 0 ) 
                saalispaivakirjaCtrl.lueTiedosto(params.getRaw().get(0));  
            else
                if ( !saalispaivakirjaCtrl.avaa() ) Platform.exit();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
	
    /**
     * Käynnistetään käyttöliittymä 
     * @param args komentorivin parametrit
     */
	public static void main(String[] args) {
		launch(args);
	}
}
