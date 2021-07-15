package com.silenteight.searpayments.scb.etl.utils;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.silenteight.searpayments.scb.etl.utils.CommonUtils.escapeRegex;
import static java.util.Collections.emptyList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ComponentExtractorHelper {

  @NotNull
  public static List<String> getElementsFromComponents(
      List<List<String>> componentsFromMatchFieldPairList, int componentElemNumber) {
    return Optional.ofNullable(componentsFromMatchFieldPairList)
        .orElseGet(Collections::emptyList)
        .stream()
        .map(component -> component.get(componentElemNumber))
        .collect(Collectors.toList());
  }

  static List<String> extractComponentsFromMatchFieldPair(
      String tag, String matchFieldPair) {
    tag = escapeRegex(tag.strip());
    String textRegex =
        "(?s)(\\[" + tag + "[\\s\\d]*?\\])(.*?\\n)\\[(.*?)\\](.*?\\n)(?=\\[[^]]+\\]|$)";

    Pattern pattern = Pattern.compile(textRegex);
    try {
      Matcher matcher = pattern.matcher(matchFieldPair);
      matcher.find();
      return (Lists.newArrayList(
          matcher.group(2).strip(), matcher.group(3).strip(), matcher.group(4).strip()));
    } catch (Exception ex) {
      return emptyList();
    }
  }

  static List<List<String>> extractComponentsFromMatchFieldPairList(
      String tag, List<String> matchFieldPairList) {
    tag = tag.strip();
    String textRegex =
        "(?s)(\\[" + tag + "[\\s\\d]*?\\])(.*?\\n)\\[(.*?)\\](.*?\\n)(?=\\[[^]]+\\]|$)";
    List<List<String>> matchFieldPairCompList = new ArrayList<>();

    if (matchFieldPairList == null) {
      return emptyList();
    }
    Pattern pattern = Pattern.compile(textRegex);
    for (String matchFieldPair : matchFieldPairList) {
      try {
        Matcher matcher = pattern.matcher(matchFieldPair);
        matcher.find();
        matchFieldPairCompList.add(
            Lists.newArrayList(matcher.group(2).strip(), matcher.group(3).strip(),
                matcher.group(4).strip()));
      } catch (Exception ex) {
        continue;
      }
    }
    return matchFieldPairCompList;
  }
}
