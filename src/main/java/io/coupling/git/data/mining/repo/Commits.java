package io.coupling.git.data.mining.repo;

import io.coupling.git.data.mining.repo.diff.GitDiff;
import io.coupling.git.data.mining.repo.log.GitLog;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.eclipse.jgit.diff.DiffEntry;

public class Commits {

  private final GitLog gitLog;
  private final GitDiff gitDiff;

  Commits(final GitLog gitLog, final GitDiff gitDiff) {
    this.gitLog = gitLog;
    this.gitDiff = gitDiff;
  }

  public Stream<Commit> stream() {
    return gitLog.stream().map(revCommit -> {
      final Instant timestamp = revCommit.getAuthorIdent().getWhen().toInstant();
      final List<DiffEntry> diffEntries = gitDiff.diffWithParent(revCommit);
      final Set<ChangedFile> changes = new ChangedFiles(diffEntries).changes();
      return new Commit(changes, timestamp);
    });
  }
}
