/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.callbackmock.pb;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.callbackmock.CallbackDto;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
class PbCmapiCallbackMockController {

  private static final String PB_MOCK_CMAPI_DESTINATION = "/mock/cmapi/pb";

  private final SimpMessagingTemplate simpMessagingTemplate;

  @PostMapping(PB_MOCK_CMAPI_DESTINATION)
  public AckDto cmapi(
      @RequestBody String callbackBody, @RequestHeader Map<String, String> headers) {
    UUID requestId = UUID.randomUUID();
    log.info("PB CMAPI callback request:{} body: {}", requestId, callbackBody);

    var callback = CallbackDto.builder().body(callbackBody).headers(headers).build();
    simpMessagingTemplate.convertAndSend(PB_MOCK_CMAPI_DESTINATION, callback);
    return AckDto.ok();
  }
}
