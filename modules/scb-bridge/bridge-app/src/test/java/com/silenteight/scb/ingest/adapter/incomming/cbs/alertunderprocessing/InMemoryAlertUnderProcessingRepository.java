package com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing;

import com.silenteight.proto.serp.scb.v1.ScbAlertIdContext;
import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertUnderProcessing.State;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nonnull;

import static java.util.stream.Collectors.toList;

class InMemoryAlertUnderProcessingRepository implements AlertUnderProcessingRepository {

  private static final int DEFAULT_PRIORITY = 1;
  private List<AlertUnderProcessing> store = new ArrayList<>();

  @Override
  public Collection<AlertUnderProcessing> findAll() {
    return store;
  }

  @Override
  public Collection<AlertUnderProcessing> findAllBySystemIdIn(Collection<String> systemIds) {
    return store.stream().filter(a -> systemIds.contains(a.getSystemId())).collect(toList());
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
  public void deleteByCreatedAtBefore(OffsetDateTime expireDate) {
    store.removeIf(p -> p.getCreatedAt().isBefore(expireDate));
  }

  @Override
  public void update(String systemId, String batchId, State state) {
    deleteBySystemIdAndBatchId(systemId, batchId);

    store.add(
        new AlertUnderProcessing(
            systemId, batchId, "5a209f6d-cb00-44e6-a603-2057bc63da4c", state, null,
            DEFAULT_PRIORITY, getPayload()));
  }

  @Override
  public void update(String systemId, String batchId, State state, String error) {
    deleteBySystemIdAndBatchId(systemId, batchId);

    store.add(
        new AlertUnderProcessing(
            systemId, batchId, "5a209f6d-cb00-44e6-a603-2057bc63da4c", state, error,
            DEFAULT_PRIORITY, getPayload()));
  }

  @Override
  public Collection<AlertUnderProcessing> findTop2000ByErrorIsNullOrderByPriorityDesc() {
    return List.of();
  }

  @Nonnull
  private static Predicate<AlertUnderProcessing> matchPredicate(String systemId, String batchId) {
    return p -> p.getBatchId().equals(batchId) && p.getSystemId().equals(systemId);
  }

  private byte[] getPayload() {
    return ScbAlertIdContext.newBuilder().build().toByteArray();
  }
}
