package saalispaivakirja;

import java.util.*;
import java.io.*;

/**
 * Saalismerkinnät, osaa lisätä uusia saaliita päivämäärämerkinnän alaisuuteen
 * @author Riku
 * @version 1.4.2020
 *
 */
public class Saaliit implements Iterable<Saalis> {

    private boolean muutettu = false;
    private String tiedostonPerusNimi = "";
    private final List<Saalis> alkiot = new ArrayList<Saalis>();

    
    /**
     * Saaliiden alustaminen
     */
    public Saaliit() {
        // 
    }


    /**
     * Lisää uuden saaliin listaan. 
     * @param saalis Lisättävä saalis.
     */
    public void lisaa(Saalis saalis) {
        alkiot.add(saalis);
        muutettu = true;
    }
    
    /**
     * Korvaa Saaliin tietorakenteessa. 
     * Etsitään samalla tunnusnumerolla oleva saalis.  Jos ei löydy, lisätään.
     * @param saalis lisättävän saaliin viite. 
     * @throws SailoException jos tietorakenne on jo täynnä
     * @example
     * <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Saaliit saaliit = new Saaliit();
     * Saalis s1 = new Saalis(), s2 = new Saalis();
     * s1.rekisteroi(); s2.rekisteroi();
     * saaliit.getLkm() === 0;
     * saaliit.korvaaTaiLisaa(s1); saaliit.getLkm() === 1;
     * saaliit.korvaaTaiLisaa(s2); saaliit.getLkm() === 2;
     * Saalis s3 = s1.clone();
     * s3.aseta(2,"Järvi");
     * Iterator<Saalis> i2=saaliit.iterator();
     * i2.next() === s1;
     * saaliit.korvaaTaiLisaa(s3); saaliit.getLkm() === 2;
     * i2=saaliit.iterator();
     * Saalis s = i2.next();
     * s === s3;
     * s == s3 === true;
     * s == s1 === false;
     * </pre>
     */ 
    public void korvaaTaiLisaa(Saalis saalis) throws SailoException {
        int id = saalis.getTunnusNro();
        for (int i = 0; i < getLkm(); i++) {
            if (alkiot.get(i).getTunnusNro() == id) {
                alkiot.set(i, saalis);
                muutettu = true;
                return;
            }
        }
        lisaa(saalis);
    }


    /**
     * Poistaa valitun saaliin
     * @param saalis poistettava saalis
     * @return tosi jos löytyi poistettava tietue 
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     *  Saaliit saaliit = new Saaliit();
     *  Saalis hauki11 = new Saalis(); hauki11.vastaaHauki(1);
     *  Saalis hauki12 = new Saalis(); hauki12.vastaaHauki(1); 
     *  Saalis hauki21 = new Saalis(); hauki21.vastaaHauki(2);
     *  Saalis hauki22 = new Saalis(); hauki22.vastaaHauki(2);   
     *  Saalis hauki23 = new Saalis(); hauki23.vastaaHauki(2); 
     *  saaliit.lisaa(hauki21);
     *  saaliit.lisaa(hauki11);
     *  saaliit.lisaa(hauki22);
     *  saaliit.lisaa(hauki12);
     *  saaliit.poista(hauki23) === false ; saaliit.getLkm() === 4;
     *  saaliit.poista(hauki11) === true;   saaliit.getLkm() === 3;
     *  List<Saalis> s = saaliit.annaSaaliit(1);
     *  s.size() === 1; 
     *  s.get(0) === hauki12;
     * </pre>
     */
    public boolean poista(Saalis saalis) {
        boolean ret = alkiot.remove(saalis);
        if (ret) muutettu = true;
        return ret;
    }

    
    /**
     * Poistaa kaikki tietyn tietyn merkinnän saaliit
     * @param tunnusNro viite siihen, mihin liittyvät tietueet poistetaan
     * @return montako poistettiin 
     * @example
     * <pre name="test">
     *  Saaliit saaliit = new Saaliit();
     *  Saalis s21 = new Saalis(); s21.vastaaHauki(2);
     *  Saalis s11 = new Saalis(); s11.vastaaHauki(1);
     *  Saalis s22 = new Saalis(); s22.vastaaHauki(2); 
     *  Saalis s12 = new Saalis(); s12.vastaaHauki(1); 
     *  Saalis s23 = new Saalis(); s23.vastaaHauki(2); 
     *  saaliit.lisaa(s21);
     *  saaliit.lisaa(s11);
     *  saaliit.lisaa(s22);
     *  saaliit.lisaa(s12);
     *  saaliit.lisaa(s23);
     *  saaliit.poistaMerkinnanSaaliit(2) === 3;  saaliit.getLkm() === 2;
     *  saaliit.poistaMerkinnanSaaliit(3) === 0;  saaliit.getLkm() === 2;
     *  List<Saalis> s = saaliit.annaSaaliit(2);
     *  s.size() === 0; 
     *  s = saaliit.annaSaaliit(1);
     *  s.get(0) === s11;
     *  s.get(1) === s12;
     * </pre>
     */
    public int poistaMerkinnanSaaliit(int tunnusNro) {
        int n = 0;
        for (Iterator<Saalis> it = alkiot.iterator(); it.hasNext();) {
            Saalis saalis = it.next();
            if ( saalis.getMerkintaNro() == tunnusNro ) {
                it.remove();
                n++;
            }
        }
        if (n > 0) muutettu = true;
        return n;
    }


