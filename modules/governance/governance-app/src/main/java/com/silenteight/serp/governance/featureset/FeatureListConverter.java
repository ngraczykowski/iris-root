package com.silenteight.serp.governance.featureset;

import com.google.common.base.Splitter;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.StreamSupport;
import javax.persistence.AttributeConverter;

import static java.util.stream.Collectors.toList;

public class FeatureListConverter implements AttributeConverter<List<String>, String> {

  @Override
  @Nullable
  public String convertToDatabaseColumn(@Nullable List<String> attribute) {
    if (attribute == null)
      return null;

    return String.join(",", attribute);
  }

  @Override
  @Nullable
  // NOTE(ahaczewski): Suppressed because that is what AttributeConverter interface requires.
  @SuppressWarnings("squid:S1168")
  public List<String> convertToEntityAttribute(@Nullable String dbData) {
    if (dbData == null)
      return null;

    return StreamSupport
        .stream(Splitter.on(',').split(dbData).spliterator(), false)
        .collect(toList());
  }
}
