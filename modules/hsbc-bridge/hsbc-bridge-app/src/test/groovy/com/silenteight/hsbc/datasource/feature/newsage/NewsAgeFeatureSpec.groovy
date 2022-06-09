package com.silenteight.hsbc.datasource.feature.newsage

import com.silenteight.hsbc.bridge.json.internal.model.NegativeNewsScreeningEntities
import com.silenteight.hsbc.bridge.json.internal.model.NegativeNewsScreeningIndividuals
import com.silenteight.hsbc.datasource.datamodel.MatchData
import com.silenteight.hsbc.datasource.datamodel.WatchlistType
import com.silenteight.hsbc.datasource.extractors.newsage.NewsAgeConfigurer

import spock.lang.Specification

class NewsAgeFeatureSpec extends Specification {

  def queryFactory = new NewsAgeConfigurer().create()
  def underTest = new NewsAgeFeature(queryFactory)

  def 'should retrieve news age values when customer is individual'() {
    given:
    def matchData = Mock(MatchData) {
      isIndividual() >> true
      getWatchlistId() >> Optional.of("watchList/id")
      getWatchlistType() >> Optional.of(WatchlistType.NNS_LIST_INDIVIDUALS)
      getNnsIndividuals() >> List.of(new NegativeNewsScreeningIndividuals(furtherInformation: "Some further Information"))
    }

    when:
    def result = underTest.retrieve(matchData)

    then:
    result.getFeature() == "features/newsAge"
    result.getWatchlistItem().getId() == "watchList/id"
    result.getWatchlistItem().getType() == "NNSIndividuals"
    result.getWatchlistItem().getFurtherInformation() == "Some further Information"
  }

  def 'should retrieve news age values when customer is entity'() {
    given:
    def matchData = Mock(MatchData) {
      isIndividual() >> false
      getWatchlistId() >> Optional.of("watchList/id")
      getWatchlistType() >> Optional.of(WatchlistType.NNS_LIST_ENTITIES)
      getNnsEntities() >> List.of(new NegativeNewsScreeningEntities(furtherInformation: "Some further Information"))
    }

    when:
    def result = underTest.retrieve(matchData)

    then:
    result.getFeature() == "features/newsAge"
    result.getWatchlistItem().getId() == "watchList/id"
    result.getWatchlistItem().getType() == "NNSEntities"
    result.getWatchlistItem().getFurtherInformation() == "Some further Information"
  }
}
