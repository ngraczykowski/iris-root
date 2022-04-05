package com.silenteight.fab.dataprep.domain

import com.silenteight.fab.dataprep.domain.model.LearningData

class Fixtures {

  static final String ALERT_NAME = 'alerts/1'
  static final String SYSTEM_ID = 'TRAINING!60C2ED1B-58A1D68E-0326AE78-A8C7CC79'
  static final String MESSAGE_ID = '00000004'
  static final String DISCRIMINATOR = "$SYSTEM_ID|$MESSAGE_ID"
  static final String ORIGINAL_ANALYST_DECISION = "COMMHUB"
  static final String ACCESS_PERMISSION_TAG = "AE"
  static final String ANALYST_DECISION = "analyst_decision_true_positive"
  static final String ANALYST_DATE_TIME = "20180827094707"
  static final String ANALYST_REASON = "reason"

  static final LearningData LEARNING_DATA = LearningData.builder()
      .alertName(ALERT_NAME)
      .discriminator(DISCRIMINATOR)
      .accessPermissionTag(ACCESS_PERMISSION_TAG)
      .originalAnalystDecision(ORIGINAL_ANALYST_DECISION)
      .analystDecision(ANALYST_DECISION)
      .analystDecisionModifiedDateTime(ANALYST_DATE_TIME)
      .analystReason(ANALYST_REASON)
      .build()
}
