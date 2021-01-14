package com.silenteight.serp.governance.policy.domain;

import com.silenteight.sep.base.common.support.persistence.BasicInMemoryRepository;

import org.testcontainers.shaded.org.apache.commons.lang.NotImplementedException;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class InMemoryStepRepository extends BasicInMemoryRepository<Step>
    implements StepRepository {

  @Override
  public Step getByStepId(UUID stepId) {
    return stream()
        .filter(step -> step.getStepId() == stepId)
        .findFirst()
        .orElseThrow();
  }

  @Override
  public Collection<Step> findAllByPolicyId(long policyId) {
    throw new NotImplementedException();
  }

  @Override
  public List<String> findAllOrderedStepsIdsByPolicyId(long policyId) {
    throw new NotImplementedException();
  }

  @Override
  public Long getIdByStepId(UUID stepId) {
    return getByStepId(stepId).getId();
  }
}
