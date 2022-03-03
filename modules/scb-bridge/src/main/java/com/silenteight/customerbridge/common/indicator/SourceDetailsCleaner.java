package com.silenteight.customerbridge.common.indicator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.of;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class SourceDetailsCleaner {

  private static final Pattern PATTERN = Pattern.compile("\n");
  private static final String LABEL_SEPARATOR = ":";

  static String clean(String sourceDetails) {
    return of(PATTERN.split(sourceDetails))
        .filter(StringUtils::isNotBlank)
        .map(SourceDetailsCleaner::removeInformationNotes)
        .map(String::trim)
        .collect(joining());
  }

  private static String removeInformationNotes(String line) {
    if (!line.contains(LABEL_SEPARATOR))
      return line;

    return line.substring(line.indexOf(LABEL_SEPARATOR) + 1);
  }
}
