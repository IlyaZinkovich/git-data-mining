package io.coupling.git.data.mining.repo;

import java.time.Instant;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;
import org.eclipse.jgit.revwalk.RevCommit;

class Commits {

  private final GitLog gitLog;

  Commits(final GitLog gitLog) {
    this.gitLog = gitLog;
  }

  Stream<Commit> stream(final Function<RevCommit, DiffWithParent> revCommitTransformer) {
    return gitLog.stream().map(revCommit -> {
      final Instant timestamp = revCommit.getAuthorIdent().getWhen().toInstant();
      final Set<ChangedFile> changes = revCommitTransformer.apply(revCommit).changes();
      return new Commit(changes, timestamp);
    });
  }
}
