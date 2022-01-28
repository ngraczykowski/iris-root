package com.silenteight.payments.bridge.firco.datasource.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;
import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.datasource.categories.api.v2.CreateCategoryValuesRequest;
import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.datasource.agent.port.CreateAgentInputsClient;
import com.silenteight.payments.bridge.datasource.category.port.CreateCategoryValuesClient;
import com.silenteight.payments.bridge.firco.datasource.model.FeatureInputUnstructuredModel;
import com.silenteight.payments.bridge.firco.datasource.port.CreateDatasourceInputsUseCase;
import com.silenteight.payments.bridge.firco.datasource.service.process.agent.CreateFeatureInput;
import com.silenteight.payments.bridge.firco.datasource.service.process.category.CreateCategoryValue;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
class CreateDatasourceInputs implements CreateDatasourceInputsUseCase {

  private final CreateFeatureInput createFeatureInput;
  private final CreateAgentInputsClient createAgentInputsClient;
  private final CreateCategoryValue createCategoryValue;
  private final CreateCategoryValuesClient createCategoryValuesClient;

  @Override
  public void processStructured(AeAlert alert, List<HitData> hitsData) {
    var agentInputs = createStructuredFeatureInputs(alert, hitsData);
    createAgentInputsInDatasource(agentInputs);

    var categoryValuesRequests =
        createStructuredCategoryValues(alert, hitsData);
    createCategoryValuesInDatasource(categoryValuesRequests);
  }

  private List<CreateCategoryValuesRequest> createStructuredCategoryValues(
      AeAlert alert, List<HitData> hitsData) {
    return createCategoryValue
        .createStructuredCategoryValues(alert, hitsData)
        .stream()
        .map(CreateDatasourceInputs::createCategoryValuesRequest)
        .collect(toList());
  }

  @Override
  public void processUnstructured(
      String alertName, Map<String, HitAndWatchlistPartyData> hitAndWatchlistPartyData) {
    var agentInputs = createUnstructuredFeatureInputs(alertName, hitAndWatchlistPartyData);
    createAgentInputsInDatasource(agentInputs);

    var categoryValuesRequests =
        createUnstructuredCategoryValues(alertName, hitAndWatchlistPartyData);
    createCategoryValuesInDatasource(categoryValuesRequests);

  }

  private List<CreateCategoryValuesRequest> createUnstructuredCategoryValues(
      String alertName, Map<String, HitAndWatchlistPartyData> hitAndWatchlistPartyData) {

    return hitAndWatchlistPartyData.entrySet().stream()
        .map(e -> createCategoryValue.createUnstructuredCategoryValues(alertName, e.getKey(),
            e.getValue()))
        .flatMap(Collection::stream)
        .map(CreateDatasourceInputs::createCategoryValuesRequest)
        .collect(toList());
  }

  private void createCategoryValuesInDatasource(
      List<CreateCategoryValuesRequest> categoryValuesRequests) {
    var batchCategoryValueRequest = createBatchCategoryValueRequest(categoryValuesRequests);
    createCategoryValuesClient.createCategoriesValues(batchCategoryValueRequest);
  }

  private List<AgentInput> createStructuredFeatureInputs(AeAlert alert, List<HitData> hitsData) {
    return createFeatureInput.createStructuredFeatureInputs(alert, hitsData);
  }

  private List<AgentInput> createUnstructuredFeatureInputs(
      String alertName, Map<String, HitAndWatchlistPartyData> hitAndWatchlistPartyData) {

    return hitAndWatchlistPartyData.entrySet().stream()
        .map(e -> getAgentInputsForUnstructured(FeatureInputUnstructuredModel.builder()
            .alertName(alertName)
            .matchName(e.getKey())
            .hitAndWatchlistPartyData(e.getValue())
            .build()))
        .flatMap(List::stream)
        .collect(toList());
  }

  private List<AgentInput> getAgentInputsForUnstructured(
      FeatureInputUnstructuredModel featureInputModel) {
    return createFeatureInput.createUnstructuredFeatureInputs(featureInputModel);
  }

  private void createAgentInputsInDatasource(List<AgentInput> agentInputs) {
    var batchInputRequest = createBatchFeatureInputRequest(agentInputs);
    createAgentInputsClient.createAgentInputs(batchInputRequest);
  }

  private static BatchCreateAgentInputsRequest createBatchFeatureInputRequest(
      List<AgentInput> agentInputs) {
    return BatchCreateAgentInputsRequest.newBuilder()
        .addAllAgentInputs(agentInputs)
        .build();
  }

  private static BatchCreateCategoryValuesRequest createBatchCategoryValueRequest(
      List<CreateCategoryValuesRequest> createCategoryValuesRequests) {
    return BatchCreateCategoryValuesRequest.newBuilder()
        .addAllRequests(createCategoryValuesRequests)
        .build();
  }

  private static CreateCategoryValuesRequest createCategoryValuesRequest(
      CategoryValue categoryValue) {
    return CreateCategoryValuesRequest
        .newBuilder()
        .setCategory(categoryValue.getName())
        .addCategoryValues(categoryValue)
        .build();
  }
}
