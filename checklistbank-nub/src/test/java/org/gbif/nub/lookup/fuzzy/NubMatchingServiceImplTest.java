package org.gbif.nub.lookup.fuzzy;

import org.gbif.api.model.checklistbank.ParsedName;
import org.gbif.nub.lookup.fuzzy.NubMatchingServiceImpl;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NubMatchingServiceImplTest {

  @Test
  public void testInterpretGenus() throws Exception {
    ParsedName pn = new ParsedName();
    pn.setGenusOrAbove("P.");
    pn.setSpecificEpithet("concolor");
    NubMatchingServiceImpl.interpretGenus(pn, "Puma");
    assertEquals("Puma concolor", pn.canonicalName());


    pn.setGenusOrAbove("P.");
    NubMatchingServiceImpl.interpretGenus(pn, "Felis");
    assertEquals("P. concolor", pn.canonicalName());
  }

  @Test
  public void testNormConfidence2() throws Exception {
    for (int x=80; x<150; x++) {
      System.out.println(x + " -> " + NubMatchingServiceImpl.normConfidence(x));
    }
  }

  @Test
  public void testNormConfidence() throws Exception {
    assertEquals(0, NubMatchingServiceImpl.normConfidence(0));
    assertEquals(0, NubMatchingServiceImpl.normConfidence(-1));
    assertEquals(0, NubMatchingServiceImpl.normConfidence(-10000));
    assertEquals(1, NubMatchingServiceImpl.normConfidence(1));
    assertEquals(10, NubMatchingServiceImpl.normConfidence(10));
    assertEquals(20, NubMatchingServiceImpl.normConfidence(20));
    assertEquals(30, NubMatchingServiceImpl.normConfidence(30));
    assertEquals(50, NubMatchingServiceImpl.normConfidence(50));
    assertEquals(60, NubMatchingServiceImpl.normConfidence(60));
    assertEquals(70, NubMatchingServiceImpl.normConfidence(70));
    assertEquals(80, NubMatchingServiceImpl.normConfidence(80));
    assertEquals(85, NubMatchingServiceImpl.normConfidence(85));
    assertEquals(88, NubMatchingServiceImpl.normConfidence(90));
    assertEquals(89, NubMatchingServiceImpl.normConfidence(92));
    assertEquals(91, NubMatchingServiceImpl.normConfidence(95));
    assertEquals(92, NubMatchingServiceImpl.normConfidence(98));
    assertEquals(92, NubMatchingServiceImpl.normConfidence(99));
    assertEquals(93, NubMatchingServiceImpl.normConfidence(100));
    assertEquals(95, NubMatchingServiceImpl.normConfidence(105));
    assertEquals(96, NubMatchingServiceImpl.normConfidence(110));
    assertEquals(97, NubMatchingServiceImpl.normConfidence(115));
    assertEquals(99, NubMatchingServiceImpl.normConfidence(120));
    assertEquals(100, NubMatchingServiceImpl.normConfidence(125));
    assertEquals(100, NubMatchingServiceImpl.normConfidence(130));
    assertEquals(100, NubMatchingServiceImpl.normConfidence(150));
    assertEquals(100, NubMatchingServiceImpl.normConfidence(175));
    assertEquals(100, NubMatchingServiceImpl.normConfidence(200));
    assertEquals(100, NubMatchingServiceImpl.normConfidence(1000));
  }

}
