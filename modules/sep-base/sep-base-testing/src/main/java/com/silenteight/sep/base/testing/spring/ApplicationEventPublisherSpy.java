package com.silenteight.sep.base.testing.spring;

import org.springframework.context.ApplicationEventPublisher;

import java.util.LinkedList;
import java.util.Queue;

public class ApplicationEventPublisherSpy implements ApplicationEventPublisher {

  private final Queue<Object> eventsQueue = new LinkedList<>();

  @Override
  public void publishEvent(Object event) {
    eventsQueue.add(event);
  }

  public <T> T getNext(Class<T> eventType) {
    ensureNotEmpty();

    Object nextEvent = eventsQueue.poll();
    return eventType.cast(nextEvent);
  }

  public Object getNext() {
    ensureNotEmpty();
    return eventsQueue.poll();
  }

  public void skip() {
    ensureNotEmpty();
    eventsQueue.remove();
  }

  public boolean isEmpty() {
    return eventsQueue.isEmpty();
  }

  private void ensureNotEmpty() {
    if (isEmpty())
      throw new IllegalStateException("No events found!");
  }
}
