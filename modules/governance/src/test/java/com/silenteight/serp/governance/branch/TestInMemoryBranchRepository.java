package com.silenteight.serp.governance.branch;

import java.util.Collection;

class TestInMemoryBranchRepository extends InMemoryBranchRepository {

  Collection<Branch> getAll() {
    return getInternalStore().values();
  }
}
