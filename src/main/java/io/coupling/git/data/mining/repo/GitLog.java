package io.coupling.git.data.mining.repo;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.revwalk.RevCommit;

final class GitLog {

  private final Git git;

  GitLog(final Git git) {
    this.git = git;
  }

  Stream<RevCommit> stream() {
    try {
      final Iterable<RevCommit> commitIterable = git.log().call();
      final boolean notParallel = false;
      return StreamSupport.stream(commitIterable.spliterator(), notParallel);
    } catch (GitAPIException e) {
      throw new GitLogException(e);
    }
  }
}
