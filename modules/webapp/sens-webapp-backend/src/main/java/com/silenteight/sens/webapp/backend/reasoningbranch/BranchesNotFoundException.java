package com.silenteight.sens.webapp.backend.reasoningbranch;

import java.util.Collection;
import java.util.LinkedList;

public class BranchesNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -7221490231830576151L;

  private final Collection<Long> nonExistingBranchIds = new LinkedList<>();

  public BranchesNotFoundException(Throwable cause) {
    super(cause);
  }

  public BranchesNotFoundException(Collection<Long> nonExistingBranchIds) {
    this.nonExistingBranchIds.addAll(nonExistingBranchIds);
  }

  public Collection<Long> getNonExistingBranchIds() {
    return nonExistingBranchIds;
  }
}
