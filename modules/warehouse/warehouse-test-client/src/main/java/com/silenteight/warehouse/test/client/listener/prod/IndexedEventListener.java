package com.silenteight.warehouse.test.client.listener.prod;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@RequiredArgsConstructor
@Slf4j
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
