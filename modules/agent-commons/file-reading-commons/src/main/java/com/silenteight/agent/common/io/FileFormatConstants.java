package com.silenteight.agent.common.io;

import org.apache.commons.lang3.StringUtils;

import java.util.Set;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

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

  static final Set<Predicate<String>> DEFAULT_DICT_FORMAT_FILTERS =
      Set.of(IGNORE_COMMENT_LINES, IGNORE_EMPTY_LINES);
  static final Set<UnaryOperator<String>> DEFAULT_DICT_FORMAT_TRANSFORMERS =
      Set.of(TRIM, UPPER_CASE);
  static final Set<UnaryOperator<String>> DEFAULT_REGEXP_FORMAT_TRANSFORMERS =
      Set.of(TRIM);
}
