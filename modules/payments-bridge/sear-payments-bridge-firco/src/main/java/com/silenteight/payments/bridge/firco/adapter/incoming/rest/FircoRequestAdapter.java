package com.silenteight.payments.bridge.firco.adapter.incoming.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.adapter.incoming.dto.input.AlertMessageDto;
import com.silenteight.payments.bridge.firco.adapter.incoming.dto.input.RequestDto;
import com.silenteight.payments.bridge.firco.alertmessage.port.CreateAlertMessageUseCase;
import com.silenteight.payments.bridge.firco.security.port.AuthenticateUseCase;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
class FircoRequestAdapter {

  private final AuthenticateUseCase authenticateUseCase;
  private final CreateAlertMessageUseCase createAlertMessageUseCase;
  private final ObjectMapper objectMapper;

  void sendMessage(RequestDto requestDto, String receiveUrl, String dataCenter) {
    authenticateRequest(requestDto);
    handleRequest(requestDto, receiveUrl, dataCenter);
  }

  private void authenticateRequest(RequestDto requestDto) {
    if (log.isDebugEnabled()) {
      log.debug(
          "Authenticating the user [{}] in the realm [{}]...",
          requestDto.getAuthenticationToken().getPrincipal(), requestDto.getAuthenticationRealm());
    }

    authenticateUseCase.authenticate(requestDto.getAuthenticationToken());
  }

  private void handleRequest(RequestDto requestDto, String receiveUrl, String dataCenter) {
    var mapper = new AlertMessageMapper(receiveUrl, dataCenter);

    var alerts = requestDto.getAlerts();

    if (log.isDebugEnabled()) {
      log.debug("Handling [{}] alert message(s)", alerts.size());
    }

    alerts.forEach(alertDto -> createAlertMessageUseCase
        .createAlertMessage(mapper.map(alertDto), toObjectNode(alertDto)));
  }

  private ObjectNode toObjectNode(AlertMessageDto alertMessageDto) {
    return objectMapper.convertValue(alertMessageDto, ObjectNode.class);
  }
}
