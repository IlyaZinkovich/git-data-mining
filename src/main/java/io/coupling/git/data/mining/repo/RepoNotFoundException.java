package io.coupling.git.data.mining.repo;

final class RepoNotFoundException extends RuntimeException {

  RepoNotFoundException(final Throwable cause) {
    super(cause);
  }
}
