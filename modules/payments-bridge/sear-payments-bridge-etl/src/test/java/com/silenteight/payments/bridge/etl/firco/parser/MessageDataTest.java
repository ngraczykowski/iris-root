package com.silenteight.payments.bridge.etl.firco.parser;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class MessageDataTest {

  private MessageData messageData;

  @Test
  void shouldWorkWithEmptyMap() {
    given(Map.of());
    assertThat(messageData.getTagCount()).isZero();
  }

  @Test
  void shouldCountTagsCorrectly() {
    given(Map.of("TAG1", "LINE1", "TAG2", "LINE2"));
    assertThat(messageData.getTagCount()).isEqualTo(2);
  }

  @Test
  void shouldHandleSingleLineTags() {
    given(Map.of("TAG", "LINE"));
    assertThat(messageData.get("TAG")).isEqualTo("LINE");
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
    messageData = new MessageData(tagValues);
  }
}
