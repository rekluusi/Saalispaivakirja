package fxSaalispaivakirja;

import static fxSaalispaivakirja.TietueDialogController.getFieldId; 
import java.awt.Desktop;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;

import fi.jyu.mit.fxgui.ComboBoxChooser;
import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import fi.jyu.mit.fxgui.StringGrid;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import saalispaivakirja.Merkinta;
import saalispaivakirja.Saalispaivakirja;
import saalispaivakirja.SailoException;
import saalispaivakirja.Saalis;

/**
 * Luokka käyttöliittymän tapahtumien hoitamiseksi.
 * @author Riku
 * @version 3.2.2020
 * @version 1.4.2020
 *
 */
public class SaalisGUIController implements Initializable {

    @FXML private TextField hakuehto;
    @FXML private ComboBoxChooser<String> cbKentat;
    @FXML private Label labelVirhe;
    @FXML private ScrollPane panelMerkinta;
    @FXML private GridPane gridMerkinta;
    @FXML private ListChooser<Merkinta> chooserMerkinnat;
    @FXML private StringGrid<Saalis> tableSaaliit;
    
    @FXML private TextField editPvm; 
    @FXML private TextField editVesisto; 
    @FXML private TextField editSaa;
    @FXML private TextField editKalastajat;    

    
    @Override
    public void initialize(URL url, ResourceBundle bundle) {
        alusta();      
    }

    
    @FXML private void handleHakuehto() {
        hae(0); 
    }

    
    
    @FXML private void handleTallenna() {
        tallenna();
    }
    
    
    @FXML private void handleAvaa() {
        avaa();
    }
    
    
    @FXML private void handleLopeta() {
        tallenna();
        Platform.exit();
    } 

    
    @FXML private void handleUusiMerkinta() {
        uusiMerkinta();
    }
    
    
    @FXML private void handleMuokkaaMerkinta() {
        muokkaa(kentta);
    }
    
    
    @FXML private void handlePoistaMerkinta() {
        poistaMerkinta();
    }
    
     
    @FXML private void handleUusiSaalis() {
        uusiSaalis();
    }
    

    @FXML private void handleMuokkaaSaalis() {
        muokkaaSaalista();
    }
    

    @FXML private void handlePoistaSaalis() {
        poistaSaalis();
    }
    

    @FXML private void handleApua() {
        avustus();
    }
    

//===========================================================================================    
// Tästä eteenpäin ei käyttöliittymään suoraan liittyvää koodia    
    
    private Saalispaivakirja saalispaivakirja;
    private Merkinta merkintaKohdalla;
    private TextField edits[]; 
    private int kentta = 0; 
    private static Saalis apusaalis = new Saalis(); 
    private static Merkinta apumerkinta = new Merkinta(); 
    
    
    /**
     * Tekee tarvittavat muut alustukset, nyt vaihdetaan GridPanen tilalle
     * yksi iso tekstikenttä, johon voidaan tulostaa päivämäärämerkintöjen tiedot.
     * Alustetaan myös merkintälistan kuuntelija 
     */
    protected void alusta() {
        chooserMerkinnat.clear();
        chooserMerkinnat.addSelectionListener(e -> naytaMerkinta());
        cbKentat.clear(); 
        for (int k = apumerkinta.ekaKentta(); k < apumerkinta.getKenttia(); k++) 
            cbKentat.add(apumerkinta.getKysymys(k), null); 
        cbKentat.getSelectionModel().select(0); 
        
        edits = TietueDialogController.luoKentat(gridMerkinta, apumerkinta); 

        for (TextField edit: edits)  
            if ( edit != null ) {  
                edit.setEditable(false);  
                edit.setOnMouseClicked(e -> { if ( e.getClickCount() > 1 ) muokkaa(getFieldId(e.getSource(),0)); });  
                edit.focusedProperty().addListener((a,o,n) -> kentta = getFieldId(edit,kentta)); 
                edit.setOnKeyPressed( e -> {if ( e.getCode() == KeyCode.F2 ) muokkaa(kentta);}); 
            }
        
        int eka = apusaalis.ekaKentta(); 
        int lkm = apusaalis.getKenttia(); 
        String[] headings = new String[lkm-eka]; 
        for (int i=0, k=eka; k<lkm; i++, k++) headings[i] = apusaalis.getKysymys(k); 
        tableSaaliit.initTable(headings); 
        tableSaaliit.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); 
        tableSaaliit.setEditable(false); 
        tableSaaliit.setPlaceholder(new Label("Ei vielä saaliita")); 
         
        tableSaaliit.setColumnSortOrderNumber(1); 
        tableSaaliit.setColumnSortOrderNumber(2); 
        tableSaaliit.setColumnWidth(1, 60); 
        
