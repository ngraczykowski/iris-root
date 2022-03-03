package com.silenteight.adjudication.api.library.v1.analysis;

import lombok.experimental.UtilityClass;

import com.silenteight.adjudication.api.library.v1.analysis.AddAlertsToAnalysisOut.AddedAlert;
import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.api.v1.Analysis.NotificationFlags;
import com.silenteight.adjudication.api.v1.AnalysisAlert;
import com.silenteight.adjudication.api.v1.AnalysisDataset;
import com.silenteight.adjudication.api.v1.BatchAddAlertsResponse;

import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
class AnalysisGrpcMapper {

  AddAlertsToAnalysisOut mapToAddAlertsToAnalysisOut(BatchAddAlertsResponse response) {
    return AddAlertsToAnalysisOut.builder()
        .addedAlerts(mapToAddedAlerts(response))
        .build();
  }

  AnalysisAlert mapToAnalysisAlert(AddAlertsToAnalysisIn.Alert alert) {
    return AnalysisAlert.newBuilder()
        .setAlert(alert.getName())
        .setDeadlineTime(alert.getDeadlineTime())
        .build();
  }

  AnalysisDatasetOut mapToAnalysisDatasetOut(AnalysisDataset analysisDataset) {
    return AnalysisDatasetOut.builder()
        .alertsCount(analysisDataset.getAlertCount())
        .name(analysisDataset.getName())
        .build();
  }

  CreateAnalysisOut mapToCreateAnalysisOut(Analysis analysis) {
    return CreateAnalysisOut.builder()
        .name(analysis.getName())
        .policy(analysis.getPolicy())
        .strategy(analysis.getStrategy())
        .build();
  }

  List<Feature> mapFeatures(List<FeatureIn> features) {
    return features.stream()
        .map(feature -> Feature.newBuilder()
            .setAgentConfig(feature.getAgentConfig())
            .setFeature(feature.getName())
            .build())
        .collect(Collectors.toList());
  }

  GetAnalysisOut mapToGetAnalysisOut(Analysis analysis) {
    return GetAnalysisOut.builder()
        .alertsCount(analysis.getAlertCount())
        .pendingAlerts(analysis.getPendingAlerts())
        .build();
  }

  NotificationFlags mapToNotificationFlags(@Nullable NotificationFlagsIn notificationFlags) {
    return Optional.ofNullable(notificationFlags)
        .map(flags -> NotificationFlags.newBuilder()
            .setAttachRecommendation(flags.isAttachRecommendation())
            .setAttachMetadata(flags.isAttachMetadata())
            .build())
        .orElseGet(() -> NotificationFlags.newBuilder()
            .setAttachRecommendation(false)
            .setAttachMetadata(false)
            .build());
  }

  private List<AddedAlert> mapToAddedAlerts(BatchAddAlertsResponse batchAddAlertsResponse) {
    return batchAddAlertsResponse.getAnalysisAlertsList().stream()
        .map(AnalysisGrpcMapper::createAddedAlert)
        .collect(Collectors.toUnmodifiableList());
  }

  private AddedAlert createAddedAlert(AnalysisAlert analysisAlert) {
    return AddedAlert.builder()
        .name(analysisAlert.getName())
        .createdAt(analysisAlert.getCreateTime())
        .build();
  }
}
