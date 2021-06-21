package com.silenteight.adjudication.engine.common.protobuf;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import org.apache.commons.lang3.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
public class ProtoStructConverter {

  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  private static final MapType MAP_TYPE;

  static {
    MAP_TYPE = OBJECT_MAPPER
        .getTypeFactory()
        .constructMapType(HashMap.class, String.class, Object.class);
  }

  public Map<String, Object> convert(Object input) {
    if (ObjectUtils.isNotEmpty(input)) {
      return OBJECT_MAPPER.convertValue(input, MAP_TYPE);
    } else {
      log.warn("There is no input object to convert to map<string,object>");
      return Map.of();
    }
  }
}
