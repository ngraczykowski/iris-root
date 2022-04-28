package com.silenteight.scb.ingest.adapter.incomming.common.store.rawalert;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface RawAlertRepository extends CrudRepository<RawAlert, Long> {

  @Modifying
  @Query("DELETE FROM RawAlert")
  void deleteAll();

}
