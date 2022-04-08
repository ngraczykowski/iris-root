package com.silenteight.bridge.core.reports.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.bridge.core.reports.domain.model.Report;
import com.silenteight.bridge.core.reports.domain.model.Report.AlertData;
import com.silenteight.bridge.core.reports.domain.model.Report.MatchData;
import com.silenteight.bridge.core.reports.domain.port.outgoing.ReportsSenderService;
import com.silenteight.bridge.core.reports.infrastructure.amqp.ReportsOutgoingConfigurationProperties;
import com.silenteight.data.api.v2.Alert;
import com.silenteight.data.api.v2.Match;
import com.silenteight.data.api.v2.ProductionDataIndexRequest;

import com.google.common.collect.Maps;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
class ReportsSenderServiceAdapter implements ReportsSenderService {

  private static final String EMPTY_STRING = "";

  private static final String ALERT_BATCH_ID_KEY = "batchId";
  private static final String ALERT_EXTERNAL_ID_KEY = "clientId";
  private static final String ALERT_NAME_KEY = "name";
  private static final String ALERT_RECOMMENDATION_SOLUTION_KEY = "s8Recommendation";
  private static final String ALERT_RECOMMENDATION_DATE_KEY = "recommendationDate";
  private static final String ALERT_COMMENT_KEY = "comment";
  private static final String ALERT_POLICY_ID_KEY = "policyId";
  private static final String ALERT_POLICY_TITLE_KEY = "policyTitle";
  private static final String ALERT_STATUS_KEY = "status";
  private static final String ALERT_ERROR_DESCRIPTION_KEY = "errorDescription";

  private static final String MATCH_EXTERNAL_ID_KEY = "clientMatchId";
  private static final String MATCH_STEP_ID_KEY = "stepId";
  private static final String MATCH_STEP_TITLE_KEY = "stepTitle";
  private static final String MATCH_FV_SIGNATURE_KEY = "fvSignature";
  private static final String MATCH_RECOMMENDATION_KEY = "s8Recommendation";
  private static final String MATCH_RECOMMENDATION_COMMENT_KEY = "s8Reason";

  private final RabbitTemplate rabbitTemplate;
  private final ReportsOutgoingConfigurationProperties properties;

  @Override
  public void send(List<Report> reports) {
    var request = getRequest(reports);
    rabbitTemplate.convertAndSend(properties.exchangeName(), properties.routingKey(), request);
  }

  private ProductionDataIndexRequest getRequest(List<Report> reports) {
    var analysisName = reports.stream().findAny().map(Report::analysisName);
    return ProductionDataIndexRequest.newBuilder()
        .setAnalysisName(analysisName.orElse(EMPTY_STRING))
        .addAllAlerts(reports.stream()
            .map(this::toAlert)
            .toList())
        .build();
  }

  private Alert toAlert(Report report) {
    return Alert.newBuilder()
        .setName(report.alertData().name())
        .setDiscriminator(report.alertData().id())
        .setAccessPermissionTag("") // TODO add correct value
        .setPayload(toStruct(getAlertDataPayload(report.batchId(), report.alertData())))
        .addAllMatches(report.matches().stream()
            .map(this::toMatch)
            .toList())
        .build();
  }

  private Map<String, String> getAlertDataPayload(String batchId, AlertData alertData) {
    var alertPayload = new HashMap<String, String>();
    alertPayload.put(ALERT_BATCH_ID_KEY, batchId);
    alertPayload.put(ALERT_EXTERNAL_ID_KEY, alertData.id());
    alertPayload.put(ALERT_NAME_KEY, alertData.name());
    alertPayload.put(ALERT_RECOMMENDATION_SOLUTION_KEY, alertData.recommendation());
    alertPayload.put(ALERT_COMMENT_KEY, alertData.comment());
    alertPayload.put(ALERT_POLICY_ID_KEY, alertData.policyId());
    alertPayload.put(ALERT_POLICY_TITLE_KEY, alertData.policyTitle());
    alertPayload.put(ALERT_STATUS_KEY, alertData.status());
    alertPayload.put(ALERT_ERROR_DESCRIPTION_KEY, alertData.errorDescription());
    alertPayload.put(
        ALERT_RECOMMENDATION_DATE_KEY,
        Optional.ofNullable(alertData.recommendedAt())
            .map(OffsetDateTime::toEpochSecond)
            .map(aLong -> Long.toString(aLong))
            .orElse(EMPTY_STRING)
    );

    alertPayload.putAll(Maps.transformValues(alertData.metadata(), Object::toString));
    return alertPayload;
  }

  private Match toMatch(MatchData matchMetadata) {
    return Match.newBuilder()
        .setDiscriminator(matchMetadata.id())
        .setName(matchMetadata.name())
        .setPayload(toStruct(getMatchDataPayload(matchMetadata)))
        .build();
  }

  private Map<String, String> getMatchDataPayload(MatchData matchData) {
    var matchPayload = new HashMap<String, String>();
    matchPayload.put(MATCH_EXTERNAL_ID_KEY, matchData.id());
    matchPayload.put(MATCH_RECOMMENDATION_KEY, matchData.recommendation());
    matchPayload.put(MATCH_RECOMMENDATION_COMMENT_KEY, matchData.comment());
    matchPayload.put(MATCH_STEP_ID_KEY, matchData.stepId());
    matchPayload.put(MATCH_STEP_TITLE_KEY, matchData.stepTitle());
    matchPayload.put(MATCH_FV_SIGNATURE_KEY, matchData.fvSignature());
    matchPayload.putAll(matchData.categories());
    matchPayload.putAll(matchData.features());
    return matchPayload;
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
