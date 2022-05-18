package com.silenteight.payments.bridge.firco.alertmessage.service;

import org.springframework.data.repository.Repository;

import java.util.Optional;
import java.util.UUID;

@org.springframework.stereotype.Repository
interface AlertMessagePayloadRepository extends Repository<AlertMessagePayload, UUID> {

  AlertMessagePayload save(AlertMessagePayload messageData);

  Optional<AlertMessagePayload> findByAlertMessageId(UUID alertMessageId);

  void deleteByAlertMessageId(UUID alertMessageId);

}
