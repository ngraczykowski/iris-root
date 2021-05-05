package com.silenteight.serp.governance.policy.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.UUID;

interface FeatureLogicRepository extends Repository<FeatureLogic, Long> {

  @Query(value = "SELECT f.*"
      + "FROM governance_policy_step_feature_logic f "
      + "WHERE f.step_id = :stepId", nativeQuery = true)
  List<FeatureLogic> findAllLogicByStepId(long stepId);

  @Query(value = "SELECT m.name "
      + "FROM governance_policy_step_feature_logic f "
      + "JOIN governance_policy_step_match_condition m ON f.id = m.feature_logic_id "
      + "JOIN governance_policy_step s ON s.id = f.step_id "
      + "JOIN governance_policy p ON p.id = s.policy_id "
      + "WHERE p.policy_id = :policyId "
      + "GROUP BY m.name",
      nativeQuery = true)
  List<String> findAllDistinctFeaturesByPolicyId(UUID policyId);
}
