package com.silenteight.hsbc.bridge.common.protobuf;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import com.google.protobuf.ListValue;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
import com.google.protobuf.Value.KindCase;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import static com.google.protobuf.Value.KindCase.BOOL_VALUE;
import static com.google.protobuf.Value.KindCase.NULL_VALUE;
import static com.google.protobuf.Value.KindCase.NUMBER_VALUE;
import static com.google.protobuf.Value.KindCase.STRING_VALUE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
public class StructMapper {

  public static Map<String, String> toMap(@NonNull Struct struct) {
    var result = new HashMap<String, String>();
    var fields = struct.getFieldsMap();

    fields.forEach((k, v) -> result.putAll(mapValues(k, v)));
    return new TreeMap<>(result);
  }

  private static Map<String, String> toMap(String keyPrefix, @NonNull Struct struct) {
    var result = new HashMap<String, String>();
    var fields = struct.getFieldsMap();

    fields.forEach((k, v) -> result.putAll(mapValues(keyPrefix + "." + k, v)));
    return result;
  }

  private static Map<String, String> mapValues(String key, Value value) {
    if (isSingleValueType(value.getKindCase())) {
      return Map.of(key, getSingleValueAsString(value));
    } else if (value.hasStructValue()) {
      return toMap(key, value.getStructValue());
    } else if (value.hasListValue()) {
      return getListValues(key, value.getListValue());
    }

    return Map.of();
  }

  private static String getSingleValueAsString(Value value) {
    if (value.hasStringValue()) {
      return value.getStringValue();
    } else if (value.hasNumberValue()) {
      return "" + value.getNumberValue();
    } else if (value.hasBoolValue()) {
      return "" + value.getBoolValue();
    }

    return "";
  }

  private static Map<String, String> getListValues(String key, ListValue listValue) {
    var result = new HashMap<String, String>();
    var values = listValue.getValuesList();

    for (int i = 0; i < values.size(); i++) {
      var value = values.get(i);
      result.putAll(mapValues(key + "[" + i + "]", value));
    }

    return result;
  }

  private static boolean isSingleValueType(KindCase type) {
    return type == BOOL_VALUE || type == NULL_VALUE || type == STRING_VALUE || type == NUMBER_VALUE;
  }
}
