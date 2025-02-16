package org.gbif.nub.lookup.straight;

import org.gbif.api.vocabulary.Rank;

import java.util.Collections;
import java.util.List;

import com.beust.jcommander.internal.Lists;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;
import static org.gbif.api.vocabulary.Kingdom.*;
import static org.gbif.api.vocabulary.TaxonomicStatus.*;

/**
 *
 */
public class LookupUsageTest {

  @Test
  public void testCompareTo() throws Exception {
    List<LookupUsage> usages = Lists.newArrayList();
    usages.add(new LookupUsage(443, "Abies", "Mill.", "1978", Rank.GENUS, ACCEPTED, PLANTAE, false));
    usages.add(new LookupUsage(13, "Abies milba", "Mill.", "1978", Rank.SPECIES, ACCEPTED, PLANTAE, false));
    usages.add(new LookupUsage(3, "Abies alba", "Mill.", "1978", Rank.SPECIES, ACCEPTED, PLANTAE, false));
    usages.add(new LookupUsage(5323, "Papaia", "L.", "1978", Rank.GENUS, ACCEPTED, ANIMALIA, false));
    usages.add(new LookupUsage(23, "Abia giganta", "Mill.", "1978", Rank.SPECIES, ACCEPTED, ANIMALIA, false));
    usages.add(new LookupUsage(113, "Keine AHnung", null, null, Rank.UNRANKED, ACCEPTED, INCERTAE_SEDIS, true));
    usages.add(new LookupUsage(88, "Keiner", null, null, Rank.SUPERFAMILY, ACCEPTED, INCERTAE_SEDIS, true));

    Collections.sort(usages);

    assertEquals(88, usages.get(0).getKey());
    assertEquals(5323, usages.get(1).getKey());
    assertEquals(443, usages.get(2).getKey());
    assertEquals(23, usages.get(3).getKey());
    assertEquals(3, usages.get(4).getKey());
    assertEquals(13, usages.get(5).getKey());
    assertEquals(113, usages.get(6).getKey());
  }
}