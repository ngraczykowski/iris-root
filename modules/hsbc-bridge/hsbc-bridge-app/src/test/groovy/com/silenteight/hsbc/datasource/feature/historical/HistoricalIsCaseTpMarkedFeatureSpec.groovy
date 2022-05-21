package com.silenteight.hsbc.datasource.feature.historical

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual
import com.silenteight.hsbc.datasource.datamodel.MatchData
import com.silenteight.hsbc.datasource.datamodel.WatchlistType
import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual
import com.silenteight.hsbc.datasource.dto.historical.MatchDto
import com.silenteight.hsbc.datasource.dto.historical.ModelKeyDto.ModelKeyType
import com.silenteight.hsbc.datasource.extractors.historical.HistoricalDecisionsQueryConfigurer
import com.silenteight.hsbc.datasource.feature.Feature

import spock.lang.Specification

class HistoricalIsCaseTpMarkedFeatureSpec extends Specification {

  def query = new HistoricalDecisionsQueryConfigurer().create()

  def underTest = new HistoricalIsCaseTpMarkedFeature(query)

  def 'should retrieve HistoricalIsCaseTpMarked feature values'() {
    given:
    def customerIndividual = Mock(CustomerIndividual) {
      getExternalProfileId() >> 'aviR126SCR5640LU259TEST0016'
    }

    def worldCheckIndividual = Mock(WorldCheckIndividual) {
      getListRecordId() >> '2309274'
    }

    def matchData = Mock(MatchData) {
      isIndividual() >> true
      getCustomerIndividuals() >> [customerIndividual]
      getWorldCheckIndividuals() >> [worldCheckIndividual]
      hasWorldCheckIndividuals() >> true
      getWatchlistId() >> Optional.of('2309274')
      getWatchlistType() >> Optional.of(WatchlistType.WORLDCHECK_INDIVIDUALS)
    }

    when:
    def result = underTest.retrieve(matchData)

    then:
    with(result) {
      feature == Feature.HISTORICAL_IS_CASE_TP_MARKED.fullName
      modelKey.modelKeyType == ModelKeyType.MATCH
      ((MatchDto) modelKey.modelKeyValue).alertedParty.id == 'aviR126SCR5640LU259TEST0016'
      ((MatchDto) modelKey.modelKeyValue).watchlistParty.id == '2309274'
      ((MatchDto) modelKey.modelKeyValue).watchlistParty.type == 'WorldCheckIndividuals'
      discriminator == "hotel_true_positive"
    }
  }
}
