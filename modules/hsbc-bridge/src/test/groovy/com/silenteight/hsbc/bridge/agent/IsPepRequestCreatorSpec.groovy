package com.silenteight.hsbc.bridge.agent

import com.silenteight.hsbc.bridge.json.external.model.AlertData
import com.silenteight.hsbc.bridge.util.CustomDateTimeFormatter

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

class IsPepRequestCreatorSpec extends Specification {

  def dateTimeFormatter = new CustomDateTimeFormatter("yyyy-MMM-dd HH:mm:ss")
  def timestampMapper = new AgentTimestampMapper(dateTimeFormatter.getDateTimeFormatter())
  def underTest = new IsPepRequestCreator(timestampMapper)

  def "should return IsPepLearningStoreExchangeRequest object"() {
    given:
    def request = [getAlertData()]

    when:
    def result = underTest.create(request)

    then:
    def alert = result.alertsList.first()
    alert.getAlertId() == "someKeyLabel"
    alert.getMatchId() == "1"
    alert.getAlertedPartyCountry() == "someEdqLoBCountryCode"
    alert.getWatchlistId() == "1"

    def comment = alert.commentsList.first()
    comment.id == "1"
    comment.value == "someComment"
    comment.createdAt == 1609286400
  }

  AlertData getAlertData() {
    def json = getClass().getResource("/files/alertData.json")
    return new ObjectMapper().readValue(json, AlertData.class)
  }
}
