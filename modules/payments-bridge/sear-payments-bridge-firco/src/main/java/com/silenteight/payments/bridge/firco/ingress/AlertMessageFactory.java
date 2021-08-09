package com.silenteight.payments.bridge.firco.ingress;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.common.protobuf.ProtobufStructConverter;
import com.silenteight.payments.bridge.firco.dto.input.AlertMessageDto;
import com.silenteight.proto.payments.bridge.internal.v1.AlertMessage;

import com.google.protobuf.Timestamp;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
class AlertMessageFactory {

  private final AlertMessageNameGenerator alertMessageNameGenerator;
  private final ProtobufStructConverter protobufStructConverter;

  AlertMessage create(AlertMessageDto dto, Timestamp receiveTime, String endpoint) {
    var alertMessage = protobufStructConverter
        .toStruct(dto)
        .orElseThrow(AlertMessageCreationException::new);

    return AlertMessage.newBuilder()
        .setName(alertMessageNameGenerator.generateName())
        .setAlertMessage(alertMessage)
        .setReceiveTime(receiveTime)
        .setRecommendationEndpoint(endpoint)
        .build();
  }

  private static final class AlertMessageCreationException extends RuntimeException {

    private static final long serialVersionUID = -2830582968738155698L;

    AlertMessageCreationException() {
    }
  }
}
