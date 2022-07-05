package com.silenteight.agent.common.dictionary;

import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import java.util.function.Predicate;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class DictionaryLineFilters {

  /**
   * Ignores lines marked as a comment with '#' character.
   */
  public static final Predicate<String> IGNORE_COMMENTS = line -> line.charAt(0) != '#';

  /**
   * Ignores empty lines.
   */
  public static final Predicate<String> IGNORE_EMPTY_LINES = StringUtils::isNotEmpty;
}
