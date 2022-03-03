package com.silenteight.customerbridge.cbs.gateway;

import lombok.Setter;

import com.silenteight.customerbridge.cbs.gateway.event.CbsCallFailedEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

import java.util.function.Supplier;

public class CbsEventPublisher {

  // NOTE(ahaczewski): Made package-private to allow derived classes use this logger.
  final Logger log = LoggerFactory.getLogger(getClass());

  @Setter
  private ApplicationEventPublisher eventPublisher;

  protected void notifyCbsCallFailed(String functionType) {
    publishEvent(() -> CbsCallFailedEvent.builder().functionType(functionType).build());
  }

  protected void publishEvent(Supplier<Object> eventSupplier) {
    if (eventPublisher != null) {
      Object event = eventSupplier.get();
      if (event != null)
        tryToPublishEvent(event);
    }
  }

  private void tryToPublishEvent(Object event) {
    try {
      eventPublisher.publishEvent(event);
    } catch (Exception e) {
      if (log.isDebugEnabled()) {
        log.debug("Cannot publish event: event={}", event, e);
      } else {
        log.info("Cannot publish event: event={}, error={}, message={}",
            event, e.getClass(), e.getLocalizedMessage());
      }
    }
  }
}
