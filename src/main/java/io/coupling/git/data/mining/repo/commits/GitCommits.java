package io.coupling.git.data.mining.repo.commits;

import io.coupling.git.data.mining.repo.diff.GitDiff;
import io.coupling.git.data.mining.repo.log.GitLog;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import org.eclipse.jgit.diff.DiffEntry;

public class GitCommits {

  private final GitLog gitLog;
  private final GitDiff gitDiff;

  public GitCommits(final GitLog gitLog, final GitDiff gitDiff) {
    this.gitLog = gitLog;
    this.gitDiff = gitDiff;
  }

  public Stream<ChangesPerCommit> changesPerCommit() {
    return gitLog.stream().map(revCommit -> {
      final Instant timestamp = revCommit.getAuthorIdent().getWhen().toInstant();
      final List<DiffEntry> diffEntries = gitDiff.diffWithParent(revCommit);
      final Set<ChangedFile> changedFiles = new ChangedFiles(diffEntries).set();
      return new ChangesPerCommit(changedFiles, timestamp);
    });
  }
}
