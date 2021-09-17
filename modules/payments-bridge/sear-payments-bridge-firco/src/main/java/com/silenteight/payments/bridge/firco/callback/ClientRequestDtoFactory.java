package com.silenteight.payments.bridge.firco.callback;

import com.silenteight.payments.bridge.firco.dto.output.*;

import org.springframework.stereotype.Component;

import java.util.List;

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

  private FircoAuthenticationDto mapToAuthentication(AlertDecisionMessageDto source) {
    FircoAuthenticationDto authentication = new FircoAuthenticationDto();
    authentication.setContinuityLogin("Login");
    authentication.setContinuityPassword("Password");
    authentication.setContinuityBusinessUnit(source.getBusinessUnit());
    return authentication;
  }

}
