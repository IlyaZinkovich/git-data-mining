package io.coupling.git.data.mining.repo;

import java.time.Instant;
import java.util.Set;

class Commit {

  private final Set<ChangedFile> changes;
  private final Instant timestamp;

  Commit(final Set<ChangedFile> changes, final Instant timestamp) {
    this.changes = changes;
    this.timestamp = timestamp;
  }

  Set<ChangedFile> changes() {
    return changes;
  }

  @Override
  public String toString() {
    return changes.toString();
  }

  public Instant timestamp() {
    return timestamp;
  }
}
