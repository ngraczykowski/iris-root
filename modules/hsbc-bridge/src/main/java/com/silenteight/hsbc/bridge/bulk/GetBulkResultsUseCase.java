package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.bulk.exception.BatchIdNotFoundException;
import com.silenteight.hsbc.bridge.bulk.exception.BatchProcessingNotCompletedException;
import com.silenteight.hsbc.bridge.bulk.rest.*;
import com.silenteight.hsbc.bridge.recommendation.GetRecommendationUseCase.GetRecommendationRequest;
import com.silenteight.hsbc.bridge.recommendation.RecommendationWithMetadataDto;
import com.silenteight.hsbc.bridge.recommendation.GetRecommendationUseCase;
import com.silenteight.hsbc.bridge.recommendation.metadata.RecommendationMetadata;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class GetBulkResultsUseCase {

  private final BulkRepository bulkRepository;
  private final GetRecommendationUseCase getRecommendationUseCase;
  private final AlertMetadataCollector alertMetadataCollector = new AlertMetadataCollector();

  public BatchSolvedAlerts getResults(String id) {
    var result = bulkRepository.findById(id);

    if (result.isEmpty()) {
      throw new BatchIdNotFoundException(id);
    }

    var batch = result.get();
    if (batch.isNotCompleted()) {
      throw new BatchProcessingNotCompletedException(id);
    }

    var response = new BatchSolvedAlerts();
    response.setBatchId(batch.getId());
    response.setBatchStatus(BatchStatus.valueOf(
        batch.getStatus().name()));
    response.setAlerts(getSolvedAlerts(batch.getValidAlerts()));
    return response;
  }

  private List<SolvedAlert> getSolvedAlerts(Collection<BulkAlertEntity> items) {
    return items.stream().map(this::getSolvedAlert).collect(Collectors.toList());
  }

  private SolvedAlert getSolvedAlert(BulkAlertEntity alert) {
    var solvedAlert = new SolvedAlert();
    var metadata = alert.getMetadata();
    solvedAlert.setAlertMetadata(map(metadata));
    solvedAlert.setId(alert.getExternalId());

    if (alert.isCompleted()) {
      var recommendation = getRecommendation(alert.getName(), metadata);
      solvedAlert.setRecommendation(recommendation.getRecommendedAction());
      solvedAlert.setComment(recommendation.getRecommendationComment());
      attachRecommendationMetadata(solvedAlert, recommendation.getMetadata());
    }
    return solvedAlert;
  }

  private void attachRecommendationMetadata(SolvedAlert alert, RecommendationMetadata metadata) {
    var recommendationAlertMetadata =
        alertMetadataCollector.collectFromRecommendationMetadata(metadata);

    alert.setFvSignature(findByKey("feature_vector_signature", recommendationAlertMetadata));
    alert.setPolicyId(findByKey("policy", recommendationAlertMetadata));
    alert.setStepId(findByKey("step", recommendationAlertMetadata));

    alert.getAlertMetadata().addAll(recommendationAlertMetadata);
  }

  private String findByKey(String key, Collection<AlertMetadata> metadata) {
    return metadata.stream()
        .filter(a-> a.getKey().equalsIgnoreCase(key))
        .map(AlertMetadata::getValue)
        .findFirst()
        .orElse("");
  }

  private List<AlertMetadata> map(List<BulkAlertMetadata> alertMetadata) {
    return alertMetadata.stream().map(a -> {
      var metadata = new AlertMetadata();
      metadata.setKey(a.getKey());
      metadata.setValue(a.getValue());
      return metadata;
    }).collect(Collectors.toList());
  }

  private RecommendationWithMetadataDto getRecommendation(
      String alertName, List<BulkAlertMetadata> metadata) {
    var extendedAttribute5 = getAttribute(metadata);

    return getRecommendationUseCase.getRecommendation(new GetRecommendationRequest() {
      @Override
      public String getAlert() {
        return alertName;
      }

      @Override
      public String getExtendedAttribute5() {
        return extendedAttribute5;
      }
    });
  }

  private String getAttribute(List<BulkAlertMetadata> metadata) {
    return metadata.stream()
        .filter(a -> a.getKey().equals("extendedAttribute5"))
        .map(BulkAlertMetadata::getValue)
        .findFirst()
        .orElse("");
  }
}
