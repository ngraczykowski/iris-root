package com.silenteight.sens.webapp.backend.changerequest.domain;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.stream.Collectors.toList;

class InMemoryChangeRequestRepository implements ChangeRequestRepository {

  private final AtomicLong nextId = new AtomicLong(1);
  private final Map<Long, ChangeRequest> store = new ConcurrentHashMap<>();

  @Override
  public ChangeRequest getById(long id) {
    return store.get(id);
  }

  @Override
  public ChangeRequest save(ChangeRequest changeRequest) {
    long id = nextId.getAndIncrement();
    store.put(id, changeRequest);
    changeRequest.setId(id);
    return changeRequest;
  }

  @Override
  public List<ChangeRequest> findAllByState(ChangeRequestState state) {
    return store
        .values()
        .stream()
        .filter(changeRequest -> changeRequest.getState().equals(state))
        .collect(toList());
  }

  ChangeRequest getByBulkChangeId(UUID bulkChangeId) {
    return store
        .values()
        .stream()
        .filter(changeRequest -> changeRequest.getBulkChangeId().equals(bulkChangeId))
        .findFirst()
        .orElseThrow();
  }
}
