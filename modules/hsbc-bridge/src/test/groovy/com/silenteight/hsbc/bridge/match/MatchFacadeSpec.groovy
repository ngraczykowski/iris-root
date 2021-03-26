package com.silenteight.hsbc.bridge.match

import com.silenteight.hsbc.bridge.alert.AlertComposite
import com.silenteight.hsbc.bridge.rest.model.input.Alert
import com.silenteight.hsbc.bridge.rest.model.input.AlertSystemInformation

import spock.lang.Specification

import static java.util.Optional.of

class MatchFacadeSpec extends Specification {

  def matchRepository = Mock(MatchRepository)
  def underTest = MatchFacade.builder()
      .matchPayloadConverter(new MatchPayloadConverter())
      .matchRepository(matchRepository)
      .matchRawMapper(new MatchRawMapper())
      .build()

  def fixtures = new Fixtures()

  def 'should get match by id'() {
    given:
    def id = 1

    when:
    def result = underTest.getMatch(id)

    then:
    result.rawData == fixtures.matchRawData
    1 * matchRepository.findById(id) >> of(new MatchEntity(2, "", fixtures.matchPayload))
  }

  def 'should prepare and save matches'() {
    given:
    def alertComposite = AlertComposite.builder()
        .id(1)
        .alert(fixtures.alert)
        .build()

    when:
    def result = underTest.prepareAndSaveMatches(alertComposite)

    then:
    1 * matchRepository.save(_ as MatchEntity) >> {MatchEntity entity -> entity.id = 2}
    result == [2]
  }

  class Fixtures {

    MatchRawData matchRawData = new MatchRawData()
    byte[] matchPayload = new MatchPayloadConverter().convert(matchRawData)

    Alert alert = new Alert(
        systemInformation: new AlertSystemInformation()
    )
  }
}
