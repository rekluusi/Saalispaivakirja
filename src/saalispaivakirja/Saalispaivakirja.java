package saalispaivakirja;

import java.util.*;
import java.io.File;

/**
 * Saalispäiväkirja-luokka. 
 * Huolehtii merkinnät ja saaliit -luokkien välisestä yhteistyöstä
 * ja välittää näitä tietoja pyydettäessä
 * @author Riku
 * @version 1.4.2020
 * 
 * Vaadittu alustus testeille
 * @example
 * <pre name="testJAVA">
 * #import saalispaivakirja.SailoException;
 * private Saalispaivakirja saalispaivakirja;
 * private Merkinta m1;
 * private Merkinta m2;
 * private int mid1;
 * private int mid2;
 * private Saalis hauki11;
 * private Saalis hauki12;
 * private Saalis hauki21;
 * private Saalis hauki22;
 * private Saalis hauki23;
 * 
 * public void alustaSaalispaivakirja() {
 * saalispaivakirja = new Saalispaivakirja();
 * m1 = new Merkinta(); m1.vastaaPvm(); m1.rekisteroi();
 * m2 = new Merkinta(); m2.vastaaPvm(); m2.rekisteroi();
 * mid1 = m1.getTunnusNro();
 * mid2 = m2.getTunnusNro();
 * hauki11 = new Saalis(mid1); hauki11.vastaaHauki(mid1);
 * hauki12 = new Saalis(mid1); hauki12.vastaaHauki(mid1);
 * hauki21 = new Saalis(mid2); hauki21.vastaaHauki(mid2);
 * hauki22 = new Saalis(mid2); hauki22.vastaaHauki(mid2);
 * hauki23 = new Saalis(mid2); hauki23.vastaaHauki(mid2);
 * try {
 * saalispaivakirja.lisaa(m1);
 * saalispaivakirja.lisaa(m2);
 * saalispaivakirja.lisaa(hauki11);
 * saalispaivakirja.lisaa(hauki12);
 * saalispaivakirja.lisaa(hauki21);
 * saalispaivakirja.lisaa(hauki22);
 * saalispaivakirja.lisaa(hauki23);
 *  } catch ( Exception e) {
 *      System.err.println(e.getMessage());
 *  }
 * }
 * </pre>
 *
 */
public class Saalispaivakirja {
    private Merkinnat merkinnat = new Merkinnat();
    private Saaliit saaliit = new Saaliit();


    /**
     * Palautaa saalispäiväkirjan päivämäärämerkintöjen lukumäärän
     * @return Päivämäärämerkintöjen lukumäärä
     */
    public int getMerkintoja() {
        return merkinnat.getLkm();
    }


    /**
     * Poistaa merkinnöistä ja saaliista ne joilla on nro. 
     * @param merkinta Viitenumero, jonka mukaan poistetaan
     * @return Montako päivämäärämerkintää poistettiin
     */
    public int poista(Merkinta merkinta) {
        if ( merkinta == null ) return 0;
        int ret = merkinnat.poista(merkinta.getTunnusNro()); 
        saaliit.poistaMerkinnanSaaliit(merkinta.getTunnusNro()); 
        return ret; 
    }

    
    /** 
     * Poistaa saaliin 
     * @param saalis poistettava saalis 
     * @example
     * <pre name="test">
     * #THROWS Exception
     *   alustaSaalispaivakirja();
     *   saalispaivakirja.annaSaaliit(m1).size() === 2;
     *   saalispaivakirja.poistaSaalis(hauki11);
     *   saalispaivakirja.annaSaaliit(m1).size() === 1;
     */ 
    public void poistaSaalis(Saalis saalis) { 
        saaliit.poista(saalis); 
    } 



    /**
     * Lisää saalispäiväkirjaan uuden päivämäärämerkinnän
     * @param merkinta Lisättävä merkintä
     * @throws SailoException Jos lisäystä ei voida tehdä
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * Saalispaivakirja saalispaivakirja = new Saalispaivakirja();
     * Merkinta m1 = new Merkinta(), m2 = new Merkinta();
     * m1.rekisteroi(); m2.rekisteroi();
     * saalispaivakirja.getMerkintoja() === 0;
     * saalispaivakirja.lisaa(m1); saalispaivakirja.getMerkintoja() === 1;
     * saalispaivakirja.lisaa(m2); saalispaivakirja.getMerkintoja() === 2;
     * saalispaivakirja.lisaa(m1); saalispaivakirja.getMerkintoja() === 3;
     * saalispaivakirja.getMerkintoja() === 3;
     * saalispaivakirja.annaMerkinta(0) === m1;
     * saalispaivakirja.annaMerkinta(1) === m2;
     * saalispaivakirja.annaMerkinta(2) === m1;
     * saalispaivakirja.annaMerkinta(3) === m1; #THROWS IndexOutOfBoundsException 
     * saalispaivakirja.lisaa(m1); saalispaivakirja.getMerkintoja() === 4;
     * saalispaivakirja.lisaa(m1); saalispaivakirja.getMerkintoja() === 5;
     * saalispaivakirja.lisaa(m1); saalispaivakirja.getMerkintoja() === 6;
     * </pre>
     */
    public void lisaa(Merkinta merkinta) throws SailoException {
        merkinnat.lisaa(merkinta);
    }
    
