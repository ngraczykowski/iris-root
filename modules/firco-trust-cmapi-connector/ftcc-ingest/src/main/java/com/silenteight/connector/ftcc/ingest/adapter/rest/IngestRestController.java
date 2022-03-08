package com.silenteight.connector.ftcc.ingest.adapter.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.ingest.dto.input.RequestDto;
import com.silenteight.connector.ftcc.ingest.dto.output.AckDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
class IngestRestController {

  @PostMapping("/v1/alert")
  public ResponseEntity<AckDto> alert(@RequestBody RequestDto request) {
    return ResponseEntity.ok(AckDto.ok());
  }
}

