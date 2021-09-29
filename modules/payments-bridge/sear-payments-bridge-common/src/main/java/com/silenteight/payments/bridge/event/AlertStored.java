package com.silenteight.payments.bridge.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.model.AlertMessageModel;

/**
 * The alert was persisted in the storage and waiting for further processing (queueing).
 */
@RequiredArgsConstructor
@Getter
public class AlertStored implements DomainEvent {

  private final AlertMessageModel alertModel;

}
