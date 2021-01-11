package com.silenteight.serp.governance.policy.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

interface FeatureLogicRepository extends Repository<FeatureLogic, Long> {

  @Query(value = "SELECT f.*"
      + "FROM governance_policy_step_feature_logic f "
      + "WHERE f.step_id = :stepId", nativeQuery = true)
  List<FeatureLogic> findAllLogicByStepId(long stepId);
}
