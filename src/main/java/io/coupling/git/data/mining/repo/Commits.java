package io.coupling.git.data.mining.repo;

import java.util.stream.Stream;
import org.eclipse.jgit.api.Git;

public class Commits {

  private final Git git;
  private final CommitTreeParserFactory parserFactory;

  Commits(final Git git, final CommitTreeParserFactory parserFactory) {
    this.git = git;
    this.parserFactory = parserFactory;
  }

  public Stream<Commit> stream() {
    return new GitLog(git).stream()
        .map(commit -> new DiffWithParent(commit, parserFactory, git))
        .map(DiffWithParent::changes)
        .map(Commit::new);
  }
}
