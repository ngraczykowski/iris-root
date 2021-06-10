package com.silenteight.hsbc.bridge.match

import com.silenteight.hsbc.bridge.json.ObjectConverter
import com.silenteight.hsbc.bridge.json.ObjectConverter.ObjectConversionException

import org.springframework.context.ApplicationEventPublisher
import spock.lang.Specification

import static java.util.Optional.of

class MatchFacadeSpec extends Specification {

  def objectConverter = Mock(ObjectConverter)
  def matchDataMapper = Mock(MatchDataMapper)
  def repository = Mock(MatchRepository)
  def eventPublisher = Mock(ApplicationEventPublisher)
  def underTest = new MatchFacade(objectConverter, matchDataMapper, repository, eventPublisher)

  def fixtures = new Fixtures()

  def 'should throw exception when try to get archived match'() {
    given:
    def someName = fixtures.someName

    when:
    underTest.getMatches([someName])

    then:
    thrown(MatchDataNoLongerAvailableException)
    1 * repository.findByName(someName) >> of(fixtures.archivedMatch)
  }

  def 'should throw exception when try to get match with non-convertible content'() {
    given:
    def someName = fixtures.someName

    when:
    underTest.getMatches([someName])

    then:
    thrown(MatchDataNoLongerAvailableException)
    1 * repository.findByName(someName) >> of(fixtures.match)
    1 * objectConverter.convert(fixtures.somePayload, MatchRawData.class) >>
        {throw new ObjectConversionException(null)}
  }

  def 'should throw exception when try to get not existing match'() {
    given:
    def someName = fixtures.someName

    when:
    underTest.getMatches([someName])

    then:
    thrown(MatchNotFoundException)
    1 * repository.findByName(someName) >> Optional.empty()
  }

  def 'should get matches by names'() {
    given:
    def someName = fixtures.someName

    when:
    def result = underTest.getMatches([fixtures.someName])

    then:
    result.size() == 1
    with(result.first()) {
      name == someName
      matchData == fixtures.someMatchData
    }

    1 * repository.findByName(someName) >> of(fixtures.match)
    1 * objectConverter.convert(fixtures.somePayload, MatchRawData.class) >> fixtures.someMatchData
  }

  class Fixtures {

    def someName = 'match/1'
    def somePayload = "somePayload".getBytes()
    def someMatchData = new MatchRawData()
    def archivedMatch = createArchivedMatch()
    def match = createMatch(someName, somePayload)

    def createArchivedMatch() {
      def match = new MatchEntity('id', 1)
      match.setPayload(new MatchPayloadEntity(1, null))
      match
    }

    def createMatch(name, payload) {
      def match = new MatchEntity('id', 1)
      match.setName(name)
      match.setPayload(new MatchPayloadEntity(1, payload))
      match
    }
  }
}
