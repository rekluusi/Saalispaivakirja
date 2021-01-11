package saalispaivakirja;

import java.io.*;
import fi.jyu.mit.ohj2.*;
import kanta.TietokantaApu;
import kanta.Tietue;
import java.util.Comparator;


/**
 * Saalispäiväkirjan päviämäärämerkintä
 * @author Riku
 * @version 1.4.2020
 * 
 */
public class Merkinta implements Cloneable, Tietue {
    private int        tunnusNro      = 0;
    private static int seuraavaNro    = 1;
    private String     pvm            = "";
    private String     vesisto        = "";
    private String     saa            = "";
    private String     kalastajat     = "";
    
    
    /** 
     * Vertailija
     */ 
    public static class Vertailija implements Comparator<Merkinta> { 
        private int k;  
         
        @SuppressWarnings("javadoc") 
        public Vertailija(int k) { 
            this.k = k; 
        } 
         
        @Override 
        public int compare(Merkinta merkinta1, Merkinta merkinta2) { 
            return merkinta1.getAvain(k).compareToIgnoreCase(merkinta2.getAvain(k)); 
        } 
    } 
     
    
    /** 
     * Antaa k:n kentän sisällön merkkijonona 
     * @param k monenenko kentän sisältö palautetaan 
     * @return kentän sisältö merkkijonona 
     */ 
    public String getAvain(int k) { 
        switch ( k ) { 
        case 0: return "" + tunnusNro; 
        case 1: return "" + pvm; 
        case 2: return "" + vesisto;
        case 3: return "" + saa; 
        case 4: return "" + kalastajat; 
        default: return ""; 
        } 
    } 

    
    /**
     * Palauttaa merkinnän kenttien lukumäärän
     * @return kenttien lukumäärä
     */
    @Override
    public int getKenttia() {
        return 5;
    }
    
    
    /**
     * Eka kenttä joka on mielekäs kysyttäväksi, tässä tapauksessa pvm
     * @return ekan kentän indeksi
     */
    @Override
    public int ekaKentta() {
        return 1;
    }
    

    /**
     * Alustetaan merkinnän merkkijono-attribuuti tyhjiksi jonoiksi
     * ja tunnusnro = 0.
     */
    public Merkinta() {
        // 
    }


    /**
     * @return Päivämäärä
     * Tässä ei testiä, koska vastaaPvm() arpoo päivämäärän, minkä testaaminen haastavaa
     */
    public String getPvm() {
        return pvm;
    }
    
    /*
     * Antaa k:n kentän sisällön
     * @param k Monenneko kentän sisältö palautetaan
     * @return sisältö merkkijonona
     */
    @Override
    public String anna(int k) {
        switch ( k ) {
        case 0: return "" + tunnusNro;
        case 1: return "" + pvm;
        case 2: return "" + vesisto;
        case 3: return "" + saa;
        case 4: return "" + kalastajat;
        default: return "x";
        }
    }

    /*
     * Asettaa k:n kentän arvoksi parametrinä annetun merkkijonon arvon
     * @param k Monennenko kentän sisältöön tehdään muutoksia
     * @param jono Merkkijono, joka asetetaan kentän arvoksi
     * @return null Jos asettaminen onnistuu, muuten virheilmoitus
     * @example
     * <pre name="test">
     *  Merkinta m1 = new Merkinta();
     *  m1.aseta(1, "20.11.2020") === null;
     *  m1.aseta(1, "40.11.2020") === "Liian suuri päivämäärä";
     *  m1.aseta(1, "00.11.2020") === "Liian pieni päivämäärä";
     *  m1.aseta(2, "Mikitänjärvi") === null;
     */
    @Override
    public String aseta(int k, String jono) {
        String tjono = jono.trim();
        StringBuffer sb = new StringBuffer(tjono);
        switch ( k ) {
        case 0:
            setTunnusNro(Mjonot.erota(sb, '§', getTunnusNro()));
            return null;
        case 1:
            TietokantaApu pvmTarkistus = new TietokantaApu();
            String virhe = pvmTarkistus.tarkista(tjono);
            if ( virhe != null ) return virhe;
            pvm = tjono;
            return null;
        case 2:
            vesisto = tjono;
            return null;
        case 3:
            saa = tjono;
            return null;
        case 4:
            kalastajat = tjono;
            return null;
        default:
            return "x";
        }
    }
    
    /*
     * Palauttaa merkinnän kenttää vastaavan kysymyksen k:nnesta arvosta
     * @param k kuinka monennen kentän kysymys palautetaan
     * @return k:nnetta kenttää vastaava kysymys
     */
    @Override
    public String getKysymys(int k) {
        switch ( k ) {
        case 0: return "TunnusNro";
        case 1: return "Päivämäärä";
        case 2: return "Vesistö";
        case 3: return "Sää";
        case 4: return "Kalastajat";
        default: return "x";
        }
    }

    
    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot päivämäärämerkinnälle.
     */
    public void vastaaPvm() {
        pvm = kanta.TietokantaApu.arvoPvm();
        vesisto = "Mikitänjärvi";
        saa = "Tihkua";
        kalastajat = "Matti ja Teppo";
    }


