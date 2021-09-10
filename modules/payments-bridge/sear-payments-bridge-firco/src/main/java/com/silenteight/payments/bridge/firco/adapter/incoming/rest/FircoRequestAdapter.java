package com.silenteight.payments.bridge.firco.adapter.incoming.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.core.alertmessage.port.CreateAlertMessageUseCase;
import com.silenteight.payments.bridge.firco.dto.input.RequestDto;
import com.silenteight.payments.bridge.firco.security.SecurityFacade;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
class FircoRequestAdapter {

  private final SecurityFacade securityFacade;
  private final CreateAlertMessageUseCase createAlertMessageUseCase;

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

    securityFacade.authenticate(requestDto.getAuthenticationToken());
  }

  private void handleRequest(RequestDto requestDto, String receiveUrl, String dataCenter) {
    var mapper = new AlertMessageMapper(receiveUrl, dataCenter);

    var alerts = requestDto.getAlerts();

    if (log.isDebugEnabled()) {
      log.debug("Handling [{}] alert message(s)", alerts.size());
    }

    mapper.map(alerts).forEach(createAlertMessageUseCase::createAlertMessage);
  }
}
