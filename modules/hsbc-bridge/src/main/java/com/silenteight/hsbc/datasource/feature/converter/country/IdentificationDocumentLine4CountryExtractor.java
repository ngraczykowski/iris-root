package com.silenteight.hsbc.datasource.feature.converter.country;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class IdentificationDocumentLine4CountryExtractor {

  private final String fieldValue;

  public Optional<String> extract() {
    var splitFieldValue = fieldValue.split(",");

    if (splitFieldValue.length <= 3 || !splitFieldValue[0].equals("\"NID\"")) {
      return Optional.empty();
    }

    return Optional.of(splitFieldValue[2].replace("\"", ""));
  }
}
