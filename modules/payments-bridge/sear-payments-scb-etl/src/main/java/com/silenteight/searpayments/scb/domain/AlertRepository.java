package com.silenteight.searpayments.scb.domain;

import com.silenteight.searpayments.scb.domain.Alert.AlertStatus;
import com.silenteight.searpayments.scb.domain.Alert.DamageReason;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.OffsetDateTime;
import java.util.List;

interface AlertRepository extends Repository<Alert, Long> {

  Alert getById(long id);

  Alert save(Alert alert);

  @Modifying
  @Query("update Alert set status = ?2 where id = ?1")
  void updateStatus(long id, Alert.AlertStatus status);

  @Modifying
  @Query("update Alert set status = ?2,"
      + " outputStatusName = ?3,"
      + " recommendationSentAt = ?4"
      + " where id = ?1")
  void updateStatusAndRecommendationSentAt(
      long id, AlertStatus status, String outputStatusName,
      OffsetDateTime recommendationSentAt);

  @Modifying
  @Query("update Alert set status = ?2, damageReason = ?3 where id = ?1")
  void updateStatusAndDamageReason(
      long alertId, AlertStatus alertStatus,
      DamageReason damageReason);

  @Modifying
  @Query("update Alert set status = ?2, damageReason = ?3 where id in ?1")
  void batchUpdateStatusAndDamageReason(
      List<Long> alertIds, AlertStatus alertStatus,
      DamageReason damageReason);
}
