package com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing;

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertid.AlertId;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertUnderProcessing.State;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nonnull;

import static java.util.stream.Collectors.toList;

class InMemoryAlertUnderProcessingRepository implements AlertUnderProcessingRepository {

  private static final int DEFAULT_PRIORITY = 1;
  private final List<AlertUnderProcessing> store = new ArrayList<>();

  @Override
  public Collection<AlertUnderProcessing> findAll() {
    return store;
  }

  @Override
  public Collection<AlertId> findAllBySystemIdIn(Collection<String> systemIds) {
    return store.stream()
        .filter(a -> systemIds.contains(a.getSystemId()))
        .map(a -> new AlertId(a.getSystemId(), a.getBatchId()))
        .collect(toList());
  }

  @Override
  public void saveAll(Iterable<AlertUnderProcessing> entities) {
    store.addAll((Collection<? extends AlertUnderProcessing>) entities);
  }

  @Override
  public void deleteBySystemIdAndBatchId(String systemId, String batchId) {
    store.removeIf(matchPredicate(systemId, batchId));
  }

  @Override
  public void deleteByCreatedAtBeforeAndStateIn(
      OffsetDateTime expireDate, EnumSet<State> states) {
    store.removeIf(p -> p.getCreatedAt().isBefore(expireDate) && states.contains(p.getState()));
  }

  @Override
  public void update(String systemId, String batchId, State state) {
    deleteBySystemIdAndBatchId(systemId, batchId);

    store.add(
        new AlertUnderProcessing(
            systemId,
            batchId,
            state,
            null,
            DEFAULT_PRIORITY,
            getPayload()));
  }

  @Override
  public void update(String systemId, String batchId, State state, String error) {
    deleteBySystemIdAndBatchId(systemId, batchId);

    store.add(
        new AlertUnderProcessing(
            systemId,
            batchId,
            state,
            error,
            DEFAULT_PRIORITY,
            getPayload()));
  }

  @Override
  public Collection<AlertUnderProcessing> findTopNByStateOrderByPriorityDesc(
      AlertUnderProcessing.State state, int limit) {
    return List.of();
  }

  @Override
  public long countByState(AlertUnderProcessing.State state) {
    return store.stream()
        .filter(alertUnderProcessing -> state.equals(alertUnderProcessing.getState()))
        .count();
  }

  private byte[] getPayload() {
    return ScbAlertIdContext.newBuilder().build().toByteArray();
  }

  @Nonnull
  private static Predicate<AlertUnderProcessing> matchPredicate(String systemId, String batchId) {
    return p -> p.getBatchId().equals(batchId) && p.getSystemId().equals(systemId);
  }
}
