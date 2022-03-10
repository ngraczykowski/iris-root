package com.silenteight.scb.ingest.domain.payload;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Slf4j
@RequiredArgsConstructor
public class PayloadConverter {

  private final ObjectMapper objectMapper;

  public <T> String serializeFromObjectToJson(T json) {
    return Try
        .of(() -> objectMapper.writeValueAsString(json))
        .onFailure(e -> log.warn("Could not serialize object.", e))
        .getOrElse(EMPTY);
  }

}
