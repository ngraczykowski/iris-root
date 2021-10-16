package com.silenteight.payments.bridge.etl.firco.parser;

import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

import com.google.common.base.Splitter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class MessageData {

  @EqualsAndHashCode.Include
  @ToString.Include
  private final Map<String, String> tagValues;

  private final Map<String, List<String>> tagLines;

  MessageData(@NonNull Map<String, String> tagValues) {
    this.tagValues = tagValues;
    tagLines = splitTagLines(tagValues);
  }

  private static Map<String, List<String>> splitTagLines(Map<String, String> tagValues) {
    var tagLines = new HashMap<String, List<String>>();
    var splitter = Splitter.on('\n');

    tagValues.forEach((tag, value) -> tagLines.put(tag, splitter.splitToList(value)));

    return tagLines;
  }

  public int getTagCount() {
    return tagValues.size();
  }

  public String get(String tagName) {
    if (!tagValues.containsKey(tagName))
      throw new IndexOutOfBoundsException("Tag not found: " + tagName);

    return tagValues.get(tagName);
  }

  public int getTagLineCount(String tagName) {
    return getLines(tagName).size();
  }

  public List<String> getLines(String tagName) {
    if (!tagLines.containsKey(tagName))
      throw new IndexOutOfBoundsException("Tag not found: " + tagName);

    return tagLines.get(tagName);
  }
}
