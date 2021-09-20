package com.silenteight.universaldatasource.app.feature.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.CreatedAgentInput;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureInput;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput;
import com.silenteight.universaldatasource.app.feature.port.outgoing.FeatureDataAccess;

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
  public List<CreatedAgentInput> saveAll(List<MatchFeatureInput> matchFeatureInputs) {
    return insertFeatureQuery.execute(matchFeatureInputs);
  }

  @Override
  public int stream(
      Collection<String> matchNames, Collection<String> featureNames,
      Consumer<MatchFeatureOutput> consumer) {

    return streamFeaturesQuery.execute(matchNames, featureNames, consumer);
  }

}
