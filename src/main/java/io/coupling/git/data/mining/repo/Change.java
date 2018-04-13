package io.coupling.git.data.mining.repo;

import java.util.Objects;

public final class Change {

  private final String file;

  Change(final String file) {
    this.file = file;
  }

  public String file() {
    return file;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Change change = (Change) o;
    return Objects.equals(file, change.file);
  }

  @Override
  public int hashCode() {
    return Objects.hash(file);
  }
}
