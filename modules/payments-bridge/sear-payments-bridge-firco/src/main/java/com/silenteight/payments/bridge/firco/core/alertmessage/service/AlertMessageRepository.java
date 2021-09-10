package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import org.springframework.data.repository.Repository;

import java.util.UUID;

interface AlertMessageRepository extends Repository<AlertMessageEntity, UUID> {

  AlertMessageEntity save(AlertMessageEntity messageData);
}
