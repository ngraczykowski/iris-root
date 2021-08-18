package com.silenteight.hsbc.bridge.alert

import com.silenteight.dataretention.api.v1.AlertsExpired
import com.silenteight.dataretention.api.v1.PersonalInformationExpired
import com.silenteight.hsbc.bridge.retention.DataRetentionMessageSender

import spock.lang.Specification

import java.time.Duration
import java.time.OffsetDateTime
import java.util.stream.Stream

import static com.silenteight.hsbc.bridge.retention.DataRetentionType.ALERTS_EXPIRED
import static com.silenteight.hsbc.bridge.retention.DataRetentionType.PERSONAL_INFO_EXPIRED

class AlertRetentionMessageSenderSpec extends Specification {

  def alertRepository = Mock(AlertRepository)
  def messageSender = Mock(DataRetentionMessageSender)

  def underTest = new AlertRetentionMessageSender(alertRepository, messageSender)

  def 'should send one messages by given alert names and chunk size from database with type AlertsExpired'() {
    given:
    def chunkSize = 3
    def daysCountProperty = 2
    def expireDate = OffsetDateTime.now().minus(Duration.ofDays(daysCountProperty))
    def alerts = ['alert_1', 'alert_2', 'alert_3']
    def alertsStream = alerts.stream()
    def message = AlertsExpired.newBuilder().addAllAlerts(alerts).build()

    when:
    underTest.send(expireDate, chunkSize, ALERTS_EXPIRED)

    then:
    1 * alertRepository.findAlertEntityNamesByAlertTimeBefore(expireDate) >> alertsStream
    1 * messageSender.send(message)
  }

  def 'should send three messages by given alert names and chunk size from database with type PersonalInformationExpired'() {
    given:
    def chunkSize = 1
    def daysCountProperty = 151
    def expireDate = OffsetDateTime.now().minus(Duration.ofDays(daysCountProperty))
    def alertsStream = Stream.of('alert_1', 'alert_2', 'alert_3')
    def firstAlertList = ['alert_1']
    def secondAlertList = ['alert_2']
    def thirdAlertList = ['alert_3']
    def firstMessage = PersonalInformationExpired.newBuilder().addAllAlerts(firstAlertList).build()
    def secondMessage = PersonalInformationExpired.newBuilder().addAllAlerts(secondAlertList).build()
    def thirdMessage = PersonalInformationExpired.newBuilder().addAllAlerts(thirdAlertList).build()

    when:
    underTest.send(expireDate, chunkSize, PERSONAL_INFO_EXPIRED)

    then:
    1 * alertRepository.findAlertEntityNamesByAlertTimeBefore(expireDate) >> alertsStream
    1 * messageSender.send(firstMessage)
    1 * messageSender.send(secondMessage)
    1 * messageSender.send(thirdMessage)
  }
}
