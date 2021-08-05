package com.silenteight.payments.bridge.firco.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.firco.dto.input.RequestDto;
import com.silenteight.payments.bridge.firco.ingress.IngressFacade;
import com.silenteight.payments.bridge.firco.security.SecurityFacade;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
class CaseManagerService {

  private final SecurityFacade securityFacade;
  private final IngressFacade ingressFacade;

  void sendMessage(RequestDto requestDto, String dc) {
    securityFacade.authenticate(
        requestDto.getAuthenticationRealm(), requestDto.getAuthenticationToken());
    ingressFacade.handleIncomingAlerts(requestDto.getAlertDataCenters(dc));
  }
}
