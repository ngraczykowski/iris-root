package com.silenteight.bridge.core.registration.adapter.incoming.scheduler

import com.silenteight.bridge.core.registration.domain.model.Alert
import com.silenteight.bridge.core.registration.domain.model.AlertStatus
import com.silenteight.bridge.core.registration.domain.model.Batch
import com.silenteight.bridge.core.registration.domain.model.Batch.BatchStatus
import com.silenteight.bridge.core.registration.infrastructure.scheduler.DataRetentionSchedulerProperties
import com.silenteight.bridge.core.registration.infrastructure.scheduler.DataRetentionSchedulerProperties.AlertsExpired
import com.silenteight.bridge.core.registration.infrastructure.scheduler.DataRetentionSchedulerProperties.DryRunMode
import com.silenteight.bridge.core.registration.infrastructure.scheduler.DataRetentionSchedulerProperties.PersonalInformationExpired
import com.silenteight.dataretention.api.v1.AlertData

import java.time.Duration
import java.time.OffsetDateTime

class DataRetentionFlowFixtures {

  static def BATCH = Batch.builder()
      .id('batch1')
      .alertsCount(3)
      .status(BatchStatus.DELIVERED)
      .batchPriority(5)
      .build()

  static def ALERT_1 = Alert.builder()
      .name('alert1')
      .alertId('1')
      .batchId(BATCH.id())
      .status(AlertStatus.RECOMMENDED)
      .alertTime(OffsetDateTime.now().minusDays(15))
      .matches([])
      .build()

  static def ALERT_2 = Alert.builder()
      .name('alert2')
      .alertId('2')
      .batchId(BATCH.id())
      .status(AlertStatus.RECOMMENDED)
      .alertTime(OffsetDateTime.now().minusDays(10))
      .matches([])
      .build()

  static def ALERT_3 = Alert.builder()
      .name('alert3')
      .alertId('3')
      .batchId(BATCH.id())
      .status(AlertStatus.RECOMMENDED)
      .alertTime(OffsetDateTime.now().minusDays(1))
      .matches([])
      .build()

  static def ALERTS = [ALERT_1, ALERT_2, ALERT_3]

  static def PERSONAL_INFO_EXPIRED_PROPERTIES = new DataRetentionSchedulerProperties(
      10,
      new DryRunMode(false),
      new PersonalInformationExpired(true, Duration.ofDays(5)),
      new AlertsExpired(false, Duration.ofDays(5))
  )

  static def ALERTS_EXPIRED_PROPERTIES = new DataRetentionSchedulerProperties(
      10,
      new DryRunMode(false),
      new PersonalInformationExpired(false, Duration.ofDays(5)),
      new AlertsExpired(true, Duration.ofDays(5))
  )

  static def EXPECTED_ALERTS_DATA = [
      AlertData.newBuilder()
          .setBatchId(BATCH.id())
          .setAlertId(ALERT_1.alertId())
          .setAlertName(ALERT_1.name())
          .build(),
      AlertData.newBuilder()
          .setBatchId(BATCH.id())
          .setAlertId(ALERT_2.alertId())
          .setAlertName(ALERT_2.name())
          .build()
  ]
}
