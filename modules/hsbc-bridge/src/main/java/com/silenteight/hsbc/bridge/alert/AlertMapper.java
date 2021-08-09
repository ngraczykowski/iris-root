package com.silenteight.hsbc.bridge.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.AlertSender.AlertDataComposite;
import com.silenteight.hsbc.bridge.json.external.model.AlertData;
import com.silenteight.hsbc.bridge.json.external.model.CaseComment;
import com.silenteight.hsbc.bridge.json.external.model.CaseInformation;
import com.silenteight.hsbc.bridge.report.Alert;
import com.silenteight.hsbc.bridge.report.Alert.Match;

import org.apache.commons.lang3.StringUtils;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.base.Strings.nullToEmpty;
import static java.time.LocalDateTime.parse;
import static java.time.ZoneOffset.UTC;
import static java.util.Comparator.comparingLong;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
class AlertMapper {

  private final AlertPayloadConverter payloadConverter;
  private final AnalystDecisionMapper analystDecisionMapper;
  private final DateTimeFormatter dateTimeFormatter;

  public Collection<Alert> toReportAlerts(@NonNull Collection<AlertDataComposite> alerts) {
    return alerts.stream()
        .map(this::mapToAlert)
        .collect(toList());
  }

  public AlertData toAlertData(byte[] payload) {
    return payloadConverter.convertAlertData(payload);
  }

  private Alert mapToAlert(AlertDataComposite alertInfo) {
    return new Alert() {
      final AlertEntity entity = alertInfo.getAlertEntity();

      @Override
      public String getName() {
        return entity.getName();
      }

      @Override
      public String getDiscriminator() {
        return entity.getExternalId() + getDiscriminatorSeparator() + entity.getDiscriminator();
      }

      @Override
      public Map<String, String> getMetadata() {
        return createAlertMetadata(entity, alertInfo.getPayload());
      }

      @Override
      public Collection<Match> getMatches() {
        return entity.getMatches().stream()
            .map(AlertMapper::mapToMatch)
            .collect(toList());
      }
    };
  }

  private Map<String, String> createAlertMetadata(AlertEntity alertEntity, AlertData alertData) {
    var map = new HashMap<String, String>();
    map.put("id", nullToEmpty(alertEntity.getExternalId()));
    map.put("name", nullToEmpty(alertEntity.getName()));
    map.put("discriminator", nullToEmpty(alertEntity.getDiscriminator()));
    map.put("errorMessage", nullToEmpty(alertEntity.getErrorMessage()));
    map.put("bulkId", alertEntity.getBulkId());
    map.put("status", alertEntity.getStatus().toString());
    map.put("analyst_decision", nullToEmpty(getAnalystDecision(alertData.getCaseInformation())));
    map.putAll(payloadConverter.convertAlertDataToMap(alertData));
    map.putAll(getAlertEntityMetadata(alertEntity));

    getLastCaseComment(alertData.getCaseComments()).ifPresent(c -> map.put("lastCaseComment", c));

    return map;
  }

  private String getAnalystDecision(CaseInformation caseInformation) {
    return analystDecisionMapper.getAnalystDecision(caseInformation.getCurrentState());
  }

  private Map<String, String> getAlertEntityMetadata(AlertEntity alertEntity) {
    return alertEntity.getMetadata().stream()
        .collect(Collectors.toMap(AlertMetadata::getKey, AlertMetadata::getValue));
  }

  private static Match mapToMatch(AlertMatchEntity matchEntity) {
    return new Match() {
      @Override
      public String getName() {
        return matchEntity.getName();
      }

      @Override
      public Map<String, String> getMetadata() {
        return createMatchMetadata(matchEntity);
      }
    };
  }

  private static Map<String, String> createMatchMetadata(AlertMatchEntity matchEntity) {
    var map = new HashMap<String, String>();
    map.put("id", nullToEmpty(matchEntity.getExternalId()));
    map.put("name", nullToEmpty(matchEntity.getName()));

    return map;
  }

  private Optional<String> getLastCaseComment(List<CaseComment> caseComments) {
    return caseComments.stream()
        .filter(c -> StringUtils.isNotEmpty(c.getCommentDateTime()))
        .sorted(comparingLong((k) -> toDate(k.getCommentDateTime())))
        .map(CaseComment::getCaseComment)
        .filter(StringUtils::isNotEmpty)
        .findFirst();
  }

  private long toDate(@NonNull String commentDateTime) {
    try {
      return parse(commentDateTime.toUpperCase(), dateTimeFormatter).toEpochSecond(UTC);
    } catch (DateTimeParseException ex) {
      log.error("Cannot parse case comment date = {}", commentDateTime, ex);
      return Long.MIN_VALUE;
    }
  }
}
