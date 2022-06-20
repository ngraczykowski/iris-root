/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.batch;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.batch.ScbBridgeConfigProperties;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class ScbBridgeConfigPropertiesTest {

  private ScbBridgeConfigProperties scbBridgeConfigProperties = new ScbBridgeConfigProperties();

  @Test
  void useChunkSizeWhenLowerThanMaxValue() {
    // given
    scbBridgeConfigProperties.setChunkSize(1);

    // when
    int oraclePageSize = scbBridgeConfigProperties.getOraclePageSize();

    // then
    assertThat(oraclePageSize).isEqualTo(1);
  }

  @Test
  void useMaxValueWhenChunkSizeGreaterThanMaxValue() {
    // given
    scbBridgeConfigProperties.setChunkSize(1001);
    int oraclePageSizeMaxValue = 1000;

    // when
    int oraclePageSize = scbBridgeConfigProperties.getOraclePageSize();

    // then
    assertThat(oraclePageSize).isEqualTo(oraclePageSizeMaxValue);
  }
}