    /**
     * Lukee saaliit tiedostosta.  
     * @param tiedosto Tiedoston hakemisto
     * @throws SailoException Jos tulee error
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     *  Saaliit saaliit = new Saaliit();
     *  Saalis s21 = new Saalis(); s21.vastaaHauki(2);
     *  Saalis s11 = new Saalis(); s11.vastaaHauki(1);
     *  Saalis s22 = new Saalis(); s22.vastaaHauki(2); 
     *  Saalis s12 = new Saalis(); s12.vastaaHauki(1); 
     *  Saalis s23 = new Saalis(); s23.vastaaHauki(2); 
     *  String tiedNimi = "";
     *  File ftied = new File(tiedNimi+".dat");
     *  ftied.delete();
     *  saaliit.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  saaliit.lisaa(s21);
     *  saaliit.lisaa(s11);
     *  saaliit.lisaa(s22);
     *  saaliit.lisaa(s12);
     *  saaliit.lisaa(s23);
     *  saaliit.tallenna();
     *  saaliit = new Saaliit();
     *  saaliit.lueTiedostosta(tiedNimi);
     *  Iterator<Saalis> i = saaliit.iterator();
     *  i.next().toString() === s21.toString();
     *  i.next().toString() === s11.toString();
     *  i.next().toString() === s22.toString();
     *  i.next().toString() === s12.toString();
     *  i.next().toString() === s23.toString();
     *  i.hasNext() === false;
     *  saaliit.lisaa(s23);
     *  saaliit.tallenna();
     *  ftied.delete() === true;
     *  File fbak = new File(tiedNimi+".bak");
     *  fbak.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String tiedosto) throws SailoException {
        setTiedostonPerusNimi(tiedosto);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {

            String rivi;
            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Saalis saalis = new Saalis();
                saalis.parse(rivi);
                lisaa(saalis);
            }
            muutettu = false;

        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }
    }


    /**
     * Luetaan aikaisemmin annetun nimisestä tiedostosta
     * @throws SailoException jos tulee poikkeus
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }


    /**
     * Tallentaa saaliit tiedostoon.  
     * @throws SailoException jos talletus epäonnistuu
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;

        File fbak = new File(getBakNimi());
        File ftied = new File(getTiedostonNimi());
        fbak.delete();
        ftied.renameTo(fbak);

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
            for (Saalis saalis : this) {
                fo.println(saalis.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedostoon " + ftied.getName() + " kirjoittamisessa ongelmia");
        }
        muutettu = false;
    }
    
    /**
     * Asettaa tiedoston perusnimen
     * @param tiedosto Tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String tiedosto) {
        tiedostonPerusNimi = tiedosto;
    }


    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return Tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }
    
    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return Tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return tiedostonPerusNimi + ".dat";
    }
    
    
    /**
     * Palauttaa backupin nimen
     * @return Backupin nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }



    /**
     * Palauttaa saaliiden lukumäärän
     * @return Saaliiden lukumäärä
     */
    public int getLkm() {
        return alkiot.size();
    }


