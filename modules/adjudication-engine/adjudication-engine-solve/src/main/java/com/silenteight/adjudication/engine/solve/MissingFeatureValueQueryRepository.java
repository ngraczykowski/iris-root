package com.silenteight.adjudication.engine.solve;

import org.springframework.data.repository.Repository;

import java.util.stream.Stream;

interface MissingFeatureValueQueryRepository
    extends Repository<MissingFeatureValueQueryEntity, MissingFeatureValueKey> {

  Stream<MissingFeatureValueQueryEntity> findAllByOrderByIdPriorityDesc();
}
