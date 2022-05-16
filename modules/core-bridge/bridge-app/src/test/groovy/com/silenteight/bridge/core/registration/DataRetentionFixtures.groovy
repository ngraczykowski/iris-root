package com.silenteight.bridge.core.registration

import com.silenteight.bridge.core.registration.domain.model.AlertToRetention

class DataRetentionFixtures {

  static def ALERT_TO_RETENTION_1 = AlertToRetention.builder()
      .id('1')
      .name('alert1')
      .batchId('batch1')
      .build()

  static def ALERT_TO_RETENTION_2 = AlertToRetention.builder()
      .id('2')
      .name('alert2')
      .batchId('batch1')
      .build()

  static def ALERT_TO_RETENTION_3 = AlertToRetention.builder()
      .id('3')
      .name('alert3')
      .batchId('batch2')
      .build()

  static def ALERTS_TO_RETENTION = [ALERT_TO_RETENTION_1, ALERT_TO_RETENTION_2, ALERT_TO_RETENTION_3]
}
