package com.silenteight.scb.ingest.adapter.incomming.common.ingest

import com.silenteight.scb.ingest.adapter.incomming.common.recommendation.ScbRecommendationService
import com.silenteight.scb.ingest.adapter.incomming.common.store.batchinfo.BatchInfoService
import com.silenteight.scb.ingest.adapter.incomming.common.trafficmanagement.TrafficManager
import com.silenteight.scb.ingest.adapter.incomming.common.util.InternalBatchIdGenerator
import com.silenteight.scb.ingest.domain.AlertRegistrationFacade
import com.silenteight.scb.reports.domain.port.outgoing.ReportsSenderService

import spock.lang.Specification

import static com.silenteight.scb.ingest.adapter.incomming.common.ingest.Fixtures.alerts
import static com.silenteight.scb.ingest.adapter.incomming.common.ingest.Fixtures.recommendation
import static com.silenteight.scb.ingest.domain.Fixtures.registrationResponse
import static com.silenteight.scb.ingest.domain.model.BatchSource.LEARNING
import static com.silenteight.scb.ingest.domain.model.RegistrationBatchContext.CBS_CONTEXT
import static com.silenteight.scb.ingest.domain.model.RegistrationBatchContext.LEARNING_CONTEXT

class IngestServiceSpec extends Specification {

  def scbRecommendationService = Mock(ScbRecommendationService)
  def alertRegistrationFacade = Mock(AlertRegistrationFacade)
  def udsFeedingPublisher = Mock(UdsFeedingPublisher)
  def reportsSenderService = Mock(ReportsSenderService)
  def trafficManager = Mock(TrafficManager)
  def batchInfoService = Mock(BatchInfoService)

  def ingestService = IngestService.builder()
      .scbRecommendationService(scbRecommendationService)
      .alertRegistrationFacade(alertRegistrationFacade)
      .udsFeedingPublisher(udsFeedingPublisher)
      .reportsSenderService(reportsSenderService)
      .trafficManager(trafficManager)
      .batchInfoService(batchInfoService)
      .build()

  def 'should ingest alerts without Recommendation for learn'() {
    given:
    def internalBatchId = InternalBatchIdGenerator.generate()
    def alerts = alerts()

    when:
    ingestService.ingestAlertsForLearn(internalBatchId, alerts)

    then:
    2 * scbRecommendationService.alertRecommendation(_, _) >> Optional.empty()
    1 * trafficManager.holdPeriodicAlertProcessing() >> false
    1 * batchInfoService.store(internalBatchId, LEARNING, _)
    1 * alertRegistrationFacade.registerAlerts(internalBatchId, alerts, LEARNING_CONTEXT) >>
        registrationResponse(alerts)
    1 * udsFeedingPublisher.publishToUds(internalBatchId, alerts,  LEARNING_CONTEXT)
    1 * reportsSenderService.send({it.size() == 2})
    0 * _
  }

  def 'should not ingest alerts with Recommendation for learn'() {
    given:
    def internalBatchId = InternalBatchIdGenerator.generate()
    def alerts = alerts()

    def r1 = recommendation(alerts[0])
    def r2 = recommendation(alerts[1])

    when:
    ingestService.ingestAlertsForLearn(internalBatchId, alerts)

    then:
    2 * scbRecommendationService.alertRecommendation(r1.systemId, r1.discriminator) >>
        Optional.of(r1)
    2 * scbRecommendationService.alertRecommendation(r2.systemId, r2.discriminator) >>
        Optional.of(r2)
    1 * trafficManager.holdPeriodicAlertProcessing() >> false
    0 * batchInfoService.store(internalBatchId, LEARNING, _)
    1 * reportsSenderService.send({it.size() == 2})
    0 * _
  }

  def 'should ingest alerts for recommendation'() {
    given:
    def internalBatchId = InternalBatchIdGenerator.generate()
    def alerts = alerts()

    when:
    ingestService.ingestAlertsForRecommendation(internalBatchId, alerts, CBS_CONTEXT)

    then:
    1 * alertRegistrationFacade.registerAlerts(internalBatchId, alerts, CBS_CONTEXT)
        >> registrationResponse(alerts)
    1 * udsFeedingPublisher.publishToUds(internalBatchId, alerts, CBS_CONTEXT)
    0 * _
  }

}
