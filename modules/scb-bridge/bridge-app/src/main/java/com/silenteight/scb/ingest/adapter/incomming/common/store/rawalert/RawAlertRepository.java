package com.silenteight.scb.ingest.adapter.incomming.common.store.rawalert;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface RawAlertRepository extends CrudRepository<RawAlert, UUID>, RawAlertRepositoryExt {

  @Modifying
  @Query("DELETE FROM RawAlert")
  void deleteAll();

}
