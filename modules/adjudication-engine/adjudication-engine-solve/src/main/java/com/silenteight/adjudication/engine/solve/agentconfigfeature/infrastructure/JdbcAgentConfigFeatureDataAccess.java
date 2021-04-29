package com.silenteight.adjudication.engine.solve.agentconfigfeature.infrastructure;

import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.engine.solve.agentconfigfeature.AgentConfigFeatureDataAccess;
import com.silenteight.adjudication.engine.solve.agentconfigfeature.dto.AgentConfigFeatureName;
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
  public int addFeatures(List<AgentConfigFeatureName> names) {
    names.forEach(acf -> insertAgentConfigFeatureQuery.execute(
        acf.getAgentConfig(), acf.getFeature()));

    var rowsAffected = insertAgentConfigFeatureQuery.flush();

    return IntStream.of(rowsAffected).sum();
  }

  @Override
  public List<AgentConfigFeatureDto> getFeatures(List<AgentConfigFeatureName> names) {
    return selectAgentConfigFeatureQuery.findAllByNamesIn(names);
  }
}
