package com.silenteight.adjudication.engine.solve;

import org.springframework.data.repository.Repository;

import java.util.stream.Stream;

interface MissingFeatureValueMatchesQueryRepository
    extends Repository<MissingFeatureValueMatchesQueryEntity, MissingFeatureValueMatchesKey> {

  Stream<MissingFeatureValueMatchesQueryEntity> findByIdFeatureAndIdAgentConfigAndIdPriority(
      String feature, String agentConfig, int priority);
}
