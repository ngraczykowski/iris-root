package com.silenteight.bridge.core.registration.adapter.outgoing;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

interface CrudAlertRepository extends CrudRepository<AlertEntity, Long> {

  Optional<AlertEntity> findByBatchIdAndAlertId(String batchId, String alertId);

  List<AlertEntity> findByBatchIdAndAlertIdIn(String batchId, List<String> alertIds);
}
