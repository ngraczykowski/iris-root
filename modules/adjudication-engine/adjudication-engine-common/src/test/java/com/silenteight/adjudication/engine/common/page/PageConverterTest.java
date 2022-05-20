package com.silenteight.adjudication.engine.common.page;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

public class PageConverterTest {

  @Test
  void shouldConvertPageFromString() {
    var page = PageConverter.fromPageToken("1");
    assertThat(page).isEqualTo(1);
  }

  @Test
  void shouldReturnFirstPageWhenBlank() {
    var page = PageConverter.fromPageToken("");
    assertThat(page).isEqualTo(0);
  }

  @Test
  void shouldReturnFirstPageWhenNull() {
    var page = PageConverter.fromPageToken(null);
    assertThat(page).isEqualTo(0);
  }

  @Test
  void shouldThrowExceptionWhenNotANumber() {
    assertThatThrownBy(() -> PageConverter.fromPageToken("not-a-number")).isInstanceOf(
        NumberFormatException.class);
  }
}
