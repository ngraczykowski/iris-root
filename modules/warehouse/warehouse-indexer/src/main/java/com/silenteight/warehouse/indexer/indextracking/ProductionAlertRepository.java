package com.silenteight.warehouse.indexer.indextracking;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

interface ProductionAlertRepository extends Repository<ProductionAlertEntity, Long> {

  List<ProductionAlertEntity> findAllByDiscriminatorIn(Iterable<String> discriminators);

  @Modifying
  @Query(nativeQuery = true, value = ""
      + "INSERT INTO warehouse_production_alert(discriminator, index_name) "
      + "VALUES (:discriminator, :indexName) ON CONFLICT DO NOTHING")
  void updateIfNotExists(String discriminator, String indexName);
}
