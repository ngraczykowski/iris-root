package com.silenteight.universaldatasource.app.feature.adapter.outgoing.jdbc;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput.AgentInput;
import com.silenteight.universaldatasource.app.feature.model.MatchFeatureOutput.MatchInput;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.type.MapType;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
class FeatureExtractor implements ResultSetExtractor<Integer> {

  private static final String AGENT_INPUT_FEATURE_KEY = "feature";
  private static final ObjectMapper OBJECT_MAPPER = JsonConversionHelper.INSTANCE.objectMapper();
  private static final MapType MAP_TYPE = JsonConversionHelper.INSTANCE
      .objectMapper()
      .getTypeFactory()
      .constructMapType(LinkedHashMap.class, String.class, ObjectNode.class);

  private final Consumer<MatchFeatureOutput> consumer;
  private final String agentInputType;
  private final int chunkSize;

  @Override
  public Integer extractData(ResultSet rs) throws SQLException {
    var rowNum = 0;

    List<MatchFeatureOutput.MatchInput> matchFeatureOutputs = new ArrayList<>();
    while (rs.next()) {
      rowNum++;

      var match = rs.getString(1);
      var agentInputAggregate = rs.getString(3);

      matchFeatureOutputs.add(MatchInput.builder()
          .match(match)
          .agentInputs(getListOfAgentInputObjects(agentInputAggregate))
          .build());

      if (chunkSize <= matchFeatureOutputs.size()) {
        consumeFeatureResponse(
            new MatchFeatureOutput(agentInputType, matchFeatureOutputs));
        matchFeatureOutputs.clear();
      }
    }

    if (!matchFeatureOutputs.isEmpty()) {
      consumeFeatureResponse(
          new MatchFeatureOutput(agentInputType, matchFeatureOutputs));
    }

    return rowNum;
  }

  private static List<AgentInput> getListOfAgentInputObjects(String agentInputAggregate) {
    var featureInputMap = agentInputAggregateToMap(agentInputAggregate);
    return featureInputMap.entrySet().stream()
        .map(a -> new AgentInput(a.getKey(), a.getValue()))
        .collect(Collectors.toList());
  }

  private static Map<String, ObjectNode> agentInputAggregateToMap(String agentInputAggregate) {
    try {
      return OBJECT_MAPPER.readValue(agentInputAggregate, MAP_TYPE);
    } catch (JsonProcessingException e) {
      throw new AggregateFeatureInputMappingException(e);
    }
  }

  private void consumeFeatureResponse(MatchFeatureOutput matchFeatureOutput) {
    try {
      consumer.accept(matchFeatureOutput);
    } catch (Exception e) {
      log.warn("Match feature output response callback failed, stopping", e);
    }
  }

  private static class AggregateFeatureInputMappingException extends RuntimeException {

    private static final long serialVersionUID = 1974986452958468981L;

    AggregateFeatureInputMappingException(Throwable cause) {
      super(
          "Unable to convert aggregate future input {feature,agentFeatureInput} to an object",
          cause);
    }
  }
}
