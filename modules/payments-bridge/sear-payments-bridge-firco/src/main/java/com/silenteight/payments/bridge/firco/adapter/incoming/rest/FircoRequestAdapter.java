package com.silenteight.payments.bridge.firco.adapter.incoming.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.alertmessage.port.CreateAlertMessageUseCase;
import com.silenteight.payments.bridge.firco.dto.input.RequestDto;
import com.silenteight.payments.bridge.firco.security.port.AuthenticateUseCase;
import com.silenteight.sep.base.aspects.metrics.Timed;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
class FircoRequestAdapter {

  private final AuthenticateUseCase authenticateUseCase;
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

    authenticateUseCase.authenticate(requestDto.getAuthenticationToken());
  }

  @Timed
  private void handleRequest(RequestDto requestDto, String receiveUrl, String dataCenter) {
    var mapper = new AlertMessageMapper(receiveUrl, dataCenter);
    log.info("Handling [{}] alert message(s)", requestDto.getAlerts().size());
    mapper.map(requestDto).forEach(createAlertMessageUseCase::createAlertMessage);
    log.info("Finished Handling [{}] alert message(s)", requestDto.getAlerts().size());
  }

}
