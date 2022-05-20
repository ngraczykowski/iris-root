package com.silenteight.universaldatasource.common.messaging;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RabbitQueueCommons {

  public static final String UDS_CATEGORY_VALUE_RETENTION_QUEUE_NAME =
      "uds.retention.category-value";
  public static final String UDS_FEATURE_INPUT_RETENTION_QUEUE_NAME =
      "uds.retention.feature-input";
  public static final String UDS_COMMENT_INPUT_RETENTION_QUEUE_NAME =
      "uds.retention.comment-input";

}
