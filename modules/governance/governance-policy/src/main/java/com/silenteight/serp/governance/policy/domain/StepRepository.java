package com.silenteight.serp.governance.policy.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

interface StepRepository extends JpaRepository<Step, Long> {

  @Query(value = "SELECT s.*"
      + " FROM governance_policy_step s"
      + " WHERE s.policy_id = :policyId"
      + " ORDER BY s.sort_order", nativeQuery = true)
  Collection<Step> findAllOrderedByPolicyId(long policyId);

  @Query(value = "SELECT s.id"
      + " FROM governance_policy_step_feature_logic fl"
      + " JOIN governance_policy_step s ON s.id = fl.step_id"
      + " JOIN governance_policy p ON s.policy_id = p.id"
      + " AND CAST(p.policy_id AS VARCHAR)= :policyUuid"
      + " JOIN governance_policy_step_match_condition mc ON fl.id = mc.feature_logic_id"
      + " WHERE mc.name IN :names", nativeQuery = true)
  Collection<Long> findByPolicyUuidAndConditionName(String policyUuid, Collection<String> names);

  @Query(value = "SELECT cast(s.step_id as VARCHAR)"
      + " FROM governance_policy_step s"
      + " WHERE s.policy_id = :policyId"
      + " ORDER BY s.sort_order", nativeQuery = true)
  List<String> findAllOrderedStepsIdsByPolicyId(long policyId);

  @Query("select s.id from Step s where s.stepId = :stepId")
  Long getIdByStepId(UUID stepId);

  @Query(value = "SELECT s.policy_id"
      + " FROM governance_policy_step s"
      + " WHERE s.step_id = :stepId", nativeQuery = true)
  Long getPolicyIdForStep(UUID stepId);

  @Query(value = "SELECT count(*) FROM governance_policy_step s WHERE s.policy_id = :policyId",
      nativeQuery = true)
  Long countStepsByPolicyId(long policyId);

  Step getStepByStepId(UUID stepId);
}
