package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.empty;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class GeoLocationExtractor {

  static String joinFields(String... values) {
    if (isNull(values)) {
      return "";
    }
    return Stream.of(values)
        .filter(StringUtils::isNotBlank)
        .map(String::strip)
        .collect(joining(" "));
  }

  static String mergeFields(List<String> values) {
    if (isNull(values)) {
      return "";
    }
    return values.stream()
        .filter(StringUtils::isNotBlank)
        .map(value -> value.strip().toUpperCase())
        .distinct()
        .collect(joining(" "));
  }

  static Stream<String> splitExtractedValueBySign(SignType signType, String value) {
    return isNull(value) ? empty() : compile(signType.getSign()).splitAsStream(value);
  }

  @AllArgsConstructor
  @Getter
  enum SignType {
    SPACE(" "),
    SEMICOLON(";"),
    COMA(",");

    private final String sign;
  }
}
