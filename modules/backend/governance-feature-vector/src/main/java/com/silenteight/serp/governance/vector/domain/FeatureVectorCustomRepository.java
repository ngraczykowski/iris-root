package com.silenteight.serp.governance.vector.domain;

public interface FeatureVectorCustomRepository {

  void saveIfNotExist(FeatureVector featureVector);
}
