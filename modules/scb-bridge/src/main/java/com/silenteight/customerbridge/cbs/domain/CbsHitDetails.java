package com.silenteight.customerbridge.cbs.domain;

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
