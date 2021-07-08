package com.silenteight.hsbc.bridge.ispep

import com.silenteight.hsbc.bridge.json.external.model.AlertData

import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Specification

class LearningStoreExchangeRequestCreatorSpec extends Specification {

  def "should return IsPepLearningStoreExchangeRequest object"(){
    when:
    def result = LearningStoreExchangeRequestCreator.create(List.of(getAlertData()))

    then:
    def alert = result.alertsList.first()
    alert.getAlertId() == "someKeyLabel"
    alert.getMatchId() == "1"
    alert.getAlertedPartyCountry() == "someEdqLoBCountryCode"
    alert.getWatchlistId() == "1"

    def comment = alert.commentsList.first()
    comment.id == "1"
    comment.value == "someComment"
    comment.createdAt == 1262304000
  }

  AlertData getAlertData(){
    def json = getClass().getResource("/files/alertData.json")
    return new ObjectMapper().readValue(json, AlertData.class)
  }
}
