package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface AlertMessageStatusRepository extends Repository<AlertMessageStatusEntity, UUID> {

  long countAllByStatus(AlertMessageStatus status);

  Optional<AlertMessageStatusEntity> findByAlertMessageId(UUID alertMessageId);

  AlertMessageStatusEntity save(AlertMessageStatusEntity entity);

  @Query(value = "SELECT * from pb_alert_message_status a WHERE "
      + "(a.status = 'RECEIVED' AND a.created_at < :dateTime) OR "
      + "(a.status = 'STORED' AND a.stored_at < :dateTime) "
      + "LIMIT :limit FOR UPDATE OF a SKIP LOCKED",
      nativeQuery = true)
  List<AlertMessageStatusEntity> findOutdated(int limit, OffsetDateTime dateTime);

}
