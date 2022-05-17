package com.silenteight.bridge.core.registration

import com.silenteight.bridge.core.registration.domain.model.AlertToRetention

class DataRetentionFixtures {

  static def ALERT_TO_RETENTION_1 = AlertToRetention.builder()
      .alertPrimaryId(1l)
      .alertId('1')
      .alertName('alert1')
      .batchId('batch1')
      .build()

  static def ALERT_TO_RETENTION_2 = AlertToRetention.builder()
      .alertPrimaryId(2l)
      .alertId('2')
      .alertName('alert2')
      .batchId('batch1')
      .build()

  static def ALERT_TO_RETENTION_3 = AlertToRetention.builder()
      .alertPrimaryId(3l)
      .alertId('3')
      .alertName('alert3')
      .batchId('batch2')
      .build()

  static def ALERTS_TO_RETENTION = [ALERT_TO_RETENTION_1, ALERT_TO_RETENTION_2, ALERT_TO_RETENTION_3]
}
