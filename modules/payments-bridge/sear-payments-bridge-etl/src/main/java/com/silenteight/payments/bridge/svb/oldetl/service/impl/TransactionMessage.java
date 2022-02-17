package com.silenteight.payments.bridge.svb.oldetl.service.impl;


import com.silenteight.payments.bridge.etl.processing.model.MessageData;

import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.Arrays.copyOfRange;
import static java.util.Collections.singletonList;

public class TransactionMessage {

  public static List<String> getAllMatchingTexts(
      MessageData messageData, String tag, String matchingText) {
    return extractLongestStrings(messageData.getValue(tag), matchingText);
  }

  protected static List<String> extractLongestStrings(
      String matchField, String matchText) {

    var matchTextLs = new ArrayList<String>();

    var commaSepMatchTextLs = matchText.split(",");
    int fullIndexShift = 0;
    int subLoopIndexShift = 0;
    for (int index = 0; index < commaSepMatchTextLs.length; index++) {
      if (index > 0)
        fullIndexShift += subLoopIndexShift;

      var commaSepMatchTextLsSub =
          copyOfRange(commaSepMatchTextLs, index + fullIndexShift, commaSepMatchTextLs.length);
      subLoopIndexShift = commaSepMatchTextLsSub.length;

      for (int subIndex = 0; subIndex < commaSepMatchTextLsSub.length; subIndex++) {
        --subLoopIndexShift;

        int endSubIndex = commaSepMatchTextLsSub.length - subIndex;
        var joinedText =
            subIndex == 0
            ? String.join(",", commaSepMatchTextLsSub)
            : String.join(",", copyOfRange(commaSepMatchTextLsSub, 0, endSubIndex));

        var processedMatchText = processMatchText(joinedText);
        var textPattern = Pattern.compile(processedMatchText.getRight());
        if (textPattern.matcher(matchField).find()) {
          matchTextLs.add(processedMatchText.getLeft());
          if (subIndex == 0)
            return matchTextLs;
          else
            break;
        }
      }
    }

    return singletonList(matchText);
  }

  private static Pair<String, String> processMatchText(String text) {
    String textForComment = text.strip();
    String textForRegex = Pattern.quote(textForComment);
    return Pair.of(textForComment, textForRegex);
  }
}
