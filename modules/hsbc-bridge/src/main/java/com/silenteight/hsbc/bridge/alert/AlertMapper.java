package com.silenteight.hsbc.bridge.alert;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.AlertSender.AlertDataComposite;
import com.silenteight.hsbc.bridge.alert.dto.AlertEntityDto;
import com.silenteight.hsbc.bridge.alert.dto.AlertMatchEntityDto;
import com.silenteight.hsbc.bridge.alert.dto.AlertMetadataDto;
import com.silenteight.hsbc.bridge.json.external.model.AlertData;
import com.silenteight.hsbc.bridge.json.external.model.CaseInformation;
import com.silenteight.hsbc.bridge.report.Alert;
import com.silenteight.hsbc.bridge.report.Alert.Match;

import com.google.common.base.Strings;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
class AlertMapper {

  private final AlertPayloadConverter payloadConverter;
  private final AnalystDecisionMapper analystDecisionMapper;
  private final CaseCommentsMapper caseCommentsMapper;

  public Collection<Alert> toReportAlerts(@NonNull Collection<AlertDataComposite> alerts) {
    return alerts.stream()
        .map(this::mapToAlert)
        .collect(Collectors.toList());
  }

  public AlertData toAlertData(byte[] payload) {
    return payloadConverter.convertAlertData(payload);
  }

  private Alert mapToAlert(AlertDataComposite alertInfo) {
    return new Alert() {
      final AlertEntityDto entity = alertInfo.getAlertEntity();

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
            .collect(Collectors.toList());
      }
    };
  }

  private Map<String, String> createAlertMetadata(AlertEntityDto alertEntity, AlertData alertData) {
    var map = new HashMap<String, String>();
    map.put("id", Strings.nullToEmpty(alertEntity.getExternalId()));

    if (alertEntity.getName() != null) {
      map.put("name", alertEntity.getName());
    }

    map.put("discriminator", Strings.nullToEmpty(alertEntity.getDiscriminator()));
    map.put("errorMessage", Strings.nullToEmpty(alertEntity.getErrorMessage()));
    map.put("bulkId", alertEntity.getBulkId());
    map.put("status", alertEntity.getStatus().toString());
    map.put("analyst_decision", Strings.nullToEmpty(getAnalystDecision(alertData.getCaseInformation())));
    map.putAll(payloadConverter.convertAlertDataToMap(alertData));
    map.putAll(caseCommentsMapper.getLastCaseCommentWithDate(alertData.getCaseComments()));
    map.putAll(getAlertEntityMetadata(alertEntity));

    return map;
  }

  private String getAnalystDecision(CaseInformation caseInformation) {
    return analystDecisionMapper.getAnalystDecision(caseInformation.getCurrentState());
  }

  private Map<String, String> getAlertEntityMetadata(AlertEntityDto alertEntity) {
    return alertEntity.getMetadata().stream()
        .collect(Collectors.toMap(AlertMetadataDto::getKey, AlertMetadataDto::getValue));
  }

  private static Match mapToMatch(AlertMatchEntityDto matchEntity) {
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

  private static Map<String, String> createMatchMetadata(AlertMatchEntityDto matchEntity) {
    var map = new HashMap<String, String>();
    map.put("id", Strings.nullToEmpty(matchEntity.getExternalId()));
    map.put("name", Strings.nullToEmpty(matchEntity.getName()));

    return map;
  }
}
