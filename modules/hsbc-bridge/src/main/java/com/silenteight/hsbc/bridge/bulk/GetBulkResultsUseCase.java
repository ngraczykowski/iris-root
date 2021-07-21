package com.silenteight.hsbc.bridge.bulk;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.bulk.exception.BatchIdNotFoundException;
import com.silenteight.hsbc.bridge.bulk.exception.BatchProcessingNotCompletedException;
import com.silenteight.hsbc.bridge.bulk.rest.*;
import com.silenteight.hsbc.bridge.recommendation.GetRecommendationUseCase.GetRecommendationRequest;
import com.silenteight.hsbc.bridge.recommendation.RecommendationWithMetadataDto;
import com.silenteight.hsbc.bridge.recommendation.GetRecommendationUseCase;

import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;
import static java.util.List.of;

@RequiredArgsConstructor
@Slf4j
public class GetBulkResultsUseCase {

  private final BulkRepository bulkRepository;
  private final GetRecommendationUseCase getRecommendationUseCase;
  private final RecommendationMetadataCollector
      recommendationMetadataCollector = new RecommendationMetadataCollector();

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
    response.setErrorAlerts(getErrorAlerts(batch.getInvalidAlerts()));
    return response;
  }

  private List<ErrorAlert> getErrorAlerts(Collection<BulkAlertEntity> items) {
    return items.stream().map(this::getErrorAlert).collect(Collectors.toList());
  }

  private List<SolvedAlert> getSolvedAlerts(Collection<BulkAlertEntity> items) {
    return items.stream().map(this::getSolvedAlert).collect(Collectors.toList());
  }

  private ErrorAlert getErrorAlert(BulkAlertEntity alert) {
    var errorAlert = new ErrorAlert();
    errorAlert.setId(alert.getExternalId());
    errorAlert.setErrorMessage(alert.getErrorMessage());
    errorAlert.setAlertMetadata(map(alert.getMetadata()));
    return errorAlert;
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
      attachRecommendationMetadata(solvedAlert, recommendation);
    }
    return solvedAlert;
  }

  private void attachRecommendationMetadata(
      SolvedAlert alert, RecommendationWithMetadataDto recommendation) {
    var metadata = recommendation.getMetadata();
    var recommendationAlertMetadata =
        recommendationMetadataCollector.collectFromRecommendationMetadata(metadata);

    alert.setFvSignature(findByKey("feature_vector_signature", recommendationAlertMetadata));
    alert.setPolicyId(findByKey("policy", recommendationAlertMetadata));
    alert.setStepId(findByKey("step", recommendationAlertMetadata));

    alert.getAlertMetadata().addAll(getAlertRecommendation(recommendation));
    alert.getAlertMetadata().addAll(recommendationAlertMetadata);
  }

  @NotNull
  private List<AlertMetadata> getAlertRecommendation(RecommendationWithMetadataDto recommendation) {
    var date = recommendation.getDate();
    return of(
        new AlertMetadata("s8_recommendation", recommendation.getRecommendedAction()),
        new AlertMetadata("recommendationYear", valueOf(date.getYear())),
        new AlertMetadata("recommendationMonth", valueOf(date.getMonthValue())),
        new AlertMetadata("recommendationDay", valueOf(date.getDayOfMonth())),
        new AlertMetadata("recommendationDate", valueOf(date))
    );
  }

  private String findByKey(String key, Collection<AlertMetadata> metadata) {
    return metadata.stream()
        .filter(a -> a.getKey().equalsIgnoreCase(key))
        .map(AlertMetadata::getValue)
        .findFirst()
        .orElse("");
  }

  private List<AlertMetadata> map(List<BulkAlertMetadata> alertMetadata) {
    return alertMetadata
        .stream()
        .map(a -> new AlertMetadata(a.getKey(), a.getValue()))
        .collect(Collectors.toList());
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
