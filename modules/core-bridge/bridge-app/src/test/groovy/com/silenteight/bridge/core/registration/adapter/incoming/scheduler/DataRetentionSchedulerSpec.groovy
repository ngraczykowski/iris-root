package com.silenteight.bridge.core.registration.adapter.incoming.scheduler

import com.silenteight.bridge.core.registration.domain.RegistrationFacade
import com.silenteight.bridge.core.registration.domain.command.StartDataRetentionCommand
import com.silenteight.bridge.core.registration.infrastructure.scheduler.DataRetentionSchedulerProperties
import com.silenteight.bridge.core.registration.infrastructure.scheduler.DataRetentionSchedulerProperties.DryRunMode

import spock.lang.Specification
import spock.lang.Unroll

import java.time.Duration

import static com.silenteight.bridge.core.registration.domain.model.DataRetentionMode.DRY
import static com.silenteight.bridge.core.registration.domain.model.DataRetentionMode.WET

class DataRetentionSchedulerSpec extends Specification {

  def registrationFacade = Mock(RegistrationFacade)

  @Unroll
  def 'should start data retention process with type #expectedMode'() {
    given:
    def properties = createProperties(dryRun, Duration.ofDays(2))
    def underTest = new DataRetentionScheduler(registrationFacade, properties)
    def command = StartDataRetentionCommand.builder()
        .mode(expectedMode)
        .duration(expectedDuration)
        .chunkSize(properties.chunk())
        .build()

    when:
    underTest.run()

    then:
    1 * registrationFacade.startDataRetention(command)

    where:
    dryRun || expectedMode || expectedDuration
    true   || DRY          || Duration.ofDays(2)
    false  || WET          || Duration.ofDays(2)
  }

  private static def createProperties(boolean dryRunEnabled, Duration duration) {
    DataRetentionSchedulerProperties.builder()
        .chunk(10)
        .dryRunMode(new DryRunMode(dryRunEnabled))
        .duration(duration)
        .build()
  }
}
