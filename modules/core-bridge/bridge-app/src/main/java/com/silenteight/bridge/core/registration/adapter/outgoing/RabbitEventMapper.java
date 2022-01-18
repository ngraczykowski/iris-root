package com.silenteight.bridge.core.registration.adapter.outgoing;

import com.silenteight.bridge.core.registration.domain.model.BatchCompleted;
import com.silenteight.bridge.core.registration.domain.model.BatchError;
import com.silenteight.bridge.core.registration.domain.model.BatchStatistics;
import com.silenteight.bridge.core.registration.domain.model.BatchStatistics.RecommendationsStats;
import com.silenteight.proto.registration.api.v1.MessageBatchCompleted;
import com.silenteight.proto.registration.api.v1.MessageBatchError;
import com.silenteight.proto.registration.api.v1.RecommendationsStatistics;
import com.silenteight.proto.registration.api.v1.Statistics;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class RabbitEventMapper {

  MessageBatchCompleted toMessageBatchCompleted(BatchCompleted event) {
    return MessageBatchCompleted.newBuilder()
        .setBatchId(event.id())
        .setAnalysisId(event.analysisId())
        .addAllAlertIds(event.alertIds())
        .setBatchMetadata(event.batchMetadata())
        .setStatistics(toStatistics(event.statistics()))
        .build();
  }

  MessageBatchError toMessageBatchError(BatchError event) {
    return MessageBatchError.newBuilder()
        .setBatchId(event.id())
        .setErrorDescription(event.errorDescription())
        .setBatchMetadata(Optional.ofNullable(event.batchMetadata()).orElse(""))
        .setStatistics(toStatistics(event.statistics()))
        .build();
  }

  private Statistics toStatistics(BatchStatistics batchStatistics) {
    return Statistics.newBuilder()
        .setTotalProcessedCount(batchStatistics.totalProcessedCount())
        .setRecommendedAlertsCount(batchStatistics.recommendedAlertsCount())
        .setTotalUnableToProcessCount(batchStatistics.totalUnableToProcessCount())
        .setRecommendationsStatistics(
            toRecommendationsStatistics(batchStatistics.recommendationsStats()))
        .build();
  }

  private RecommendationsStatistics toRecommendationsStatistics(
      RecommendationsStats stats) {
    return RecommendationsStatistics.newBuilder()
        .setTruePositiveCount(stats.truePositiveCount())
        .setFalsePositiveCount(stats.falsePositiveCount())
        .setManualInvestigationCount(stats.manualInvestigationCount())
        .setErrorCount(stats.errorCount())
        .build();
  }
}
