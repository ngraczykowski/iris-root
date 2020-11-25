package com.silenteight.serp.governance.branch;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import java.util.Optional;

class InMemoryBranchRepository extends BasicInMemoryRepository<Branch>
    implements BranchRepository {

  @Override
  public Optional<Branch> findByDecisionTreeIdAndFeatureVectorId(long treeId, long vectorId) {
    return getInternalStore().values()
                             .stream()
                             .filter(branch -> branch.getDecisionTreeId().equals(treeId))
                             .filter(branch -> branch.getFeatureVectorId().equals(vectorId))
                             .findAny();
  }
}
