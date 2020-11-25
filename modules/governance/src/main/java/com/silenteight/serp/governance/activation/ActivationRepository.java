package com.silenteight.serp.governance.activation;

import org.springframework.data.repository.Repository;

import java.util.Optional;

interface ActivationRepository extends Repository<Activation, Long> {

  Activation save(Activation entity);

  Optional<Activation> findByDecisionGroupId(long decisionGroupId);

  void deleteByDecisionTreeIdAndDecisionGroupId(long decisionTreeId, long decisionGroupId);
}
