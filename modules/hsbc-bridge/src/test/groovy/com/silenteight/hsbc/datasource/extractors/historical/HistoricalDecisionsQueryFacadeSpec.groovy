package com.silenteight.hsbc.datasource.extractors.historical

import com.silenteight.hsbc.datasource.datamodel.CustomerIndividual
import com.silenteight.hsbc.datasource.datamodel.MatchData
import com.silenteight.hsbc.datasource.datamodel.WatchlistType
import com.silenteight.hsbc.datasource.datamodel.WorldCheckIndividual
import com.silenteight.hsbc.datasource.extractors.historical.ModelKeyDto.ModelKeyType

import spock.lang.Specification

class HistoricalDecisionsQueryFacadeSpec extends Specification {

  def serviceClient = Mock(HistoricalDecisionsServiceClient)

  def 'should correctly get IsApTpMarkedInput'() {
    given:
    def modelKeyDto = ModelKeyDto.builder()
        .modelKeyType(ModelKeyType.ALERTED_PARTY)
        .modelKeyValue(AlertedPartyDto.builder().id('aviR126SCR5640LU259TEST0016').build())
        .build()

    def modelCountsDto = ModelCountsDto.builder()
        .modelKey(modelKeyDto)
        .truePositivesCount(1)
        .build()

    def response = GetHistoricalDecisionsResponseDto.builder()
        .modelCounts([modelCountsDto])
        .build()

    def customerIndividual = Mock(CustomerIndividual) {
      getExternalProfileId() >> 'aviR126SCR5640LU259TEST0016'
    }

    def matchData = Mock(MatchData) {
      isIndividual() >> true
      getCustomerIndividuals() >> [customerIndividual]
    }

    def underTest = new HistoricalDecisionsQueryFacade(matchData, serviceClient)

    when:
    def result = underTest.getIsApTpMarkedInput()

    then:
    1 * serviceClient.getHistoricalDecisions(_ as GetHistoricalDecisionsRequestDto) >> response

    result.size() == 1

    with(result.first()) {
      it.getModelKey().getModelKeyType() == ModelKeyType.ALERTED_PARTY
      ((AlertedPartyDto) it.getModelKey().getModelKeyValue()).getId() ==
          'aviR126SCR5640LU259TEST0016'
      it.getTruePositivesCount() == 1
    }
  }

  def 'should correctly get CaseTpMarkedInput'() {
    given:
    def watchlistPartyDto = WatchlistPartyDto.builder()
        .id('2309274')
        .type(WatchlistType.WORLDCHECK_INDIVIDUALS.getLabel())
        .build()

    def matchDto = MatchDto.builder()
        .alertedParty(AlertedPartyDto.builder().id('aviR126SCR5640LU259TEST0016').build())
        .watchlistParty(watchlistPartyDto)
        .build()

    def modelKeyDto = ModelKeyDto.builder()
        .modelKeyType(ModelKeyType.MATCH)
        .modelKeyValue(matchDto)
        .build()

    def modelCountsDto = ModelCountsDto.builder()
        .modelKey(modelKeyDto)
        .truePositivesCount(1)
        .build()

    def response = GetHistoricalDecisionsResponseDto.builder()
        .modelCounts([modelCountsDto])
        .build()

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

    def underTest = new HistoricalDecisionsQueryFacade(matchData, serviceClient)

    when:
    def result = underTest.getCaseTpMarkedInput()

    then:
    1 * serviceClient.getHistoricalDecisions(_ as GetHistoricalDecisionsRequestDto) >> response

    result.size() == 1

    with(result.first()) {
      it.getModelKey().getModelKeyType() == ModelKeyType.MATCH
      ((MatchDto) it.getModelKey().getModelKeyValue()).getAlertedParty().getId() ==
          'aviR126SCR5640LU259TEST0016'
      ((MatchDto) it.getModelKey().getModelKeyValue()).getWatchlistParty().getId() == '2309274'
      ((MatchDto) it.getModelKey().getModelKeyValue()).getWatchlistParty().getType() ==
          'WorldCheckIndividuals'
      it.getTruePositivesCount() == 1
    }
  }

  def 'should correctly get IsTpMarkedInput'() {
    def watchlistPartyDto = WatchlistPartyDto.builder()
        .id('2309274')
        .type(WatchlistType.WORLDCHECK_INDIVIDUALS.getLabel())
        .build()

    def modelKeyDto = ModelKeyDto.builder()
        .modelKeyType(ModelKeyType.WATCHLIST_PARTY)
        .modelKeyValue(watchlistPartyDto)
        .build()

    def modelCountsDto = ModelCountsDto.builder()
        .modelKey(modelKeyDto)
        .truePositivesCount(1)
        .build()

    def response = GetHistoricalDecisionsResponseDto.builder()
        .modelCounts([modelCountsDto])
        .build()

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

    def underTest = new HistoricalDecisionsQueryFacade(matchData, serviceClient)

    when:
    def result = underTest.getIsTpMarkedInput()

    then:
    1 * serviceClient.getHistoricalDecisions(_ as GetHistoricalDecisionsRequestDto) >> response

    result.size() == 1

    with(result.first()) {
      it.getModelKey().getModelKeyType() == ModelKeyType.WATCHLIST_PARTY
      ((WatchlistPartyDto) it.getModelKey().getModelKeyValue()).getId() == '2309274'
      ((WatchlistPartyDto) it.getModelKey().getModelKeyValue()).getType() == 'WorldCheckIndividuals'
      it.getTruePositivesCount() == 1
    }
  }
}
