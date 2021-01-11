package saalispaivakirja;

import java.io.*;
import fi.jyu.mit.ohj2.*;
import kanta.Tietue;
/**
 * Saalismerkinnän luokka. 
 * Tietää saaliiden kentät 
 * Osaa antaa merkkijonona i:n kentän tiedot
 * Osaa laittaa merkkijonon i:ksi kentäksi
 * Osaa muuttaa tolppaerotellun merkkijonon saaliin tiedoiksi
 * @author Riku
 * @version 1.4.2020
 *
 */
public class Saalis implements Cloneable, Tietue {
    private int tunnusNro;
    private int merkintaNro;
    private String laji;
    private double paino;
    private double pituus;
    private String pyydys;

    private static int seuraavaNro = 1;


    /**
     * Alustetaan Saalis
     */
    public Saalis() {
        // 
    }


    /**
     * Alustetaan päivämäärämerkinnän alainen saalismerkintä
     * @param merkintaNro Merkinnän viitenumero 
     */
    public Saalis(int merkintaNro) {
        this.merkintaNro = merkintaNro;
    }
    
    /**
     * @return saaliin kenttien lukumäärä
     */
    @Override
    public int getKenttia() {
        return 6;
    }


    /**
     * @return ensimmäinen käyttäjän syötettävän kentän indeksi
     */
    @Override
    public int ekaKentta() {
        return 2;
    }
    

    /**
     * @param k minkä kentän kysymys halutaan
     * @return valitun kentän kysymysteksti
     */
    @Override
    public String getKysymys(int k) {
        switch (k) {
            case 0:
                return "id";
            case 1:
                return "merkintaNro";
            case 2:
                return "Laji";
            case 3:
                return "Paino";
            case 4:
                return "Pituus";
            case 5:
                return "Pyydys";
            default:
                return "";
        }
    }


    /**
     * @param k Minkä kentän sisältö halutaan
     * @return valitun kentän sisältö
     * @example
     * <pre name="test">
     *   Saalis s = new Saalis();
     *   s.parse("   1   |  4  |   Hauki  | 7.5 | 56 | Lusikka  ");
     *   s.anna(0) === "1";   
     *   s.anna(1) === "4";   
     *   s.anna(2) === "Hauki";   
     *   s.anna(3) === "7.5";   
     *   s.anna(4) === "56.0";   
     *   
     * </pre>
     */
    @Override
    public String anna(int k) {
        switch (k) {
            case 0:
                return "" + tunnusNro;
            case 1:
                return "" + merkintaNro;
            case 2:
                return laji;
            case 3:
                return "" + paino;
            case 4:
                return "" + pituus;
            case 5:
                return "" + pyydys;
            default:
                return "";
        }
    }


    /**
     * Asetetaan valitun kentän sisältö.  Mikäli asettaminen onnistuu,
     * palautetaan null, muutoin virheteksti.
     * @param k minkä kentän sisältö asetetaan
     * @param s asetettava sisältö merkkijonona
     * @return null jos ok, muuten virheteksti
     * @example
     * <pre name="test">
     *   Saalis s = new Saalis();
     *   s.aseta(2,"Hauki")  === null;
     *   s.aseta(3,"9")    === null;
     *   
     * </pre>
     */
    @Override
    public String aseta(int k, String s) {
        String st = s.trim();
        StringBuffer sb = new StringBuffer(st);
        switch (k) {
            case 0:
                setTunnusNro(Mjonot.erota(sb, '$', getTunnusNro()));
                return null;
            case 1:
                merkintaNro = Mjonot.erota(sb, '$', merkintaNro);
                return null;
            case 2:
                laji = st;
                return null;
            case 3:
                try {
                    paino = Mjonot.erotaEx(sb, '§', paino);
                } catch (NumberFormatException ex) {
                    return "Paino väärin " + ex.getMessage();
                }
                return null;

            case 4:
                try {
                    pituus = Mjonot.erotaEx(sb, '§', pituus);
                } catch (NumberFormatException ex) {
                    return "Pituus väärin " + ex.getMessage();
                }
                return null;
            case 5:
                pyydys = st;
                return null;

            default:
                return "Väärä kentän indeksi";
        }
    }


    /**
     * Tehdään identtinen klooni saaliista
     * @return Object kloonattu saalis
     * @example
     */
    @Override
    public Saalis clone() throws CloneNotSupportedException { 
        return (Saalis)super.clone();
    }
    

