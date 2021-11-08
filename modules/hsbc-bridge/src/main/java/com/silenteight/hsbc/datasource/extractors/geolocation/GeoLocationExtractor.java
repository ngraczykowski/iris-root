package com.silenteight.hsbc.datasource.extractors.geolocation;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class GeoLocationExtractor {

  static String joinFields(String... values) {
    if (Objects.isNull(values)) {
      return "";
    }
    return Stream.of(values)
        .filter(StringUtils::isNotBlank)
        .map(String::strip)
        .collect(Collectors.joining(" "));
  }

  static String mergeFields(List<String> values) {
    if (Objects.isNull(values)) {
      return "";
    }
    return values.stream()
        .filter(StringUtils::isNotBlank)
        .map(GeoLocationExtractor::stripAndUpper)
        .distinct()
        .collect(Collectors.joining(" "));
  }

  static String stripAndUpper(String value) {
    return Optional.ofNullable(value).map(String::strip).map(String::toUpperCase).orElse("");
  }

  static Stream<String> splitExtractedValueBySign(SignType signType, String value) {
    return Objects.isNull(value) ? Stream.empty() : Pattern.compile(signType.getSign()).splitAsStream(value);
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
