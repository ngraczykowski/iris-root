package com.silenteight.searpayments.scb.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.searpayments.bridge.dto.input.AlertMessageDto;
import com.silenteight.searpayments.bridge.dto.validator.AlertMessageDtoValidator;
import com.silenteight.searpayments.bridge.dto.validator.CompleteAlertDefinition;
import com.silenteight.searpayments.bridge.dto.validator.RequestMessageDtoValidator;
import com.silenteight.searpayments.scb.domain.Alert;
import com.silenteight.searpayments.scb.domain.Alert.AlertMessageFormat;
import com.silenteight.searpayments.scb.domain.Alert.DamageReason;
import com.silenteight.searpayments.scb.domain.Hit;
import com.silenteight.searpayments.scb.domain.NextStatus;
import com.silenteight.searpayments.scb.etl.AlertParser;
import com.silenteight.searpayments.scb.etl.countrycode.CountryCodeExtractor;
import com.silenteight.searpayments.scb.etl.response.AlertEtlResponse;

import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Slf4j
@RequiredArgsConstructor
class CreateAlertImpl implements CreateAlert {

  @NonNull private final AlertMessageDto alertMessageDto;
  @NonNull private final String dataCenter;
  @NonNull private final CreateHitsFactory createHitsFactory;
  @NonNull private final CreateMessageTypeFactory createMessageTypeFactory;
  @NonNull private final CreateBasicAlertFactory createBasicAlertFactory;
  @NonNull private final String gitCommitId;
  @NonNull private final AlertMessageDtoValidator alertMessageDtoValidator;
  @NonNull private final CountryCodeExtractor countryCodeExtractor;
  private Alert alert;
  private Alert.AlertBuilder builder;
  private AlertEtlResponse alertEtlResponse;
  private List<NextStatus> nextStatusList;

  public Optional<Alert> create() {
    try {
      createBaseAlertData();
      validateAlertMessage();
      createEtlResponse();
      enhanceAlert();
      return Optional.of(alert);
    } catch (Exception e) {
      return handleCreateException(e);
    }
  }

  private void validateAlertMessage() {
    alertMessageDtoValidator.validate(alertMessageDto, CompleteAlertDefinition.class);
  }

  @NotNull
  private Optional<Alert> handleCreateException(Exception e) {
    log.warn("I am unable create an alert [systemId={},messageId={}]."
        + " I will try to create a damaged alert", getSystemId(), getMessageId(), e);
    try {
      createDamagedAlert(e.getMessage());
      return Optional.of(alert);
    } catch (Exception e2) {
      return handleCreateDamagedAlertException(e2);
    }
  }

  @NotNull
  private Optional<Alert> handleCreateDamagedAlertException(Exception e) {
    log.error("I was unable to create a damaged alert [systemId={},messageId={}]."
        + " Something must be really broken", getSystemId(), getMessageId(), e);
    return Optional.empty();
  }

  @NotNull
  private String getMessageId() {
    return alertMessageDto.getMessageId();
  }

  @NotNull
  private String getSystemId() {
    return alertMessageDto.getSystemId();
  }

  private void createDamagedAlert(String message) {
    alert = builder.build();
    addNextStatuses();
    alert.markAsDamaged(
        new DamageReason(
            "REQUEST_MESSAGE_IS_DAMAGED",
            StringUtils.substring(message, 0, 256)));
  }

  private void createBaseAlertData() {
    var baseAlert = createBasicAlertFactory.create(
        dataCenter, alertMessageDto).create();

    if (log.isTraceEnabled()) {
      log.trace("baseAlert builder and nextStatuses: {}", baseAlert);
    }

    builder = baseAlert.getAlertBuilder();
    nextStatusList = baseAlert.getNextStatusList();
  }

  private void createEtlResponse() {
    alertEtlResponse =
        new AlertParser(countryCodeExtractor, alertMessageDto).invoke();
  }

  private void enhanceAlert() {
    builder.messageType(createMessageTypeFactory.create(alertEtlResponse).create())
        .messageFormat(mapToAlertMessageFormat(alertEtlResponse.getMessageFormat()))
        .applicationCode(alertEtlResponse.getApplicationCode())
        .messageData(alertEtlResponse.getMessageData())
        .businessUnitId(alertEtlResponse.getBusinessUnit())
        .unit(alertEtlResponse.getUnit())
        .transactionReference(alertEtlResponse.getSenderReference())
        .direction(alertEtlResponse.getIoIndicator())
        .dataCenter(dataCenter)
        .gitCommitId(gitCommitId)
        .countryCode(alertEtlResponse.getCountryCode());
    alert = builder.build();
    addHits(alertEtlResponse);
    addNextStatuses();
  }

  private void addNextStatuses() {
    if (isNotEmpty(nextStatusList)) {
      for (NextStatus nextStatus : nextStatusList) {
        alert.addNextStatus(nextStatus);
      }
    }
  }

  private static AlertMessageFormat mapToAlertMessageFormat(String messageFormat) {
    return AlertMessageFormat.of(messageFormat);
  }

  private void addHits(AlertEtlResponse alertEtlResponse) {
    for (Hit hit : createHitsFactory.create(alertEtlResponse).create()) {
      alert.addHit(hit);
    }
  }
}