    /**
     * Apumetodi, jolla saadaan täytettyä testiarvot Saaliille.
     * Paino ja pituus arvotaan
     * @param nro Viite merkintään, jonka alaisesta saalismerkinnästä on kyse.
     */
    public void vastaaHauki(int nro) {
        merkintaNro = nro;
        laji = "hauki";
        paino = kanta.TietokantaApu.rand(5, 10);
        pituus = kanta.TietokantaApu.rand(50, 100);
        pyydys = "Nils Master";
    }


    /**
     * Tulostetaan yksittäisen saaliin tiedot
     * @param out Tietovirta johon tulostetaan
     */
    public void tulosta(PrintStream out) {
        out.println(laji + " " + paino + " " + pituus + " " + pyydys );
    }


    /**
     * Tulostetaan saaliin tiedot
     * @param os Tietovirta johon tulostetaan
     */
    public void tulosta(OutputStream os) {
        tulosta(new PrintStream(os));
    }


    /**
     * Antaa saaliille seuraavan tunnusnumeron.
     * @return saaliin uusi tunnusnumero
     * @example
     * <pre name="test">
     *   Saalis hauki1 = new Saalis();
     *   hauki1.getTunnusNro() === 0;
     *   hauki1.rekisteroi();
     *   Saalis hauki2 = new Saalis();
     *   hauki2.rekisteroi();
     *   int n1 = hauki1.getTunnusNro();
     *   int n2 = hauki2.getTunnusNro();
     *   n1 === n2-1;
     * </pre>
     */
    public int rekisteroi() {
        tunnusNro = seuraavaNro;
        seuraavaNro++;
        return tunnusNro;
    }


    /**
     * Palautetaan saaliin oma id
     * @return saaliin id
     */
    public int getTunnusNro() {
        return tunnusNro;
    }


    /**
     * Palautetaan minkä päivämäärämerkinnän alle saalismerkintä kuuluu
     * @return Merkinnän id
     */
    public int getMerkintaNro() {
        return merkintaNro;
    }

    /*
     * Asettaa tunnusnumeron ja samalla, että seuraava tunnusnro on edellistä suurempi
     * @param nr Tunnusnumero, joka asetetaan
     */
    private void setTunnusNro(int nr) {
        tunnusNro = nr;
        if ( tunnusNro >= seuraavaNro ) seuraavaNro = tunnusNro + 1;
    }


    /**
     * Palauttaa saaliin tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return Saalis tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Saalis saalis = new Saalis();
     *   saalis.parse("   1   |  1  |   hauki  | 5.6 | 68.0 | Räsänen  ");
     *   saalis.toString()    === "1|1|hauki|5.6|68.0|Räsänen";
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
     * Selvitää saaliin tiedot tolppaerotellusta merkkijonosta.
     * Pitää huolen että seuraavaNro on suurempi kuin tuleva tunnusnro.
     * @param rivi josta saaliin tiedot otetaan
     * @example
     * <pre name="test">
     *   Saalis saalis = new Saalis();
     *   saalis.parse("   1   |  7  |   hauki  | 5.6 | 68.0 | Räsänen  ");
     *   saalis.getMerkintaNro() === 7;
     *   saalis.toString()    === "1|7|hauki|5.6|68.0|Räsänen";
     *   
     *   saalis.rekisteroi();
     *   int n = saalis.getTunnusNro();
     *   saalis.parse(""+(n+1));
     *   saalis.rekisteroi();
     *   saalis.getTunnusNro() === n+1+1;
     *   saalis.toString()     === "" + (n+1+1) + "|7||5.6|68.0|"; //Aiemmin testi kirjoitettu väärin, toimi niin kuin pitikin
     * </pre>
     */
    public void parse(String rivi) {
        StringBuilder sb = new StringBuilder(rivi);
        for (int k = 0; k < getKenttia(); k++)
            aseta(k, Mjonot.erota(sb, '|'));
    }

    /*
     * Tarkistaa, onko saalismerkintä sisällöltään sama kuin toinen
     * @return Palauttaa true jos samat, false jos null tai ei samat
     */
    @Override
    public boolean equals(Object obj) {
        if ( obj == null ) return false;
        return this.toString().equals(obj.toString());
    }
    
    /*
     * Funktio, joka palauttaa tunnusNro:n
     */
    @Override
    public int hashCode() {
        return tunnusNro;
    }

    
    /**
     * Testiohjelma Saaliille.
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Saalis saalis = new Saalis();
        saalis.vastaaHauki(2);
        saalis.tulosta(System.out);
    }

}
