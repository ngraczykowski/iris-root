package com.silenteight.warehouse.indexer.production.indextracking;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

interface ProductionMatchRepository extends Repository<ProductionMatchEntity, Long> {

  List<ProductionMatchEntity> findAllByDiscriminatorIn(Iterable<String> discriminators);

  @Modifying
  @Query(nativeQuery = true, value = ""
      + "INSERT INTO warehouse_production_match(alert_discriminator, discriminator, index_name) "
      + "VALUES (:alertDiscriminator, :matchDiscriminator, :indexName) ON CONFLICT DO NOTHING")
  void updateIfNotExists(String alertDiscriminator, String matchDiscriminator, String indexName);
}
