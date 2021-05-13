package com.silenteight.adjudication.engine.common.protobuf;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class ProtoStructConverter {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  public Map<String, Object> convert(Object input) {
    return OBJECT_MAPPER.convertValue(input, new TypeReference<>() {});
  }
}
