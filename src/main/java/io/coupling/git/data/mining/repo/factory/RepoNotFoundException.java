package io.coupling.git.data.mining.repo.factory;

final class RepoNotFoundException extends RuntimeException {

  RepoNotFoundException(final Throwable cause) {
    super(cause);
  }
}
