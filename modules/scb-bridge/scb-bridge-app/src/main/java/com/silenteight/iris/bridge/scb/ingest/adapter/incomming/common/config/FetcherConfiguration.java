/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.config;

import lombok.Data;

@Data
public class FetcherConfiguration {

  private final String dbRelationName;
  private final int timeout;
}
