package io.coupling.git.data.mining.repo;

import java.util.Objects;

class ChangesPair {

  private final ChangedFile firstFile;
  private final ChangedFile secondFile;

  ChangesPair(final ChangedFile firstFile, final ChangedFile secondFile) {
    this.firstFile = firstFile;
    this.secondFile = secondFile;
  }

  String firstPath() {
    return firstFile.path();
  }

  String secondPath() {
    return secondFile.path();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ChangesPair that = (ChangesPair) o;
    final boolean equal = Objects.equals(firstFile, that.firstFile) &&
        Objects.equals(secondFile, that.secondFile);
    final boolean swappedEqual = Objects.equals(firstFile, that.secondFile) &&
        Objects.equals(secondFile, that.firstFile);
    return equal || swappedEqual;
  }

  @Override
  public int hashCode() {
    if (firstFile.path().compareTo(secondFile.path()) >= 0) {
      return Objects.hash(firstFile, secondFile);
    } else {
      return Objects.hash(secondFile, firstFile);
    }
  }
}
