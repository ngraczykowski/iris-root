package com.silenteight.hsbc.bridge.agent

import com.silenteight.hsbc.bridge.json.external.model.AlertData
import com.silenteight.hsbc.bridge.util.CustomDateTimeFormatter

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

import static com.silenteight.hsbc.bridge.agent.Fixtures.FURTHER_INFORMATION

class IsPepRequestCreatorSpec extends Specification {

  def dateTimeFormatter = new CustomDateTimeFormatter("[yyyy-MMM-dd HH:mm:ss][dd-MMM-yy]")
  def timestampMapper = new AgentTimestampMapper(dateTimeFormatter.getDateTimeFormatter())
  def underTest = new IsPepRequestCreator(timestampMapper)
  def static DATA_PATH_PROD_DATE_FORMAT = "/files/alertDataProdDateFormat.json"
  def static DATA_PATH_TEST_DATE_FORMAT = "/files/alertDataTestDateFormat.json"

  def 'should return IsPepLearningStoreExchangeRequest object for data with prod env date format'() {
    given:
    def request = [getData(DATA_PATH_PROD_DATE_FORMAT)]

    when:
    def result = underTest.create(request)

    then:
    def alert = result.alertsList.first()
    alert.getAlertId() == 'someKeyLabelProd'
    alert.getMatchId() == '1'
    alert.getAlertedPartyCountry() == 'someEdqLoBCountryCodeProd'
    alert.getWatchlistId() == '1'
    alert.getFurtherInformation() == FURTHER_INFORMATION

    def comment = alert.commentsList.first()
    comment.id == '1'
    comment.value == 'someCommentProd'
    comment.createdAt == 1609320006

    def linkedTo = alert.linkedPepsUidsList.first()
    linkedTo == '28966;376830;376831;376832;448756;80217'
  }

  def 'should return IsPepLearningStoreExchangeRequest object for data with testing env date format'() {
    given:
    def request = [getData(DATA_PATH_TEST_DATE_FORMAT)]

    when:
    def result = underTest.create(request)

    then:
    def alert = result.alertsList.first()
    alert.getAlertId() == 'someKeyLabelTest'
    alert.getMatchId() == '1'
    alert.getAlertedPartyCountry() == 'someEdqLoBCountryCodeTest'
    alert.getWatchlistId() == '1'
    alert.getFurtherInformation() == FURTHER_INFORMATION

    def comment = alert.commentsList.first()
    comment.id == '2'
    comment.value == 'someCommentTest'
    comment.createdAt == 1262304000

    def linkedTo = alert.linkedPepsUidsList.first()
    linkedTo == '28966;376830;376831;376832;448756;80217'
  }

  AlertData getData(String path) {
    def json = getClass().getResource(path)
    return new ObjectMapper().readValue(json, AlertData.class)
  }
}
