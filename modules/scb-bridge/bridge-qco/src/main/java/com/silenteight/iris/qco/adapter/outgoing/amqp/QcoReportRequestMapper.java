/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.adapter.outgoing.amqp;

import lombok.experimental.UtilityClass;

import com.silenteight.data.api.v2.Alert;
import com.silenteight.data.api.v2.Match;
import com.silenteight.data.api.v2.ProductionDataIndexRequest;
import com.silenteight.iris.qco.domain.model.MatchSolution;
import com.silenteight.iris.qco.domain.model.QcoRecommendationMatch;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.apache.commons.lang3.BooleanUtils.toStringYesNo;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@UtilityClass
class QcoReportRequestMapper {

  private static final String QCO_RECOMMENDATION_STATUS = "qcoRecommendationStatus";
  private static final String QCO_SAMPLED = "qcoSampled";

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
            List.of(toMatch(recommendationMatch.matchName(), matchSolution)))
        .build();
  }


  private Match toMatch(String matchName, MatchSolution matchSolution) {
    return Match.newBuilder()
        .setName(matchName)
        .setPayload(toStruct(getMatchDataPayload(matchSolution)))
        .build();
  }

  private Map<String, String> getMatchDataPayload(MatchSolution matchSolution) {
    Map<String, String> result = new HashMap<>(2);
    result.put(QCO_SAMPLED, toStringYesNo(matchSolution.qcoMarked()));
    if (isNotBlank(matchSolution.solution())) {
      result.put(QCO_RECOMMENDATION_STATUS, matchSolution.solution());
    }
    return result;
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
