package com.silenteight.payments.bridge.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class ModelUpdatedEvent extends DomainEvent {

  public static final String CHANNEL = "modelUpdatedEventChannel";

}
