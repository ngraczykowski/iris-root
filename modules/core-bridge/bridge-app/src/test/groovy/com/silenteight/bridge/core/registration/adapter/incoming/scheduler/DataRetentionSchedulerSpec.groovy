package com.silenteight.bridge.core.registration.adapter.incoming.scheduler

import com.silenteight.bridge.core.registration.domain.RegistrationFacade
import com.silenteight.bridge.core.registration.domain.command.StartDataRetentionCommand
import com.silenteight.bridge.core.registration.infrastructure.scheduler.DataRetentionSchedulerProperties
import com.silenteight.bridge.core.registration.infrastructure.scheduler.DataRetentionSchedulerProperties.AlertsExpired
import com.silenteight.bridge.core.registration.infrastructure.scheduler.DataRetentionSchedulerProperties.DryRunMode
import com.silenteight.bridge.core.registration.infrastructure.scheduler.DataRetentionSchedulerProperties.PersonalInformationExpired

import spock.lang.Specification
import spock.lang.Unroll

import java.time.Duration

import static com.silenteight.bridge.core.registration.domain.model.DataRetentionType.ALERTS_EXPIRED
import static com.silenteight.bridge.core.registration.domain.model.DataRetentionType.ALERTS_EXPIRED_DRY_RUN
import static com.silenteight.bridge.core.registration.domain.model.DataRetentionType.PERSONAL_INFO_EXPIRED
import static com.silenteight.bridge.core.registration.domain.model.DataRetentionType.PERSONAL_INFO_EXPIRED_DRY_RUN

class DataRetentionSchedulerSpec extends Specification {

  def registrationFacade = Mock(RegistrationFacade)

  @Unroll
  def 'should not start data retention process when #desc'() {
    given:
    def properties = createProperties(personalInfoEnabled, alertsEnabled)
    def underTest = new DataRetentionScheduler(registrationFacade, properties)

    when:
    underTest.run()

    then:
    0 * _

    where:
    personalInfoEnabled | alertsEnabled || desc
    false               | false         || 'neither personal info nor alerts expired is enabled'
    true                | true          || 'personal info and alerts expired are enabled'
  }

  @Unroll
  def 'should start data retention process with type #expectedType'() {
    given:
    def properties = createProperties(
        personalInfoEnabled,
        Duration.ofDays(1),
        alertsEnabled,
        Duration.ofDays(2),
        dryRun
    )
    def underTest = new DataRetentionScheduler(registrationFacade, properties)
    def command = StartDataRetentionCommand.builder()
        .type(expectedType)
        .duration(expectedDuration)
        .chunkSize(properties.chunk())
        .build()

    when:
    underTest.run()

    then:
    1 * registrationFacade.startDataRetention(command)

    where:
    personalInfoEnabled | alertsEnabled | dryRun || expectedType                  ||  expectedDuration
    true                | false         | false  || PERSONAL_INFO_EXPIRED         ||  Duration.ofDays(1)
    true                | false         | true   || PERSONAL_INFO_EXPIRED_DRY_RUN ||  Duration.ofDays(1)
    false               | true          | false  || ALERTS_EXPIRED                ||  Duration.ofDays(2)
    false               | true          | true   || ALERTS_EXPIRED_DRY_RUN        ||  Duration.ofDays(2)
  }

  private static def createProperties(
      boolean personalInfoEnabled, boolean alertsEnabled) {
    createProperties(personalInfoEnabled, Duration.ofDays(1), alertsEnabled, Duration.ofDays(1))
  }

  private static def createProperties(
      boolean personalInfoEnabled, Duration personalInfoDuration, boolean alertsEnabled,
      Duration alertsDuration, boolean dryRunEnabled = false) {
    DataRetentionSchedulerProperties.builder()
        .chunk(10)
        .dryRunMode(new DryRunMode(dryRunEnabled))
        .personalInformationExpired(
            new PersonalInformationExpired(
                personalInfoEnabled,
                personalInfoDuration
            ))
        .alertsExpired(
            new AlertsExpired(
                alertsEnabled,
                alertsDuration
            ))
        .build()
  }
}
