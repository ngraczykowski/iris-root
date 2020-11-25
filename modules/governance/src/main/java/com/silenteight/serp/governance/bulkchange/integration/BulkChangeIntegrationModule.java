package com.silenteight.serp.governance.bulkchange.integration;

public final class BulkChangeIntegrationModule {

  private BulkChangeIntegrationModule() {
  }

  static final String CREATE_BULK_CHANGE_INBOUND_CHANNEL = "createBulkChangeInbound";
  static final String CREATE_BULK_CHANGE_OUTBOUND_CHANNEL = "createBulkChangeOutbound";

  static final String APPLY_BULK_CHANGE_INBOUND_CHANNEL = "applyBulkChangeInbound";
  static final String APPLY_BULK_CHANGE_OUTBOUND_CHANNEL = "applyBulkChangeOutbound";

  static final String REJECT_BULK_CHANGE_INBOUND_CHANNEL = "rejectBulkChangeInbound";
  static final String REJECT_BULK_CHANGE_OUTBOUND_CHANNEL = "rejectBulkChangeOutbound";

}
