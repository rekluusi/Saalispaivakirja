package saalispaivakirja;

import java.util.*;
import java.io.*;
import fi.jyu.mit.ohj2.WildChars;

/**
 * Saalispäiväkirjan merkinnät. Pitää yllä päivämäärämerkintälistaa.
 * @author Riku
 * @version 1.4.2020
 *
 */
public class Merkinnat implements Iterable<Merkinta> {
    private static final int MAX_MERKINTOJA   = 5;
    private int              lkm           = 0;
    private Merkinta         alkiot[]      = new Merkinta[MAX_MERKINTOJA];
    private boolean muutettu = false;
    private String kokoNimi = "";
    private String tiedostonPerusNimi = "merkinnat";


    /**
     * Oletusmuodostaja
     */
    public Merkinnat() {
        //
    }


    /**
     * Lisää uuden merkinnän taulukkoon. 
     * @param merkinta Lisätävän merkinnän viite.
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * Merkinnat merkinnat = new Merkinnat();
     * Merkinta m1 = new Merkinta(), m2 = new Merkinta();
     * merkinnat.getLkm() === 0;
     * merkinnat.lisaa(m1); merkinnat.getLkm() === 1;
     * merkinnat.lisaa(m2); merkinnat.getLkm() === 2;
     * merkinnat.lisaa(m1); merkinnat.getLkm() === 3;
     * Iterator<Merkinta> it = merkinnat.iterator(); 
     * it.next() === m1;
     * it.next() === m2; 
     * merkinnat.lisaa(m1); merkinnat.getLkm() === 4;
     * merkinnat.lisaa(m1); merkinnat.getLkm() === 5;
     * </pre>
     */
    public void lisaa(Merkinta merkinta) {
        if (lkm >= alkiot.length) { 
            kasvataTaulukkoa();
        }
        alkiot[lkm] = merkinta;
        lkm++;
        muutettu = true;
    }
    
    /**
     * Korvaa merkinnän tietorakenteessa.
     * Etsitään samalla tunnusnumerolla oleva merkintä. Jos ei löydy, listään.
     * @param merkinta lisätäävän jäsenen viite. 
     * @throws SailoException jos tietorakenne on jo täynnä
     * <pre name="test">
     * #THROWS SailoException,CloneNotSupportedException
     * #PACKAGEIMPORT
     * Merkinnat merkinnat = new Merkinnat();
     * Merkinta m1 = new Merkinta(), m2 = new Merkinta();
     * m1.rekisteroi(); m2.rekisteroi();
     * merkinnat.getLkm() === 0;
     * merkinnat.korvaaTaiLisaa(m1); merkinnat.getLkm() === 1;
     * merkinnat.korvaaTaiLisaa(m2); merkinnat.getLkm() === 2;
     * Merkinta m3 = m1.clone();
     * m3.aseta(5, "Riku");
     * Iterator<Merkinta> it = merkinnat.iterator();
     * it.next() == m1 === true;
     * merkinnat.korvaaTaiLisaa(m3); merkinnat.getLkm() === 2;
     * it = merkinnat.iterator();
     * Merkinta m0 = it.next();
     * m0 === m3;
     * m0 == m3 === true;
     * m0 == m1 === false;
     * </pre>
     */
    public void korvaaTaiLisaa(Merkinta merkinta) throws SailoException {
        int id = merkinta.getTunnusNro();
        for (int i = 0; i < lkm; i++) {
            if ( alkiot[i].getTunnusNro() == id ) {
                alkiot[i] = merkinta;
                muutettu = true;
                return;
            }
        }
        lisaa(merkinta);
    }

    
    /**
     * Kasvattaa taulukon kokoa kymmenellä, jos taulukko täysi.
     */
    private void kasvataTaulukkoa() {
        alkiot = Arrays.copyOf(alkiot, alkiot.length+10);
    }


