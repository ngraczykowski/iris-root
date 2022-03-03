package com.silenteight.customerbridge.common.messaging;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessagingConstants {

  public static final String EXCHANGE_ALERT_ID = "alert.id";
  public static final String EXCHANGE_ALERT_METADATA = "alert.metadata";
  public static final String EXCHANGE_ALERT_ORDERS = "alert.orders";
  public static final String EXCHANGE_ALERT_PROCESSED = "alert.processed";
  public static final String EXCHANGE_ALERT_UNPROCESSED = "alert.unprocessed";
  public static final String EXCHANGE_ALERT_RECOMMENDATION = "alert.recommendation";
  public static final String EXCHANGE_ALERT_NOT_ROUTED = "alert.not-routed";
  public static final String EXCHANGE_REPORT_DATA = "report.data";
  public static final String EXCHANGE_LEARNING = "learning";
  public static final String EXCHANGE_NOTIFICATION = "notification";
  public static final String EXCHANGE_BULK_CHANGE = "bulk-change";
  public static final String EXCHANGE_DISCREPANCY = "discrepancy";
  public static final String EXCHANGE_WAREHOUSE = "wh.event";

  public static final String ROUTE_ALERT_DECISION_GROUPS = "alert.decision-groups";
  public static final String ROUTE_ALERT_MODEL = "alert.model";
  public static final String ROUTE_ALERT_RECOMMENDATION = "alert.recommendation";
  public static final String ROUTE_ORDER_CACHED = "order.cached";
  public static final String ROUTE_ORDER_UNCACHED = "order.uncached";
  public static final String ROUTE_ORDER_REJECTED = "order.rejected";
  public static final String ROUTE_PIPELINE_FEATURE_GROUPS = "pipeline.feature-groups";
  public static final String ROUTE_PIPELINE_MODEL = "pipeline.model";
  public static final String ROUTE_INFO_DESCRIPTIONS = "info.*";
  public static final String ROUTE_INFO_FROM_PIPELINE = "info.pipeline";
  public static final String ROUTE_INFO_FROM_RECO = "info.reco";
  public static final String ROUTE_DISCREPANCY_DETECTED = "learning.discrepancy-detected";
  public static final String ROUTE_BULK_CREATE = "bulk-change.create";
  public static final String ROUTE_BULK_APPLY = "bulk-change.apply";
  public static final String ROUTE_BULK_REJECT = "bulk-change.reject";
  public static final String ROUTE_BULK_CREATED = "bulk-change.created";
  public static final String ROUTE_BULK_APPLIED = "bulk-change.applied";
  public static final String ROUTE_BULK_REJECTED = "bulk-change.rejected";
  public static final String ROUTE_DISCREPANCY_ARCHIVE = "discrepancy.archive";
  public static final String ROUTE_WAREHOUSE = "event.index-response.production";

  public static final String QUEUE_GOVERNANCE_BULK_CHANGE_CREATE = "governance.bulk-change.create";
  public static final String QUEUE_GOVERNANCE_BULK_CHANGE_APPLY = "governance.bulk-change.apply";
  public static final String QUEUE_GOVERNANCE_BULK_CHANGE_REJECT = "governance.bulk-change.reject";
  public static final String QUEUE_DISCREPANCY_ARCHIVE = "circuit-breaker.discrepancy.archive";
  public static final String QUEUE_WAREHOUSE = "wh.index-request.production";

  public static final int ALERT_NON_DENY_PRIORITY = 2;
  public static final int ALERT_DENY_PRIORITY = 5;
  public static final int ALERT_ORDER_PRIORITY = 10;

  public static final String HEADER_PRIORITY = "priority";
  public static final String ROUTE_INFO_FROM_SCB_BRIDGE = "info.scb-bridge";
  private static final String SCB_SUFFIX = ".scb";
  public static final String ROUTE_SCB_INFO_FROM_SYNC = ROUTE_INFO_FROM_SCB_BRIDGE + SCB_SUFFIX;
}
