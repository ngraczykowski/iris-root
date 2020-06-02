package com.silenteight.sens.webapp.backend.bulkchange;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BulkChangeIntegrationChannels {

  public static final String CREATE_BULK_CHANGE_OUTBOUND_CHANNEL =
      "createBulkChangeOutboundChannel";
  public static final String APPLY_BULK_CHANGE_OUTBOUND_CHANNEL =
      "applyBulkChangeOutboundChannel";
  public static final String REJECT_BULK_CHANGE_OUTBOUND_CHANNEL =
      "rejectBulkChangeOutboundChannel";
}
