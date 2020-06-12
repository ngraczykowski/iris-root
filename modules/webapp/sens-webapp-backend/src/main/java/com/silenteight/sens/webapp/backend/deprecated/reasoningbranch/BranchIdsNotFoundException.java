package com.silenteight.sens.webapp.backend.deprecated.reasoningbranch;

import java.util.Collection;
import java.util.LinkedList;

public class BranchIdsNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -7221490231830576151L;

  private final Collection<Long> nonExistingBranchIds = new LinkedList<>();

  public BranchIdsNotFoundException(Throwable cause) {
    super(cause);
  }

  public BranchIdsNotFoundException(Collection<Long> nonExistingBranchIds) {
    this.nonExistingBranchIds.addAll(nonExistingBranchIds);
  }

  public Collection<Long> getNonExistingBranchIds() {
    return nonExistingBranchIds;
  }
}
