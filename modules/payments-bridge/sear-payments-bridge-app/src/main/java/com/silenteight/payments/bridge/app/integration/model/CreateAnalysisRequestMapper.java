package com.silenteight.payments.bridge.app.integration.model;

import com.silenteight.adjudication.api.v1.Analysis;
import com.silenteight.adjudication.api.v1.Analysis.Feature;
import com.silenteight.adjudication.api.v1.Analysis.NotificationFlags;
import com.silenteight.adjudication.api.v1.CreateAnalysisRequest;
import com.silenteight.payments.bridge.governance.solvingmodel.model.AnalysisModel;

import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;

import static java.util.stream.Collectors.toList;

@Component
public class CreateAnalysisRequestMapper {

  @Nonnull
  public CreateAnalysisRequest map(AnalysisModel model) {
    return CreateAnalysisRequest
        .newBuilder()
        .setAnalysis(Analysis
            .newBuilder()
            .addAllCategories(model.getCategories())
            .addAllFeatures(model
                .getFeatures()
                .stream()
                .map(f -> Feature
                    .newBuilder()
                    .setFeature(f.getName())
                    .setAgentConfig(f.getAgentConfig())
                    .build())
                .collect(
                    toList()))
            .setPolicy(model.getPolicyName())
            .setStrategy(model.getStrategyName())
            .setNotificationFlags(notificationFlags())
            .build())
        .build();
  }

  @Nonnull
  private NotificationFlags notificationFlags() {
    return NotificationFlags
        .newBuilder()
        .setAttachMetadata(true)
        .setAttachRecommendation(true)
        .build();
  }

}
