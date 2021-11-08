package com.silenteight.universaldatasource.app.feature.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.CreatedAgentInput;
import com.silenteight.universaldatasource.app.feature.model.BatchFeatureRequest;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureInput;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput;
import com.silenteight.universaldatasource.app.feature.port.outgoing.FeatureDataAccess;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.Consumer;

@RequiredArgsConstructor
@Repository
class JdbcFeatureDataAccess implements FeatureDataAccess {

  private final StreamFeaturesQuery streamFeaturesQuery;
  private final InsertFeatureQuery insertFeatureQuery;
  private final UpdateAgentInputTypeQuery updateAgentInputTypeQuery;
  private final SelectOldAgentInputTypeQuery selectOldAgentInputTypeQuery;

  @Override
  @Transactional
  public List<CreatedAgentInput> saveAll(List<MatchFeatureInput> matchFeatureInputs) {
    return insertFeatureQuery.execute(matchFeatureInputs);
  }

  @Override
  public int stream(
      BatchFeatureRequest batchFeatureRequest, Consumer<MatchFeatureOutput> consumer) {
    return streamFeaturesQuery.execute(batchFeatureRequest, consumer);
  }

  @Override
  @Transactional
  public int updateAgentInputType() {
    return updateAgentInputTypeQuery.execute();
  }

  @Override
  public int isAgentInputTypeUpdated() {
    return selectOldAgentInputTypeQuery.execute();
  }
}
