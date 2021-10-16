package com.silenteight.payments.bridge.etl.processing.model;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;

import com.google.common.base.Splitter;

import java.util.List;

@Value
public class MessageTag {

  private static final Splitter LINE_SPLITTER = Splitter.on('\n');

  String name;
  String value;

  @EqualsAndHashCode.Exclude
  @ToString.Exclude
  List<String> lines;

  public MessageTag(String name, String value) {
    this.name = name;
    this.value = value;
    lines = LINE_SPLITTER.splitToList(value);
  }
}
