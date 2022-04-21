package com.silenteight.connector.ftcc.ingest.adapter.incoming.rest;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.connector.ftcc.common.dto.input.RequestDto;
import com.silenteight.connector.ftcc.common.dto.output.AckDto;
import com.silenteight.connector.ftcc.common.resource.BatchResource;
import com.silenteight.connector.ftcc.ingest.domain.BatchIdGenerator;
import com.silenteight.connector.ftcc.ingest.domain.IngestFacade;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.MDC;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.connector.ftcc.common.MdcParams.BATCH_NAME;

@RestController
@RequiredArgsConstructor
@Slf4j
class IngestRestController {

  @NonNull
  private final IngestFacade ingestFacade;

  @NonNull
  private final ObjectMapper objectMapper;

  @NonNull
  private final BatchIdGenerator batchIdGenerator;

  @PostMapping("/v1/alert")
  public ResponseEntity<AckDto> alert(@RequestBody String request) throws JsonProcessingException {
    UUID batchId = batchIdGenerator.generate();
    MDC.put(BATCH_NAME, BatchResource.toResourceName(batchId));
    try {
      log.info("Alert received");
      log.info("Received request body:\n{}", request);
      ingestFacade.ingest(objectMapper.readValue(request, RequestDto.class), batchId);
      return ResponseEntity.ok(AckDto.ok());
    } finally {
      MDC.remove(BATCH_NAME);
    }
  }
}
