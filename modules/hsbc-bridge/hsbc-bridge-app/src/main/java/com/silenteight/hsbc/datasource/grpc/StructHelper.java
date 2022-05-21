package com.silenteight.hsbc.datasource.grpc;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.google.protobuf.ListValue;
import com.google.protobuf.Value;

import java.util.Collection;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class StructHelper {

  static Value buildStringValue(String value) {
    return Value.newBuilder().setStringValue(value).build();
  }

  static Value buildNumberValue(double value) {
    return Value.newBuilder().setNumberValue(value).build();
  }

  static Value buildListValues(Collection<String> values) {
    return Value.newBuilder().setListValue(
        ListValue.newBuilder().addAllValues(mapToValues(values))).build();
  }

  private static Iterable<? extends Value> mapToValues(Collection<String> collection) {
    return collection.stream()
        .map(value -> Value.newBuilder().setStringValue(value).build())
        .collect(Collectors.toList());
  }
}
