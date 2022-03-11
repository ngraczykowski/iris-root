package com.silenteight.bridge.core.registration.adapter.incoming

import com.silenteight.bridge.core.registration.domain.command.AddAlertToAnalysisCommand
import com.silenteight.bridge.core.registration.domain.command.AddAlertToAnalysisCommand.FeedingStatus
import com.silenteight.bridge.core.registration.domain.RegistrationFacade
import com.silenteight.proto.registration.api.v1.FedMatch
import com.silenteight.proto.registration.api.v1.MessageAlertMatchesFeatureInputFed

import spock.lang.Specification
import spock.lang.Subject

class AlertMatchesFeatureInputFedReceivedAmqpListenerSpec extends Specification {

  def registrationFacade = Mock(RegistrationFacade)

  @Subject
  def underTest = new AlertMatchesFeatureInputFedReceivedAmqpListener(registrationFacade)

  def 'should call RegistrationFacade with proper arguments'() {
    given:
    def messages = [
        MessageAlertMatchesFeatureInputFed.newBuilder()
            .setBatchId('1')
            .setAlertId('2')
            .setAlertErrorDescription('')
            .setFeedingStatus(MessageAlertMatchesFeatureInputFed.FeedingStatus.SUCCESS)
            .addAllFedMatches(
                [
                    FedMatch.newBuilder()
                        .setMatchId('3')
                        .build()
                ])
            .build()
    ]

    def expectedCommand = AddAlertToAnalysisCommand.builder()
        .batchId(messages.first().batchId)
        .alertId(messages.first().alertId)
        .errorDescription('')
        .feedingStatus(FeedingStatus.SUCCESS)
        .fedMatches(
            [
                new AddAlertToAnalysisCommand.FedMatch(
                    messages.first().getFedMatchesList().first().matchId)
            ])
        .build()

    when:
    underTest.matchFeatureInputSetFed(messages)

    then:
    1 * registrationFacade.addAlertsToAnalysis([expectedCommand])
  }
}
