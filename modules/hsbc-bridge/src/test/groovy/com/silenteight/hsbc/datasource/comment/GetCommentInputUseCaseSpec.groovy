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
  def underTest = new GetCommentInputUseCase(matchFacade)

  // TODO Struct checks
  def 'should get comment inputs'() {
    given:
    def request = StreamCommentInputsRequestDto.builder()
        .alerts(['alerts/1'])
        .build()

    def matchCommentInputDto = MatchCommentInputDto.builder()
        .match('alerts/1/matches/1')
        .build()

    def alertCommentInput = [caseId: "1", apId: "1", listName: "someListName", apType: "I", wlType: "I", wlId: "someWlId"]

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
    2 * matchData.getCaseInformation() >> caseInformation
    1 * matchData.isIndividual() >> true
    1 * matchData.getWatchlistId() >> Optional.of("someWlId")
    1 * caseInformation.getExternalId() >> "1"
    1 * caseInformation.getExtendedAttribute10() >> "someListName"

    result.size() == 1
    with(result.first()) {
      alert == response.alert
      with(alertCommentInput as Map<String, String>){
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
}
