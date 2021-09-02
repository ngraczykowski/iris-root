package com.silenteight.hsbc.bridge.agent

import com.silenteight.hsbc.bridge.json.external.model.AlertData
import com.silenteight.hsbc.bridge.util.CustomDateTimeFormatter

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

class HistoricalDecisionRequestCreatorSpec extends Specification {

  def dateTimeFormatter = new CustomDateTimeFormatter("yyyy-MMM-dd HH:mm:ss")
  def timestampMapper = new AgentTimestampMapper(dateTimeFormatter.getDateTimeFormatter())
  def underTest = new HistoricalDecisionRequestCreator(timestampMapper)

  def "should return HistoricalDecisionRequest object"(){
    when:
    def result = underTest.create(List.of(getAlertData()))

    then:
    def alert = result.alertsList.first()
    alert.alertId == "someKeyLabel"
    alert.matchId == "1"
    alert.alertedParty.country == "someEdqLoBCountryCode"

    def watchlist = alert.watchlist
    watchlist.id == "1"
    watchlist.type == "WorldCheckIndividuals"

    def decision = alert.decisionsList.first()
    decision.value == "newValue"
    decision.createdAt == 1609286400
  }

  AlertData getAlertData(){
    def json = getClass().getResource("/files/alertData.json")
    return new ObjectMapper().readValue(json, AlertData.class)
  }
}
