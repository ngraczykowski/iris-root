/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.reports.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v2.Alert;
import com.silenteight.data.api.v2.ProductionDataIndexRequest;
import com.silenteight.iris.bridge.scb.reports.domain.model.Report;
import com.silenteight.iris.bridge.scb.reports.domain.model.Report.AlertData;
import com.silenteight.iris.bridge.scb.reports.infrastructure.amqp.ReportsOutgoingConfigurationProperties;
import com.silenteight.iris.bridge.scb.reports.domain.port.outgoing.ReportsSenderService;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.time.format.DateTimeFormatter.ISO_OFFSET_DATE_TIME;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReportsSenderServiceAdapter implements ReportsSenderService {

  private static final String EMPTY_STRING = "";
  private static final String ANALYST_DECISION = "analystDecision";
  private static final String ANALYST_DECISION_MODIFIED_DATE_TIME =
      "analystDecisionModifiedDateTime";
  private static final String ANALYST_REASON = "analystReason";

  private final RabbitTemplate rabbitTemplate;
  private final ReportsOutgoingConfigurationProperties properties;

  @Override
  public void send(List<Report> reports) {
    log.info("Sending {} reports to Warehouse", reports.size());
    var request = getRequest(reports);
    rabbitTemplate.convertAndSend(properties.exchangeName(), properties.routingKey(), request);
  }

  private ProductionDataIndexRequest getRequest(List<Report> reports) {
    return ProductionDataIndexRequest.newBuilder()
        .addAllAlerts(reports.stream()
            .map(this::toAlert)
            .toList())
        .build();
  }

  private Alert toAlert(Report report) {
    return Alert.newBuilder()
        .setName(report.alertData().alertName())
        .setDiscriminator(report.alertData().id())
        .setAccessPermissionTag(EMPTY_STRING)
        .setPayload(toStruct(getAlertDataPayload(report.alertData())))
        .addAllMatches(List.of())
        .build();
  }

  private Map<String, String> getAlertDataPayload(AlertData alertData) {
    return Map.of(
        ANALYST_DECISION, alertData.analystDecision(),
        ANALYST_DECISION_MODIFIED_DATE_TIME, alertData.analystDecisionModifiedDateTime()
            .format(ISO_OFFSET_DATE_TIME),
        ANALYST_REASON, alertData.analystReason());
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
