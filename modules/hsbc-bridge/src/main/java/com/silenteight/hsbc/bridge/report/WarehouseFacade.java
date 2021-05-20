package com.silenteight.hsbc.bridge.report;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.data.api.v1.ProductionDataIndexRequest;
import com.silenteight.hsbc.bridge.report.Alert.Match;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class WarehouseFacade {

  private final AlertFinder alertFinder;
  private final WarehouseMessageSender messageSender;

  public void sendAlerts(@NonNull Collection<Alert> alerts) {
    var dataIndexRequest = createDataIndexRequest(alerts);

    messageSender.send(dataIndexRequest);
  }

  public void findAndSendAlerts(@NonNull Collection<Long> alertIds) {
    var alerts = alertFinder.find(alertIds);

    sendAlerts(alerts);
  }

  private ProductionDataIndexRequest createDataIndexRequest(Collection<Alert> alerts) {
    return ProductionDataIndexRequest.newBuilder()
        .addAllAlerts(mapAlerts(alerts))
        .build();
  }

  private List<com.silenteight.data.api.v1.Alert> mapAlerts(Collection<Alert> alerts) {
    return alerts.stream().map(this::toAlert).collect(toList());
  }

  private com.silenteight.data.api.v1.Alert toAlert(Alert alert) {
    return com.silenteight.data.api.v1.Alert.newBuilder()
        .setName(alert.getName())
        .setPayload(toStruct(alert.getMetadata()))
        .addAllMatches(mapMatches(alert.getMatches()))
        .build();
  }

  private Struct toStruct(Map<String, String> metadata) {
    var builder = Struct.newBuilder();
    metadata.forEach((k,v) -> builder.putFields(k, Value.newBuilder().setStringValue(v).build()));
    return builder.build();
  }

  private List<com.silenteight.data.api.v1.Match> mapMatches(Collection<Match> matches) {
    return matches.stream().map(this::toMatch).collect(toList());
  }

  private com.silenteight.data.api.v1.Match toMatch(Match match) {
    return com.silenteight.data.api.v1.Match.newBuilder()
        .setName(match.getName())
        .setPayload(toStruct(match.getMetadata()))
        .build();
  }
}
