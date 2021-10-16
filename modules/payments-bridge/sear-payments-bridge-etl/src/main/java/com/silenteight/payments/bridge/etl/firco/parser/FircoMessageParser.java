package com.silenteight.payments.bridge.etl.firco.parser;

import com.silenteight.payments.bridge.etl.processing.model.MessageData;
import com.silenteight.payments.bridge.etl.processing.model.MessageTag;

import java.util.ArrayList;
import java.util.List;

class FircoMessageParser {

  private final List<MessageTag> tags;
  private final char[] chars;

  FircoMessageParser(String message) {
    tags = new ArrayList<>();
    chars = message.toCharArray();
  }

  MessageData parse() {
    var i = 0;

    while (i < chars.length) {
      var keyIndex = readKey(i);
      i = keyIndex.getIndex();
      var valueIndex = readValue(i);
      i = valueIndex.getIndex();
      tags.add(new MessageTag(keyIndex.getValue(), valueIndex.getValue().stripTrailing()));
    }

    return new MessageData(tags);
  }

  private IndexValue readKey(int index) {
    var word = new StringBuilder();

    if (chars[index] != '[')
      throw new IllegalArgumentException();

    index++;

    for (var i = index; i < chars.length; i++) {
      if (chars[i] == ' ' || chars[i] == ']')
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

    if (chars[index + 1] == '[')
      return new IndexValue(index + 1, word.toString());

    index += 2;

    for (var i = index; i < chars.length; i++) {
      if (chars[i] == '[')
        return new IndexValue(i, word.toString());

      word.append(chars[i]);
    }

    return new IndexValue(chars.length, word.toString());
  }
}
