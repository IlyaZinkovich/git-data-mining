package io.coupling.git.data.mining.repo.commits;

import com.google.common.collect.ImmutableMap;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;

public class ChangedFilesRelation {

  private final ChangedFile firstFile;
  private final ChangedFile secondFile;
  private final Instant timestamp;

  ChangedFilesRelation(final ChangedFile firstFile, final ChangedFile secondFile,
      final Instant timestamp) {
    this.firstFile = firstFile;
    this.secondFile = secondFile;
    this.timestamp = timestamp;
  }

  public Map<String, Object> toParameters() {
    return ImmutableMap.<String, Object>builder()
        .put("firstPath", firstFile.path())
        .put("secondPath", secondFile.path())
        .put("timestamp", timestamp.toString())
        .build();
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final ChangedFilesRelation that = (ChangedFilesRelation) o;
    final boolean equal = Objects.equals(firstFile, that.firstFile) &&
        Objects.equals(secondFile, that.secondFile) && Objects.equals(timestamp, that.timestamp);
    final boolean swappedEqual = Objects.equals(firstFile, that.secondFile) &&
        Objects.equals(secondFile, that.firstFile) && Objects.equals(timestamp, that.timestamp);
    return equal || swappedEqual;
  }

  @Override
  public int hashCode() {
    int hash;
    if (firstFile.path().compareTo(secondFile.path()) >= 0) {
      hash = Objects.hash(firstFile, secondFile, timestamp);
    } else {
      hash = Objects.hash(secondFile, firstFile, timestamp);
    }
    return hash;
  }
}
