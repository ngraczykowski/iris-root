package com.silenteight.adjudication.engine.analysis.recommendation.domain;

import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.adjudication.api.v1.Recommendation;
import com.silenteight.adjudication.api.v2.RecommendationMetadata;
import com.silenteight.adjudication.api.v2.RecommendationMetadata.FeatureMetadata;
import com.silenteight.adjudication.api.v2.RecommendationMetadata.MatchMetadata;
import com.silenteight.adjudication.api.v2.RecommendationWithMetadata;
import com.silenteight.adjudication.engine.analysis.recommendation.transform.AlertMetaDataTransformer;
import com.silenteight.adjudication.engine.analysis.recommendation.transform.dto.AnalysisRecommendationContext;
import com.silenteight.adjudication.engine.comments.comment.domain.AlertContext;
import com.silenteight.adjudication.engine.comments.comment.domain.FeatureContext;
import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;

import java.sql.Timestamp;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;

import static com.silenteight.adjudication.engine.common.protobuf.TimestampConverter.fromSqlTimestamp;

@Value
@Builder
@Slf4j
public class AlertRecommendation {

  long alertId;

  long analysisId;

  long recommendationId;

  Timestamp createdTime;

  AlertContext alertContext;

  long[] matchIds;

  String comment;

  Map<String, String> matchComments;

  Map<String, String> alertLabels;

  public Recommendation toRecommendation() {
    return Recommendation
        .newBuilder()
        .setAlert("alerts/" + getAlertId())
        .setCreateTime(fromSqlTimestamp(getCreatedTime()))
        .setName("analysis/" + getAnalysisId() + "/recommendations/" + getRecommendationId())
        .setRecommendedAction(alertContext.getRecommendedAction())
        .setRecommendationComment(comment)
        .build();
  }

  public RecommendationMetadata toMetadata() {
    var matches = alertContext.getMatches();
    return AlertMetaDataTransformer.transferToRecommendationMetaData(
        new AnalysisRecommendationContext(
            matches, analysisId, recommendationId, alertId, matchIds, matchComments, alertLabels));
  }

  public RecommendationWithMetadata toRecommendationWithMetadata() {
    return RecommendationWithMetadata
        .newBuilder()
        .setRecommendation(toRecommendation())
        .setMetadata(toMetadata())
        .build();
  }

  @SuppressWarnings("FeatureEnvy")
  @Nonnull
  private static MatchMetadata convertMatchMetadata(
      MatchContext context, long matchId, long alertId) {

    var reasonJson = JsonConversionHelper.INSTANCE.serializeToString(context.getReason());
    var reasonStructBuilder = Struct.newBuilder();

    try {
      JsonFormat.parser().merge(reasonJson, reasonStructBuilder);
    } catch (InvalidProtocolBufferException e) {
      throw new JsonStructConversionException("Cannot convert match reason to Struct", e);
    }

    return MatchMetadata
        .newBuilder()
        .setMatch("alerts/" + alertId + "/matches/" + matchId)
        .putAllCategories(context.getCategories())
        .putAllFeatures(convertFeaturesMap(context.getFeatures()))
        .setReason(reasonStructBuilder)
        .setSolution(context.getSolution())
        .build();
  }

  private static Map<String, FeatureMetadata> convertFeaturesMap(
      Map<String, FeatureContext> context) {
    return context
        .entrySet()
        .stream()
        .collect(Collectors.toMap(Entry::getKey, e -> convertFeatureMetadata(e.getValue())));
  }

  @SuppressWarnings("FeatureEnvy")
  private static FeatureMetadata convertFeatureMetadata(FeatureContext context) {

    var reasonJson = JsonConversionHelper.INSTANCE.serializeToString(context.getReason());
    var reasonStructBuilder = Struct.newBuilder();

    try {
      JsonFormat.parser().merge(reasonJson, reasonStructBuilder);
    } catch (InvalidProtocolBufferException e) {
      throw new JsonStructConversionException("Cannot convert feature reason to Struct", e);
    }

    return FeatureMetadata
        .newBuilder()
        .setReason(reasonStructBuilder)
        .setAgentConfig(context.getAgentConfig())
        .setSolution(context.getSolution())
        .build();
  }
}
