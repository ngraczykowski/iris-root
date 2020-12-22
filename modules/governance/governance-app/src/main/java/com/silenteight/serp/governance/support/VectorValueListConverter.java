package com.silenteight.serp.governance.support;

import com.silenteight.proto.serp.v1.alert.VectorValue;

import com.google.common.base.Splitter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.StreamSupport;
import javax.persistence.AttributeConverter;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class VectorValueListConverter
    implements AttributeConverter<List<VectorValue>, String> {

  @Override
  @Nullable
  public String convertToDatabaseColumn(@Nullable List<VectorValue> attribute) {
    if (attribute == null)
      return null;

    return attribute
        .stream()
        .map(VectorValue::getTextValue)
        .collect(joining(","));
  }

  @Override
  @Nullable
  public List<VectorValue> convertToEntityAttribute(@Nullable String dbData) {
    if (dbData == null)
      return null;

    return StreamSupport
        .stream(Splitter.on(',').split(dbData).spliterator(), false)
        .map(value -> VectorValue.newBuilder().setTextValue(value).build())
        .collect(toList());
  }
}
