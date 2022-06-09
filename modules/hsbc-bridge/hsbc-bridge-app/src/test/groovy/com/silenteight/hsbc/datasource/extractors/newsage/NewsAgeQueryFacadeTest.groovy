package com.silenteight.hsbc.datasource.extractors.newsage

import com.silenteight.hsbc.bridge.json.internal.model.NegativeNewsScreeningEntities
import com.silenteight.hsbc.bridge.json.internal.model.NegativeNewsScreeningIndividuals
import com.silenteight.hsbc.datasource.datamodel.MatchData

import spock.lang.Specification

class NewsAgeQueryFacadeTest extends Specification {

  def matchData = Mock(MatchData)
  def underTest = new NewsAgeQueryFacade(matchData)

  def "should return further information for many individual if further information are same"() {
    given:
    def expected = "Some Further Information"

    when:
    def result = underTest.nnsIndividualsFurtherInformation()

    then:
    1 * matchData.getNnsIndividuals() >> List.of(
        new NegativeNewsScreeningIndividuals(furtherInformation: "Some Further Information"),
        new NegativeNewsScreeningIndividuals(furtherInformation: "Some Further Information"))
    result == expected
  }

  def "should return further information for single individual"() {
    given:
    def expected = "Some Further Information"

    when:
    def result = underTest.nnsIndividualsFurtherInformation()

    then:
    1 * matchData.getNnsIndividuals() >> List.of(new NegativeNewsScreeningIndividuals(furtherInformation: "Some Further Information"))
    result == expected
  }

  def "should return null as further information for multiple individuals with different further information"() {
    when:
    def result = underTest.nnsIndividualsFurtherInformation()

    then:
    1 * matchData.getNnsIndividuals() >> List.of(
        new NegativeNewsScreeningIndividuals(furtherInformation: "Some Further Information"),
        new NegativeNewsScreeningIndividuals(furtherInformation: "Other Further Information"))
    result == null
  }

  def "should return further information for many entities if further information are same"() {
    given:
    def expected = "Some Further Information"

    when:
    def result = underTest.nnsEntitiesFurtherInformation()

    then:
    1 * matchData.getNnsEntities() >> List.of(
        new NegativeNewsScreeningEntities(furtherInformation: "Some Further Information"),
        new NegativeNewsScreeningEntities(furtherInformation: "Some Further Information"))
    result == expected
  }

  def "should return further information for single entity"() {
    given:
    def expected = "Some Further Information"

    when:
    def result = underTest.nnsEntitiesFurtherInformation()

    then:
    1 * matchData.getNnsEntities() >> List.of(new NegativeNewsScreeningEntities(furtherInformation: "Some Further Information"))
    result == expected
  }

  def "should return null as further information for multiple entities with different further information"() {
    when:
    def result = underTest.nnsEntitiesFurtherInformation()

    then:
    1 * matchData.getNnsEntities() >> List.of(
        new NegativeNewsScreeningEntities(furtherInformation: "Some Further Information"),
        new NegativeNewsScreeningEntities(furtherInformation: "Other Further Information"))
    result == null
  }
}
