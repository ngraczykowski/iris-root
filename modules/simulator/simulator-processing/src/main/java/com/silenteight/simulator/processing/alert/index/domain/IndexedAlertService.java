package com.silenteight.simulator.processing.alert.index.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import static com.silenteight.simulator.processing.alert.index.domain.State.SENT;

@RequiredArgsConstructor
public class IndexedAlertService {

  @NonNull
  private final IndexedAlertRepository repository;

  @NonNull
  private final IndexedAlertQuery query;

  public void saveAsSent(@NonNull String requestId, @NonNull String analysisName, long alertCount) {
    IndexedAlertEntity entity = IndexedAlertEntity.builder()
        .requestId(requestId)
        .analysisName(analysisName)
        .alertCount(alertCount)
        .state(SENT)
        .build();

    repository.save(entity);
  }

  public void ack(@NonNull String requestId) {
    IndexedAlertEntity alertEntity = query.getIndexedAlertEntityByRequestId(requestId);
    alertEntity.ack();
    repository.save(alertEntity);
  }
}
