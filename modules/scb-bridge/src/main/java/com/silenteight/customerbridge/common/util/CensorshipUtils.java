package com.silenteight.customerbridge.common.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CensorshipUtils {

  private static final float MASKING_RATIO = 0.7f;
  private static final char MASKING_LETTER = '*';

  public static String maskEnd(@Nullable String input) {
    if (input == null)
      return "";

    int length = input.length();
    int cuttingIndex = (int) Math.ceil(length * (1 - MASKING_RATIO));

    String clearText = input.substring(0, cuttingIndex);

    return StringUtils.rightPad(clearText, input.length(), MASKING_LETTER);
  }
}
