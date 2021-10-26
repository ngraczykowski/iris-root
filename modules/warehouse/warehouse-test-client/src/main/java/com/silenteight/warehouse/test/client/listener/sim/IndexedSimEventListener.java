package com.silenteight.warehouse.test.client.listener.sim;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.data.api.v1.DataIndexResponse;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.util.Optional.empty;
import static java.util.Optional.of;

@RequiredArgsConstructor
@Slf4j
public class IndexedSimEventListener {

  @NonNull
  private final List<DataIndexResponse> responses = new CopyOnWriteArrayList<>();

  void onEvent(DataIndexResponse dataIndexResponse) {
    log.info("DataIndexResponse received : {}", dataIndexResponse);
    responses.add(dataIndexResponse);
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

  public Optional<DataIndexResponse> getLastEvent() {
    if (!hasAnyEvent()) {
      return empty();
    }

    int size = responses.size();
    DataIndexResponse lastElement = responses.get(size - 1);
    return of(lastElement);
  }
}