    /**
     * Palauttaa viitteen i:teen merkintään.
     * @param i monennenko merkinnän viite halutaan
     * @return viite merkintään, jonka indeksi on i
     * @throws IndexOutOfBoundsException jos i on taulukon ulkopuolella
     */
    protected Merkinta anna(int i) throws IndexOutOfBoundsException {
        if (i < 0 || lkm <= i)
            throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
        return alkiot[i];
    }

    
    /** 
     * Poistaa merkinnän jolla on valittu tunnusnumero  
     * @param id poistettavan merkinnän tunnusnumero 
     * @return 1 jos poistettiin, 0 jos ei löydy 
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Merkinnat merkinnat = new Merkinnat(); 
     * Merkinta m1 = new Merkinta(), m2 = new Merkinta(), m3 = new Merkinta(); 
     * m1.rekisteroi(); m2.rekisteroi(); m3.rekisteroi(); 
     * int id1 = m1.getTunnusNro(); 
     * merkinnat.lisaa(m1); merkinnat.lisaa(m2); merkinnat.lisaa(m3); 
     * merkinnat.poista(id1+1) === 1; 
     * merkinnat.annaId(id1+1) === null; merkinnat.getLkm() === 2; 
     * merkinnat.poista(id1) === 1; merkinnat.getLkm() === 1; 
     * merkinnat.poista(id1+3) === 0; merkinnat.getLkm() === 1; 
     * </pre> 
     *  
     */ 
    public int poista(int id) { 
        int ind = etsiId(id); 
        if (ind < 0) return 0; 
        lkm--; 
        for (int i = ind; i < lkm; i++) 
            alkiot[i] = alkiot[i + 1]; 
        alkiot[lkm] = null; 
        muutettu = true; 
        return 1; 
    } 


