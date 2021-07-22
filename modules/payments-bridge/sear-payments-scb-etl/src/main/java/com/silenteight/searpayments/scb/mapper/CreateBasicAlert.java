package com.silenteight.searpayments.scb.mapper;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.dto.input.RequestStatusDto;
import com.silenteight.payments.bridge.dto.input.StatusDto;
import com.silenteight.searpayments.scb.domain.Alert;
import com.silenteight.searpayments.scb.domain.Alert.AlertBuilder;
import com.silenteight.searpayments.scb.domain.Alert.AlertStatus;
import com.silenteight.searpayments.scb.domain.NextStatus;

import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotNull;

import static com.google.common.base.Strings.nullToEmpty;
import static com.silenteight.searpayments.scb.domain.Alert.AlertMessageFormat.OTHER;
import static com.silenteight.searpayments.scb.domain.Alert.AlertStatus.STATE_STORED;

@RequiredArgsConstructor
@Slf4j
class CreateBasicAlert {

  private static final String EMPTY_STRING = "";
  @NonNull private final String gitCommitId;
  @NonNull private final String dataCenter;
  @NonNull private final AlertMessageDto inputDto;

  @NotNull CreateBasicAlert.BaseAlertDto create() {
    return new BaseAlertDto(createAlertBuilder(), createNextStatuses());
  }

  private AlertBuilder createAlertBuilder() {
    var builder = Alert.builder()
        .systemId(inputDto.getSystemId())
        .messageId(inputDto.getMessageId())
        .status(AlertStatus.STATE_STORED)
        .messageType(EMPTY_STRING)
        .applicationCode(EMPTY_STRING)
        .messageFormat(OTHER)
        .messageData(EMPTY_STRING)
        .businessUnitId(EMPTY_STRING)
        .outputStatusName(EMPTY_STRING)
        .unit(inputDto.getUnit())
        .status(STATE_STORED)
        .dataCenter(dataCenter)
        .gitCommitId(gitCommitId)
        .transactionReference(EMPTY_STRING)
        .direction(EMPTY_STRING);

    setCurrentStatus(builder);
    return builder;
  }

  private void setCurrentStatus(AlertBuilder builder) {
    var currentStatus = inputDto.getCurrentStatus();
    builder.currentStatusId(nullToEmpty(currentStatus.getId()))
        .currentStatusName(nullToEmpty(currentStatus.getName()))
        .currentStatusChecksum(nullToEmpty(currentStatus.getChecksum()))
        .currentStatusRoutingCode(nullToEmpty(currentStatus.getRoutingCode()));
  }

  private List<NextStatus> createNextStatuses() {
    List<NextStatus> nextStatuses = new ArrayList<>();
    for (RequestStatusDto requestStatusDto : inputDto.getNextStatuses()) {
      nextStatuses.add(createNextStatus(requestStatusDto.getStatus()));
    }
    return nextStatuses;
  }

  private static NextStatus createNextStatus(StatusDto status) {
    return new NextStatus(
        status.getId(),
        status.getName(),
        status.getRoutingCode(),
        status.getChecksum()
    );
  }

  @Value
  static class BaseAlertDto {

    @NonNull Alert.AlertBuilder alertBuilder;
    @NonNull List<NextStatus> nextStatusList;
  }
}
