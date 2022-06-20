/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.recommendation;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ScbRecommendationRepository extends CrudRepository<ScbRecommendation, Long> {

  @Query(value = "SELECT * FROM ( "
      + "SELECT *, row_number() over (PARTITION BY system_id "
      + "ORDER BY recommended_at DESC) "
      + "AS row_number "
      + "FROM scb_recommendation "
      + "WHERE system_id IN (:systemIds)) "
      + "TEMP WHERE row_number = 1", nativeQuery = true)
  List<ScbRecommendation> findAllBySystemIdIn(List<String> systemIds);

  @Modifying
  @Query("DELETE FROM ScbRecommendation")
  void deleteAll();

  @Modifying
  @Query(value =
      "UPDATE scb_recommendation SET recom_status = :status "
          + "WHERE scb_recommendation_id = (SELECT scb_recommendation_id "
          + "FROM scb_recommendation WHERE system_id = :systemId AND watchlist_id = :watchlistId "
          + "ORDER BY created_at DESC LIMIT 1)", nativeQuery = true)
  void updateRecomStatus(String systemId, String watchlistId, String status);
}
