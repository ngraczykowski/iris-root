package com.silenteight.hsbc.bridge.report;

import lombok.experimental.UtilityClass;

import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.hsbc.bridge.report.Alert.Match;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@UtilityClass
class DataIndexRequestCreator {

  public static ProductionDataIndexRequest create(Collection<Alert> alerts) {
    return ProductionDataIndexRequest.newBuilder()
        .addAllAlerts(mapAlerts(alerts))
        .build();
  }

  private static List<com.silenteight.data.api.v1.Alert> mapAlerts(Collection<Alert> alerts) {
    return alerts.stream().map(DataIndexRequestCreator::toAlert).collect(Collectors.toList());
  }

  private static com.silenteight.data.api.v1.Alert toAlert(Alert alert) {
    var builder = com.silenteight.data.api.v1.Alert.newBuilder()
        .setDiscriminator(alert.getDiscriminator())
        .setPayload(toStruct(alert.getMetadata()));

    return (alert.getName() == null) ?
           builder.build() :
           builder
               .setName(alert.getName())
               .addAllMatches(mapMatches(alert.getMatches()))
               .build();
  }

  private static Struct toStruct(Map<String, String> payloadMetadata) {
    var builder = Struct.newBuilder();
    payloadMetadata.forEach((k, v) -> {
      if (Objects.nonNull(v)) {
        builder.putFields(k, Value.newBuilder().setStringValue(v).build());
      }
    });
    return builder.build();
  }

  private static List<com.silenteight.data.api.v1.Match> mapMatches(Collection<Match> matches) {
    return matches.stream().map(DataIndexRequestCreator::toMatch).collect(Collectors.toList());
  }

  private static com.silenteight.data.api.v1.Match toMatch(Match match) {
    return com.silenteight.data.api.v1.Match.newBuilder()
        .setName(match.getName())
        .setPayload(toStruct(match.getMetadata()))
        .build();
  }
}
