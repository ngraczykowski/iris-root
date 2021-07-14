package com.silenteight.hsbc.bridge.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.hsbc.bridge.alert.AlertGetter.AlertInformation;
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

  public Collection<Alert> toReportAlerts(@NonNull Collection<AlertInformation> alerts) {
    return alerts.stream()
        .map(this::mapToAlert)
        .collect(toList());
  }

  public AlertData toAlertData(byte[] payload) {
    return payloadConverter.convertAlertData(payload);
  }

  private Alert mapToAlert(AlertInformation alertInfo) {
    return new Alert() {
      @Override
      public String getDiscriminator() {
        var entity = alertInfo.getAlertEntity();
        return entity.getExternalId() + getDiscriminatorSeparator() + entity.getDiscriminator();
      }

      @Override
      public Map<String, String> getMetadata() {
        return createAlertMetadata(alertInfo.getAlertEntity(), alertInfo.getPayload());
      }

      @Override
      public Collection<Match> getMatches() {
        return alertInfo.getAlertEntity().getMatches().stream()
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
    map.putAll(alertEntity.getMetadata().stream()
        .collect(Collectors.toMap(AlertMetadata::getKey, AlertMetadata::getValue)));

    var payload = payloadConverter.convertAlertDataToMap(alertData);
    map.putAll(payload);

    return map;
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
