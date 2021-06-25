package com.silenteight.adjudication.engine.analysis.recommendation.domain;

import lombok.Builder;
import lombok.Value;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;

import java.sql.Timestamp;

import static com.silenteight.adjudication.engine.common.protobuf.TimestampConverter.fromSqlTimestamp;

@Value
@Builder
public class AlertRecommendation {

  long alertId;

  long analysisId;

  long recommendationId;

  Timestamp createdTime;

  AlertContext alertContext;

  public Recommendation toRecommendation(String comment) {
    return Recommendation
        .newBuilder()
        .setAlert("alerts/" + getAlertId())
        .setCreateTime(fromSqlTimestamp(getCreatedTime()))
        .setName("analysis/" + getAnalysisId() + "/recommendations/" + getRecommendationId())
        .setRecommendedAction(alertContext.getRecommendedAction())
        .setRecommendationComment(comment)
        .build();
  }
}
