package com.silenteight.searpayments.scb.etl.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;

/**
 * Extract corporate name based on start of string to corporate-related words like "LTD"
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CorpNameRegexExtractionHelper {

  private static final List<String> KEY_WORDS =
      asList("LLC", "LTD\\.?", "LIMITED", "INC\\.?", "P[\\/\\s]L", "SDN[\\.]?\\sBHD[\\.]?", "PTY");

  private static final String CORP_NAME_REGEX =
      String.format("([^}]+?(%s)(?=[\\s,$]|$))", String.join("|", KEY_WORDS));

  private static final Pattern PATTERN = Pattern.compile(CORP_NAME_REGEX);

  static Pair<String, Integer> extractCorpName(String text) {
    if (null == text)
      return Pair.of(null, null);

    Matcher matcher = PATTERN.matcher(text);
    if (!matcher.find())
      return Pair.of(null, null);

    String name = matcher.group(0).strip();
    int endSpan = matcher.end(0);
    return Pair.of(name, endSpan);
  }

  static Pair<String, String> extractAddrCorpName(String text) {
    Pair<String, Integer> corpName = extractCorpName(text);
    if (corpName.getLeft() != null) {
      int addressBegin = corpName.getRight() + 1;
      String address = null;
      if (addressBegin < text.length()) {
        address = text.substring(addressBegin).trim();
      }
      return Pair.of(corpName.getLeft(), address);
    }
    return Pair.of(null, null);
  }
}
