package com.silenteight.adjudication.engine.solve.agentconfigfeature.data;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.engine.solve.agentconfigfeature.AgentConfigFeatureDataAccess;
import com.silenteight.adjudication.engine.solve.agentconfigfeature.dto.AgentConfigFeatureDto;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.IntStream;

@RequiredArgsConstructor
@Repository
class JdbcAgentConfigFeatureDataAccess implements AgentConfigFeatureDataAccess {

  private final InsertAgentConfigFeatureQuery insertAgentConfigFeatureQuery;
  private final SelectAgentConfigFeatureQuery selectAgentConfigFeatureQuery;

  @Override
  public int addUnique(List<Feature> features) {
    features.forEach(acf -> insertAgentConfigFeatureQuery.execute(
        acf.getAgentConfig(), acf.getFeature()));

    var rowsAffected = insertAgentConfigFeatureQuery.flush();

    return IntStream.of(rowsAffected).sum();
  }

  @Override
  public List<AgentConfigFeatureDto> findAll(List<Feature> features) {
    return selectAgentConfigFeatureQuery.findAllByNamesIn(features);
  }
}
