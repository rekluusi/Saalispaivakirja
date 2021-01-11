package kanta;


/**
 * Rajapinta tietueelle johon voidaan taulukon avulla rakentaa 
 * "attribuutit".
 * @author vesal
 * @version Mar 23, 2012
 * @example
 */
public interface Tietue {

    
    /**
     * @return tietueen kenttien lukumäärä
     * @example
     * <pre name="test">
     *   #import saalispaivakirja.Saalis;
     *   Saalis s = new Saalis();
     *   s.getKenttia() === 6;
     * </pre>
     */
    public abstract int getKenttia();


    /**
     * @return ensimmäinen käyttäjän syötettävän kentän indeksi
     * @example
     * <pre name="test">
     *   Saalis s = new Saalis();
     *   s.ekaKentta() === 2;
     * </pre>
     */
    public abstract int ekaKentta();


    /**
     * @param k minkä kentän kysymys halutaan
     * @return valitun kentän kysymysteksti
     * @example
     * <pre name="test">
     *   Saalis s = new Saalis();
     *   s.getKysymys(2) === "laji";
     * </pre>
     */
    public abstract String getKysymys(int k);


    /**
     * @param k Minkä kentän sisältö halutaan
     * @return valitun kentän sisältö
     * @example
     * <pre name="test">
     *   Saalis s = new Saalis();
     *   s.parse("   1   |  4  |   Hauki  | 7.5 | 56 | Verkolla  ");
     *   s.anna(0) === "1";   
     *   s.anna(1) === "4";   
     *   s.anna(2) === "Hauki";   
     *   s.anna(3) === "7.5";   
     *   s.anna(4) === "56"; 
     *   s.anna(5) === "Verkolla";  
     * </pre>
     */
    public abstract String anna(int k);

    
    /**
     * Asetetaan valitun kentän sisältö.  Mikäli asettaminen onnistuu,
     * palautetaan null, muutoin virheteksti.
     * @param k minkä kentän sisältö asetetaan
     * @param s asetettava sisältö merkkijonona
     * @return null jos ok, muuten virheteksti
     * @example
     * <pre name="test">
     *   Saalis saalis = new Saalis();
     *   saalis.aseta(2,"Hauki")  === null;
     *   saalis.aseta(4,"56")    === null;
     * </pre>
     */
    public abstract String aseta(int k, String s);


    /**
     * Tehdään identtinen klooni tietueesta
     * @return kloonattu tietue
     * @throws CloneNotSupportedException jos kloonausta ei tueta
     * @example
     * <pre name="test">
     * #THROWS CloneNotSupportedException 
     *   Saalis s = new Saalis();
     *   s.parse("   1   |  4  |   Hauki  | 7.5 | 56 | Verkolla  ");
     *   Object kopio = s.clone();
     *   kopio.toString() === s.toString();
     *   s.parse("   2   |  5  |   Hauki  | 6.5 | 72 | Lusikalla kaislikosta mökin läheltä  ");
     *   kopio.toString().equals(s.toString()) === false;
     *   kopio instanceof Saalis === true;
     * </pre>
     */
    public abstract Tietue clone() throws CloneNotSupportedException;


    /**
     * Palauttaa tietueen tiedot merkkijonona jonka voi tallentaa tiedostoon.
     * @return tietue tolppaeroteltuna merkkijonona 
     * @example
     * <pre name="test">
     *   Saalis s = new Saalis();
     *   s.parse("   1   |  4  |   Hauki  | 7.5 | 56 | Verkolla  ");
     *   s.toString()    =R= "1\\|4\\|Hauki\\|7.5\\|56\\|Verkolla.*";
     * </pre>
     */
    @Override
    public abstract String toString();

}
