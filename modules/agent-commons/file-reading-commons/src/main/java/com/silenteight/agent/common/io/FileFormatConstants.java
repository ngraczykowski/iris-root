package com.silenteight.agent.common.io;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class FileFormatConstants {

  static final char COMMENT_MARKER = '#';
  static final char VALUES_SEPARATOR_CHAR = ';';
  static final char QUOTE_CHAR = '"';

  static final String KEY_VALUE_SEPARATOR = "=";
  static final String VALUES_SEPARATOR = ";";

  static final Predicate<String> IGNORE_COMMENT_LINES =
      line -> line.charAt(0) != COMMENT_MARKER;
  static final Predicate<String> IGNORE_EMPTY_LINES =
      StringUtils::isNotEmpty;

  static final UnaryOperator<String> TRIM = String::trim;
  static final UnaryOperator<String> UPPER_CASE = String::toUpperCase;

  static final Collection<Predicate<String>> DEFAULT_DICT_FORMAT_FILTERS =
      List.of(IGNORE_EMPTY_LINES, IGNORE_COMMENT_LINES);
  static final Collection<UnaryOperator<String>> DEFAULT_DICT_FORMAT_TRANSFORMERS =
      List.of(TRIM, UPPER_CASE);
  static final Collection<UnaryOperator<String>> DEFAULT_REGEXP_FORMAT_TRANSFORMERS =
      List.of(TRIM);
}
