/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.domain;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CbsHitDetails {

  private String systemId;
  private Integer seqNo;
  private String batchId;
  private NeoFlag hitNeoFlag;
}
