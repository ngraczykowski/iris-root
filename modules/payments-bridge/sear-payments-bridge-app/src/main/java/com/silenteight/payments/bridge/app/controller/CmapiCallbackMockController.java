package com.silenteight.payments.bridge.app.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.dto.output.AckDto;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@Profile("mockcmapi")
class CmapiCallbackMockController {

  @PostMapping("/mock/cmapi")
  public AckDto cmapi(@RequestBody String callbackBody) {
    UUID requestId = UUID.randomUUID();
    log.info("CMAPI callback request:{} body: {}", requestId, callbackBody);
    return AckDto.ok();
  }
}
