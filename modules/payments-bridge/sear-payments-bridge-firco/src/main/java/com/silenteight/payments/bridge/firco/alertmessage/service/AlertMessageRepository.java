package com.silenteight.payments.bridge.firco.alertmessage.service;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface AlertMessageRepository extends Repository<AlertMessageEntity, UUID> {

  AlertMessageEntity save(AlertMessageEntity messageData);

  Optional<AlertMessageEntity> findById(UUID alertMessageId);

  int deleteAllByIdIn(List<UUID> alertMessageIds);

  /* https://github.com/spring-projects/spring-data-jpa/issues/1796 */
  @Query(value =
      "SELECT cast(pam.alert_message_id as varchar)  as id, pam.received_at as receivedAt"
          + " FROM pb_alert_message pam "
          + " WHERE pam.alert_message_id = :id", nativeQuery = true)
  Optional<AlertIdWithReceivedAtView> findAlertMessageEntitiesByIdProjected(UUID id);

}
