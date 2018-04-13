package io.coupling.git.data.mining.repo;

import java.io.File;
import java.io.IOException;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

final class RepoFactory {

  Repository get(final String pathToRepo) {
    try {
      return new FileRepositoryBuilder()
          .setGitDir(new File(pathToRepo))
          .readEnvironment()
          .findGitDir()
          .build();
    } catch (final IOException e) {
      throw new RepoNotFoundException(e);
    }
  }
}
