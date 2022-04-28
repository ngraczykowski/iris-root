package com.silenteight.adjudication.engine.alerts.alert

import com.silenteight.adjudication.api.v1.Alert
import com.silenteight.adjudication.api.v1.Alert.Builder

import com.google.protobuf.util.Timestamps
import spock.lang.Specification

import java.time.Instant

import static com.silenteight.adjudication.engine.alerts.alert.AlertFixtures.getRandomAlertId
import static com.silenteight.adjudication.engine.alerts.alert.AlertFixtures.inMemoryAlertFacade
import static com.silenteight.adjudication.engine.common.protobuf.TimestampConverter.fromInstant

class CreateAlertsUseCaseAcceptanceSpec extends Specification {

  private AlertFacade facade = inMemoryAlertFacade()

  def "creates an alert given the alert with id and labels"() {
    given:
    def inputAlertTime = Timestamps.parse("2021-03-18T16:21:34.765+01:00")
    def inputAlertId = getRandomAlertId()
    def inputAlert = Alert.newBuilder()
        .setAlertId(inputAlertId)
        .setAlertTime(inputAlertTime)
        .putLabels("the_label", "the_value")
        .build()

    when:
    def alerts = facade.createAlerts([inputAlert])

    then:
    verifyAll(alerts.first()) {
      name =~ "alerts/\\d+"
      alertTime == inputAlertTime
      alertId == inputAlertId
      labelsMap == [the_label: "the_value"]
    }
  }

  def "creates an alert with a priority"() {
    given:
    def alert = alert().setPriority(inputPriority).build()

    expect:
    facade.createAlerts([alert]).first().priority == expectedPriority

    where:
    inputPriority || expectedPriority
    0             || 5
    3             || 3
    1             || 1
    10            || 10
    -1312         || 1
    3213          || 10
  }

  def "creates alerts in batch"() {
    given:
    def alert1 = alert().build()
    def alert2 = alert().build()
    def alert3 = alert().build()
    def inputAlerts = [alert1, alert2, alert3]

    when:
    def createdAlerts = facade.createAlerts(inputAlerts)

    then:
    createdAlerts.size() == inputAlerts.size()
  }

  private static Builder alert() {
    def now= Instant.now()
    Alert.newBuilder().setAlertId(getRandomAlertId())
        .setAlertTime(fromInstant(now))
        .setCreateTime(fromInstant(now))
  }
}
