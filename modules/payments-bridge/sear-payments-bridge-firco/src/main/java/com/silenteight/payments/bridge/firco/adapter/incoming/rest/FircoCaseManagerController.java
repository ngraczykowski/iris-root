package com.silenteight.payments.bridge.firco.adapter.incoming.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.payments.bridge.firco.dto.common.AckDto;
import com.silenteight.payments.bridge.firco.dto.input.RequestDto;
import com.silenteight.payments.bridge.firco.dto.validator.MinimalAlertDefinition;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The main REST controller for Firco Continuity integration, implementing the <em>Case Manager
 * API</em>.
 * <p/>
 * The Firco Continuity system sends message with alert(s) to the {@literal /alert} endpoint.
 * Firco is then waiting for decision on that message, exposing its own HTTP service, and expecting
 * this system to properly transition each message to the correct status.
 */
@RestController
@RequiredArgsConstructor
@Slf4j
class FircoCaseManagerController {

  private final FircoRequestAdapter fircoRequestAdapter;

  @PostMapping("/alert")
  public ResponseEntity<AckDto> sendMessage(
      @RequestBody @Validated(MinimalAlertDefinition.class) RequestDto requestDto,
      @RequestParam(name = "receiveUrl", required = false, defaultValue = "") String receiveUrl,
      @RequestParam(name = "dc", required = false, defaultValue = "") String dataCenter/*,
      HttpServletResponse response*/) {

    fircoRequestAdapter.sendMessage(requestDto, receiveUrl, dataCenter);
    //response.getWriter().write(...);
    //response.getWriter().close();
    //response.flushBuffer();

    // sendMessage to queue.
    // return;
    return ResponseEntity.ok(AckDto.ok());
  }
}
