package com.silenteight.bridge.core.registration.adapter.outgoing;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

interface CrudAlertRepository extends CrudRepository<AlertEntity, Long> {

  List<AlertIdProjection> findByBatchIdAndAlertIdIn(String batchId, List<String> alertIds);

  List<AlertEntity> findAllByBatchId(String batchId);
}
