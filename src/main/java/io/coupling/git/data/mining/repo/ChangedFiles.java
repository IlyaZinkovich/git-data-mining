package io.coupling.git.data.mining.repo;

import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;
import org.eclipse.jgit.diff.DiffEntry;

public class ChangedFiles {

  private final List<DiffEntry> diffEntries;

  ChangedFiles(final List<DiffEntry> diffEntries) {
    this.diffEntries = diffEntries;
  }

  public Set<ChangedFile> changes() {
    return diffEntries.stream()
        .map(DiffEntry::getNewPath)
        .distinct()
        .map(ChangedFile::new)
        .collect(toSet());
  }
}
