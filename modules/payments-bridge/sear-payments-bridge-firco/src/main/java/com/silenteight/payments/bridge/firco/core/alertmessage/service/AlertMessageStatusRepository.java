package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import com.silenteight.payments.bridge.firco.core.alertmessage.model.AlertMessageStatus;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.persistence.LockModeType;
import javax.persistence.QueryHint;

interface AlertMessageStatusRepository extends Repository<AlertMessageStatusEntity, UUID> {

  long countAllByStatus(AlertMessageStatus status);

  Optional<AlertMessageStatusEntity> findByAlertMessageId(UUID alertMessageId);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @QueryHints({ @QueryHint(name = "javax.persistence.lock.timeout", value = "200")})
  @Query("FROM AlertMessageStatus a WHERE a.alertMessageId = :alertMessageId")
  Optional<AlertMessageStatusEntity> findByAlertMessageIdAndLockForWrite(UUID alertMessageId);

  AlertMessageStatusEntity save(AlertMessageStatusEntity entity);

  @Query(value = "SELECT * from pb_alert_message_status a WHERE "
      + "(a.status = 'RECEIVED' AND a.created_at < :dateTime) OR "
      + "(a.status = 'STORED' AND a.stored_at < :dateTime) "
      + "LIMIT :limit FOR UPDATE OF a SKIP LOCKED",
      nativeQuery = true)
  List<AlertMessageStatusEntity> findOutdated(int limit, OffsetDateTime dateTime);

}
