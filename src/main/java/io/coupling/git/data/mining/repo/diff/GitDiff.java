package io.coupling.git.data.mining.repo.diff;

import java.util.List;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.revwalk.RevCommit;

public class GitDiff {

  private final CommitTreeParserFactory factory;
  private final Git git;

  public GitDiff(final CommitTreeParserFactory factory, final Git git) {
    this.factory = factory;
    this.git = git;
  }

  public List<DiffEntry> diffWithParent(final RevCommit commit) {
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
