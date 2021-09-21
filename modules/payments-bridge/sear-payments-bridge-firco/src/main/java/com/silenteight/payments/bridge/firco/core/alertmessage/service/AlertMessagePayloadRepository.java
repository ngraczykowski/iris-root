package com.silenteight.payments.bridge.firco.core.alertmessage.service;

import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

interface AlertMessagePayloadRepository extends Repository<AlertMessagePayload, UUID> {

  AlertMessagePayload save(AlertMessagePayload messageData);

  Optional<AlertMessagePayload> findByAlertMessageId(UUID alertMessageId);

}
