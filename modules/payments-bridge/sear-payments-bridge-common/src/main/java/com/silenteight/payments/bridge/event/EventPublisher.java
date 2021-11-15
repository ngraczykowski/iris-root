package com.silenteight.payments.bridge.event;

public interface EventPublisher {

  <T extends DomainEvent> void send(T event);

}
