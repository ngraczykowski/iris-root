package com.silenteight.hsbc.bridge.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.alert.AlertSender.AlertDataComposite;
import com.silenteight.hsbc.bridge.json.external.model.AlertData;
import com.silenteight.hsbc.bridge.report.Alert;
import com.silenteight.hsbc.bridge.report.Alert.Match;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import static com.google.common.base.Strings.nullToEmpty;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class AlertMapper {

  private final AlertPayloadConverter payloadConverter;
  private final AnalystDecisionMapper analystDecisionMapper;

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
    var currentState = alertData.getCaseInformation().getCurrentState();
    var analystDecision = analystDecisionMapper.getAnalystDecision(currentState);
    var payload = payloadConverter.convertAlertDataToMap(alertData);

    var map = new HashMap<String, String>();
    map.put("id", nullToEmpty(alertEntity.getExternalId()));
    map.put("name", nullToEmpty(alertEntity.getName()));
    map.put("discriminator", nullToEmpty(alertEntity.getDiscriminator()));
    map.put("errorMessage", nullToEmpty(alertEntity.getErrorMessage()));
    map.put("bulkId", alertEntity.getBulkId());
    map.put("status", alertEntity.getStatus().toString());
    map.put("analyst_decision", nullToEmpty(analystDecision));
    map.putAll(payload);
    map.putAll(getAlertEntityMetadata(alertEntity));

    return map;
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
}
