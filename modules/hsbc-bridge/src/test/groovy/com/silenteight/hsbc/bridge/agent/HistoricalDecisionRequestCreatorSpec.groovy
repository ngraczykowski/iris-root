package com.silenteight.hsbc.bridge.agent

import com.silenteight.hsbc.bridge.json.external.model.AlertData
import com.silenteight.hsbc.bridge.util.CustomDateTimeFormatter

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

class HistoricalDecisionRequestCreatorSpec extends Specification {

  def dateTimeFormatter = new CustomDateTimeFormatter("[yyyy-MMM-dd HH:mm:ss][dd-MMM-yy]")
  def timestampMapper = new AgentTimestampMapper(dateTimeFormatter.getDateTimeFormatter())
  def underTest = new HistoricalDecisionRequestCreator(timestampMapper)
  def static DATA_PATH_PROD_DATE_FORMAT = "/files/alertDataProdDateFormat.json"
  def static DATA_PATH_TEST_DATE_FORMAT = "/files/alertDataTestDateFormat.json"

  def "should return HistoricalDecisionRequest object for data with prod env date format"() {
    given:
    def request = [getData(DATA_PATH_PROD_DATE_FORMAT)]

    when:
    def result = underTest.create(request)

    then:
    def alert = result.alertsList.first()
    alert.alertId == "someKeyLabelProd"
    alert.matchId == "1"
    alert.alertedParty.country == "someEdqLoBCountryCodeProd"

    def watchlist = alert.watchlist
    watchlist.id == "1"
    watchlist.type == "WorldCheckIndividuals"

    def decision = alert.decisionsList.first()
    decision.value == "newValueProd"
    decision.createdAt == 1609320006
  }

  def "should return HistoricalDecisionRequest object for data with testing env date format"() {
    given:
    def request = [getData(DATA_PATH_TEST_DATE_FORMAT)]

    when:
    def result = underTest.create(request)

    then:
    def alert = result.alertsList.first()
    alert.alertId == "someKeyLabelTest"
    alert.matchId == "1"
    alert.alertedParty.country == "someEdqLoBCountryCodeTest"

    def watchlist = alert.watchlist
    watchlist.id == "1"
    watchlist.type == "WorldCheckIndividuals"

    def decision = alert.decisionsList.first()
    decision.value == "newValueTest"
    decision.createdAt == 1262304000
  }

  AlertData getData(String path) {
    def json = getClass().getResource(path)
    return new ObjectMapper().readValue(json, AlertData.class)
  }
}
