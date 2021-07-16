package com.silenteight.searpayments.scb.etl.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Slf4j
class MatchingFieldPairExtractorHelper {

  static List<String> extractMatchingFieldPairList(
      @NotNull String tag, @NotNull String messageData) {
    tag = tag.strip();
    String textRegex =
        "(?s)(\\[" + tag + "[\\s\\d]*?\\])(.*?\\n)\\[(.*?)\\](.*?\\n)(?=\\[[^]]+\\]|$)";
    List<String> matchList = extractMatchList(messageData, textRegex);

    return matchList;
  }

  static List<String> extractMatchingFieldPairCleanList(
      @NotNull String tag, @NotNull List<String> matchFieldPairList, @NotNull String matchText) {
    List<String> matchingFieldPairCleanList = new ArrayList<>();

    for (String matchFieldPair : matchFieldPairList) {
      String matchField = ComponentExtractorHelper
          .extractComponentsFromMatchFieldPair(tag, matchFieldPair).get(0);
      List<String> matchingTextList =
          ExtractMatchTextListHelper.extractMatchTextLs(matchField, matchText);
      for (int i = 0; i < matchingTextList.size(); i++) {
        int matchTextListLength = matchingTextList.size() - 1;
        Pattern textPattern = Pattern.compile(Pattern.quote(matchingTextList.get(i)));
        boolean matchFieldMatchTextPattern = textPattern.matcher(matchField).find();

        try {
          if (matchFieldMatchTextPattern && i == matchTextListLength) {
            matchingFieldPairCleanList.add(matchFieldPair);
          } else if (matchFieldMatchTextPattern) {
            continue;
          } else {
            break;
          }
        } catch (Exception ex) {
          log.error("", ex);
          continue;
        }
      }
    }

    return matchingFieldPairCleanList;
  }

  private static List<String> extractMatchList(String messageData, String textRegex) {
    Pattern pattern = Pattern.compile(textRegex);
    Matcher matcher = pattern.matcher(messageData);
    List<String> matches = new ArrayList<>();
    while (matcher.find()) {
      matches.add(matcher.group());
    }
    return matches;
  }
}
