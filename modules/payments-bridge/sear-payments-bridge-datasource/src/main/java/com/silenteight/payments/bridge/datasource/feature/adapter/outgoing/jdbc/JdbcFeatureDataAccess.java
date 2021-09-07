package com.silenteight.payments.bridge.datasource.feature.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.datasource.feature.model.MatchFeatureInput;
import com.silenteight.payments.bridge.datasource.feature.model.MatchFeatureOutput;
import com.silenteight.payments.bridge.datasource.feature.port.outgoing.FeatureDataAccess;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Repository
class JdbcFeatureDataAccess implements FeatureDataAccess {

  private final StreamFeaturesQuery streamFeaturesQuery;
  private final InsertFeatureQuery insertFeatureQuery;

  @Override
  @Transactional
  public void saveAll(List<MatchFeatureInput> matchFeatureInputs) {
    insertFeatureQuery.execute(matchFeatureInputs);
  }

  @Override
  public int stream(
      Collection<String> matchNames, Collection<String> featureNames,
      Consumer<MatchFeatureOutput> consumer) {

    return streamFeaturesQuery.execute(matchNames, featureNames, consumer);
  }

}
