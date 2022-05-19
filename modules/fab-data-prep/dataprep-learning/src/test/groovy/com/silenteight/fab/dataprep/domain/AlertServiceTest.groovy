package com.silenteight.fab.dataprep.domain

import com.silenteight.fab.dataprep.BaseSpecificationIT
import com.silenteight.fab.dataprep.domain.model.AlertItem
import com.silenteight.fab.dataprep.domain.model.AlertState
import com.silenteight.fab.dataprep.domain.model.CreateAlertItem

import org.hibernate.Session
import org.hibernate.tuple.TimestampGenerators
import org.hibernate.tuple.ValueGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.boot.test.context.ConfigDataApplicationContextInitializer
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import org.springframework.transaction.annotation.Transactional
import spock.lang.Subject
import spock.lang.Unroll

import java.time.Clock
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

import static com.silenteight.fab.dataprep.domain.Fixtures.*
import static java.time.ZoneOffset.UTC

@ContextConfiguration(classes = [AlertService, LearningConfiguration, TestEntityManager, TestConfig],
    initializers = ConfigDataApplicationContextInitializer)
@TestPropertySource("classpath:/data-test.properties")
@EnableAutoConfiguration
@Transactional
class AlertServiceTest extends BaseSpecificationIT {

  @Autowired
  @Subject
  AlertService underTest

  @Autowired
  AlertRepository alertRepository

  @Autowired
  TestEntityManager testEntityManager

  @Autowired
  SettableClock clock

  static {
    //generator used by Hibernate to initialize createdAt field
    TimestampGenerators.generators.put(
        OffsetDateTime,
        new SettableValueGenerator()
    )
  }

  def setupSpec() {
    startPostgresql()
  }

  def setup() {
    clock.fixedInstant = Instant.now()
  }

  def 'alert should be stored in DB'() {
    when:
    underTest.save(
        CreateAlertItem.builder()
            .alertName(ALERT_NAME)
            .messageName(MESSAGE_NAME)
            .matchNames([MATCH_NAME])
            .build())
    def result = underTest.getAlertItem(MESSAGE_NAME).get()

    then:
    result == AlertItem.builder()
        .alertName(ALERT_NAME)
        .messageName(MESSAGE_NAME)
        .state(AlertState.REGISTERED)
        .matchNames([MATCH_NAME])
        .build()
  }

  @Unroll
  def 'learning alert should be found in DB #alerName #messageName'() {
    given:
    underTest.save(
        CreateAlertItem.builder()
            .alertName(alerName)
            .messageName(messageName)
            .matchNames([])
            .build())

    when:
    def result = underTest.getAlertItem(messageName).get().getAlertName()

    then:
    result == alerName

    when:
    clock.fixedInstant = Instant.now().plus(30, ChronoUnit.DAYS)
    result = underTest.getAlertItem(messageName)

    then:
    result.isEmpty()

    where:
    alerName   | messageName
    ALERT_NAME | MESSAGE_NAME
    ""         | MESSAGE_NAME
    ALERT_NAME | ""
  }

  def 'state should be updated'() {
    given:
    underTest.save(
        CreateAlertItem.builder()
            .alertName(ALERT_NAME)
            .messageName(MESSAGE_NAME)
            .matchNames([])
            .build())

    when:
    underTest.setAlertState(MESSAGE_NAME, AlertState.IN_UDS)
    def result = underTest.getAlertItem(MESSAGE_NAME).get()

    then:
    result.getState() == AlertState.IN_UDS
  }

  def 'partitions should be created'() {
    given:
    clock.fixedInstant = Instant.parse('2010-01-01T00:00:00.00Z')
    SettableValueGenerator.fixedOffsetDateTime = OffsetDateTime.now(clock)

    when: 'partition for current month is created'
    underTest.createPartitions()
    underTest.save(
        CreateAlertItem.builder()
            .alertName(ALERT_NAME)
            .messageName('alert-1')
            .matchNames([])
            .build())
    testEntityManager.flush()

    and: 'partition for next month is created'
    clock.fixedInstant = Instant.parse('2010-02-02T00:00:00.00Z')
    SettableValueGenerator.fixedOffsetDateTime = OffsetDateTime.now(clock)
    underTest.save(
        CreateAlertItem.builder()
            .alertName(ALERT_NAME)
            .messageName('alert-2')
            .matchNames([])
            .build())
    testEntityManager.flush()

    then:
    noExceptionThrown()
  }

  def 'partition should be created if not exists'() {
    given:
    clock.fixedInstant = Instant.parse('2010-01-01T00:00:00.00Z')
    SettableValueGenerator.fixedOffsetDateTime = OffsetDateTime.now(clock)

    when:
    underTest.createPartitions()
    underTest.createPartitions()

    then:
    noExceptionThrown()
  }

  static class SettableValueGenerator implements ValueGenerator<OffsetDateTime> {

    static OffsetDateTime fixedOffsetDateTime = OffsetDateTime.now()

    @Override
    OffsetDateTime generateValue(Session session, Object owner) {
      fixedOffsetDateTime
    }
  }

  static class SettableClock extends Clock {

    static Instant fixedInstant = Instant.now()

    ZoneId getZone() {
      UTC
    }

    Clock withZone(ZoneId zoneId) {
      this
    }

    Instant instant() {
      fixedInstant
    }
  }

  @TestConfiguration
  static class TestConfig {

    @Bean
    @Primary
    Clock settableClock() {
      new SettableClock()
    }
  }
}
