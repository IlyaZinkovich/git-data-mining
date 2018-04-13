package io.coupling.git.data.mining.repo;

import java.util.Set;

public class Commit {

  private final Set<Change> changes;

  public Commit(final Set<Change> changes) {
    this.changes = changes;
  }
}