    /**
     * Iteraattori kaikkien saaliiden läpikäymiseen
     * @return Iteraattori
     * 
     * @example
     * <pre name="test">
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     *  Saaliit saaliit = new Saaliit();
     *  Saalis hauki21 = new Saalis(2); saaliit.lisaa(hauki21);
     *  Saalis hauki11 = new Saalis(1); saaliit.lisaa(hauki11);
     *  Saalis hauki22 = new Saalis(2); saaliit.lisaa(hauki22);
     *  Saalis hauki12 = new Saalis(1); saaliit.lisaa(hauki12);
     *  Saalis hauki23 = new Saalis(2); saaliit.lisaa(hauki23);
     * 
     *  Iterator<Saalis> i2=saaliit.iterator();
     *  i2.next() === hauki21;
     *  i2.next() === hauki11;
     *  i2.next() === hauki22;
     *  i2.next() === hauki12;
     *  i2.next() === hauki23;
     *  i2.next() === hauki12;  #THROWS NoSuchElementException  
     *  
     *  int n = 0;
     *  int jnrot[] = {2,1,2,1,2};
     *  
     *  for ( Saalis saalis : saaliit ) { 
     *    saalis.getMerkintaNro() === jnrot[n]; n++;  
     *  }
     *  
     *  n === 5;
     *  
     * </pre>
     */
    @Override
    public Iterator<Saalis> iterator() {
        return alkiot.iterator();
    }


    /**
     * Haetaan kaikki päivämäärämerkinnän alaiset saaliit 
     * @param tunnusnro Päivämäärämerkinnän tunnusnumero jonka alaiset saaliit haetaan
     * @return tietorakenne Jossa viiteet löydetteyihin saalismerkintöihin
     * @example
     * <pre name="test">
     * #import java.util.*;
     * 
     *  Saaliit saaliit = new Saaliit();
     *  Saalis hauki21 = new Saalis(2); saaliit.lisaa(hauki21);
     *  Saalis hauki11 = new Saalis(1); saaliit.lisaa(hauki11);
     *  Saalis hauki22 = new Saalis(2); saaliit.lisaa(hauki22);
     *  Saalis hauki12 = new Saalis(1); saaliit.lisaa(hauki12);
     *  Saalis hauki23 = new Saalis(2); saaliit.lisaa(hauki23);
     *  Saalis hauki51 = new Saalis(5); saaliit.lisaa(hauki51);
     *  
     *  List<Saalis> loytyneet;
     *  loytyneet = saaliit.annaSaaliit(3);
     *  loytyneet.size() === 0; 
     *  loytyneet = saaliit.annaSaaliit(1);
     *  loytyneet.size() === 2; 
     *  loytyneet.get(0) == hauki11 === true;
     *  loytyneet.get(1) == hauki12 === true;
     *  loytyneet = saaliit.annaSaaliit(5);
     *  loytyneet.size() === 1; 
     *  loytyneet.get(0) == hauki51 === true;
     * </pre> 
     */
    public List<Saalis> annaSaaliit(int tunnusnro) {
        List<Saalis> loydetyt = new ArrayList<Saalis>();
        for (Saalis saalis : alkiot)
            if (saalis.getMerkintaNro() == tunnusnro) loydetyt.add(saalis);
        return loydetyt;
    }


    /**
     * Testiohjelma saaliiden tulostamiselle
     * @param args ei käytössä
     */
    public static void main(String[] args) {
        Saaliit saaliit = new Saaliit();
        
        Saalis hauki1 = new Saalis();
        hauki1.vastaaHauki(1);
        
        Saalis hauki2 = new Saalis();
        hauki2.vastaaHauki(2);
        
        Saalis hauki3 = new Saalis();
        hauki3.vastaaHauki(2);
        
        Saalis hauki4 = new Saalis();
        hauki4.vastaaHauki(2);
        
        Saalis hauki5 = new Saalis();
        hauki4.vastaaHauki(3);

        saaliit.lisaa(hauki1);
        saaliit.lisaa(hauki1);
        saaliit.lisaa(hauki2);
        saaliit.lisaa(hauki3);
        saaliit.lisaa(hauki4);
        saaliit.lisaa(hauki5);

        System.out.println("============= Saaliit testitulostus =================");

        List<Saalis> saaliit2 = saaliit.annaSaaliit(3);

        for (Saalis saalis : saaliit2) {
            System.out.print(saalis.getMerkintaNro() + " ");
            saalis.tulosta(System.out);
        }

    }

}
