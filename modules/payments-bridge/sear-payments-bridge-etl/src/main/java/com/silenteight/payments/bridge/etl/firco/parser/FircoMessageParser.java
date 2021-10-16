package com.silenteight.payments.bridge.etl.firco.parser;

import java.util.HashMap;
import java.util.Map;

class FircoMessageParser {

  private final Map<String, String> tagValues;
  private final char[] chars;

  FircoMessageParser(String message) {
    tagValues = new HashMap<>();
    chars = message.toCharArray();
  }

  MessageData parse() {
    var i = 0;
    while (i < chars.length) {
      var keyIndex = readKey(i);
      i = keyIndex.getIndex();
      var valueIndex = readValue(i);
      i = valueIndex.getIndex();
      tagValues.put(keyIndex.getValue(), valueIndex.getValue().stripTrailing());
    }

    return new MessageData(tagValues);
  }

  private IndexValue readKey(int index) {
    var word = new StringBuilder();

    if (chars[index] != '[')
      throw new IllegalArgumentException();

    index++;

    for (var i = index; i < chars.length; i++) {
      if (chars[i] == ' ')
        return new IndexValue(readUntilClose(i), word.toString());

      word.append(chars[i]);
    }

    return new IndexValue(chars.length, word.toString());
  }

  private int readUntilClose(int index) {
    for (var i = index; i <= chars.length; i++) {
      if (chars[i] == ']')
        return i;
    }

    return chars.length;
  }

  private IndexValue readValue(int index) {
    var word = new StringBuilder();

    if (chars[index] != ']')
      throw new IllegalArgumentException();

    index += 2;

    for (var i = index; i < chars.length; i++) {
      if (chars[i] == '[')
        return new IndexValue(i, word.toString());

      word.append(chars[i]);
    }

    return new IndexValue(chars.length, word.toString());
  }
}
