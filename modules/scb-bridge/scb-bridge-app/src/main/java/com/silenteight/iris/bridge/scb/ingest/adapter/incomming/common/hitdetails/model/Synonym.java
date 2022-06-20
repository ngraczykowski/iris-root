/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.common.hitdetails.model;

import lombok.Data;

@Data
public class Synonym {

  private final String text;
  private final boolean active;
}
