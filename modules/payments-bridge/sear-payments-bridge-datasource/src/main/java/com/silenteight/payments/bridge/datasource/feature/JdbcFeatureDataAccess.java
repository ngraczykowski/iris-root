package com.silenteight.payments.bridge.datasource.feature;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Repository;

import java.util.function.Consumer;

@Repository
@RequiredArgsConstructor
class JdbcFeatureDataAccess implements FeatureDataAccess {

  private final SelectFeaturesQuery selectFeaturesQuery;

  @Override
  public void saveFeature(Object feature) {
  }

  @Override
  public void streamFeatures(Consumer<String> consumer) {
    selectFeaturesQuery.execute();
  }
}
