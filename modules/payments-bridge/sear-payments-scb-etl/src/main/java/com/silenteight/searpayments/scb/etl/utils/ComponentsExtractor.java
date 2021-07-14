package com.silenteight.searpayments.scb.etl.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ComponentsExtractor {

  @Nullable
  public static List<List<String>> getComponents(
      String messageData, String tag, String matchText) {
    List<String> mainTagFieldPairValues =
        MatchingFieldPairExtractorHelper.extractMatchingFieldPairList(tag, messageData);
    List<String> mainTagFieldPairCleanValues =
        MatchingFieldPairExtractorHelper.extractMatchingFieldPairCleanList(
            tag, mainTagFieldPairValues, matchText);
    List<List<String>> componentsFromMatchFieldPairList =
        ComponentExtractorHelper.extractComponentsFromMatchFieldPairList(
            tag, mainTagFieldPairCleanValues);
    return componentsFromMatchFieldPairList;
  }
}
