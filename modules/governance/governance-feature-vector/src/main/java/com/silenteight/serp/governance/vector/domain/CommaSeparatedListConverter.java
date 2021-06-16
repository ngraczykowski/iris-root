package com.silenteight.serp.governance.vector.domain;

import java.util.List;
import javax.persistence.AttributeConverter;

import static com.google.common.base.Splitter.on;
import static java.lang.String.join;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

public class CommaSeparatedListConverter implements AttributeConverter<List<String>, String> {

  @Override
  public String convertToDatabaseColumn(List<String> attribute) {
    if (attribute == null)
      return null;

    return join(",", attribute);
  }

  @Override
  @SuppressWarnings("java:S1168")
  public List<String> convertToEntityAttribute(String value) {
    if (value == null)
      return null;

    return stream(on(',').split(value).spliterator(), false)
        .collect(toList());
  }
}
