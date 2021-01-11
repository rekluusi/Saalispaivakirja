package kanta;

import static kanta.SisaltaaTarkistaja.*;

/**
 * Apuluokka kaikenlaisia tietokantaan liittyvien aliohjelmia varten
 * @author Riku
 * @version 1.4.2020
 *
 */
public class TietokantaApu  { 
    
    /**
     * Tarkistuksessa käytettävien kuukausien päivien lukumäärät
     */
    public static int[] KUUKAUDET = {31,29,31,30,31,30,31,31,30,31,30,31};
    
    /**
     * Pvm tarkistus
     * @param s joka tutkitaan.
     * @return null jos oikein, muuten virhettä kuvaava teksti
     * @example
     * <pre name="test">
     * #PACKAGEIMPORT
     * TietokantaApu pvmTarkistus = new TietokantaApu();
     * pvmTarkistus.tarkista("11")              === "Anna päivämäärä muodossa PP.KK.VVVV";
     * pvmTarkistus.tarkista("11.11.11111")     === "Anna päivämäärä muodossa PP.KK.VVVV";
     * pvmTarkistus.tarkista("11.13.2020")      === "Liian suuri kuukausi";
     * pvmTarkistus.tarkista("33.12.2020")      === "Liian suuri päivämäärä";
     * pvmTarkistus.tarkista("20.12.2020")      === null;
     * </pre>
     */  
    public String tarkista(String s) {
        int pituus = s.length();
        if ( pituus < 10 ) return "Anna päivämäärä muodossa PP.KK.VVVV";
        if ( pituus > 10 ) return "Anna päivämäärä muodossa PP.KK.VVVV";  
        int pv = Integer.parseInt(s.substring(0,2));
        int kk = Integer.parseInt(s.substring(3,5));
        if ( kk < 1 )  return "Liian pieni kuukausi";
        if ( 12 < kk ) return "Liian suuri kuukausi";
        int pvmkk = KUUKAUDET[kk-1];
        if ( pv < 1 )  return "Liian pieni päivämäärä";
        if ( pvmkk < pv ) return "Liian suuri päivämäärä";
        if ( !onkoVain(s,SALLITUT)) return "Käytä vain numeroita ja pisteitä"; 
        return null;
    }

    
    /**
     * Arvotaan satunnainen kokonaisluku välille [ala,yla]
     * @param ala arvonnan alaraja
     * @param yla arvonnan yläraja
     * @return satunnainen luku väliltä [ala,yla]
     */
    public static int rand(int ala, int yla) {
      double n = (yla-ala)*Math.random() + ala;
      return (int)Math.round(n);
    }

    
    /**
     * Arpoo päivämäärän ht5 vaiheessa  
     * @return Satunnainen päivämäärä
     */
    public static String arvoPvm() {
        String apupvm = String.format("%02d", rand(1,28)) + "." +
                        String.format("%02d", rand(1,12)) + "." +
                        String.format("%02d", rand(1990,2020));
        return apupvm;
    }
    
}
