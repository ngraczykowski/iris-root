package com.silenteight.bridge.core.registration.adapter.outgoing;

import lombok.RequiredArgsConstructor;

import com.silenteight.bridge.core.registration.domain.model.Match.Status;
import com.silenteight.bridge.core.registration.domain.port.outgoing.MatchRepository;

import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
class JdbcMatchRepository implements MatchRepository {

  private final CrudMatchRepository crudMatchRepository;

  @Override
  public void updateStatusByBatchIdAndMatchIdInAndExternalAlertIdIn(
      Status status, String batchId, List<String> matchIds, List<String> externalAlertIds) {
    crudMatchRepository.updateStatusByBatchIdAndMatchIdInAndExternalAlertIdIn(
        MatchEntity.Status.valueOf(status.name()), batchId, matchIds, externalAlertIds);
  }
}
