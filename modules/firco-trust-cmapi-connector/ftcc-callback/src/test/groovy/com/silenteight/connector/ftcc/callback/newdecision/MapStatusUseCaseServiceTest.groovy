package com.silenteight.connector.ftcc.callback.newdecision

import com.silenteight.connector.ftcc.callback.decision.DestinationStatus
import com.silenteight.connector.ftcc.callback.decision.MapStatusRequest
import com.silenteight.connector.ftcc.common.dto.input.StatusInfoDto

import spock.lang.Specification

class MapStatusUseCaseServiceTest extends Specification {

  def "MapStatus {currentStatus} and {recommendation} should be {destinationStatus} or throw {exception}"() {
    given:
    def decisions = List.of(
        new DecisionTransition("NEW", "ACTION_FALSE_POSITIVE", "Level 2- FALSE1"),
        new DecisionTransition("NEW", "ACTION_POTENTIAL_TRUE_POSITIVE", "Level 2- TRUE1"),
        new DecisionTransition("NEW", "ACTION_INVESTIGATE", "Level 1- SEAR_UNRESOLVED"))
    def holder = new DecisionConfigurationHolder(decisions)
    def ms = new MapStatusUseCaseService(holder)

    def nextStatuses = List.of(
        new StatusInfoDto("1", "Level 2- FALSE1", "12", "checksum"),
        new StatusInfoDto("2", "Level 2- TRUE1", "21", "checksum"),
        new StatusInfoDto("3", "Level 1- SEAR_UNRESOLVED", "31", "checksum"))
    def caughtException
    def status

    when:
    try {
      status = ms.mapStatus(
          MapStatusRequest.builder()
              .dataCenter("DATA_CENTER")
              .unit("MESSAGEENTITY.GETUNIT")
              .nextStatuses(nextStatuses)
              .currentStatusName(currentStatus)
              .recommendedAction(recommendation)
              .build())
    } catch (Exception e) {
      caughtException = e
    }

    then:
    assert status == destinationStatus

    if (exception.expected) {
      assert caughtException != null && exception.ex.isInstance(caughtException)
    } else {
      assert caughtException == null
    }

    where:
    //  @formatter:off
    currentStatus  | recommendation                   || destinationStatus                                                                                                | exception
    "NEW"          | "ACTION_FALSE_POSITIVE"          || DestinationStatus.builder().status(new StatusInfoDto("1", "Level 2- FALSE1", "12", "checksum")).build()          | [expected: false, ex: null]
    "NEW"          | "ACTION_POTENTIAL_TRUE_POSITIVE" || DestinationStatus.builder().status(new StatusInfoDto("2", "Level 2- TRUE1", "21", "checksum")).build()           | [expected: false, ex: null]
    "NEW"          | "ACTION_INVESTIGATE"             || DestinationStatus.builder().status(new StatusInfoDto("3", "Level 1- SEAR_UNRESOLVED", "31", "checksum")).build() | [expected: false, ex: null]
    "WRONG_STATUS" | "ACTION_FALSE_POSITIVE"          || null                                                                                                             | [expected: true, ex: IllegalStateException]
    "NEW"          | "ACTION_NOT_EXIST"               || null                                                                                                             | [expected: true, ex: IllegalStateException]
    //  @formatter:on
  }
}
