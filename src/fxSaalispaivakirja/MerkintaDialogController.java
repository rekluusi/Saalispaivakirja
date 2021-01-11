package fxSaalispaivakirja;

import java.net.URL;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ModalController;
import fi.jyu.mit.fxgui.ModalControllerInterface;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import saalispaivakirja.Merkinta;
import fi.jyu.mit.ohj2.Mjonot;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;

/** 
 * Kysyy merkinnän tiedot avaamalla uuden ikkunan, johon ne syötetään
 * @author Riku
 * @version 24.4.2020
 *
 */
public class MerkintaDialogController implements ModalControllerInterface<Merkinta>,Initializable{

    @FXML private TextField editPvm;
    @FXML private TextField editVesisto;
    @FXML private TextField editSaa;
    @FXML private TextField editKalastajat;    
    @FXML private Label labelVirhe;
    @FXML private ScrollPane panelMerkinta;
    @FXML private GridPane gridMerkinta;

    /*
     * Alustaa uuden päivämäärämerkintä-ikkunan
     */
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();  
    }
    
    
    /*
     * Käsittelee OK-napin painalluksen, tarkistaa onko tyhjä
     */
    @FXML private void handleOK() {
        if ( merkintaKohdalla != null && merkintaKohdalla.getPvm().trim().equals("") ) {
            naytaVirhe("Päivämäärä ei saa olla tyhjä!");
            return;
        }
        ModalController.closeStage(labelVirhe);
    }

    /*
     * Käsittelee Cancel-napin painalluksen
     */
    @FXML private void handleCancel() {
        merkintaKohdalla = null;
        ModalController.closeStage(labelVirhe);
    }

    
// ========================================================    
    private Merkinta merkintaKohdalla;
    private static Merkinta apumerkinta = new Merkinta();
    private TextField[] edits;
    private int kentta = 0;
    

    /**
     * Luodaan GridPaneen merkinnän tiedot
     * @param gridMerkinta mihin tiedot luodaan
     * @return luodut tekstikentät
     */
    public static TextField[] luoKentat(GridPane gridMerkinta) {
        gridMerkinta.getChildren().clear();
        TextField[] edits = new TextField[apumerkinta.getKenttia()];
        
        for (int i=0, k = apumerkinta.ekaKentta(); k < apumerkinta.getKenttia(); k++, i++) {
            Label label = new Label(apumerkinta.getKysymys(k));
            gridMerkinta.add(label, 0, i);
            TextField edit = new TextField();
            edits[k] = edit;
            edit.setId("e"+k);
            gridMerkinta.add(edit, 1, i);
        }
        return edits;
    }

    
    /**
     * Tyhjennetään tekstikentät
     * @param edits Tyhjennettävät kentät
     */
    public static void tyhjenna(TextField[] edits) {
        for (TextField edit: edits) 
            if ( edit != null ) edit.setText(""); 
    }

    /**
    * Palautetaan komponentin id:stä saatava luku
    * @param obj Tutkittava komponentti
    * @param oletus Mikä arvo jos id ei ole kunnollinen
    * @return komponentin id lukuna 
    */
    public static int getFieldId(Object obj, int oletus) {
        if ( !( obj instanceof Node)) return oletus;
        Node node = (Node)obj;
        return Mjonot.erotaInt(node.getId().substring(1),oletus);
    }


    /**
     * Tekee tarvittavat muut alustukset.
     */
    protected void alusta() {
        edits = luoKentat(gridMerkinta);
        for (TextField edit : edits)
            if ( edit != null )
                edit.setOnKeyReleased( e -> kasitteleMuutosMerkintaan((TextField)(e.getSource())));
        panelMerkinta.setFitToHeight(true);

        }

     
    @Override
    public void setDefault(Merkinta oletus) {
        merkintaKohdalla = oletus;
        naytaMerkinta(edits, merkintaKohdalla);
    }

    
    @Override
    public Merkinta getResult() {
        return merkintaKohdalla;
    }
    
    
    private void setKentta(int kentta) {
        this.kentta = kentta;
    }

    
    /**
     * Mitä tehdään kun dialogi on näytetty
     */
    @Override
    public void handleShown() {
        kentta = Math.max(apumerkinta.ekaKentta(), Math.min(kentta, apumerkinta.getKenttia()-1));
        edits[kentta].requestFocus();
    }
    
    
    private void naytaVirhe(String virhe) {
        if ( virhe == null || virhe.isEmpty() ) {
            labelVirhe.setText("");
            labelVirhe.getStyleClass().removeAll("virhe");
            return;
        }
        labelVirhe.setText(virhe);
        labelVirhe.getStyleClass().add("virhe");
    }

    
    /**
     * Käsitellään merkintään tullut muutos
     * @param edit Muuttunut kenttä
     */
    protected void kasitteleMuutosMerkintaan(TextField edit) {
        if (merkintaKohdalla == null) return;
        int k = getFieldId(edit,apumerkinta.ekaKentta());
        String s = edit.getText();
        String virhe = null;
        virhe = merkintaKohdalla.aseta(k,s);
        if (virhe == null) {
            Dialogs.setToolTipText(edit,"");
            edit.getStyleClass().removeAll("virhe");
            naytaVirhe(virhe);
        } else {
            Dialogs.setToolTipText(edit,virhe);
            edit.getStyleClass().add("virhe");
            naytaVirhe(virhe);
        }
    }
    
    
    /**
     * Näytetään merkinnän tiedot TextField komponentteihin
     * @param edits Tekstikenteistä muodostuva taulukko
     * @param merkinta näytettävä merkintä
     */
    public static void naytaMerkinta(TextField[] edits, Merkinta merkinta) {
        if (merkinta == null) return;
        for (int k = merkinta.ekaKentta(); k < merkinta.getKenttia(); k++) {
            edits[k].setText(merkinta.anna(k));
        }
    }
    
    
    /**
     * Luodaan merkinnän kysymisdialogi ja palautetaan sama tietue muutettuna tai null
     * @param modalityStage mille ollaan modaalisia, null = sovellukselle
     * @param oletus mitä dataan näytetään oletuksena
     * @param kentta Mikä kenttä saa fokuksen kun näytetään
     * @return null jos painetaan Cancel, muuten täytetty tietue
     */
    public static Merkinta kysyMerkinta(Stage modalityStage, Merkinta oletus, int kentta) {
        return ModalController.<Merkinta, MerkintaDialogController>showModal(
                    MerkintaDialogController.class.getResource("MerkintaDialogView.fxml"),
                    "Saalispäiväkirja",
                    modalityStage, oletus,
                    ctrl -> ctrl.setKentta(kentta)
                );
    }

}
