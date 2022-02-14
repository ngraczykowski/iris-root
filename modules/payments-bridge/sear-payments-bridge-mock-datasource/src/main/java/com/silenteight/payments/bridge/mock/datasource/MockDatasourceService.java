package com.silenteight.payments.bridge.mock.datasource;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsResponse;
import com.silenteight.datasource.agentinput.api.v1.CreatedAgentInput;
import com.silenteight.datasource.categories.api.v2.*;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@Profile("mockdatasource")
public class MockDatasourceService {

  private final List<AgentInput> agentInputs = new ArrayList<>();

  private final List<CategoryValue> categoryValues = new ArrayList<>();

  public BatchCreateAgentInputsResponse createAgentInputsResponse(
      BatchCreateAgentInputsRequest request) {
    var inputs = request.getAgentInputsList();
    agentInputs.addAll(inputs);
    return BatchCreateAgentInputsResponse
        .newBuilder()
        .addAllCreatedAgentInputs(createFeatureInputs(inputs))
        .build();
  }

  private List<CreatedAgentInput> createFeatureInputs(List<AgentInput> inputs) {
    return inputs
        .stream()
        .map(i -> CreatedAgentInput
            .newBuilder()
            .setName(i.getName())
            .setMatch(i.getMatch())
            .build())
        .collect(
            toList());
  }

  public BatchCreateCategoryValuesResponse createCategoryValuesResponse(
      BatchCreateCategoryValuesRequest request) {
    var inputs = request
        .getRequestsList()
        .stream()
        .map(CreateCategoryValuesRequest::getCategoryValuesList)
        .flatMap(Collection::stream)
        .collect(toList());
    categoryValues.addAll(inputs);
    return BatchCreateCategoryValuesResponse
        .newBuilder()
        .addAllCreatedCategoryValues(createCategoryValues(inputs))
        .build();
  }

  private static List<CreatedCategoryValue> createCategoryValues(List<CategoryValue> inputs) {
    return inputs
        .stream()
        .map(i -> CreatedCategoryValue
            .newBuilder()
            .setMatch(i.getMatch())
            .setName(i.getName())
            .build())
        .collect(
            toList());
  }

  public long getCreatedFeatureInputsCount(String alertName) {
    return agentInputs
        .stream()
        .filter(agentInput -> agentInput.getAlert().equals(alertName))
        .map(AgentInput::getFeatureInputsList)
        .mapToLong(Collection::size)
        .sum();
  }

  public long getCreatedCategoryValuesCount(String alertName) {
    return categoryValues.stream()
        .filter(agentInput -> agentInput.getAlert().equals(alertName))
        .count();
  }
}
