/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.domain.payload;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Optional;
import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

@Slf4j
@RequiredArgsConstructor
public class PayloadConverter {

  private final Validator validator;
  private final ObjectMapper objectMapper;

  public <T> Optional<String> serializeFromObjectToJson(T json) {
    try {
      return Optional.ofNullable(objectMapper.writeValueAsString(json));
    } catch (JsonProcessingException e) {
      log.warn("Could not serialize object.", e);
      return Optional.empty();
    }
  }

  public <T, X> T deserializeFromJsonToObject(X json, Class<T> clazz) {
    var deserializedJson = deserialize(json, clazz);
    validate(deserializedJson);
    return deserializedJson;
  }

  private <T> void validate(T input) {
    Set<ConstraintViolation<T>> violations = validator.validate(input);

    if (!violations.isEmpty()) {
      throw new ConstraintViolationException(violations);
    }
  }

  @SneakyThrows
  private <T, X> T deserialize(X json, Class<T> clazz) {
    if (json instanceof byte[] bytes) {
      return objectMapper.readValue(bytes, clazz);
    } else if (json instanceof String string) {
      return objectMapper.readValue(string, clazz);
    }
    throw new IllegalArgumentException(
        "Type specified in X json argument is unsupported please use byte[] or String.");
  }
}
