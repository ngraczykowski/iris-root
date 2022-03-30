package com.silenteight.fab.dataprep.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v2.Alert;
import com.silenteight.data.api.v2.Match;
import com.silenteight.data.api.v2.ProductionDataIndexRequest;
import com.silenteight.fab.dataprep.domain.model.WarehouseEvent;
import com.silenteight.fab.dataprep.domain.outgoing.LearningEventPublisher;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static com.silenteight.rabbitcommonschema.definitions.RabbitConstants.BRIDGE_COMMAND_EXCHANGE;
import static java.util.stream.Collectors.toList;

@Slf4j
@Component
@RequiredArgsConstructor
class LearningRabbitEventPublisher implements LearningEventPublisher {

  static final String EXCHANGE_NAME = BRIDGE_COMMAND_EXCHANGE;
  static final String ROUTING_KEY = "command.index-request.production";

  private final RabbitTemplate rabbitTemplate;

  @Override
  public void publish(WarehouseEvent event) {
    log.info("Sending request: {} to Warehouse", event.getRequestId());

    var message = ProductionDataIndexRequest.newBuilder()
        .setRequestId(event.getRequestId())
        .addAllAlerts(createAlerts(event.getAlerts()))
        .build();

    rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, message);
  }

  private static Iterable<Alert> createAlerts(List<WarehouseEvent.Alert> alerts) {
    return alerts.stream()
        .map(alert -> Alert.newBuilder()
            .setName(alert.getAlertName())
            .setDiscriminator(alert.getDiscriminator())
            .setAccessPermissionTag(alert.getAccessPermissionTag())
            .addAllMatches(createMatches(alert.getMatches()))
            .setPayload(createStruct(alert.getPayload()))
            .build())
        .collect(toList());
  }

  private static Iterable<Match> createMatches(List<WarehouseEvent.Match> matches) {
    return matches.stream()
        .map(match -> Match.newBuilder()
            .setName(match.getMatchName())
            .setDiscriminator(match.getDiscriminator())
            .setPayload(createStruct(match.getPayload()))
            .build())
        .collect(toList());
  }

  private static Struct createStruct(Map<String, String> map) {
    return Struct.newBuilder()
        .putAllFields(map.entrySet()
            .stream()
            .collect(Collectors.toMap(Entry::getKey, e -> Value.newBuilder()
                .setStringValue(e.getValue())
                .build())))
        .build();
  }
}
