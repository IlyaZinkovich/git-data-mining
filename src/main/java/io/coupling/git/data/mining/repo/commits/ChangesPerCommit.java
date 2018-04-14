package io.coupling.git.data.mining.repo.commits;

import com.google.common.collect.ImmutableMap;
import java.time.Instant;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class ChangesPerCommit {

  private final Instant timestamp;
  private final Set<ChangedFile> changes;

  ChangesPerCommit(final Set<ChangedFile> changes, final Instant timestamp) {
    this.changes = changes;
    this.timestamp = timestamp;
  }

  public Stream<Map<String, Object>> toChangedFileRelationsParameters() {
    return changes.stream()
        .flatMap(this::relationsWith)
        .distinct()
        .map(ChangedFilesRelation::toParameters);
  }

  private Stream<ChangedFilesRelation> relationsWith(final ChangedFile changedFile) {
    return changes.stream()
        .filter(otherChangedFile -> !changedFile.equals(otherChangedFile))
        .map(otherChangedFile ->
            new ChangedFilesRelation(changedFile, otherChangedFile, timestamp));
  }

  public Stream<Map<String, Object>> toChangedFilesParameters(final Predicate<String> pathFilter) {
    return changes.stream()
        .map(ChangedFile::path)
        .filter(pathFilter)
        .map(this::pathToParameters);
  }

  private Map<String, Object> pathToParameters(final String path) {
    return ImmutableMap.of("path", path);
  }
}
