package com.silenteight.connector.ftcc.callback.incoming.rest;

import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.common.dto.output.AckDto;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Profile("mockcallback")
@RestController
@Slf4j
public class DummyCallbackController {

  @PostMapping("/callback")
  public ResponseEntity<AckDto> dummyCallbackHandler(
      @RequestBody JsonNode clientRequestDto) {
    log.info("Request:\n{}", clientRequestDto.toPrettyString());
    return ResponseEntity.ok(AckDto.ok());
  }
}