    /**
     * Lukee merkinnät tiedostosta. 
     * @param tiedosto tiedoston perusnimi
     * @throws SailoException jos lukeminen epäonnistuu
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #import java.io.File;
     * 
     *  Merkinnat merkinnat = new Merkinnat();
     *  Merkinta m1 = new Merkinta(), m2 = new Merkinta();
     *  m1.vastaaPvm();
     *  m2.vastaaPvm();
     *  String hakemisto = "testi";
     *  String tiedNimi = hakemisto+"/merkinnat";
     *  File ftied = new File(tiedNimi+".dat");
     *  File dir = new File(hakemisto);
     *  dir.mkdir();
     *  ftied.delete();
     *  merkinnat.lueTiedostosta(tiedNimi); #THROWS SailoException
     *  merkinnat.lisaa(m1);
     *  merkinnat.lisaa(m2);
     *  merkinnat.tallenna();
     *  merkinnat = new Merkinnat();          
     *  merkinnat.lueTiedostosta(tiedNimi);
     *  Iterator<Merkinta> i = merkinnat.iterator();
     *  i.next() === m1;
     *  i.next() === m2;
     *  i.hasNext() === false;
     *  merkinnat.lisaa(m2);
     *  merkinnat.tallenna();
     *  ftied.delete() === true;
     *  File fbak = new File(tiedNimi+".bak");
     *  fbak.delete() === true;
     *  dir.delete() === true;
     * </pre>
     */
    public void lueTiedostosta(String tiedosto) throws SailoException {
        setTiedostonPerusNimi(tiedosto);
        try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
            kokoNimi = fi.readLine();
            if ( kokoNimi == null ) throw new SailoException("Saalispäiväkirjan nimi puuttuu");
            String rivi = fi.readLine();
            if ( rivi == null ) throw new SailoException("Maksimikoko puuttuu");

            while ( (rivi = fi.readLine()) != null ) {
                rivi = rivi.trim();
                if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
                Merkinta merkinta = new Merkinta();
                merkinta.parse(rivi);
                lisaa(merkinta);
            }
            muutettu = false;
        } catch ( FileNotFoundException e ) {
            throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
        } catch ( IOException e ) {
            throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
        }

    }
    
    
    /** Lukee merkinnät tiedostosta
     * @throws SailoException Jos vikaa
     */
    public void lueTiedostosta() throws SailoException {
        lueTiedostosta(getTiedostonPerusNimi());
    }


    /**
     * Tallentaa päivämäärämerkinnät tiedostoon
     * @throws SailoException jos talletus epäonnistuu
     */
    public void tallenna() throws SailoException {
        if ( !muutettu ) return;

        File fbak = new File(getBakNimi());
        File ftiedosto = new File(getTiedostonNimi());
        fbak.delete(); 
        ftiedosto.renameTo(fbak);

        try ( PrintWriter fo = new PrintWriter(new FileWriter(ftiedosto.getCanonicalPath())) ) {
            fo.println(getKokoNimi());
            fo.println(alkiot.length);
            for (Merkinta merkinta : this) {
                fo.println(merkinta.toString());
            }
        } catch ( FileNotFoundException ex ) {
            throw new SailoException("Tiedosto " + ftiedosto.getName() + " ei aukea");
        } catch ( IOException ex ) {
            throw new SailoException("Tiedoston " + ftiedosto.getName() + " kirjoittamisessa ongelmia");
        }

        muutettu = false;
    }
    
    
    /**
     * @return Saalispaivakirjan koko nimi merkkijonona
     */
    public String getKokoNimi() {
        return kokoNimi;
    }

    
    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonPerusNimi() {
        return tiedostonPerusNimi;
    }


    /**
     * Asettaa tiedoston perusnimen ilman tarkenninta
     * @param nimi tallennustiedoston perusnimi
     */
    public void setTiedostonPerusNimi(String nimi) {
        tiedostonPerusNimi = nimi;
    }


    /**
     * Palauttaa tiedoston nimen, jota käytetään tallennukseen
     * @return tallennustiedoston nimi
     */
    public String getTiedostonNimi() {
        return getTiedostonPerusNimi() + ".dat";
    }


    /**
     * Palauttaa varakopiotiedoston nimen
     * @return varakopiotiedoston nimi
     */
    public String getBakNimi() {
        return tiedostonPerusNimi + ".bak";
    }
    
    
    /**
     * Luokka merkintöjen iteroimiseksi.
     * @example
     * <pre name="test">
     * #THROWS SailoException 
     * #PACKAGEIMPORT
     * #import java.util.*;
     * 
     * Merkinnat merkinnat = new Merkinnat();
     * Merkinta m1 = new Merkinta(), m2 = new Merkinta();
     * m1.rekisteroi(); m2.rekisteroi();
     *
     * merkinnat.lisaa(m1); 
     * merkinnat.lisaa(m2); 
     * merkinnat.lisaa(m1); 
     * 
     * StringBuilder ids = new StringBuilder(30);
     * for (Merkinta merkinta : merkinnat)   // Kokeillaan for-silmukan toimintaa
     *   ids.append(" "+merkinta.getTunnusNro());           
     * 
     * String tulos = " " + m1.getTunnusNro() + " " + m2.getTunnusNro() + " " + m1.getTunnusNro();
     * 
     * ids.toString() === tulos; 
     * 
     * ids = new StringBuilder(30);
     * for (Iterator<Merkinta>  i=merkinnat.iterator(); i.hasNext(); ) { // ja iteraattorin toimintaa
     *   Merkinta merkinta = i.next();
     *   ids.append(" "+merkinta.getTunnusNro());           
     * }
     * 
     * ids.toString() === tulos;
     * 
     * Iterator<Merkinta>  i=merkinnat.iterator();
     * i.next() == m1  === true;
     * i.next() == m2  === true;
     * i.next() == m1  === true;
     * i.next();  #THROWS NoSuchElementException 
     * </pre>
     */
    public class MerkinnatIterator implements Iterator<Merkinta> {
        private int kohdalla = 0;
       
        /**
         * Onko olemassa vielä seuraavaa merkintää
         * @see java.util.Iterator#hasNext()
         * @return true jos on vielä merkintöjä
         */
        @Override
        public boolean hasNext() {
            return kohdalla < getLkm();
        }

        
        /**
         * Annetaan seuraava merkintä
         * @return seuraava merkintä
         * @throws NoSuchElementException jos seuraava alkiota ei enää ole
         * @see java.util.Iterator#next()
         */
        @Override
        public Merkinta next() throws NoSuchElementException {
            if ( !hasNext() ) throw new NoSuchElementException("Ei ole enää alkioita");
            return anna(kohdalla++);
        }


        /** Ei toteutettu
         * @throws UnsupportedOperationException Heittää joka kerta
         * @see java.util.Iterator#remove()
         */
        @Override
        public void remove() throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Ei toteutettu");
        }
    }


    /**
     * Palautetaan iteraattori merkinnöistä.
     * @return Merkintöjen iteraattori
     */
    @Override
    public Iterator<Merkinta> iterator() {
        return new MerkinnatIterator();
    }


    /** 
     * Palauttaa "taulukossa" hakuehtoon vastaavien päivämäärämerkintöjen viitteet 
     * @param hakuehto Hakuehto 
     * @param k Etsittävän kentän indeksi  
     * @return Tietorakenteen löytyneistä merkinnöistä 
     * @example 
     * <pre name="test"> 
     * #THROWS SailoException  
     *   Merkinnat merkinnat = new Merkinnat(); 
     *   Merkinta m1 = new Merkinta(); m1.parse("1|29.1.2018|Mikitänjärvi|Aurinkoista|Matti ja Teppo"); 
     *   Merkinta m2 = new Merkinta(); m2.parse("2|7.7.2018|Mikitänjärvi|Sateista|Matti ja Seppo"); 
     *   Merkinta m3 = new Merkinta(); m3.parse("3|8.7.2018|Mikitänjärvi|Pilvistä|Teppo"); 
     *   Merkinta m4 = new Merkinta(); m4.parse("4|10.7.2019|Mikitänjärvi|Poutaa|Matti ja Seppo"); 
     *   Merkinta m5 = new Merkinta(); m5.parse("5|30.8.2019|Mikitänjärvi|Trombeja|Matti"); 
     *   merkinnat.lisaa(m1); merkinnat.lisaa(m2); merkinnat.lisaa(m3); merkinnat.lisaa(m4); merkinnat.lisaa(m5);
     * </pre> 
     */ 
    public Collection<Merkinta> etsi(String hakuehto, int k) { 
        String ehto = "*"; 
        if ( hakuehto != null && hakuehto.length() > 0 ) ehto = hakuehto; 
        int hk = k; 
        if ( hk < 0 ) hk = 0;

        List<Merkinta> loytyneet = new ArrayList<Merkinta>(); 
        for (Merkinta merkinta : this) { 
            if (WildChars.onkoSamat(merkinta.anna(hk), ehto)) loytyneet.add(merkinta); 
        } 
        Collections.sort(loytyneet, new Merkinta.Vertailija(hk)); 
        return loytyneet; 
    }

    
    /** 
     * Etsii merkinnän id:n perusteella 
     * @param id tunnusnumero, jonka mukaan etsitään 
     * @return merkintä, jolla etsittävä id tai null 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Merkinnat merkinnat = new Merkinnat(); 
     * Merkinta m1 = new Merkinta(), m2 = new Merkinta(), m3 = new Merkinta(); 
     * m1.rekisteroi(); m2.rekisteroi(); m3.rekisteroi(); 
     * int id1 = m1.getTunnusNro(); 
     * merkinnat.lisaa(m1); merkinnat.lisaa(m2); merkinnat.lisaa(m3); 
     * merkinnat.annaId(id1  ) == m1 === true; 
     * merkinnat.annaId(id1+1) == m2 === true; 
     * merkinnat.annaId(id1+2) == m3 === true; 
     * </pre> 
     */ 
    public Merkinta annaId(int id) { 
        for (Merkinta merkinta : this) { 
            if (id == merkinta.getTunnusNro()) return merkinta; 
        } 
        return null; 
    } 


    /** 
     * Etsii merkinnän id:n perusteella 
     * @param id tunnusnumero, jonka mukaan etsitään 
     * @return löytyneen merkinnän indeksi tai -1 jos ei löydy 
     * <pre name="test"> 
     * #THROWS SailoException  
     * Merkinnat merkinnat = new Merkinnat(); 
     * Merkinta m1 = new Merkinta(), m2 = new Merkinta(), m3 = new Merkinta(); 
     * m1.rekisteroi(); m2.rekisteroi(); m3.rekisteroi(); 
     * int id1 = m1.getTunnusNro(); 
     * merkinnat.lisaa(m1); merkinnat.lisaa(m2); merkinnat.lisaa(m3); 
     * merkinnat.etsiId(id1+1) === 1; 
     * merkinnat.etsiId(id1+2) === 2; 
     * </pre> 
     */ 
    public int etsiId(int id) { 
        for (int i = 0; i < lkm; i++) 
            if (id == alkiot[i].getTunnusNro()) return i; 
        return -1; 
    } 

    


    /**
     * Palauttaa saalispäiväkirjan päivämäärämerkintöjen lukumäärän
     * @return Päivämäärämerkintöjen lukumäärän
     */
    public int getLkm() {
        return lkm;
    }


    /**
     * Testiohjelma päivämäärämerkinnöille
     * @param args Ei käytössä
     */
    public static void main(String args[]) {
        Merkinnat merkinnat = new Merkinnat();

        Merkinta m1 = new Merkinta(), m2 = new Merkinta();
        m1.rekisteroi();
        m1.vastaaPvm();
        m2.rekisteroi();
        m2.vastaaPvm();

        merkinnat.lisaa(m1);
        merkinnat.lisaa(m2);

        System.out.println("============= Merkinnät testitulostus =================");

        int i = 0;
        for (Merkinta merkinta : merkinnat) { 
            System.out.println("Merkintä nro: " + i++);
            merkinta.tulosta(System.out);
        }
    }

}
