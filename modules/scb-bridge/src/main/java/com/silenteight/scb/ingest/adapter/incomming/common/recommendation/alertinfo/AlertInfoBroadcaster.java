package com.silenteight.scb.ingest.adapter.incomming.common.recommendation.alertinfo;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.common.messaging.OutboundProtoMessage;
import com.silenteight.scb.ingest.adapter.incomming.common.messaging.ReactiveMessageSender;
import com.silenteight.sep.base.common.messaging.MessageSender;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Stream;

import static com.silenteight.scb.ingest.adapter.incomming.common.messaging.MessagingConstants.EXCHANGE_REPORT_DATA;
import static com.silenteight.scb.ingest.adapter.incomming.common.messaging.MessagingConstants.ROUTE_INFO_FROM_SCB_BRIDGE;
import static com.silenteight.scb.ingest.adapter.incomming.common.messaging.MessagingConstants.ROUTE_SCB_INFO_FROM_SYNC;

@RequiredArgsConstructor
class AlertInfoBroadcaster {

  private final MessageSender messageSender;
  private final ReactiveMessageSender sender;

  public void send(@NonNull AlertInfoMapResult alertInfoMapResult) {
    messageSender.send(ROUTE_INFO_FROM_SCB_BRIDGE, alertInfoMapResult.getAlertInfo());
    messageSender.send(ROUTE_SCB_INFO_FROM_SYNC, alertInfoMapResult.getScbAlertInfo());
  }

  public Mono<Void> send(@NonNull List<AlertInfoMapResult> infoList) {
    Flux<OutboundProtoMessage> messageFlux = Flux
        .fromStream(infoList.stream()
            .flatMap(AlertInfoBroadcaster::makeAlertInfoMessages));

    return sender.send(messageFlux);
  }

  private static Stream<? extends OutboundProtoMessage> makeAlertInfoMessages(
      AlertInfoMapResult infos) {

    return Stream.of(
        OutboundProtoMessage
            .builder()
            .exchange(EXCHANGE_REPORT_DATA)
            .routingKey(ROUTE_INFO_FROM_SCB_BRIDGE)
            .message(infos.getAlertInfo())
            .build(),
        OutboundProtoMessage
            .builder()
            .exchange(EXCHANGE_REPORT_DATA)
            .routingKey(ROUTE_SCB_INFO_FROM_SYNC)
            .message(infos.getScbAlertInfo())
            .build()
    );
  }
}
