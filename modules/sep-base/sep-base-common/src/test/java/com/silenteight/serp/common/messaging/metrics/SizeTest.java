package com.silenteight.serp.common.messaging.metrics;

import com.silenteight.serp.common.messaging.metrics.Size.SizeUnit;

import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class SizeTest {

  @Test
  void bytesTest() {
    Size size = new Size(SizeUnit.MEBIBYTES, 1.5);

    assertThat(size.getBytes()).isEqualTo((long) (1.5 * 1024 * 1024));
  }

  @Test
  void fromMebibytesToKibibytesAndBytesTest() {
    Size size = new Size(SizeUnit.MEBIBYTES, 2.5);

    size = size.convert(SizeUnit.KIBIBYTES);
    assertThat(size.getValue()).isEqualTo(2.5 * 1024, Offset.offset(0.0001));

    size = size.convert(SizeUnit.BYTES);
    assertThat(size.getValue()).isEqualTo(2.5 * 1024 * 1024, Offset.offset(0.0001));
  }

  @Test
  void fromBytesToKibibytesAndMebibytesTest() {
    Size size = new Size(SizeUnit.BYTES, 1700);

    size = size.convert(SizeUnit.KIBIBYTES);
    assertThat(size.getValue()).isEqualTo(1.66, Offset.offset(0.01));

    size = size.convert(SizeUnit.MEBIBYTES);
    assertThat(size.getValue()).isEqualTo(0.001621246, Offset.offset(0.0001));
  }
}
