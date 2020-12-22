package com.silenteight.serp.governance.branch;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BranchDbFixturesService {

  private final BranchRepository branchRepository;

  public long storeBranch(BranchFixture branchFixture) {
    Branch branch = new Branch();
    branch.setDecisionTreeId(branchFixture.getDecisionTreeId());
    branch.setFeatureVectorId(branchFixture.getFeatureVectorId());
    branch.setSolution(branchFixture.getSolution());
    branch.setEnabled(branchFixture.isEnabled());
    branch.setLastUsedAt(branchFixture.getLastUsedAt());
    return branchRepository.save(branch).getId();
  }
}
