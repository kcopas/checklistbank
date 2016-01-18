package org.gbif.checklistbank.nub;

import org.gbif.api.vocabulary.Rank;
import org.gbif.checklistbank.neo.NeoProperties;
import org.gbif.checklistbank.neo.traverse.StartEndHandler;

import java.util.LinkedList;

import com.google.common.collect.Lists;
import org.neo4j.graphdb.Node;

/**
 * throws AssertionError when problems are encountered to fail fast.
 */
public class TreeRankValidation implements StartEndHandler {
  private final LinkedList<Rank> parents = Lists.newLinkedList();
  private boolean valid = true;

  @Override
  public void start(Node n) {
    Rank rank = NeoProperties.getRank(n, null);
    if (rank == null) {
      fail("Missing rank", n);
    }
    if (!parents.isEmpty()) {
      Rank parent = parents.getLast();
      if (!parent.higherThan(rank)) {
        fail("Rank "+rank+" is higher than parent rank "+parent, n);
      }
    }
    parents.add(rank);
  }

  @Override
  public void end(Node n) {
    parents.removeLast();
  }

  public boolean isValid() {
    return valid;
  }

  private void fail(String msg, Node n) {
    valid = false;
    throw new AssertionError(msg + " for node " + n.getId());
  }
}