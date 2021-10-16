package com.silenteight.payments.bridge.svb.oldetl.util;

import lombok.NonNull;

import com.silenteight.payments.bridge.svb.oldetl.response.MessageFieldStructure;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public class StringUtil {

  private static final String LINE_BREAK = "\r\n|\n|\r";
  private static final Pattern LINE_BREAK_PATTERN = compile(LINE_BREAK);
  private static final List<String> LINE_BREAKERS1 = Arrays.asList("\r\n[1-8]/", "\n[1-8]/");
  private static final List<String> LINE_BREAKERS2 = Arrays.asList("\r\n", "\n", "\r");

  private StringUtil() {

  }

  public static String lfToCrLf(String input) {
    if (input == null)
      return null;

    // @mwilkowski: I am aware that the code is far from elegant
    // but it works, it is easy and performance here is not crucialt
    return input
        .replace("\r\n", "\n")
        .replace("\n", "\r\n");
  }

  private static final String LEGAL_REGEX_CHARACTERS =
      "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789_ ";

  public static String regexEscape(@NonNull String input) {
    var len = input.length();
    StringBuilder buffer = new StringBuilder();
    for (int i = 0; i < len; i++) {
      var c = input.charAt(i);
      if (LEGAL_REGEX_CHARACTERS.indexOf(c) < 0)
        buffer.append('\\');
      buffer.append(c);
    }

    return buffer.toString();
  }

  public static String removeLineBreakersFromMatchingText(
      String matchingText, String messageFieldStructure) {

    if (messageFieldStructure.equals(MessageFieldStructure.NAMEADDRESS_FORMAT_F.toString()))
      return replaceEveryRegexFromListWithString(matchingText, LINE_BREAKERS1, "");
    else
      return replaceEveryRegexFromListWithString(matchingText, LINE_BREAKERS2, "");

  }

  @NotNull
  public static Matcher createMatcherFromLineBreakinPattern(String matchingTextPattern) {
    return LINE_BREAK_PATTERN.matcher(matchingTextPattern);
  }

  @NotNull
  public static Pattern getPattern(
      MessageFieldStructure messageFieldStructure, @NonNull String matchingText) {

    if (messageFieldStructure == MessageFieldStructure.NAMEADDRESS_FORMAT_F)
      return createPatternAfterReplacingRegex(
          matchingText, LINE_BREAKERS1);
    else
      return createPatternAfterReplacingRegex(matchingText, LINE_BREAKERS2);
  }

  @NotNull
  private static Pattern createPatternAfterReplacingRegex(
      String matchingText, List<String> replaceRegex) {

    String modifiedTextWithSpaces =
        replaceEveryRegexFromListWithString(matchingText, replaceRegex, " ");
    String modifiedTextWithoutSpaces =
        replaceEveryRegexFromListWithString(matchingText, replaceRegex, "");

    String joinedStrings = String.join(
        "|",
        Pattern.quote(modifiedTextWithSpaces.strip()),
        Pattern.quote(modifiedTextWithoutSpaces.strip()));
    return compile(joinedStrings);
  }

  private static String replaceEveryRegexFromListWithString(
      String matchingText, List<String> replaceRegex, String replacementString) {

    StringBuilder modifiedTextSB = new StringBuilder();
    modifiedTextSB.append(matchingText);

    for (String regex : replaceRegex) {

      replaceSubstring(modifiedTextSB, regex, replacementString);

    }
    return modifiedTextSB.toString();
  }

  private static StringBuilder replaceSubstring(
      StringBuilder builder, String stringToBeReplaced, String replacementString) {

    String temp1 = builder.toString();
    builder.setLength(0);
    builder.append(cleanString(temp1, stringToBeReplaced, replacementString));
    return builder;
  }

  private static String cleanString(String source, String toBeReplaced, String replacement) {
    return source.replaceAll(toBeReplaced, replacement);
  }
}
