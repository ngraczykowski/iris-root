package com.silenteight.scb.ingest.adapter.incomming.common.util;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.util.Collection;
import javax.annotation.Nullable;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.collections4.CollectionUtils.emptyCollection;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isBlank;

@AllArgsConstructor
public class MatchingTextTransformer {

  private static final char MATCHING_TEXT_DELIMITER = ',';

  @NonNull
  private final Collection<String> matchingTexts;

  @Getter
  @Nullable
  private String name;

  @Getter
  @Nullable
  private Collection<String> alternateNames;

  public void transform() {
    boolean nameContainsMatchingText = containsAnyOfMatchingTexts(name);
    boolean anyAlternateNameContainsMatchingText =
        transformAlternateNamesWithMatchingText(nameContainsMatchingText);

    if (!nameContainsMatchingText && anyAlternateNameContainsMatchingText)
      clearName();
  }

  private boolean transformAlternateNamesWithMatchingText(boolean nameContainsMatchingText) {
    boolean alternateNamesTransformed = false;

    if (hasAlternateNames()) {
      Collection<String> transformed = transformAlternateNamesWithMatchingText();

      if (transformed.isEmpty() && nameContainsMatchingText)
        clearAlternateNames();

      if (!transformed.isEmpty()) {
        alternateNames = transformed;

        alternateNamesTransformed = true;
      }
    }

    return alternateNamesTransformed;
  }

  private Collection<String> transformAlternateNamesWithMatchingText() {
    if (alternateNames == null)
      return emptyCollection();

    return alternateNames
        .stream()
        .filter(this::containsAnyOfMatchingTexts)
        .collect(toList());
  }

  private boolean hasAlternateNames() {
    return isNotEmpty(alternateNames);
  }

  private void clearAlternateNames() {
    alternateNames = null;
  }

  private boolean containsAnyOfMatchingTexts(@Nullable String value) {
    if (isBlank(value))
      return false;

    return matchingTexts
        .stream()
        .anyMatch(s -> containsMatchingText(s, value));
  }

  private static boolean containsMatchingText(String matchingText, String value) {
    int lastSeparator;
    while ((lastSeparator = matchingText.lastIndexOf(MATCHING_TEXT_DELIMITER)) != -1) {
      matchingText = matchingText.substring(0, lastSeparator);

      if (containsIgnoreCase(value, matchingText))
        return true;
    }

    return false;
  }

  private void clearName() {
    name = null;
  }
}
