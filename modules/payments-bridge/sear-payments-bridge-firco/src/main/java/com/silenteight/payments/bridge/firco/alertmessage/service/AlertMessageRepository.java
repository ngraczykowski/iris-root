package com.silenteight.payments.bridge.firco.alertmessage.service;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface AlertMessageRepository extends Repository<AlertMessageEntity, UUID> {

  AlertMessageEntity save(AlertMessageEntity messageData);

  Optional<AlertMessageEntity> findById(UUID alertMessageId);

  int deleteAllByIdIn(List<UUID> alertMessageIds);
}
