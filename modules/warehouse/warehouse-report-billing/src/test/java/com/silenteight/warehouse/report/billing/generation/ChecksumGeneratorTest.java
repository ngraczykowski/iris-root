package com.silenteight.warehouse.report.billing.generation;

import org.junit.jupiter.api.Test;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

class ChecksumGeneratorTest {

  // INFO(kdzieciol): Verified with the online tool: https://md5calc.com/hash/sha3-256/
  // This page can be used to verified the checksum is correct.

  @Test
  void generateChecksum() {
    assertThat(ChecksumGenerator.generateChecksum(of("first", "second")))
        .isEqualTo("BB30DF9A5F0F7CE04AE8CD201C95979147E5F4587557BD0D0BA6458164966A36");
  }
}
