package com.silenteight.adjudication.engine.analysis.recommendation.transform;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v2.RecommendationMetadata;
import com.silenteight.adjudication.api.v2.RecommendationMetadata.FeatureMetadata;
import com.silenteight.adjudication.api.v2.RecommendationMetadata.MatchMetadata;
import com.silenteight.adjudication.engine.analysis.recommendation.transform.dto.AnalysisRecommendationContext;
import com.silenteight.adjudication.engine.comments.comment.domain.FeatureContext;
import com.silenteight.adjudication.engine.comments.comment.domain.MatchContext;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class AlertMetaDataTransformer {


  public static RecommendationMetadata transferToRecommendationMetaData(
      final AnalysisRecommendationContext context
  ) {
    var matches = context.getMatches();
    var analysisId = context.getAnalysisId();
    var alertId = context.getAlertId();
    var builder = RecommendationMetadata.newBuilder()
        .putAllLabels(context.getAlertLabels())
        .setName("analysis/" + analysisId + "/recommendations/" + context.getRecommendationId()
            + "/metadata")
        .setAlert("alerts/" + alertId);

    for (int i = 0; i < matches.size(); i++) {
      builder.addMatches(convertMatchMetadata(matches.get(i), context.getMatchIds()[i], alertId,
          context.getMatchComments()));
    }

    return builder.build();
  }


  private static MatchMetadata convertMatchMetadata(
      MatchContext context, long matchId, long alertId, Map<String, String> matchComments) {

    var reasonStructBuilder = createStructBuilderFromMatchContext(
        context.getReason(),
        "Cannot convert match reason to Struct");

    var matchComment = matchComments.getOrDefault(context.getMatchId(), "");

    return MatchMetadata
        .newBuilder()
        .setMatch(buildMatch(matchId, alertId))
        .putAllCategories(context.getCategories())
        .putAllFeatures(convertFeaturesMap(context.getFeatures()))
        .setReason(reasonStructBuilder)
        .setSolution(context.getSolution())
        .setMatchComment(matchComment)
        .build();
  }

  private static String buildMatch(long matchId, long alertId) {
    return "alerts/" + alertId + "/matches/" + matchId;
  }

  private static Struct.Builder createStructBuilderFromMatchContext(
      Map<String, Object> context, String message) {
    var reasonJson = JsonConversionHelper.INSTANCE.serializeToString(context);
    var reasonStructBuilder = Struct.newBuilder();

    try {
      JsonFormat.parser().merge(reasonJson, reasonStructBuilder);
    } catch (InvalidProtocolBufferException exception) {
      throw new JsonStructConversionException(message, exception);
    }
    return reasonStructBuilder;
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

    var reasonStructBuilder = createStructBuilderFromMatchContext(
        context.getReason(),
        "Cannot convert feature reason to Struct");

    return FeatureMetadata
        .newBuilder()
        .setReason(reasonStructBuilder)
        .setAgentConfig(context.getAgentConfig())
        .setSolution(context.getSolution())
        .build();
  }
}
