package com.silenteight.scb.ingest.adapter.incomming.cbs.domain;

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
