package com.silenteight.hsbc.bridge.recommendation;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

interface RecommendationRepository extends CrudRepository<RecommendationEntity, Long> {

  Optional<RecommendationEntity> findByAlert(String alert);

  boolean existsByName(String name);

  /**
   * Simulator test only (DEV profile). In order to simulate recommendation flow we have to pass the
   * original alert names.
   *
   * @param analysis name of the analysis
   */
  @Query(
      nativeQuery = true,
      value = "SELECT a.name from hsbc_bridge_alert a, hsbc_bridge_analysis an, hsbc_bridge_bulk b"
          + " WHERE an.name = :analysisName AND an.id = b.analysis_id AND a.bulk_id = b.id"
          + " AND a.status != 'ERROR'")
  List<String> getAlertsByAnalysis(@Param("analysisName") String analysis);
}
