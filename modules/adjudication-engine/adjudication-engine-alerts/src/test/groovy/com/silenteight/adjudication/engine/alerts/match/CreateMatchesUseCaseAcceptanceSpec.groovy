package com.silenteight.adjudication.engine.alerts.match

import com.silenteight.adjudication.api.v1.Match
import com.silenteight.adjudication.api.v1.Match.Builder

import spock.lang.Specification

import java.util.stream.Collectors

import static com.silenteight.adjudication.engine.alerts.match.MatchFixtures.inMemoryMatchFacade
import static java.util.Collections.emptyList

class CreateMatchesUseCaseAcceptanceSpec extends Specification {

  private MatchFacade facade = inMemoryMatchFacade()

  def "should return empty output for empty input"() {
    when:
    def result = facade.createMatches(emptyList())

    then:
    result.empty
  }

  def "should throw on duplicated indexes"() {
    given:
    def newAlertMatches = NewAlertMatches.builder()
        .parentAlert("alerts/123")
        .match(match("match 1").setIndex(3).build())
        .match(match("match 2").setIndex(3).build())
        .build()

    when:
    facade.createMatches([newAlertMatches])

    then:
    def e = thrown(IllegalArgumentException)
    e.message =~ "duplicated"
  }

  def "should create matches"() {
    given:
    def newAlertMatches = NewAlertMatches.builder()
        .parentAlert("alerts/123")
        .match(match("match 1").setIndex(1).putLabels("label", "value").build())
        .match(match("match 2").setIndex(2).putLabels("another", "value").build())
        .build()

    when:
    def matches = facade.createMatches([newAlertMatches]).stream().map(m -> m.toMatch()).collect(
        Collectors.toList())

    then:
    matches.size() == 2
    verifyAll(matches[0]) {
      name =~ "alerts/123/matches/\\d+"
      matchId == "match 1"
      index == 0
      labelsMap == [label: "value"]
    }
    verifyAll(matches[1]) {
      name =~ "alerts/123/matches/\\d+"
      matchId == "match 2"
      index == 0
      labelsMap == [another: "value"]
    }
  }

  def "should base sort index on match input order if not specified"() {
    given:
    def newAlertMatches = NewAlertMatches.builder()
        .parentAlert("alerts/123")
        .match(match("match 1").build())
        .match(match("match 2").build())
        .build()

    when:
    def matches = facade.createMatches([newAlertMatches])

    then:
    matches[0].sortIndex == 0
    matches[1].sortIndex == 0
  }

  def "should check latest sort index and set correct indexes in new matches"() {
    given:
    facade.createMatches(
        [NewAlertMatches.builder()
             .parentAlert("alerts/123")
             .match(match("match 1").build())
             .match(match("match 2").build())
             .build()])
    def newAlertMatches = NewAlertMatches.builder()
        .parentAlert("alerts/123")
        .match(match("match 3").build())
        .match(match("match 4").build())
        .build()

    when:
    def matches = facade.createMatches([newAlertMatches])

    then:
    matches[0].sortIndex == 0
    matches[1].sortIndex == 0
  }

  private static Builder match(String matchId) {
    Match.newBuilder().setMatchId(matchId)
  }
}
