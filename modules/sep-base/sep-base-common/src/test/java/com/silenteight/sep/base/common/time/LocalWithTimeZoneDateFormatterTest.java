package com.silenteight.sep.base.common.time;

import com.silenteight.sep.base.testing.time.MockTimeSource;

import org.junit.jupiter.api.Test;

import java.time.ZoneOffset;

import static org.assertj.core.api.Assertions.*;

class LocalWithTimeZoneDateFormatterTest {

  LocalWithTimeZoneDateFormatter underTest = LocalWithTimeZoneDateFormatter.INSTANCE;

  @Test
  void testFormatter() {
    String formattedDate = underTest.format(MockTimeSource.ARBITRARY_INSTANCE.now().atOffset(
        ZoneOffset.ofHours(3)));

    assertThat(formattedDate).isEqualTo("1970-01-19 07:05:28 UTC");
  }
}
