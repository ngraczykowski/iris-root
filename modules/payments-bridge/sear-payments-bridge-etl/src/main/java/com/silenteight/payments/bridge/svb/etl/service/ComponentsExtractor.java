package com.silenteight.payments.bridge.svb.etl.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.jetbrains.annotations.Nullable;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ComponentsExtractor {

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
