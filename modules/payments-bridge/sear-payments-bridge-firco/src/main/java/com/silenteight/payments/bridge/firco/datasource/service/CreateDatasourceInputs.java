package com.silenteight.payments.bridge.firco.datasource.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsRequest;
import com.silenteight.datasource.categories.api.v2.BatchCreateCategoryValuesRequest;
import com.silenteight.datasource.categories.api.v2.CategoryValue;
import com.silenteight.datasource.categories.api.v2.CreateCategoryValuesRequest;
import com.silenteight.payments.bridge.common.exception.MatchNotFoundException;
import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.datasource.agent.port.CreateAgentInputsClient;
import com.silenteight.payments.bridge.datasource.category.port.CreateCategoryValuesClient;
import com.silenteight.payments.bridge.firco.datasource.model.DatasourceUnstructuredModel;
import com.silenteight.payments.bridge.firco.datasource.port.CreateDatasourceInputsUseCase;
import com.silenteight.payments.bridge.firco.datasource.service.process.agent.CreateFeatureInput;
import com.silenteight.payments.bridge.firco.datasource.service.process.category.CreateCategoryValue;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
      AeAlert alert, Map<String, HitAndWatchlistPartyData> hitAndWatchlistPartyData) {
    var agentInputs = createUnstructuredFeatureInputs(alert, hitAndWatchlistPartyData);
    createAgentInputsInDatasource(agentInputs);

    var categoryValuesRequests =
        createUnstructuredCategoryValues(alert, hitAndWatchlistPartyData);
    createCategoryValuesInDatasource(categoryValuesRequests);
  }

  private List<CreateCategoryValuesRequest> createUnstructuredCategoryValues(
      AeAlert alert, Map<String, HitAndWatchlistPartyData> hitAndWatchlistPartyData) {

    return hitAndWatchlistPartyData.entrySet().stream()
        .map(entry -> createCategoryValue.createUnstructuredCategoryValues(
            createUnstructuredModel(alert, entry)))
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
      AeAlert alert, Map<String, HitAndWatchlistPartyData> hitAndWatchlistPartyData) {

    return hitAndWatchlistPartyData.entrySet().stream()
        .map(entry -> getAgentInputsForUnstructured(createUnstructuredModel(alert, entry)))
        .flatMap(List::stream)
        .collect(toList());
  }

  private static DatasourceUnstructuredModel createUnstructuredModel(
      AeAlert alert, Entry<String, HitAndWatchlistPartyData> hitAndWatchlistEntry) {
    return DatasourceUnstructuredModel.builder()
        .alertName(alert.getAlertName())
        .matchName(getMatchNameFromMathId(alert.getMatches(), hitAndWatchlistEntry.getKey()))
        .hitAndWatchlistPartyData(hitAndWatchlistEntry.getValue())
        .build();
  }

  private static String getMatchNameFromMathId(Map<String, String> matches, String matchId) {
    return matches.entrySet().stream()
        .filter(entry -> entry.getKey().equals(matchId))
        .map(Entry::getValue)
        .findFirst()
        .orElseThrow(
            () -> new MatchNotFoundException("No match name found for matchId: " + matchId));
  }

  private List<AgentInput> getAgentInputsForUnstructured(
      DatasourceUnstructuredModel featureInputModel) {
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
