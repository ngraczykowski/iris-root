package com.silenteight.bridge.core.reports.adapter.incoming

import com.silenteight.bridge.core.reports.domain.ReportsFacade
import com.silenteight.bridge.core.reports.domain.commands.SendReportsCommand
import com.silenteight.proto.registration.api.v1.MessageBatchDelivered

import spock.lang.Specification
import spock.lang.Subject

class BatchDeliveredAmqpListenerSpec extends Specification {

  def reportsFacade = Mock(ReportsFacade)

  @Subject
  def underTest = new BatchDeliveredAmqpListener(reportsFacade)

  def 'should call reports facade with proper arguments'() {
    given:
    def batchId = 'some batch id'
    def analysisName = 'some analysis name'

    def batchDeliveredMessage = MessageBatchDelivered.newBuilder()
        .setBatchId(batchId)
        .setAnalysisName(analysisName)
        .build()

    def expectedCommand = new SendReportsCommand(batchId, analysisName)

    when:
    underTest.batchDelivered(batchDeliveredMessage)

    then:
    1 * reportsFacade.sendReports(expectedCommand)
  }
}