    /** 
     * Korvaa merkinnän tietorakenteessa. 
     * Etsitään samalla tunnusnumerolla oleva merkintä. Jos ei löydy, lisätään.
     * @param merkinta lisätäävän saaliin viite.  
     * @throws SailoException jos tietorakenne on jo täynnä 
     * @example
     * <pre name="test">
     * #THROWS SailoException
     * alustaSaalispaivakirja();
     * saalispaivakirja.etsi("*",0).size() === 2;
     * saalispaivakirja.korvaaTaiLisaa(m1);
     * saalispaivakirja.etsi("*",0).size() === 2;
     * </pre>
     */ 
    public void korvaaTaiLisaa(Merkinta merkinta) throws SailoException { 
        merkinnat.korvaaTaiLisaa(merkinta); 
    } 
    
    
    /** 
     * Korvaa saaliin tietorakenteessa.  
     * Etsitään samalla tunnusnumerolla oleva saalis. Jos ei löydy, lisätään.
     * @param saalis lisättävän saaliin viite.  
     * @throws SailoException jos tietorakenne on jo täynnä 
     */ 
    public void korvaaTaiLisaa(Saalis saalis) throws SailoException { 
        saaliit.korvaaTaiLisaa(saalis); 
    } 

    
    /**
     * Lisää saalismerkinnän
     * @param saalis Lisättävä saalismerkintä
     */
    public void lisaa(Saalis saalis) {
        saaliit.lisaa(saalis);
    }

    
    /**
     * Palauttaa i:n merkinnän
     * @param i monesko merkintä palautetaan
     * @return viite i:teen merkintään
     * @throws IndexOutOfBoundsException jos i väärin
     */
    public Merkinta annaMerkinta(int i) throws IndexOutOfBoundsException {
        return merkinnat.anna(i);
    }
    
    
    /**
     * Palauttaa taulukossa hakuehtoa vastaavien päivämäärämerkintöjen indeksit
     * @param hakuehto Hakuehto
     * @param k Etsittävän kentän indeksi
     * @return Tietorakenne hakuehdot täyttävistä merkinnöistä
     */
    public Collection<Merkinta> etsi(String hakuehto, int k) { 
        return merkinnat.etsi(hakuehto, k); 
    } 


    /**
     * Haetaan päivämäärämerkinnän alla olevat saalismerkinnät
     * @param merkinta Päivämäärämerkintä, jonka saaliit haetaan 
     * @return Lista, jossa saaliiden tunnusnumerot
     * @example
     * <pre name="test">
     * #import java.util.*;
     * 
     *  Saalispaivakirja pk = new Saalispaivakirja();
     *  Merkinta m1 = new Merkinta(), m2 = new Merkinta(), m3 = new Merkinta();
     *  m1.rekisteroi(); m2.rekisteroi(); m3.rekisteroi();
     *  int id1 = m1.getTunnusNro();
     *  int id2 = m2.getTunnusNro();
     *  Saalis hauki1 = new Saalis(id1); pk.lisaa(hauki1);
     *  Saalis hauki2 = new Saalis(id1); pk.lisaa(hauki2);
     *  Saalis hauki3 = new Saalis(id2); pk.lisaa(hauki3);
     *  Saalis hauki4 = new Saalis(id2); pk.lisaa(hauki4);
     *  Saalis hauki5 = new Saalis(id2); pk.lisaa(hauki5);
     *  
     *  List<Saalis> loytyneet;
     *  loytyneet = pk.annaSaaliit(m3);
     *  loytyneet.size() === 0; 
     *  loytyneet = pk.annaSaaliit(m1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == hauki1 === true;
     *  loytyneet.get(1) == hauki2 === true;
     *  loytyneet = pk.annaSaaliit(m2);
     *  loytyneet.size() === 3; 
     *  loytyneet.get(0) == hauki3 === true;
     * </pre> 
     */
    public List<Saalis> annaSaaliit(Merkinta merkinta) {
        return saaliit.annaSaaliit(merkinta.getTunnusNro());
    }

    
    /**
     * Asettaa tiedostojen perusnimet
     * @param nimi uusi nimi
     */
    public void setTiedosto(String nimi) {
        File dir = new File(nimi);
        dir.mkdirs();
        String hakemistonNimi = "";
        if ( !nimi.isEmpty() ) hakemistonNimi = nimi +"/";
        merkinnat.setTiedostonPerusNimi(hakemistonNimi + "merkinnat");
        saaliit.setTiedostonPerusNimi(hakemistonNimi + "saaliit");
    }


