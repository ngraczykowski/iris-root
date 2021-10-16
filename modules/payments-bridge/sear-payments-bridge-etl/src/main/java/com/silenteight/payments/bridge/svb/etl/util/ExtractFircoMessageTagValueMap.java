package com.silenteight.payments.bridge.svb.etl.util;

import com.silenteight.payments.bridge.svb.etl.model.IndexValue;

import java.util.HashMap;
import java.util.Map;

class ExtractFircoMessageTagValueMap {

  public static Map<String, String> extract(String message) {
    var tagValues = new HashMap<String, String>();

    var chars = message.toCharArray();

    var i = 0;
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

    if (chars[index] != '[')
      throw new IllegalArgumentException();

    index++;

    for (int i = index; i < chars.length; i++) {
      if (chars[i] == ' ')
        return new IndexValue(readUnilClose(i, chars), word.toString());
      word.append(chars[i]);
    }

    return new IndexValue(chars.length, word.toString());
  }

  private static int readUnilClose(int index, char[] chars) {
    for (int i = index; i <= chars.length; i++) {
      if (chars[i] == ']')
        return i;
    }
    return chars.length;
  }

  private static IndexValue readValue(int index, char[] chars) {
    var word = new StringBuilder();

    if (chars[index] != ']')
      throw new IllegalArgumentException();

    index += 2;

    for (int i = index; i < chars.length; i++) {
      if (chars[i] == '[')
        return new IndexValue(i, word.toString());
      word.append(chars[i]);
    }

    return new IndexValue(chars.length, word.toString());
  }
}
