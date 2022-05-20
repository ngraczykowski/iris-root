package com.silenteight.warehouse.test.client.listener.prod;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@Slf4j
/*
IMPORTANT: usage of this class requires adding @DirtiesContext on the test class.

This bean is typically used in integration tests to store references of the received events,
making it possible to verify that the event has reached the expected target
(subscriber of the AMQP message).

It is essential that there's only a single instance of this bean -
receiving happens on the very same instance that we use later to verify that the event has arrived.

Unfortunately each time Spring wires instances via @Autowired based on configuration detected
via @ComponentScan, it may create a new instance. At the same time, Spring by default caches,
contexts between tests to improve performance. As a result, when having multiple tests that
instantiates this class, we may end up with the same instance registered as
ServiceActivator / subscriber and a new instance autowired in each test,
making it impossible to perform correct test assertion.
See below for further details:
- https://stackoverflow.com/questions/11547240/spring-creating-multiple-instances-of-a-singleton
- https://stackoverflow.com/questions/11975922/spring-multiple-bean-instances-being-created
*/
public class IndexedEventListener {

  @NonNull
  private final List<String> responses = new CopyOnWriteArrayList<>();

  void onEvent(String requestId) {
    log.info("DataIndexResponse received : {}", requestId);
    responses.add(requestId);
  }

  public void clear() {
    responses.clear();
  }

  public boolean hasAnyEvent() {
    return !responses.isEmpty();
  }

  public boolean hasAtLeastEventCount(int count) {
    return responses.size() >= count;
  }

  public Optional<String> getLastEventId() {
    if (!hasAnyEvent()) {
      return empty();
    }

    int size = responses.size();
    String lastElement = responses.get(size - 1);
    return of(lastElement);
  }
}
