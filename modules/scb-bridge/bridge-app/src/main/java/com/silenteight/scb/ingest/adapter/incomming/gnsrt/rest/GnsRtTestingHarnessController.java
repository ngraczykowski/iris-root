package com.silenteight.scb.ingest.adapter.incomming.gnsrt.rest;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.common.hitdetails.HitDetailsParser.ParserException;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.generator.GnsRtRequestGenerator;
import com.silenteight.scb.ingest.adapter.incomming.gnsrt.model.request.GnsRtRecommendationRequest;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class GnsRtTestingHarnessController {

  private final GnsRtRequestGenerator gnsRtRequestGenerator;

  @GetMapping(value = "/v1/gnsrt/system-id/{systemId}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<GnsRtRecommendationRequest> getBySystemId(@PathVariable String systemId) {
    return ResponseEntity.ok(gnsRtRequestGenerator.generateBySystemId(systemId));
  }

  @GetMapping(value = "/v1/gnsrt/record-id/{recordId}", produces = APPLICATION_JSON_VALUE)
  public ResponseEntity<GnsRtRecommendationRequest> getByRecordId(@PathVariable String recordId) {
    return ResponseEntity.ok(gnsRtRequestGenerator.generateByRecordId(recordId));
  }

  @ExceptionHandler(EmptyResultDataAccessException.class)
  @ResponseStatus(
      value = HttpStatus.NOT_FOUND, reason = "No alerts have been found for passed identifier")
  public void handleRecordIdNotFoundException() {
    //do nothing
  }

  @ExceptionHandler(ParserException.class)
  @ResponseStatus(
      value = HttpStatus.NOT_FOUND,
      reason = "One of the found alerts has hit details that cannot be parsed")
  public void handleParserException() {
    //do nothing
  }
}