    /**
     * Tulostetaan merkinnän tiedot
     * @param out Tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println("id: " + tunnusNro);
        out.println("Päivämäärä: " + pvm);
        out.println("Vesistö: " + vesisto);
        out.println("Sää: " + saa);
        out.println("Kalastajat: " + kalastajat);
    }


    /**
     * Tulostetaan merkinnän tiedot
     * @param os Tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }


    /**
     * Antaa merkinnälle seuraavan rekisterinumeron.
     * @return Merkinnän uusi tunnusNro
     * @example
     * <pre name="test">
     *   Merkinta m1 = new Merkinta();
     *   m1.getTunnusNro() === 0;
     *   m1.rekisteroi();
     *   Merkinta m2 = new Merkinta();
     *   m2.rekisteroi();
     *   int n1 = m1.getTunnusNro();
     *   int n2 = m2.getTunnusNro();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }


    /**
     * Palauttaa merkinnän tunnusnumeron.
     * @return Merkinnän tunnusnumero
     */
    public int getTunnusNro() {
        return tunnusNro;
    }

    
    /**
     * Asettaa tunnusnumeron ja samalla varmistaa että
     * seuraava numero on aina suurempi kuin tähän mennessä suurin.
     * @param nr asetettava tunnusnumero
     */
    private void setTunnusNro(int nr) {
        tunnusNro = nr;
        if (tunnusNro >= seuraavaNro) seuraavaNro = tunnusNro + 1;
    }
    

    /**
     * Palauttaa päivämäärämerkinnän tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return Merkintä tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Merkinta merkinta = new Merkinta();
     *   merkinta.parse("   1  |  29.1.2018   | Mikitänjärvi |  Poutaa  |");
     *   merkinta.toString().startsWith("1|29.1.2018|Mikitänjärvi|Poutaa|") === false; 
     * </pre>  
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("");
        String erotin = "";
        for (int k = 0; k < getKenttia(); k++) {
            sb.append(erotin);
            sb.append(anna(k));
            erotin = "|";
        }
        return sb.toString();
    }


    /**
     * Selvitää merkinnän tiedot tolppaerotellusta merkkijonosta
     * Pitää huolen että seuraavaNro on suurempi kuin tuleva tunnusNro.
     * @param rivi josta merkinnän tiedot otetaan
     * @example
     * <pre name="test">
     *   Merkinta merkinta = new Merkinta();
     *   merkinta.parse("    1  |  29.1.2018   | Mikitänjärvi |  Poutaa  |");
     *   merkinta.getTunnusNro() === 1;
     *   merkinta.toString().startsWith("1|29.1.2018|Mikitänjärvi|Poutaa|") === false;
     *
     *   merkinta.rekisteroi();
     *   int n = merkinta.getTunnusNro();
     *   merkinta.parse(""+(n+1));        
     *   merkinta.rekisteroi();           
     *   merkinta.getTunnusNro() === n+1+1;    
     * </pre>
     */
    public void parse(String rivi) {
        StringBuffer sb = new StringBuffer(rivi);
        for (int k = 0; k < getKenttia(); k++)
            aseta(k, Mjonot.erota(sb, '|'));
    }
    
    /**
     * Tehdään identtinen klooni merkinnästä
     * @return Object kloonattu merkintä
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Merkinta m = new Merkinta();
     *   m.parse("   1  |  20.11.1995   | 1");
     *   Merkinta kopio = m.clone();
     *   kopio.toString() === m.toString();
     *   m.parse("   2  |  21.11.1944   | 1");
     *   kopio.toString().equals(m.toString()) === false;
     * </pre>
     */
    @Override
    public Merkinta clone() throws CloneNotSupportedException {
        Merkinta uusi;
        uusi = (Merkinta) super.clone();
        return uusi;
    }
    

    
    /**
     * Tutkii onko merkinnän tiedot samat kuin parametrina tuodun merkinnän tiedot
     * @param merkinta Merkintä johon verrataan
     * @return true jos kaikki tiedot samat, false muuten
     * @example
     * <pre name="test">
     *   Merkinta m1 = new Merkinta();
     *   m1.parse("   1  | 20.11.1995  | 1");
     *   Merkinta m2 = new Merkinta();
     *   m2.parse("   1  | 20.11.1995  | 1");
     *   Merkinta m3 = new Merkinta();
     *   m3.parse("   1  | 20.11.1996  | 1");
     *   
     *   m1.equals(m2) === true;
     *   m2.equals(m1) === true;
     *   m1.equals(m3) === false;
     *   m3.equals(m2) === false;
     * </pre>
     */
    public boolean equals(Merkinta merkinta) {
        if ( merkinta == null ) return false;
        for (int k = 0; k < getKenttia(); k++)
            if ( !anna(k).equals(merkinta.anna(k)) ) return false;
        return true;
    }

    
    @Override
    public boolean equals(Object merkinta) {
        if ( merkinta instanceof Merkinta ) return equals((Merkinta)merkinta);
        return false;

    }

    
    @Override
    public int hashCode() {
        return tunnusNro;
    }


    /**
     * Testiohjelma päivämäärämerkinnälle.
     * @param args Ei käytössä
     */
    public static void main(String args[]) {
        Merkinta m1 = new Merkinta(), m2 = new Merkinta();
        
        m1.rekisteroi();
        m1.vastaaPvm();
        m1.tulosta(System.out);
        
        m2.rekisteroi();
        m2.vastaaPvm();
        m2.tulosta(System.out);
    }

}
