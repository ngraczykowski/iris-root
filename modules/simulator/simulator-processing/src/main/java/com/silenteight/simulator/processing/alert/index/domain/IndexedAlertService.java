package com.silenteight.simulator.processing.alert.index.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static com.silenteight.simulator.processing.alert.index.domain.State.SENT;

@Slf4j
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
    log.debug("Saved as 'SENT' IndexedAlertEntity={}", entity);
  }

  public void ack(@NonNull String requestId) {
    IndexedAlertEntity alertEntity = query.getIndexedAlertEntityByRequestId(requestId);
    alertEntity.ack();
    alertEntity.setCurrentTimeForUpdatedAt();
    repository.save(alertEntity);
    log.debug("Acked IndexedAlertEntity={}", alertEntity);
  }
}
