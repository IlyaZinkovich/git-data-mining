package io.coupling.git.data.mining.repo.commits;

import java.util.Objects;

final class ChangedFile {

  private final String path;

  ChangedFile(final String path) {
    this.path = path;
  }

  String path() {
    return path;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChangedFile change = (ChangedFile) o;
    return Objects.equals(path, change.path);
  }

  @Override
  public int hashCode() {
    return Objects.hash(path);
  }

  @Override
  public String toString() {
    return path;
  }
}
