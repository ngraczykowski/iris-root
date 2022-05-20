package com.silenteight.payments.bridge.svb.oldetl.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

import static com.silenteight.payments.bridge.common.dto.common.CommonTerms.APPLICATION_CODE_GFX;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonUtils {

  public static List<String> createOneElementList(String element) {
    if (element != null) {
      return singletonList(element);
    }
    return emptyList();
  }

  public static int toPositiveInt(String text, int defaultValue) {
    try {
      int value = Integer.parseInt(text);
      return Math.max(value, defaultValue);
    } catch (Exception e) {
      return defaultValue;
    }
  }

  public static String processMessage(String text) {
    return text.strip().toUpperCase();
  }

  public static @NonNull String escapeRegex(@NonNull String text) {
    char[] charArray = text.toCharArray();
    StringBuilder stringBuilder = new StringBuilder();
    for (char c : charArray) {
      if (!charOnWhiteList(c)) {
        stringBuilder.append('\\');
      }
      stringBuilder.append(c);
    }
    return stringBuilder.toString();
  }

  private static boolean charOnWhiteList(char c) {
    final String whiteList = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789 ";
    return whiteList.indexOf(c) != -1;
  }

  public static int getLastLineNotUsIndex(List<String> lines, String applicationCode) {
    var lastIndex = lines.size() - 1;
    if ("US".equals(lines.get(lastIndex)) && APPLICATION_CODE_GFX.equals(applicationCode)) {
      return lastIndex - 1;
    }
    return lastIndex;
  }

}
