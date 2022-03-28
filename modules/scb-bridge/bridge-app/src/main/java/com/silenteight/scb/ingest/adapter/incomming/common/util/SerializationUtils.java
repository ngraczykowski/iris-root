package com.silenteight.scb.ingest.adapter.incomming.common.util;

import lombok.experimental.UtilityClass;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@UtilityClass
public class SerializationUtils {

  private final ObjectMapper objectMapper = new ObjectMapper()
      .registerModule(new JavaTimeModule());

  public byte[] serialize(Object object) throws JsonProcessingException {
    return objectMapper.writeValueAsBytes(object);
  }
}
