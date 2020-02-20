package com.silenteight.sens.webapp.backend.reasoningbranch;

import lombok.Value;

@Value(staticConstructor = "of")
public class BranchId {

  long treeId;
  long branchNo;
}
