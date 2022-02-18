package com.silenteight.payments.bridge.firco.callback.service;

import com.silenteight.payments.bridge.firco.dto.output.AlertDecisionMessageDto;
import com.silenteight.payments.bridge.firco.dto.output.ClientRequestDto;
import com.silenteight.payments.bridge.firco.dto.output.ReceiveDecisionDto;
import com.silenteight.payments.bridge.firco.dto.output.ReceiveDecisionMessageDto;

import org.springframework.stereotype.Component;

import java.util.List;

import static com.silenteight.payments.bridge.firco.callback.service.ClientRequestDtoMapper.mapToAuthentication;

@Component
class ClientRequestDtoFactory {

  public ClientRequestDto create(AlertDecisionMessageDto source) {
    var receiveDecisionMessageDto = new ReceiveDecisionMessageDto();
    receiveDecisionMessageDto.setDecisionMessage(source);

    var receiveDecisionDto = new ReceiveDecisionDto();
    receiveDecisionDto.setMessages(List.of(receiveDecisionMessageDto));
    receiveDecisionDto.setAuthentication(mapToAuthentication(source));

    var clientRequestDto = new ClientRequestDto();
    clientRequestDto.setReceiveDecisionDto(receiveDecisionDto);
    return clientRequestDto;
  }
}
