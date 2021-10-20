package com.silenteight.warehouse.report.billing.v1.generation;

import org.junit.jupiter.api.Test;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

class DeprecatedChecksumGeneratorTest {

  // INFO(kdzieciol): Verified with the online tool: https://md5calc.com/hash/sha3-256/
  // This page can be used to verified the checksum is correct.

  @Test
  void generateChecksum() {
    assertThat(ChecksumGenerator.generateChecksum(of("first", "second")))
        .isEqualTo("F075FD2810D32E73FD71D852EE720DA2A5775711F271AB8D393ABD046D3C51D7");
  }
}
