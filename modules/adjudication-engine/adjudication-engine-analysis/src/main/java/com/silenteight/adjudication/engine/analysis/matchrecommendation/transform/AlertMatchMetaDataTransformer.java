package com.silenteight.adjudication.engine.analysis.matchrecommendation.transform;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

import com.silenteight.adjudication.api.v3.MatchRecommendationMetadata.FeatureMetadata;
import com.silenteight.adjudication.api.v3.MatchRecommendationMetadata.MatchMetadata;
import com.silenteight.adjudication.engine.analysis.matchrecommendation.domain.AnalysisMatchRecommendationContext;
import com.silenteight.adjudication.engine.analysis.recommendation.transform.JsonStructConversionException;
import com.silenteight.adjudication.engine.comments.comment.domain.FeatureContext;
import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Struct;
import com.google.protobuf.util.JsonFormat;

import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class AlertMatchMetaDataTransformer {


  public static MatchMetadata convertMatchMetadata(AnalysisMatchRecommendationContext context) {
    var matchContext = context.getMatchContext();

    var reasonStructBuilder = createStructBuilderFromMatchContext(
        matchContext.getReason(),
        "Cannot convert match reason to Struct");

    return MatchMetadata
        .newBuilder()
        .setMatch(buildMatch(context.getMatchId(), context.getAlertId()))
        .putAllCategories(context.getMatchContext().getCategories())
        .putAllFeatures(convertFeaturesMap(matchContext.getFeatures()))
        .setReason(reasonStructBuilder)
        .setSolution(matchContext.getSolution())
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
