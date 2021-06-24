package com.silenteight.adjudication.engine.common.protobuf;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.base.common.support.jackson.JsonConversionHelper;

import com.fasterxml.jackson.databind.type.MapType;
import org.apache.commons.lang3.ObjectUtils;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ObjectToMapConverter {

  private static final MapType MAP_TYPE;

  static {
    MAP_TYPE = JsonConversionHelper.INSTANCE.objectMapper()
        .getTypeFactory()
        .constructMapType(HashMap.class, String.class, Object.class);
  }

  public static Map<String, Object> convert(@Nullable Object input) {
    if (ObjectUtils.isNotEmpty(input)) {
      return JsonConversionHelper.INSTANCE.objectMapper().convertValue(input, MAP_TYPE);
    } else {
      log.warn("There is no input object to convert to map<string,object>");
      return Map.of();
    }
  }
}
