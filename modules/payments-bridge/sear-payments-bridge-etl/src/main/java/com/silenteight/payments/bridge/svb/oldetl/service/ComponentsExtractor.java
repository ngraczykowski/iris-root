package com.silenteight.payments.bridge.svb.oldetl.service;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ComponentsExtractor {

  public static List<List<String>> getComponents(
      String messageData, String tag, String matchText) {
    List<String> mainTagFieldPairValues =
        MatchingFieldPairExtractorHelper.extractMatchingFieldPairList(tag, messageData);
    List<String> mainTagFieldPairCleanValues =
        MatchingFieldPairExtractorHelper.extractMatchingFieldPairCleanList(
            tag, mainTagFieldPairValues, matchText);
    return ComponentExtractorHelper.extractComponentsFromMatchFieldPairList(
        tag, mainTagFieldPairCleanValues);
  }
}
