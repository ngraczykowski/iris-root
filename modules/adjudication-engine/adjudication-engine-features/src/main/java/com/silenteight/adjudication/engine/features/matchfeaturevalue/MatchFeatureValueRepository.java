package com.silenteight.adjudication.engine.features.matchfeaturevalue;

import org.springframework.data.repository.Repository;

interface MatchFeatureValueRepository extends Repository<MatchFeatureValue, MatchFeatureValueKey> {

  MatchFeatureValue save(MatchFeatureValue singleValue);

  void saveAll(Iterable<MatchFeatureValue> values);
}
