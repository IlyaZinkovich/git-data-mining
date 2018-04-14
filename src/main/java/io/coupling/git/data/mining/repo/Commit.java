package io.coupling.git.data.mining.repo;

import static java.util.stream.Collectors.toSet;

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

  Set<ChangesPair> changesPairs() {
    return changes.stream()
        .flatMap(changedFile -> changes.stream()
            .filter(anotherChangedFile -> !changedFile.equals(anotherChangedFile))
            .map(anotherChangedFile -> new ChangesPair(changedFile, anotherChangedFile))
        ).collect(toSet());
  }

  @Override
  public String toString() {
    return changes.toString();
  }

  public Instant timestamp() {
    return timestamp;
  }
}
