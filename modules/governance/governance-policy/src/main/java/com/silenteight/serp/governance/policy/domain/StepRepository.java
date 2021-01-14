package com.silenteight.serp.governance.policy.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface StepRepository extends Repository<Step, Long> {

  Step save(Step step);

  Optional<Step> findById(long id);

  Step getByStepId(UUID stepId);

  @Query(value = "SELECT s.* "
      + "FROM governance_policy_step s "
      + "WHERE s.policy_id = :policyId", nativeQuery = true)
  Collection<Step> findAllByPolicyId(long policyId);

  @Query(value = "SELECT cast(s.step_id as VARCHAR) "
      + "FROM governance_policy_step s "
      + "WHERE s.policy_id = :policyId "
      + "ORDER BY s.sort_order", nativeQuery = true)
  List<String> findAllOrderedStepsIdsByPolicyId(long policyId);

  @Query("select s.id from Step s where s.stepId = :stepId")
  Long getIdByStepId(UUID stepId);
}
