package com.silenteight.adjudication.engine.features.agentconfigfeature.data;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.engine.features.agentconfigfeature.AgentConfigFeatureDataAccess;
import com.silenteight.adjudication.engine.features.agentconfigfeature.dto.AgentConfigFeatureDto;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
class JdbcAgentConfigFeatureDataAccess implements AgentConfigFeatureDataAccess {

  private final InsertAgentConfigFeatureQuery insertAgentConfigFeatureQuery;
  private final SelectAgentConfigFeatureQuery selectAgentConfigFeatureQuery;

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  public int addUnique(List<Feature> features) {
    return insertAgentConfigFeatureQuery.execute(features);
  }

  @Override
  @Timed(percentiles = { 0.5, 0.95, 0.99}, histogram = true)
  public List<AgentConfigFeatureDto> findAll(List<Feature> features) {
    return selectAgentConfigFeatureQuery.findAllByNamesIn(features);
  }
}
