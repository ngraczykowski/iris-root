package com.silenteight.payments.bridge.svb.oldetl.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;

/**
 * Address regex based on alphanumeric/numeric tokens with 1-6 numbers to end of string
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class NameAddressRegexExtractionHelper {

  private static final List<String> KEY_WORDS = asList(
      "(NO|UNIT|FLAT|APT|HOUSE|LOT\\sNO)+[,\\.\\s]{1,2}\\d|\\/[^ ]{1,6}[\\-]?[A-Z]*?",
      "(NO\\.?)?\\s?[A-Z]*[\\-]?\\d{1,6}[A-Z]*",
      "\\d|\\/[^ ]{1,6}");

  private static final String ADDR_REGEX =
      String.format("((?<=\\s)(%s[\\s,])([^\\t]+))", String.join("|", KEY_WORDS));

  private static final Pattern PATTERN = Pattern.compile(ADDR_REGEX);

  static NameAddressResult extractNameAddress(String text) {
    if (null == text)
      return NameAddressResult.of(null, null);

    Matcher matcher = PATTERN.matcher(text);
    if (!matcher.find())
      return NameAddressResult.of(text, null);

    String address = matcher.group(0).trim();
    int addressStartSpan = matcher.start(0);
    String name = text.substring(0, addressStartSpan).trim().replaceAll(",$", "");

    return NameAddressResult.of(name, address);
  }

  @Value(staticConstructor = "of")
  public static class NameAddressResult {

    String name;
    String address;
  }
}