        tableSaaliit.setOnMouseClicked( e -> { if ( e.getClickCount() > 1 ) muokkaaSaalista(); } );
        tableSaaliit.setOnKeyPressed( e -> {if ( e.getCode() == KeyCode.F2 ) muokkaaSaalista();}); 
    }

    
    @SuppressWarnings("unused")
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
     * Alustaa saalispäiväkirjan lukemalla sen valitun nimisestä tiedostosta
     * @param nimi tiedosto josta saalispäiväkirjan tiedot luetaan
     * @return Palauttaa null, jos tiedoston luku onnistuu. Muuten error.
     */
    protected String lueTiedosto(String nimi) {
        try {
            saalispaivakirja.lueTiedostosta(nimi);
            hae(0);
            return null;
        } catch (SailoException e) {
            hae(0);
            String virhe = e.getMessage(); 
            if ( virhe != null ) Dialogs.showMessageDialog(virhe);
            return virhe;
        }
     }

    
    /**
     * Kysytään tiedoston nimi ja luetaan se
     * @return true jos onnistui, false jos ei
     */
    public boolean avaa() {
        String uusinimi = "";
        lueTiedosto(uusinimi);
        return true;
    }

    
    /**
     * Tietojen tallennus
     * @return Null jos talletus onnistuu. Muuten error.
     */
    private String tallenna() {
        try {
            saalispaivakirja.tallenna();
            return null;
        } catch (SailoException ex) {
            Dialogs.showMessageDialog("Tallennuksessa ongelmia! " + ex.getMessage());
            return ex.getMessage();
        }
    }


    /**
     * Kysyy, haluaako käyttäjä sulkea ohjelman. Tallentaa, jos vastaa kyllä.
     * @return true Jos saa sulkea sovelluksen, false jos ei
     */
    public boolean voikoSulkea() {
        boolean vastaus = Dialogs.showQuestionDialog("Lopetus",
                "Haluatko tallentaa ja sulkea ohjelman?", "Kyllä", "Ei");
        if (vastaus) tallenna();
        return vastaus;
    }
    
    
    /**
     * Näyttää listasta valitun päivämäärämerkinnän tiedot tekstikenttiin
     */
    protected void naytaMerkinta() {
        merkintaKohdalla = chooserMerkinnat.getSelectedObject();
            if (merkintaKohdalla == null) return;
            
            TietueDialogController.naytaTietue(edits, merkintaKohdalla);
            naytaSaaliit(merkintaKohdalla);
        }
    

    /**
     * Hakee päivämäärämerkinnän tiedot listaan
     * @param pnr mm
     */
    protected void hae(int pnr) {
        int pnro = pnr;
        if ( pnro <= 0 ) { 
            Merkinta kohdalla = merkintaKohdalla; 
            if ( kohdalla != null ) pnro = kohdalla.getTunnusNro(); 
        }
        
        int k = cbKentat.getSelectionModel().getSelectedIndex() + apumerkinta.ekaKentta();
        String ehto = hakuehto.getText(); 
        if (ehto.indexOf('*') < 0) ehto = "*" + ehto + "*"; 

        chooserMerkinnat.clear();

        int index = 0;
        Collection<Merkinta> merkinnat;
        merkinnat = saalispaivakirja.etsi(ehto, k);
        int j = 0;
        for (Merkinta merkinta : merkinnat) {
            if (merkinta.getTunnusNro() == pnro) index = j;
            chooserMerkinnat.add(merkinta.getPvm(), merkinta);
            j++;
        }
        chooserMerkinnat.setSelectedIndex(index);
    }


    /**
     * Luo uuden päivämäärämerkinnän
     */
    protected void uusiMerkinta() {
        try {
            Merkinta uusi = new Merkinta();
            uusi = TietueDialogController.kysyTietue(null, uusi, 1);   
            if ( uusi == null ) return;
            uusi.rekisteroi();
            saalispaivakirja.lisaa(uusi);
            hae(uusi.getTunnusNro());
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
            return;
        }

    }
    
    /*
     * Näyttää merkinnän saaliit
     */
    private void naytaSaaliit(Merkinta merkinta) {
        tableSaaliit.clear();
        if ( merkinta == null ) return;
        
        List<Saalis> Saaliit = saalispaivakirja.annaSaaliit(merkinta);
        if ( Saaliit.size() == 0 ) return;
        for (Saalis saalis: Saaliit)
            naytaSaalis(saalis); 
    }

    /*
     * Näyttää saaliin tiedot
     */
    private void naytaSaalis(Saalis saalis) {
        int kenttia = saalis.getKenttia(); 
        String[] rivi = new String[kenttia-saalis.ekaKentta()]; 
        for (int i=0, k=saalis.ekaKentta(); k < kenttia; i++, k++) 
            rivi[i] = saalis.anna(k); 
        tableSaaliit.add(saalis,rivi);

    }

    
    /** 
     * Lisää uuden tyhjän saalismerkinnän
     */ 
    public void uusiSaalis() {
        if ( merkintaKohdalla == null ) return; 
        Saalis uusi = new Saalis(merkintaKohdalla.getTunnusNro());
        uusi = TietueDialogController.kysyTietue(null, uusi, 0);
        if ( uusi == null ) return;
        uusi.rekisteroi();
        saalispaivakirja.lisaa(uusi);
        naytaSaaliit(merkintaKohdalla); 
        tableSaaliit.selectRow(1000); 
    }
    
    /*
     * Saaliin muokkauksen käsittely
     */
    private void muokkaaSaalista() {
        int r = tableSaaliit.getRowNr();
        if ( r < 0 ) return; 
        Saalis saalis = tableSaaliit.getObject();
        if ( saalis == null ) return;
        int k = tableSaaliit.getColumnNr()+saalis.ekaKentta();
        try {
            saalis = TietueDialogController.kysyTietue(null, saalis.clone(), k);
            if ( saalis == null ) return;
            saalispaivakirja.korvaaTaiLisaa(saalis); 
            naytaSaaliit(merkintaKohdalla); 
            tableSaaliit.selectRow(r);  
        } catch (CloneNotSupportedException  e) { // 
        } catch (SailoException e) {
            Dialogs.showMessageDialog("Ongelmia lisäämisessä: " + e.getMessage());
        }
    }
  

    /**
     * @param saalispaivakirja Saalispäiväkirja jota käytetään tässä käyttöliittymässä
     */
    public void setSaalispaivakirja(Saalispaivakirja saalispaivakirja) {
        this.saalispaivakirja = saalispaivakirja;
        naytaMerkinta();
    }
    

    /**
     * Poistetaan saalistaulukosta valitulla kohdalla oleva saalis. 
     */
    private void poistaSaalis() {
        int rivi = tableSaaliit.getRowNr();
        if ( rivi < 0 ) return;
        Saalis saalis = tableSaaliit.getObject();
        if ( saalis == null ) return;
        if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko saalis?", "Kyllä", "Ei") )
            return;
        saalispaivakirja.poistaSaalis(saalis);
        naytaSaaliit(merkintaKohdalla);
        int saaliita = tableSaaliit.getItems().size(); 
        if ( rivi >= saaliita ) rivi = saaliita -1;
        tableSaaliit.getFocusModel().focus(rivi);
        tableSaaliit.getSelectionModel().select(rivi);
    }


    /*
     * Poistetaan listalta valittu merkintä
     */
    private void poistaMerkinta() {
        Merkinta merkinta = merkintaKohdalla;
        if ( merkinta == null ) return;
        if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko merkintä: " + merkinta.getPvm(), "Kyllä", "Ei") )
            return;
        saalispaivakirja.poista(merkinta);
        int index = chooserMerkinnat.getSelectedIndex();
        hae(0);
        chooserMerkinnat.setSelectedIndex(index);
    }

    
    /**
     * Näytetään ohjelman suunnitelma erillisessä selaimessa.
     */
    private void avustus() {
        Desktop desktop = Desktop.getDesktop();
        try {
            URI uri = new URI("https://tim.jyu.fi/view/kurssit/tie/ohj2/2020k/ht/heikkiri#ohjelman-k%C3%A4ytt%C3%B6");
            desktop.browse(uri);
        } catch (URISyntaxException e) {
            return;
        } catch (IOException e) {
            return;
        }
    }
    
    /*
     * Päivämäärämerkinnän muokkauksen käsittely
     */
    private void muokkaa(int k) { 
        if ( merkintaKohdalla == null ) return; 
        try { 
            Merkinta merkinta; 
            merkinta = TietueDialogController.kysyTietue(null, merkintaKohdalla.clone(), k); 
            if ( merkinta == null ) return; 
            saalispaivakirja.korvaaTaiLisaa(merkinta); 
            hae(merkinta.getTunnusNro()); 
        } catch (CloneNotSupportedException e) { 
            // 
        } catch (SailoException e) { 
            Dialogs.showMessageDialog(e.getMessage()); 
        } 
    }     

    
    /**
     * Tulostaa päivämäärämerkinnän tiedot
     * @param os Tietovirta johon tulostetaan
     * @param merkinta Tulostettava merkintä
     */
    public void tulosta(PrintStream os, final Merkinta merkinta) {
        os.println("----------------------------------------------");
        merkinta.tulosta(os);
        os.println("----------------------------------------------");
        List<Saalis> saaliit = saalispaivakirja.annaSaaliit(merkinta);
        for (Saalis saalis : saaliit) 
            saalis.tulosta(os);
    }
    
    
    /**
     * Tulostaa listassa olevat päivämäärämerkinnän tekstialueeseen
     * @param text Alue johon tulostetaan
     */
    public void tulostaValitut(TextArea text) {
        try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
            os.println("Tulostetaan kaikki päivämäärämerkinnät");
            Collection<Merkinta> merkinnat = saalispaivakirja.etsi("", -1); 
            for (Merkinta merkinta : merkinnat) { 
                tulosta(os, merkinta);
                os.println("\n\n");
            }
        }
    }
}
