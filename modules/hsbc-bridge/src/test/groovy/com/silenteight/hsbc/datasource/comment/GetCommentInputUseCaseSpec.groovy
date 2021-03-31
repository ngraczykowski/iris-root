package com.silenteight.hsbc.datasource.comment

import com.silenteight.hsbc.bridge.alert.AlertFacade
import com.silenteight.hsbc.bridge.alert.AlertInfo
import com.silenteight.hsbc.bridge.match.MatchComposite
import com.silenteight.hsbc.bridge.match.MatchFacade

import spock.lang.Specification

class GetCommentInputUseCaseSpec extends Specification {

  def alertFacade = Mock(AlertFacade)
  def matchFacade = Mock(MatchFacade)
  def underTest = new GetCommentInputUseCase(alertFacade, matchFacade)

  // TODO Struct checks
  def 'should get comment inputs'() {
    given:
    def request = StreamCommentInputsRequestDto.builder()
        .alerts(['alerts/1'])
        .build()

    def matchCommentInputDto = MatchCommentInputDto.builder()
        .match('alerts/1/matches/1')
        .build()

    def response = CommentInputDto.builder()
        .alert('alerts/1')
        .matchCommentInputsDto([matchCommentInputDto])
        .build()

    def alertInfo = AlertInfo.builder()
        .id(1)
        .build()

    def matchComposite = MatchComposite.builder()
        .name('alerts/1/matches/1')
        .build()

    when:
    def result = underTest.getInputRequestsResponse(request)

    then:
    1 * alertFacade.getAlertByName(_ as String) >> [alertInfo]
    1 * matchFacade.getMatchesByAlertId(_ as Long) >> [matchComposite]

    result.size() == 1
    with(result.first()) {
      alert == response.alert
      with(matchCommentInputsDto as List<MatchCommentInputDto>) {
        size() == 1
        first().match == response.matchCommentInputsDto.first().match
      }
    }
  }
}
