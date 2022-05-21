package com.silenteight.hsbc.datasource.feature.historical

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual
import com.silenteight.hsbc.datasource.datamodel.MatchData
import com.silenteight.hsbc.datasource.dto.historical.AlertedPartyDto
import com.silenteight.hsbc.datasource.dto.historical.ModelKeyDto.ModelKeyType
import com.silenteight.hsbc.datasource.extractors.historical.HistoricalDecisionsQueryConfigurer
import com.silenteight.hsbc.datasource.feature.Feature

import spock.lang.Specification

class HistoricalIsApTpMarkedFeatureSpec extends Specification {

  def query = new HistoricalDecisionsQueryConfigurer().create()

  def underTest = new HistoricalIsApTpMarkedFeature(query)

  def 'should retrieve HistoricalIsApTpMarked feature values'() {
    given:
    def customerIndividual = Mock(CustomerIndividual) {
      getExternalProfileId() >> 'aviR126SCR5640LU259TEST0016'
    }

    def matchData = Mock(MatchData) {
      isIndividual() >> true
      getCustomerIndividuals() >> [customerIndividual]
    }

    when:
    def result = underTest.retrieve(matchData)

    then:
    with(result) {
      feature == Feature.HISTORICAL_IS_AP_TP_MARKED.fullName
      modelKey.modelKeyType == ModelKeyType.ALERTED_PARTY
      ((AlertedPartyDto) modelKey.modelKeyValue).id == 'aviR126SCR5640LU259TEST0016'
      discriminator == "hotel_true_positive"
    }
  }
}
