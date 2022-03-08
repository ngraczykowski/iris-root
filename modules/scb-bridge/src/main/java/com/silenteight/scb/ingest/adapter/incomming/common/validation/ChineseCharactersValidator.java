package com.silenteight.scb.ingest.adapter.incomming.common.validation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

import static org.apache.commons.lang3.StringUtils.isEmpty;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ChineseCharactersValidator {

  private static final Pattern CHINESE_CHARACTERS = Pattern.compile("[\\p{IsHan}]+");

  public static boolean isValid(String text) {
    if (isEmpty(text)) {
      return false;
    }

    return CHINESE_CHARACTERS.matcher(text).matches();
  }
}