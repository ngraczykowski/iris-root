package com.silenteight.hsbc.datasource.comment

import com.silenteight.hsbc.bridge.match.MatchComposite
import com.silenteight.hsbc.bridge.match.MatchFacade
import com.silenteight.hsbc.datasource.datamodel.CaseInformation
import com.silenteight.hsbc.datasource.datamodel.MatchData

import spock.lang.Specification

class GetCommentInputUseCaseSpec extends Specification {

  def matchFacade = Mock(MatchFacade)
  def matchData = Mock(MatchData)
  def caseInformation = Mock(CaseInformation)
  def fixtures = new Fixtures()
  def underTest = new GetCommentInputUseCase(matchFacade, fixtures.wlTypes)

  // TODO Struct checks
  def 'should get comment inputs'() {
    given:
    def request = StreamCommentInputsRequestDto.builder()
        .alerts(['alerts/1'])
        .build()

    def matchCommentInputDto = MatchCommentInputDto.builder()
        .match('alerts/1/matches/1')
        .build()

    def alertCommentInput = [caseId: "1", apId: "1", listName: "someWatchlistType", apType: "I", wlType: "someWatchlistValue", wlId: "someWlId"]

    def response = CommentInputDto.builder()
        .alert('alerts/1')
        .alertCommentInput(alertCommentInput)
        .matchCommentInputsDto([matchCommentInputDto])
        .build()

    def matchComposite = MatchComposite.builder()
        .name('alerts/1/matches/1')
        .externalId("1")
        .matchData(matchData)
        .build()

    when:
    def result = underTest.getInputRequestsResponse(request)

    then:
    1 * matchFacade.getMatchesByAlertNames(_ as List<String>) >> [matchComposite]
    3 * matchData.getCaseInformation() >> caseInformation
    1 * matchData.isIndividual() >> true
    1 * matchData.getWatchlistId() >> Optional.of("someWlId")
    1 * caseInformation.getExternalId() >> "1"
    2 * caseInformation.getExtendedAttribute5() >> "someWatchlistType"

    result.size() == 1
    with(result.first()) {
      alert == response.alert
      with(alertCommentInput as Map<String, String>) {
        def comments = response.alertCommentInput
        get("caseId") == comments.get("caseId")
        get("apId") == comments.get("apId")
        get("listName") == comments.get("listName")
        get("apType") == comments.get("apType")
      }
      with(matchCommentInputsDto as List<MatchCommentInputDto>) {
        size() == 1
        first().match == response.matchCommentInputsDto.first().match
      }
    }
  }

  class Fixtures {

    def wlTypes =
        [
            'SAN'  : [
                'AE-MEWOLF',
                'MENA-GREY',
                'MEWOLF',
                'MX-DARK-GREY',
                'SAN',
                'US-HBUS',
            ] as List,
            'AML'  : [
                'AML',
                'CTF-P2',
                'INNIA',
                'MX-AML',
                'MX-SHCP',
            ] as List,
            'PEP'  : [
                'PEP'
            ] as List,
            'EXITS': [
                'SCION'
            ] as List,
            'SSC'  : [
                'SSC'
            ]
        ] as Map
  }
}
