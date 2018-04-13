package io.coupling.git.data.mining.repo;

import static java.util.stream.Collectors.toSet;

import java.util.List;
import java.util.Set;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.revwalk.RevCommit;

final class DiffWithParent {

  private final RevCommit commit;
  private final CommitTreeParserFactory factory;
  private final Git git;

  DiffWithParent(final RevCommit commit, final CommitTreeParserFactory factory, final Git git) {
    this.commit = commit;
    this.factory = factory;
    this.git = git;
  }

  public Set<Change> changes() {
    return diffEntries().stream()
        .map(DiffEntry::getNewPath)
        .distinct()
        .map(Change::new)
        .collect(toSet());
  }

  private List<DiffEntry> diffEntries() {
    try {
      return git.diff()
          .setNewTree(factory.commitTreeParser(commit))
          .setOldTree(factory.parentCommitTreeParser(commit))
          .call();
    } catch (GitAPIException e) {
      throw new DiffException(e);
    }
  }
}
