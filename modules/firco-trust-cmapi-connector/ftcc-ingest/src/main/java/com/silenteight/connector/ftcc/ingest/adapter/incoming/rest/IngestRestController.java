package com.silenteight.connector.ftcc.ingest.adapter.incoming.rest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.common.dto.input.RequestDto;
import com.silenteight.connector.ftcc.common.dto.output.AckDto;
import com.silenteight.connector.ftcc.ingest.domain.IngestFacade;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
class IngestRestController {

  @NonNull
  private final IngestFacade ingestFacade;

  @PostMapping("/v1/alert")
  public ResponseEntity<AckDto> alert(@RequestBody RequestDto request) {
    log.info("Alert received");
    ingestFacade.ingest(request);
    return ResponseEntity.ok(AckDto.ok());
  }
}
