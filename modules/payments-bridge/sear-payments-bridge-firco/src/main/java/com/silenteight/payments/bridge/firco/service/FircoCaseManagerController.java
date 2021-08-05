package com.silenteight.payments.bridge.firco.service;

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

import javax.validation.constraints.NotBlank;

@RestController
@RequiredArgsConstructor
@Slf4j
class FircoCaseManagerController {

  private final CaseManagerService caseManagerService;

  @PostMapping("/sendMessage")
  public ResponseEntity<AckDto> sendMessage(
      @RequestBody @Validated(MinimalAlertDefinition.class) RequestDto requestDto,
      @RequestParam @NotBlank String dc) {

    caseManagerService.sendMessage(requestDto, dc);

    return ResponseEntity.ok(AckDto.ok());
  }
}
