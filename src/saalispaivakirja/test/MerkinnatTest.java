package saalispaivakirja.test;
// Generated by ComTest BEGIN
import saalispaivakirja.*;
import java.io.File;
import java.util.*;
import static org.junit.Assert.*;
import org.junit.*;
// Generated by ComTest END

/**
 * Test class made by ComTest
 * @version 2020.04.29 20:22:42 // Generated by ComTest
 *
 */
@SuppressWarnings({ "all" })
public class MerkinnatTest {


  // Generated by ComTest BEGIN
  /** 
   * testLisaa34 
   * @throws SailoException when error
   */
  @Test
  public void testLisaa34() throws SailoException {    // Merkinnat: 34
    Merkinnat merkinnat = new Merkinnat(); 
    Merkinta m1 = new Merkinta(), m2 = new Merkinta(); 
    assertEquals("From: Merkinnat line: 38", 0, merkinnat.getLkm()); 
    merkinnat.lisaa(m1); assertEquals("From: Merkinnat line: 39", 1, merkinnat.getLkm()); 
    merkinnat.lisaa(m2); assertEquals("From: Merkinnat line: 40", 2, merkinnat.getLkm()); 
    merkinnat.lisaa(m1); assertEquals("From: Merkinnat line: 41", 3, merkinnat.getLkm()); 
    Iterator<Merkinta> it = merkinnat.iterator(); 
    assertEquals("From: Merkinnat line: 43", m1, it.next()); 
    assertEquals("From: Merkinnat line: 44", m2, it.next()); 
    merkinnat.lisaa(m1); assertEquals("From: Merkinnat line: 45", 4, merkinnat.getLkm()); 
    merkinnat.lisaa(m1); assertEquals("From: Merkinnat line: 46", 5, merkinnat.getLkm()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testKorvaaTaiLisaa63 
   * @throws SailoException when error
   * @throws CloneNotSupportedException when error
   */
  @Test
  public void testKorvaaTaiLisaa63() throws SailoException,CloneNotSupportedException {    // Merkinnat: 63
    Merkinnat merkinnat = new Merkinnat(); 
    Merkinta m1 = new Merkinta(), m2 = new Merkinta(); 
    m1.rekisteroi(); m2.rekisteroi(); 
    assertEquals("From: Merkinnat line: 69", 0, merkinnat.getLkm()); 
    merkinnat.korvaaTaiLisaa(m1); assertEquals("From: Merkinnat line: 70", 1, merkinnat.getLkm()); 
    merkinnat.korvaaTaiLisaa(m2); assertEquals("From: Merkinnat line: 71", 2, merkinnat.getLkm()); 
    Merkinta m3 = m1.clone(); 
    m3.aseta(5, "Riku"); 
    Iterator<Merkinta> it = merkinnat.iterator(); 
    assertEquals("From: Merkinnat line: 75", true, it.next() == m1); 
    merkinnat.korvaaTaiLisaa(m3); assertEquals("From: Merkinnat line: 76", 2, merkinnat.getLkm()); 
    it = merkinnat.iterator(); 
    Merkinta m0 = it.next(); 
    assertEquals("From: Merkinnat line: 79", m3, m0); 
    assertEquals("From: Merkinnat line: 80", true, m0 == m3); 
    assertEquals("From: Merkinnat line: 81", false, m0 == m1); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testPoista123 
   * @throws SailoException when error
   */
  @Test
  public void testPoista123() throws SailoException {    // Merkinnat: 123
    Merkinnat merkinnat = new Merkinnat(); 
    Merkinta m1 = new Merkinta(), m2 = new Merkinta(), m3 = new Merkinta(); 
    m1.rekisteroi(); m2.rekisteroi(); m3.rekisteroi(); 
    int id1 = m1.getTunnusNro(); 
    merkinnat.lisaa(m1); merkinnat.lisaa(m2); merkinnat.lisaa(m3); 
    assertEquals("From: Merkinnat line: 130", 1, merkinnat.poista(id1+1)); 
    assertEquals("From: Merkinnat line: 131", null, merkinnat.annaId(id1+1)); assertEquals("From: Merkinnat line: 131", 2, merkinnat.getLkm()); 
    assertEquals("From: Merkinnat line: 132", 1, merkinnat.poista(id1)); assertEquals("From: Merkinnat line: 132", 1, merkinnat.getLkm()); 
    assertEquals("From: Merkinnat line: 133", 0, merkinnat.poista(id1+3)); assertEquals("From: Merkinnat line: 133", 1, merkinnat.getLkm()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testLueTiedostosta154 
   * @throws SailoException when error
   */
  @Test
  public void testLueTiedostosta154() throws SailoException {    // Merkinnat: 154
    Merkinnat merkinnat = new Merkinnat(); 
    Merkinta m1 = new Merkinta(), m2 = new Merkinta(); 
    m1.vastaaPvm(); 
    m2.vastaaPvm(); 
    String hakemisto = "testi"; 
    String tiedNimi = hakemisto+"/merkinnat"; 
    File ftied = new File(tiedNimi+".dat"); 
    File dir = new File(hakemisto); 
    dir.mkdir(); 
    ftied.delete(); 
    try {
    merkinnat.lueTiedostosta(tiedNimi); 
    fail("Merkinnat: 168 Did not throw SailoException");
    } catch(SailoException _e_){ _e_.getMessage(); }
    merkinnat.lisaa(m1); 
    merkinnat.lisaa(m2); 
    merkinnat.tallenna(); 
    merkinnat = new Merkinnat(); 
    merkinnat.lueTiedostosta(tiedNimi); 
    Iterator<Merkinta> i = merkinnat.iterator(); 
    assertEquals("From: Merkinnat line: 175", m1, i.next()); 
    assertEquals("From: Merkinnat line: 176", m2, i.next()); 
    assertEquals("From: Merkinnat line: 177", false, i.hasNext()); 
    merkinnat.lisaa(m2); 
    merkinnat.tallenna(); 
    assertEquals("From: Merkinnat line: 180", true, ftied.delete()); 
    File fbak = new File(tiedNimi+".bak"); 
    assertEquals("From: Merkinnat line: 182", true, fbak.delete()); 
    assertEquals("From: Merkinnat line: 183", true, dir.delete()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testMerkinnatIterator294 
   * @throws SailoException when error
   */
  @Test
  public void testMerkinnatIterator294() throws SailoException {    // Merkinnat: 294
    Merkinnat merkinnat = new Merkinnat(); 
    Merkinta m1 = new Merkinta(), m2 = new Merkinta(); 
    m1.rekisteroi(); m2.rekisteroi(); 
    merkinnat.lisaa(m1); 
    merkinnat.lisaa(m2); 
    merkinnat.lisaa(m1); 
    StringBuilder ids = new StringBuilder(30); 
    for (Merkinta merkinta : merkinnat) // Kokeillaan for-silmukan toimintaa
    ids.append(" "+merkinta.getTunnusNro()); 
    String tulos = " " + m1.getTunnusNro() + " " + m2.getTunnusNro() + " " + m1.getTunnusNro(); 
    assertEquals("From: Merkinnat line: 313", tulos, ids.toString()); 
    ids = new StringBuilder(30); 
    for (Iterator<Merkinta>  i=merkinnat.iterator(); i.hasNext(); ) { // ja iteraattorin toimintaa
    Merkinta merkinta = i.next(); 
    ids.append(" "+merkinta.getTunnusNro()); 
    }
    assertEquals("From: Merkinnat line: 321", tulos, ids.toString()); 
    Iterator<Merkinta>  i=merkinnat.iterator(); 
    assertEquals("From: Merkinnat line: 324", true, i.next() == m1); 
    assertEquals("From: Merkinnat line: 325", true, i.next() == m2); 
    assertEquals("From: Merkinnat line: 326", true, i.next() == m1); 
    try {
    i.next(); 
    fail("Merkinnat: 327 Did not throw NoSuchElementException");
    } catch(NoSuchElementException _e_){ _e_.getMessage(); }
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testEtsi384 
   * @throws SailoException when error
   */
  @Test
  public void testEtsi384() throws SailoException {    // Merkinnat: 384
    Merkinnat merkinnat = new Merkinnat(); 
    Merkinta m1 = new Merkinta(); m1.parse("1|29.1.2018|Mikitänjärvi|Aurinkoista|Matti ja Teppo"); 
    Merkinta m2 = new Merkinta(); m2.parse("2|7.7.2018|Mikitänjärvi|Sateista|Matti ja Seppo"); 
    Merkinta m3 = new Merkinta(); m3.parse("3|8.7.2018|Mikitänjärvi|Pilvistä|Teppo"); 
    Merkinta m4 = new Merkinta(); m4.parse("4|10.7.2019|Mikitänjärvi|Poutaa|Matti ja Seppo"); 
    Merkinta m5 = new Merkinta(); m5.parse("5|30.8.2019|Mikitänjärvi|Trombeja|Matti"); 
    merkinnat.lisaa(m1); merkinnat.lisaa(m2); merkinnat.lisaa(m3); merkinnat.lisaa(m4); merkinnat.lisaa(m5); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testAnnaId414 
   * @throws SailoException when error
   */
  @Test
  public void testAnnaId414() throws SailoException {    // Merkinnat: 414
    Merkinnat merkinnat = new Merkinnat(); 
    Merkinta m1 = new Merkinta(), m2 = new Merkinta(), m3 = new Merkinta(); 
    m1.rekisteroi(); m2.rekisteroi(); m3.rekisteroi(); 
    int id1 = m1.getTunnusNro(); 
    merkinnat.lisaa(m1); merkinnat.lisaa(m2); merkinnat.lisaa(m3); 
    assertEquals("From: Merkinnat line: 421", true, merkinnat.annaId(id1  ) == m1); 
    assertEquals("From: Merkinnat line: 422", true, merkinnat.annaId(id1+1) == m2); 
    assertEquals("From: Merkinnat line: 423", true, merkinnat.annaId(id1+2) == m3); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testEtsiId438 
   * @throws SailoException when error
   */
  @Test
  public void testEtsiId438() throws SailoException {    // Merkinnat: 438
    Merkinnat merkinnat = new Merkinnat(); 
    Merkinta m1 = new Merkinta(), m2 = new Merkinta(), m3 = new Merkinta(); 
    m1.rekisteroi(); m2.rekisteroi(); m3.rekisteroi(); 
    int id1 = m1.getTunnusNro(); 
    merkinnat.lisaa(m1); merkinnat.lisaa(m2); merkinnat.lisaa(m3); 
    assertEquals("From: Merkinnat line: 445", 1, merkinnat.etsiId(id1+1)); 
    assertEquals("From: Merkinnat line: 446", 2, merkinnat.etsiId(id1+2)); 
  } // Generated by ComTest END
}