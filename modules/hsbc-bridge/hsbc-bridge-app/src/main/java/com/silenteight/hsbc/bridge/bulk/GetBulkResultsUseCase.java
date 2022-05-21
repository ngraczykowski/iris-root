package com.silenteight.hsbc.bridge.bulk;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.bulk.exception.BatchIdNotFoundException;
import com.silenteight.hsbc.bridge.bulk.exception.BatchResultNotAvailableException;
import com.silenteight.hsbc.bridge.bulk.exception.BatchResultNotAvailableException.Reason;
import com.silenteight.hsbc.bridge.bulk.rest.*;
import com.silenteight.hsbc.bridge.recommendation.GetRecommendationUseCase;
import com.silenteight.hsbc.bridge.recommendation.GetRecommendationUseCase.GetRecommendationRequest;
import com.silenteight.hsbc.bridge.recommendation.RecommendationWithMetadataDto;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
class GetBulkResultsUseCase {

  private final BulkRepository bulkRepository;
  private final GetRecommendationUseCase getRecommendationUseCase;
  private final RecommendationMetadataCollector
      recommendationMetadataCollector = new RecommendationMetadataCollector();

  public BatchSolvedAlerts getResults(@NonNull String id) {
    var batch = getAndVerifyBatch(id);

    var response = new BatchSolvedAlerts();
    response.setBatchId(batch.getId());
    response.setBatchStatus(BatchStatus.valueOf(batch.getStatus().name()));
    response.setAlerts(mapAlerts(batch.getAlerts()));

    return response;
  }

  private Bulk getAndVerifyBatch(String id) {
    var result = bulkRepository.findById(id);

    if (result.isEmpty()) {
      throw new BatchIdNotFoundException(id);
    }

    var batch = result.get();
    verifyBatch(id, batch);
    return batch;
  }

  private void verifyBatch(String id, Bulk batch) {
    if (batch.isNotCompleted()) {
      throw new BatchResultNotAvailableException(id, Reason.NOT_COMPLETED);
    }

    if (batch.isLearning()) {
      throw new BatchResultNotAvailableException(id, Reason.LEARNING_BATCH);
    }
  }

  private List<AlertRecommendation> mapAlerts(Collection<BulkAlertEntity> items) {
    return items.stream().map(this::getAlertRecommendation).collect(Collectors.toList());
  }

  private AlertRecommendation getAlertRecommendation(BulkAlertEntity alert) {
    return (alert.isCompleted() && alert.isValid()) ? getSolvedAlert(alert) : getErrorAlert(alert);
  }

  private AlertRecommendation getErrorAlert(BulkAlertEntity alert) {
    var errorAlert = new AlertRecommendation();
    errorAlert.setId(alert.getExternalId());
    errorAlert.status(BatchAlertItemStatus.ERROR);

    errorAlert.setComment(
        "Adjudication failed. Invalid alert data or an unexpected error occurred.");
    errorAlert.setErrorMessage(alert.getErrorMessage());
    errorAlert.setAlertMetadata(map(alert.getMetadata()));
    errorAlert.setRecommendation(getRecommendationForErrorAlert(alert.getMetadata()));
    return errorAlert;
  }

  private AlertRecommendation getSolvedAlert(BulkAlertEntity alert) {
    var solvedAlert = new AlertRecommendation();
    var metadata = alert.getMetadata();
    solvedAlert.setAlertMetadata(map(metadata));
    solvedAlert.setId(alert.getExternalId());
    solvedAlert.status(BatchAlertItemStatus.COMPLETED);

    var recommendation = getRecommendation(alert.getName(), metadata);
    solvedAlert.setRecommendation(recommendation.getRecommendedAction());
    solvedAlert.setComment(recommendation.getRecommendationComment());
    attachRecommendationMetadata(solvedAlert, recommendation);

    return solvedAlert;
  }

  private void attachRecommendationMetadata(
      AlertRecommendation alert, RecommendationWithMetadataDto recommendation) {
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
    return List.of(
        new AlertMetadata("s8_recommendation", recommendation.getS8recommendedAction()),
        new AlertMetadata("recommendationYear", String.valueOf(date.getYear())),
        new AlertMetadata("recommendationMonth", String.valueOf(date.getMonthValue())),
        new AlertMetadata("recommendationDay", String.valueOf(date.getDayOfMonth())),
        new AlertMetadata("recommendationDate", String.valueOf(date))
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

  private String getRecommendationForErrorAlert(List<BulkAlertMetadata> metadata) {
    var extendedAttribute5 = getAttribute(metadata);
    return getRecommendationUseCase.getRecommendationForErrorAlert(extendedAttribute5);
  }

  private String getAttribute(List<BulkAlertMetadata> metadata) {
    return metadata.stream()
        .filter(a -> a.getKey().equals("extendedAttribute5"))
        .map(BulkAlertMetadata::getValue)
        .findFirst()
        .orElse("");
  }
}
