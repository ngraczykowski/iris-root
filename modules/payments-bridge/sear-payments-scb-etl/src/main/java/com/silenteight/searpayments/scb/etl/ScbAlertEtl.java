package com.silenteight.searpayments.scb.etl;

import com.silenteight.searpayments.scb.domain.Alert;
import com.silenteight.searpayments.scb.domain.AlertService;
import com.silenteight.searpayments.scb.mapper.CreateAlertFactory;
import lombok.NonNull;

import com.silenteight.searpayments.bridge.AlertEtl;
import com.silenteight.searpayments.bridge.dto.input.AlertMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
class ScbAlertEtl implements AlertEtl {

  @NonNull private final CreateAlertFactory createAlertFactory;
  @NonNull private final AlertService alertService;

  @Override
  public long invoke(@NonNull AlertMessageDto alertDto) {
    log.debug("invoked");
    //TO DO what to do with dataCenter
    //TO Do what to do if alert is broken
    Alert alert = createAlertFactory.create(alertDto, "???????").create().orElseThrow();
    alertService.save(alert);
    return alert.getId();
  }
}
