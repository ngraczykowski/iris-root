package com.silenteight.payments.bridge.etl.processing.model;

import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;

class MessageDataTest {

  private MessageData messageData;

  @Test
  void shouldWorkWithEmptyMap() {
    given(Map.of());
    assertThat(messageData.getSize()).isZero();
  }

  @Test
  void shouldCountTagsCorrectly() {
    given(Map.of("TAG1", "LINE1", "TAG2", "LINE2"));
    assertThat(messageData.getSize()).isEqualTo(2);
  }

  @Test
  void shouldHandleSingleLineTags() {
    given(Map.of("TAG", "LINE"));
    assertThat(messageData.getValue("TAG")).isEqualTo("LINE");
  }

  @Test
  void shouldHandleMultiLineTags() {
    given(Map.of("MULTI", "LINE1\nLINE2\nLINE3"));

    assertThat(messageData.getTagLineCount("MULTI")).isEqualTo(3);
    assertThat(messageData.getLines("MULTI")).contains("LINE1", "LINE2", "LINE3");
  }

  @Test
  void shouldHandleEmptyLines() {
    given(Map.of("EMPTY", "FIRST\n\n\nLAST"));

    assertThat(messageData.getLines("EMPTY")).contains("FIRST", "", "", "LAST");
  }

  private void given(Map<String, String> tagValues) {
    messageData = new MessageData(tagValues
        .entrySet()
        .stream()
        .map(entry -> new MessageTag(entry.getKey(), entry.getValue()))
        .collect(Collectors.toUnmodifiableList()));
  }

  @Test
  void shouldAllowMultipleTagsWithSameName() {
    given("TAG", "VALUE1", "TAG", "VALUE2");

    assertThat(messageData.getSize()).isEqualTo(2);
    assertThat(messageData.findAllValues("TAG")).containsExactly("VALUE1", "VALUE2");
  }

  private void given(String... tagValues) {
    if ((tagValues.length % 2) != 0)
      throw new IllegalArgumentException("Must be even number of tagValues");

    var tags = IntStream.range(0, tagValues.length / 2)
        .mapToObj(num -> new MessageTag(tagValues[num * 2], tagValues[(num * 2) + 1]))
        .collect(toList());

    messageData = new MessageData(tags);
  }
}
