package com.silenteight.serp.governance.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MessagingConstants {

  public static final String EXCHANGE_ALERT_METADATA = "alert.metadata";
  public static final String EXCHANGE_BULK_CHANGE = "bulk-change";

  public static final String ROUTE_BULK_CREATED = "bulk-change.created";
  public static final String ROUTE_BULK_APPLIED = "bulk-change.applied";
  public static final String ROUTE_BULK_REJECTED = "bulk-change.rejected";

  public static final String QUEUE_GOVERNANCE_BULK_CHANGE_CREATE = "governance.bulk-change.create";
  public static final String QUEUE_GOVERNANCE_BULK_CHANGE_APPLY = "governance.bulk-change.apply";
  public static final String QUEUE_GOVERNANCE_BULK_CHANGE_REJECT = "governance.bulk-change.reject";
  public static final String QUEUE_GOVERNANCE_DECISION_GROUP = "governance.decision-groups";
  public static final String QUEUE_GOVERNANCE_PIPELINE_FEATURE_GROUPS =
      "governance.pipeline-feature-groups";
  public static final String QUEUE_GOVERNANCE_PIPELINE_MODEL = "governance.pipeline-model";

}
