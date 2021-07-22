package com.silenteight.searpayments.scb.etl;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.AlertEtl;
import com.silenteight.payments.bridge.dto.input.AlertDataCenterDto;
import com.silenteight.searpayments.scb.domain.Alert;
import com.silenteight.searpayments.scb.domain.AlertService;
import com.silenteight.searpayments.scb.mapper.CreateAlertFactory;

import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
class ScbAlertEtl implements AlertEtl {

  @NonNull
  private final CreateAlertFactory createAlertFactory;
  @NonNull
  private final AlertService alertService;

  @Override
  public long invoke(@NonNull AlertDataCenterDto alertDataCenterDto) {
    log.debug("invoked");
    Alert alert = createAlertFactory
        .create(alertDataCenterDto.getAlertMessageDto(), alertDataCenterDto.getDataCenter())
        .create()
        .orElseThrow();
    alertService.save(alert);
    return alert.getId();
  }
}
