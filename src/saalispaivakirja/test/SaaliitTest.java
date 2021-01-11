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
 * @version 2020.04.29 20:22:54 // Generated by ComTest
 *
 */
@SuppressWarnings({ "all" })
public class SaaliitTest {


  // Generated by ComTest BEGIN
  /** 
   * testKorvaaTaiLisaa42 
   * @throws SailoException when error
   * @throws CloneNotSupportedException when error
   */
  @Test
  public void testKorvaaTaiLisaa42() throws SailoException,CloneNotSupportedException {    // Saaliit: 42
    Saaliit saaliit = new Saaliit(); 
    Saalis s1 = new Saalis(), s2 = new Saalis(); 
    s1.rekisteroi(); s2.rekisteroi(); 
    assertEquals("From: Saaliit line: 48", 0, saaliit.getLkm()); 
    saaliit.korvaaTaiLisaa(s1); assertEquals("From: Saaliit line: 49", 1, saaliit.getLkm()); 
    saaliit.korvaaTaiLisaa(s2); assertEquals("From: Saaliit line: 50", 2, saaliit.getLkm()); 
    Saalis s3 = s1.clone(); 
    s3.aseta(2,"Järvi"); 
    Iterator<Saalis> i2=saaliit.iterator(); 
    assertEquals("From: Saaliit line: 54", s1, i2.next()); 
    saaliit.korvaaTaiLisaa(s3); assertEquals("From: Saaliit line: 55", 2, saaliit.getLkm()); 
    i2=saaliit.iterator(); 
    Saalis s = i2.next(); 
    assertEquals("From: Saaliit line: 58", s3, s); 
    assertEquals("From: Saaliit line: 59", true, s == s3); 
    assertEquals("From: Saaliit line: 60", false, s == s1); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testPoista81 
   * @throws SailoException when error
   */
  @Test
  public void testPoista81() throws SailoException {    // Saaliit: 81
    Saaliit saaliit = new Saaliit(); 
    Saalis hauki11 = new Saalis(); hauki11.vastaaHauki(1); 
    Saalis hauki12 = new Saalis(); hauki12.vastaaHauki(1); 
    Saalis hauki21 = new Saalis(); hauki21.vastaaHauki(2); 
    Saalis hauki22 = new Saalis(); hauki22.vastaaHauki(2); 
    Saalis hauki23 = new Saalis(); hauki23.vastaaHauki(2); 
    saaliit.lisaa(hauki21); 
    saaliit.lisaa(hauki11); 
    saaliit.lisaa(hauki22); 
    saaliit.lisaa(hauki12); 
    assertEquals("From: Saaliit line: 94", false, saaliit.poista(hauki23)); assertEquals("From: Saaliit line: 94", 4, saaliit.getLkm()); 
    assertEquals("From: Saaliit line: 95", true, saaliit.poista(hauki11)); assertEquals("From: Saaliit line: 95", 3, saaliit.getLkm()); 
    List<Saalis> s = saaliit.annaSaaliit(1); 
    assertEquals("From: Saaliit line: 97", 1, s.size()); 
    assertEquals("From: Saaliit line: 98", hauki12, s.get(0)); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testPoistaMerkinnanSaaliit113 */
  @Test
  public void testPoistaMerkinnanSaaliit113() {    // Saaliit: 113
    Saaliit saaliit = new Saaliit(); 
    Saalis s21 = new Saalis(); s21.vastaaHauki(2); 
    Saalis s11 = new Saalis(); s11.vastaaHauki(1); 
    Saalis s22 = new Saalis(); s22.vastaaHauki(2); 
    Saalis s12 = new Saalis(); s12.vastaaHauki(1); 
    Saalis s23 = new Saalis(); s23.vastaaHauki(2); 
    saaliit.lisaa(s21); 
    saaliit.lisaa(s11); 
    saaliit.lisaa(s22); 
    saaliit.lisaa(s12); 
    saaliit.lisaa(s23); 
    assertEquals("From: Saaliit line: 125", 3, saaliit.poistaMerkinnanSaaliit(2)); assertEquals("From: Saaliit line: 125", 2, saaliit.getLkm()); 
    assertEquals("From: Saaliit line: 126", 0, saaliit.poistaMerkinnanSaaliit(3)); assertEquals("From: Saaliit line: 126", 2, saaliit.getLkm()); 
    List<Saalis> s = saaliit.annaSaaliit(2); 
    assertEquals("From: Saaliit line: 128", 0, s.size()); 
    s = saaliit.annaSaaliit(1); 
    assertEquals("From: Saaliit line: 130", s11, s.get(0)); 
    assertEquals("From: Saaliit line: 131", s12, s.get(1)); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** 
   * testLueTiedostosta153 
   * @throws SailoException when error
   */
  @Test
  public void testLueTiedostosta153() throws SailoException {    // Saaliit: 153
    Saaliit saaliit = new Saaliit(); 
    Saalis s21 = new Saalis(); s21.vastaaHauki(2); 
    Saalis s11 = new Saalis(); s11.vastaaHauki(1); 
    Saalis s22 = new Saalis(); s22.vastaaHauki(2); 
    Saalis s12 = new Saalis(); s12.vastaaHauki(1); 
    Saalis s23 = new Saalis(); s23.vastaaHauki(2); 
    String tiedNimi = ""; 
    File ftied = new File(tiedNimi+".dat"); 
    ftied.delete(); 
    try {
    saaliit.lueTiedostosta(tiedNimi); 
    fail("Saaliit: 165 Did not throw SailoException");
    } catch(SailoException _e_){ _e_.getMessage(); }
    saaliit.lisaa(s21); 
    saaliit.lisaa(s11); 
    saaliit.lisaa(s22); 
    saaliit.lisaa(s12); 
    saaliit.lisaa(s23); 
    saaliit.tallenna(); 
    saaliit = new Saaliit(); 
    saaliit.lueTiedostosta(tiedNimi); 
    Iterator<Saalis> i = saaliit.iterator(); 
    assertEquals("From: Saaliit line: 175", s21.toString(), i.next().toString()); 
    assertEquals("From: Saaliit line: 176", s11.toString(), i.next().toString()); 
    assertEquals("From: Saaliit line: 177", s22.toString(), i.next().toString()); 
    assertEquals("From: Saaliit line: 178", s12.toString(), i.next().toString()); 
    assertEquals("From: Saaliit line: 179", s23.toString(), i.next().toString()); 
    assertEquals("From: Saaliit line: 180", false, i.hasNext()); 
    saaliit.lisaa(s23); 
    saaliit.tallenna(); 
    assertEquals("From: Saaliit line: 183", true, ftied.delete()); 
    File fbak = new File(tiedNimi+".bak"); 
    assertEquals("From: Saaliit line: 185", true, fbak.delete()); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testIterator294 */
  @Test
  public void testIterator294() {    // Saaliit: 294
    Saaliit saaliit = new Saaliit(); 
    Saalis hauki21 = new Saalis(2); saaliit.lisaa(hauki21); 
    Saalis hauki11 = new Saalis(1); saaliit.lisaa(hauki11); 
    Saalis hauki22 = new Saalis(2); saaliit.lisaa(hauki22); 
    Saalis hauki12 = new Saalis(1); saaliit.lisaa(hauki12); 
    Saalis hauki23 = new Saalis(2); saaliit.lisaa(hauki23); 
    Iterator<Saalis> i2=saaliit.iterator(); 
    assertEquals("From: Saaliit line: 306", hauki21, i2.next()); 
    assertEquals("From: Saaliit line: 307", hauki11, i2.next()); 
    assertEquals("From: Saaliit line: 308", hauki22, i2.next()); 
    assertEquals("From: Saaliit line: 309", hauki12, i2.next()); 
    assertEquals("From: Saaliit line: 310", hauki23, i2.next()); 
    try {
    assertEquals("From: Saaliit line: 311", hauki12, i2.next()); 
    fail("Saaliit: 311 Did not throw NoSuchElementException");
    } catch(NoSuchElementException _e_){ _e_.getMessage(); }
    int n = 0; 
    int jnrot[] = { 2,1,2,1,2} ; 
    for ( Saalis saalis : saaliit ) {
    assertEquals("From: Saaliit line: 317", jnrot[n], saalis.getMerkintaNro()); n++; 
    }
    assertEquals("From: Saaliit line: 320", 5, n); 
  } // Generated by ComTest END


  // Generated by ComTest BEGIN
  /** testAnnaSaaliit335 */
  @Test
  public void testAnnaSaaliit335() {    // Saaliit: 335
    Saaliit saaliit = new Saaliit(); 
    Saalis hauki21 = new Saalis(2); saaliit.lisaa(hauki21); 
    Saalis hauki11 = new Saalis(1); saaliit.lisaa(hauki11); 
    Saalis hauki22 = new Saalis(2); saaliit.lisaa(hauki22); 
    Saalis hauki12 = new Saalis(1); saaliit.lisaa(hauki12); 
    Saalis hauki23 = new Saalis(2); saaliit.lisaa(hauki23); 
    Saalis hauki51 = new Saalis(5); saaliit.lisaa(hauki51); 
    List<Saalis> loytyneet; 
    loytyneet = saaliit.annaSaaliit(3); 
    assertEquals("From: Saaliit line: 348", 0, loytyneet.size()); 
    loytyneet = saaliit.annaSaaliit(1); 
    assertEquals("From: Saaliit line: 350", 2, loytyneet.size()); 
    assertEquals("From: Saaliit line: 351", true, loytyneet.get(0) == hauki11); 
    assertEquals("From: Saaliit line: 352", true, loytyneet.get(1) == hauki12); 
    loytyneet = saaliit.annaSaaliit(5); 
    assertEquals("From: Saaliit line: 354", 1, loytyneet.size()); 
    assertEquals("From: Saaliit line: 355", true, loytyneet.get(0) == hauki51); 
  } // Generated by ComTest END
}