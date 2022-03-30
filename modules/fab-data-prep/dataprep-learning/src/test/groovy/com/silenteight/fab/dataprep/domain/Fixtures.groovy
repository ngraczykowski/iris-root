package com.silenteight.fab.dataprep.domain

import com.silenteight.fab.dataprep.domain.model.LearningData

class Fixtures {

  static final String ALERT_NAME = 'alerts/1'
  static final String SYSTEM_ID = 'TRAINING!60C2ED1B-58A1D68E-0326AE78-A8C7CC79'
  static final String MESSAGE_ID = '00000004'

  static final LearningData LEARNING_DATA = LearningData.builder()
      .alertName(ALERT_NAME)
      .messageId(MESSAGE_ID)
      .systemId(SYSTEM_ID)
      .build()
}
