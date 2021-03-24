package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.hsbc.bridge.alert.event.UpdateAlertWithNameEvent;

import org.springframework.context.event.EventListener;

import javax.transaction.Transactional;

@Slf4j
@RequiredArgsConstructor
class AlertEventHandler {

  private final AlertRepository alertRepository;

  @EventListener
  @Transactional
  public void onUpdateAlertEventWithNameEvent(UpdateAlertWithNameEvent updateAlertWithNameEvent) {
    log.info("Received updateAlertEventWithNameEvent.");
    updateAlertWithNameEvent
        .getAlertIdToName()
        .forEach((k, v) -> alertRepository.updateNameById(Long.parseLong(k), v));
    log.info("End processing updateAlertEventWithNameEvent.");
  }

}
