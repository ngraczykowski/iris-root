package com.silenteight.payments.bridge.svb.etl.util;

import com.silenteight.payments.bridge.svb.etl.model.IndexValue;

import java.util.HashMap;
import java.util.Map;

class ExtractSwiftMessageTagValueMap {

  public static Map<String, String> extract(String message) {
    var tagValues = new HashMap<String, String>();

    var chars = message.toCharArray();

    var i = firstSecondLineCharIndex(chars);
    while (i < chars.length) {
      var keyIndex = readKey(i, chars);
      i = keyIndex.getIndex();
      var valueIndex = readValue(i, chars);
      i = valueIndex.getIndex();
      tagValues.put(keyIndex.getValue(), valueIndex.getValue());
    }

    return tagValues;
  }

  private static IndexValue readKey(int index, char[] chars) {
    var word = new StringBuilder();

    if (chars[index] != ':')
      throw new IllegalArgumentException();

    index++;

    for (int i = index; i < chars.length; i++) {
      if (chars[i] == ':')
        return new IndexValue(i + 1, word.toString());
      word.append(chars[i]);
    }

    return new IndexValue(chars.length, word.toString());
  }

  private static IndexValue readValue(int index, char[] chars) {
    var word = new StringBuilder();

    for (int i = index; i < chars.length; i++) {
      if (chars[i] == ':')
        return new IndexValue(i, word.toString());
      else if (chars[i] == '-' && chars[i + 1] == '}')
        return new IndexValue(chars.length, word.toString());
      word.append(chars[i]);
    }

    return new IndexValue(chars.length, word.toString());
  }

  private static int firstSecondLineCharIndex(char[] chars) {
    for (int i = 0; i < chars.length; i++) {
      if (chars[i] == '\n')
        return i + 1;
    }

    return chars.length;
  }
}
