package com.silenteight.adjudication.engine.comments.comment;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import javax.annotation.Nullable;

import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;

@FreemarkerUtils(name = "stringUtils")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FreemarkerStringUtils {

  private static final String DEFAULT_JOIN_SEPARATOR = ", ";

  public static String join(@Nullable List<String> values) {
    return join(values, DEFAULT_JOIN_SEPARATOR);
  }

  public static String join(@Nullable List<String> values, String separator) {
    return ofNullable(values)
        .orElse(emptyList())
        .stream()
        .filter(StringUtils::isNotBlank)
        .distinct()
        .collect(joining(separator));
  }

  public static boolean containsIgnoreCase(@Nullable List<String> values, String value) {
    return ofNullable(values)
        .orElse(emptyList())
        .stream()
        .anyMatch(v -> equalsIgnoreCase(v, value));
  }
}
