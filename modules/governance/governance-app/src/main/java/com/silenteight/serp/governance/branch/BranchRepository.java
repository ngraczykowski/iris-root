package com.silenteight.serp.governance.branch;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface BranchRepository extends Repository<Branch, Long> {

  Branch save(Branch branch);

  Optional<Branch> findByDecisionTreeIdAndFeatureVectorId(long treeId, long vectorId);
}
