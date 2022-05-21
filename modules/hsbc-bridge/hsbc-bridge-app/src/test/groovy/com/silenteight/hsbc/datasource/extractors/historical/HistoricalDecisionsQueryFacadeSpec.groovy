package com.silenteight.hsbc.datasource.extractors.historical

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual
import com.silenteight.hsbc.datasource.datamodel.MatchData
import com.silenteight.hsbc.datasource.datamodel.WatchlistType
import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual
import com.silenteight.hsbc.datasource.dto.historical.AlertedPartyDto
import com.silenteight.hsbc.datasource.dto.historical.MatchDto
import com.silenteight.hsbc.datasource.dto.historical.ModelKeyDto.ModelKeyType
import com.silenteight.hsbc.datasource.dto.historical.WatchlistPartyDto

import spock.lang.Specification

class HistoricalDecisionsQueryFacadeSpec extends Specification {

  def 'should correctly get IsApTpMarkedInput'() {
    given:
    def customerIndividual = Mock(CustomerIndividual) {
      getExternalProfileId() >> 'aviR126SCR5640LU259TEST0016'
    }

    def matchData = Mock(MatchData) {
      isIndividual() >> true
      getCustomerIndividuals() >> [customerIndividual]
    }

    def underTest = new HistoricalDecisionsQueryFacade(matchData)

    when:
    def result = underTest.getIsApTpMarkedInput().get()

    then:
    with(result) {
      it.getModelKeyType() == ModelKeyType.ALERTED_PARTY
      ((AlertedPartyDto) it.getModelKeyValue()).getId() == 'aviR126SCR5640LU259TEST0016'
    }
  }

  def 'should correctly get CaseTpMarkedInput'() {
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

    def underTest = new HistoricalDecisionsQueryFacade(matchData)

    when:
    def result = underTest.getCaseTpMarkedInput().get()

    then:
    with(result) {
      it.getModelKeyType() == ModelKeyType.MATCH
      ((MatchDto) it.getModelKeyValue()).getAlertedParty().getId() == 'aviR126SCR5640LU259TEST0016'
      ((MatchDto) it.getModelKeyValue()).getWatchlistParty().getId() == '2309274'
      ((MatchDto) it.getModelKeyValue()).getWatchlistParty().getType() == 'WorldCheckIndividuals'
    }
  }

  def 'should correctly get IsTpMarkedInput'() {
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

    def underTest = new HistoricalDecisionsQueryFacade(matchData)

    when:
    def result = underTest.getIsTpMarkedInput().get()

    then:
    with(result) {
      it.getModelKeyType() == ModelKeyType.WATCHLIST_PARTY
      ((WatchlistPartyDto) it.getModelKeyValue()).getId() == '2309274'
      ((WatchlistPartyDto) it.getModelKeyValue()).getType() == 'WorldCheckIndividuals'
    }
  }
}
