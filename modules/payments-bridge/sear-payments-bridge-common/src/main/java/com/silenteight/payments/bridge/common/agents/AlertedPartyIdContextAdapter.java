package com.silenteight.payments.bridge.common.agents;

import lombok.RequiredArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.String.join;
import static java.util.Arrays.copyOfRange;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.normalizeSpace;

@RequiredArgsConstructor
public class AlertedPartyIdContextAdapter {

  private static final Map<String, String> LINE_BREAK_MAP = new LinkedHashMap<>();

  static {
    LINE_BREAK_MAP.put("\r\n", " mixed_linebreak_string ");
    LINE_BREAK_MAP.put("\n", " linebreak_new_line ");
    LINE_BREAK_MAP.put("\r", " linebreak_raw_string ");
  }

  public static String generateAlertedPartyId(ContextualAlertedPartyIdModel contextualModel) {
    var context = prepareContext(contextualModel);
    return contextualModel.isLineBreaks() ? addLineBreaks(context) : context;
  }

  private static String prepareContext(ContextualAlertedPartyIdModel contextualModel) {

    boolean lineBreaks = contextualModel.isLineBreaks();
    var matchingField = getMatchingField(contextualModel.getMatchingField(), lineBreaks);
    var matchText = getMatchText(contextualModel.getMatchText(), lineBreaks);
    int numberOfTokensLeft = contextualModel.getNumberOfTokensLeft();
    int numberOfTokensRight = contextualModel.getNumberOfTokensRight();
    int minTokens = contextualModel.getMinTokens();

    var matchingFieldSides = matchingField.split(matchText, -1);
    if (matchingFieldSides.length < 2) {
      return EMPTY;
    }

    var leftTokens = getLeftSideTokens(matchingFieldSides[0], numberOfTokensLeft);
    var rightTokens = getRightSideTokens(matchingFieldSides[1], numberOfTokensRight);

    if (leftTokens.length + rightTokens.length >= minTokens) {
      return joinNonBlanks(SPACE, joinTokens(leftTokens), matchText, joinTokens(rightTokens));
    }
    return EMPTY;
  }

  private static String getMatchingField(String matchingField, boolean lineBreaks) {
    return lineBreaks ? removeLineBreaks(matchingField) : matchingField;
  }

  private static String getMatchText(String matchText, boolean lineBreaks) {
    return lineBreaks ? removeLineBreaks(matchText) : matchText;
  }

  private static String joinNonBlanks(String... tokens) {
    return Stream.of(tokens)
        .filter(s -> s != null && !s.isBlank())
        .collect(Collectors.joining(" "));
  }

  private static String removeLineBreaks(String text) {
    for (String newLine : LINE_BREAK_MAP.keySet()) {
      text = text.replaceAll(newLine, LINE_BREAK_MAP.get(newLine));
    }
    return text;
  }

  private static String addLineBreaks(String text) {
    for (String newLine : LINE_BREAK_MAP.keySet()) {
      text = text.replaceAll(LINE_BREAK_MAP.get(newLine).strip(), newLine);
    }
    return text;
  }

  private static String joinTokens(String[] tokens) {
    return join(SPACE, tokens);
  }

  private static String[] getLeftSideTokens(String text, int numberOfTokensLeft) {
    var tokens = normalizeSpace(text).split(SPACE);
    var fromIndex = max(tokens.length - numberOfTokensLeft, 0);
    return copyOfRange(tokens, fromIndex, tokens.length);
  }

  private static String[] getRightSideTokens(String text, int numberOfTokensRight) {
    var tokens = normalizeSpace(text).split(SPACE);
    var toIndex = min(numberOfTokensRight, tokens.length);
    return copyOfRange(tokens, 0, toIndex);
  }
}
