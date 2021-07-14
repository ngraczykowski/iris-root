package com.silenteight.searpayments.scb.etl.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import static java.util.Arrays.copyOfRange;
import static java.util.Collections.singletonList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExtractMatchTextListHelper {

  public static List<String> extractAllMatchingTexts(
      AbstractMessageStructure messageStructure, String messageData, String matchingText) {

    List<List<String>> fieldValues =
        FieldValueExtractor.extractFieldValues(messageStructure, messageData);
    if (!fieldValues.get(0).isEmpty()) {
      return extractMatchTextLs(fieldValues.get(0).get(0), matchingText);
    } else {
      return Collections.emptyList();
    }
  }

  public static List<String> extractMatchTextLs(String matchField, String matchText) {
    List<String> matchTextLs = new ArrayList<>();

    if (matchText == null) {
      return Collections.emptyList();
    }
    String[] commaSepMatchTextLs = matchText.split(",");
    int fullIndexShift = 0;
    int subLoopIndexShift = 0;
    for (int index = 0; index < commaSepMatchTextLs.length; index++) {
      if (index > 0)
        fullIndexShift += subLoopIndexShift;

      String[] commaSepMatchTextLsSub =
          copyOfRange(commaSepMatchTextLs, index + fullIndexShift, commaSepMatchTextLs.length);
      subLoopIndexShift = commaSepMatchTextLsSub.length;

      for (int subIndex = 0; subIndex < commaSepMatchTextLsSub.length; subIndex++) {
        --subLoopIndexShift;

        int endSubIndex = commaSepMatchTextLsSub.length - subIndex;
        String joinedText = subIndex == 0 ?
                            String.join(",", commaSepMatchTextLsSub) :
                            String.join(",", copyOfRange(commaSepMatchTextLsSub, 0,
                                endSubIndex));

        Pair<String, String> processedMatchText = processMatchText(joinedText);
        Pattern textPattern = Pattern.compile(processedMatchText.getRight());
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
