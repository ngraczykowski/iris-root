package com.silenteight.connector.ftcc.ingest.adapter.rest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.ingest.dto.input.RequestDto;
import com.silenteight.connector.ftcc.ingest.dto.output.AckDto;
import com.silenteight.connector.ftcc.ingest.registration.RegistrationService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static java.util.UUID.randomUUID;

@RestController
@RequiredArgsConstructor
@Slf4j
class IngestRestController {

  @NonNull
  private final RegistrationService registrationService;

  @PostMapping("/v1/alert")
  public ResponseEntity<AckDto> alert(@RequestBody RequestDto request) {
    String batchId = randomUUID().toString();
    registrationService.registerBatch(batchId, request);
    return ResponseEntity.ok(AckDto.ok());
  }
}

