package com.silenteight.hsbc.bridge.match;

import lombok.NonNull;

import com.silenteight.hsbc.bridge.rest.model.input.AlertSystemInformation;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

class MatchRawMapper {

  private final ObjectMapper objectMapper;

  MatchRawMapper() {
    objectMapper = new ObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  MatchRawData map(@NonNull AlertSystemInformation systemInformation) {
    return objectMapper.convertValue(systemInformation, MatchRawData.class);
  }
}