    /**
     * Lukee saalispaivakirjan tiedot tiedostosta
     * @param nimi jota käyteään lukemisessa
     * @throws SailoException jos lukeminen epäonnistuu
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.*;
     * #import java.util.*;
     *   
     *  String hakemisto = "h";
     *  File dir = new File(hakemisto);
     *  File ftied  = new File(hakemisto+"/merkinnat.dat");
     *  File fhtied = new File(hakemisto+"/saaliit.dat");
     *  dir.mkdir();  
     *  ftied.delete();
     *  fhtied.delete();
     *  saalispaivakirja = new Saalispaivakirja();
     *  saalispaivakirja.lueTiedostosta(hakemisto); #THROWS SailoException
     *  alustaSaalispaivakirja();
     *  saalispaivakirja.setTiedosto(hakemisto); 
     *  saalispaivakirja.tallenna(); 
     *  saalispaivakirja = new Saalispaivakirja();
     *  saalispaivakirja.lueTiedostosta(hakemisto);
     *  Collection<Merkinta> kaikki = saalispaivakirja.etsi("",-1); 
     *  Iterator<Merkinta> it = kaikki.iterator();
     *  it.next() === m1;
     *  it.next() === m2;
     *  it.hasNext() === false;
     *  List<Saalis> loytyneet = saalispaivakirja.annaSaaliit(m1);
     *  Iterator<Saalis> ih = loytyneet.iterator();
     *  ih.next() === hauki11;
     *  ih.next() === hauki12;
     *  ih.hasNext() === false;
     *  loytyneet = saalispaivakirja.annaSaaliit(m2);
     *  ih = loytyneet.iterator();
     *  ih.next() === hauki21;
     *  ih.next() === hauki22;
     *  ih.next() === hauki23;
     *  ih.hasNext() === false;
     *  saalispaivakirja.lisaa(m2);
     *  saalispaivakirja.lisaa(hauki23);
     *  saalispaivakirja.tallenna();
     *  ftied.delete()  === true;
     *  fhtied.delete() === true;
     *  File fbak = new File(hakemisto+"/merkinnat.bak");
     *  File fhbak = new File(hakemisto+"/saaliit.bak");
     *  fbak.delete() === true;
     *  fhbak.delete() === true;
     *  dir.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String nimi) throws SailoException {
        merkinnat = new Merkinnat(); 
        saaliit = new Saaliit();

        setTiedosto(nimi);
        merkinnat.lueTiedostosta();
        saaliit.lueTiedostosta();
    }
    
    
    /**
     * Tallettaa saalispaivakirjan tiedot tiedostoon
     * @throws SailoException jos tallettamisessa ongelmia
     */
    public void tallenna() throws SailoException {
        String virhe = "";
        try {
            merkinnat.tallenna();
        } catch ( SailoException ex ) {
            virhe = ex.getMessage();
        }

        try {
            saaliit.tallenna();
        } catch ( SailoException ex ) {
            virhe += ex.getMessage();
        }
        if ( !"".equals(virhe) ) throw new SailoException(virhe);
    }


    /**
     * Testiohjelma saalispaivakirjasta
     * @param args ei käytössä
     */
    public static void main(String args[]) {
        Saalispaivakirja saalispaivakirja = new Saalispaivakirja();

        try {
            
            Merkinta m1 = new Merkinta(), m2 = new Merkinta();
            m1.rekisteroi();
            m1.vastaaPvm();
            m2.rekisteroi();
            m2.vastaaPvm();

            saalispaivakirja.lisaa(m1);
            saalispaivakirja.lisaa(m2);
            
            int id1 = m1.getTunnusNro();
            int id2 = m2.getTunnusNro();
            
            Saalis hauki11 = new Saalis(id1);
            hauki11.vastaaHauki(id1);
            saalispaivakirja.lisaa(hauki11);
            Saalis hauki12 = new Saalis(id1);
            hauki12.vastaaHauki(id1);
            saalispaivakirja.lisaa(hauki12);
            Saalis hauki21 = new Saalis(id2);
            hauki21.vastaaHauki(id2);
            saalispaivakirja.lisaa(hauki21);
            Saalis hauki22 = new Saalis(id2);
            hauki22.vastaaHauki(id2);
            saalispaivakirja.lisaa(hauki22);
            Saalis hauki23 = new Saalis(id2);
            hauki23.vastaaHauki(id2);
            saalispaivakirja.lisaa(hauki23);



            System.out.println("============= Saalispäiväkirjan testitulostus =================");

            Collection<Merkinta> merkinnat = saalispaivakirja.etsi("", -1);
            int i = 0;
            for (Merkinta merkinta : merkinnat) {
                System.out.println("Merkintä indeksissä: " + i);
                merkinta.tulosta(System.out);
                i++;
            }

        } catch (SailoException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
