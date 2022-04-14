package com.silenteight.qco.adapter.outgoing.amqp;

import lombok.experimental.UtilityClass;

import com.silenteight.data.api.v2.Alert;
import com.silenteight.data.api.v2.Match;
import com.silenteight.data.api.v2.ProductionDataIndexRequest;
import com.silenteight.qco.domain.model.MatchSolution;
import com.silenteight.qco.domain.model.QcoRecommendationMatch;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@UtilityClass
class QcoReportRequestMapper {

  private static final String QCO_RECOMMENDATION_STATUS = "qcoRecommendationStatus";

  ProductionDataIndexRequest toRequest(
      QcoRecommendationMatch recommendationMatch, MatchSolution matchSolution) {
    return ProductionDataIndexRequest.newBuilder()
        .addAllAlerts(List.of(toAlert(recommendationMatch, matchSolution)))
        .build();
  }

  private Alert toAlert(QcoRecommendationMatch recommendationMatch, MatchSolution matchSolution) {
    return Alert.newBuilder()
        .setName(recommendationMatch.alertName())
        .setDiscriminator(recommendationMatch.alertId())
        .addAllMatches(
            List.of(toMatch(recommendationMatch.matchName(), matchSolution.solution())))
        .build();
  }


  private Match toMatch(String matchName, String targetSolution) {
    return Match.newBuilder()
        .setName(matchName)
        .setPayload(toStruct(getMatchDataPayload(targetSolution)))
        .build();
  }

  private Map<String, String> getMatchDataPayload(String targetSolution) {
    return Map.of(QCO_RECOMMENDATION_STATUS, targetSolution);
  }

  private static Struct toStruct(Map<String, String> source) {
    var builder = Struct.newBuilder();
    source.forEach((k, v) -> {
      if (Objects.nonNull(v)) {
        builder.putFields(k, Value.newBuilder().setStringValue(v).build());
      }
    });
    return builder.build();
  }
}
