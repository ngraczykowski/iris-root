package com.silenteight.hsbc.datasource.feature.historical

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual
import com.silenteight.hsbc.datasource.datamodel.MatchData
import com.silenteight.hsbc.datasource.datamodel.WatchlistType
import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual
import com.silenteight.hsbc.datasource.dto.historical.ModelKeyDto.ModelKeyType
import com.silenteight.hsbc.datasource.dto.historical.WatchlistPartyDto
import com.silenteight.hsbc.datasource.extractors.historical.HistoricalDecisionsQueryConfigurer
import com.silenteight.hsbc.datasource.feature.Feature

import spock.lang.Specification

class HistoricalIsTpMarkedFeatureSpec extends Specification {

  def query = new HistoricalDecisionsQueryConfigurer().create()

  def underTest = new HistoricalIsTpMarkedFeature(query)

  def 'should retrieve HistoricalIsTpMarked feature values'() {
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
      feature == Feature.HISTORICAL_IS_TP_MARKED.fullName
      modelKey.modelKeyType == ModelKeyType.WATCHLIST_PARTY
      ((WatchlistPartyDto) modelKey.modelKeyValue).id == '2309274'
      ((WatchlistPartyDto) modelKey.modelKeyValue).type == 'WorldCheckIndividuals'
      discriminator == "hotel_true_positive"
    }
  }
}
