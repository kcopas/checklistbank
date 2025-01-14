package org.gbif.checklistbank.nub;

import static org.gbif.api.vocabulary.Kingdom.*;
import static org.gbif.api.vocabulary.Rank.*;
import org.gbif.nub.lookup.straight.IdLookup;
import org.gbif.nub.lookup.straight.IdLookupImpl;
import org.gbif.nub.lookup.straight.LookupUsage;

import java.util.Collection;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2IntArrayMap;
import it.unimi.dsi.fastutil.ints.Int2IntMap;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IdGeneratorTest {

  public static IdLookup newTestLookup() {
    Collection<LookupUsage> usages = Lists.newArrayList(
        new LookupUsage(1, "Animalia", null, null, KINGDOM, null, ANIMALIA, false),
        new LookupUsage(2, "Oenanthe", "Vieillot", "1816", GENUS, null, ANIMALIA, false),
        new LookupUsage(3, "Oenanthe", "Linnaeus", "1753", GENUS, null, PLANTAE, false),
        new LookupUsage(4, "Oenanthe aquatica", "Poir.", null, SPECIES, null, PLANTAE, false),
        new LookupUsage(5, "Oenanthe aquatica", "Senser", "1957", SPECIES, null, PLANTAE, false),
        new LookupUsage(6, "Oenanthe aquatica", null, null, SPECIES, null, PLANTAE, true),
        new LookupUsage(7, "Rodentia", "Bowdich", "1821", ORDER, null, ANIMALIA, false),
        new LookupUsage(8, "Rodentia", null, null, GENUS, null, ANIMALIA, true),
        new LookupUsage(9, "Abies alba", null, null, SPECIES, null, PLANTAE, false),
        new LookupUsage(10, "Abies alba", "Mumpf.", null, SPECIES, null, PLANTAE, true),
        new LookupUsage(11, "Abies alba", null, "1778", SPECIES, null, PLANTAE, true),
        new LookupUsage(12, "Picea alba", null, "1778", SPECIES, null, PLANTAE, true),
        new LookupUsage(13, "Picea", null, null, GENUS, null, PLANTAE, true),
        new LookupUsage(14, "Carex cayouettei", null, null, SPECIES, null, PLANTAE, true),
        new LookupUsage(15, "Carex comosa × Carex lupulina", null, null, SPECIES, null, PLANTAE, true),
        new LookupUsage(16, "Aeropyrum coil-shaped virus", null, null, UNRANKED, null, VIRUSES, true),

        new LookupUsage(17, map(100, 17, 110, -18, 111, 19),  "Admetidae", "Troschel", "1865", FAMILY, null, ANIMALIA, false),
        new LookupUsage(20, null, "Admetidae", null, null, FAMILY, null, ANIMALIA, true),
        new LookupUsage(5093664, map(5093663, 5093664, 1673124, -8431281, 1673124, 8710209),  "Bombylius scintillans", "Brunetti", "1909", SPECIES, null, ANIMALIA, false)
    );
    return IdLookupImpl.temp().load(usages);
  }

  /**
   * key, value, key, value, ...
   * pro parte maps have the parent usageKey as key, the pro parte usage key as value
   */
  private static Int2IntMap map(int ... kvs) {
    Int2IntMap m = new Int2IntArrayMap(kvs.length / 2);
    int idx = 0;
    while (idx < kvs.length) {
      m.put(kvs[idx], kvs[idx+1]);
      idx = idx + 2;
    }
    return m;
  }

  @Test
  public void testIssueId() throws Exception {
    IdGenerator gen = new IdGenerator(newTestLookup(), 1000);
    assertEquals(1000, gen.issue("Dracula", null, null, GENUS, null, PLANTAE));
    assertEquals(1, gen.issue("Animalia", null, null, KINGDOM, null, ANIMALIA));
    assertEquals(8, gen.issue("Rodentia", null, null, GENUS, null, ANIMALIA));
    // external issueing
    gen.reissue(14);
    // was issued already!
    assertEquals(1001, gen.issue("Carex cayouettei", null, null, SPECIES, null, PLANTAE));
    assertEquals(1002, gen.issue("Animalia", null, null, KINGDOM, null, ANIMALIA));
    assertEquals(1003, gen.issue("Carex cayouettei", null, null, SPECIES, null, PLANTAE));
  }

  @Test
  public void testProParte() throws Exception {
    IdGenerator gen = new IdGenerator(newTestLookup(), 1000);
    // wrong kingdom
    assertEquals(1000, gen.issue("Admetidae", null, null, FAMILY, null, PLANTAE));
    // regular canonical match
    assertEquals(20, gen.issue("Admetidae", null, null, FAMILY, null, ANIMALIA));
    assertEquals(1001, gen.issue("Admetidae", null, null, FAMILY, null, ANIMALIA));
    assertEquals(17, gen.issue("Admetidae", "Troschel", null, FAMILY, null, ANIMALIA));
    // pro parte matching
    assertEquals(1002, gen.issue("Admetidae", "Troschel", null, FAMILY, null, ANIMALIA, 100));
    // deleted, but reissued
    assertEquals(18, gen.issue("Admetidae", "Troschel", null, FAMILY, null, ANIMALIA, 110));
    assertEquals(19, gen.issue("Admetidae", "Troschel", null, FAMILY, null, ANIMALIA, 111));
    assertEquals(1003, gen.issue("Admetidae", "Troschel", null, FAMILY, null, ANIMALIA, 200));

    assertEquals(8710209, gen.issue("Bombylius scintillans", "Brunetti", "1909", SPECIES, null, ANIMALIA, 1673124));
    assertEquals(5093664, gen.issue("Bombylius scintillans", "Brunetti", "1909", SPECIES, null, ANIMALIA));
  }

}