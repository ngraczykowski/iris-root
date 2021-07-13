package com.silenteight.searpayments.bridge;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;

@MessagingGateway
interface SubmitRequest {

  @Gateway(requestChannel = "submitRequestChannel", requestTimeout = 0L)
  void invoke(RequestDto requestDto);
}
