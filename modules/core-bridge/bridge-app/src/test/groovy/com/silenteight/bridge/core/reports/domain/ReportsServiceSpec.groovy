package com.silenteight.bridge.core.reports.domain

import com.silenteight.bridge.core.reports.ReportFixtures
import com.silenteight.bridge.core.reports.domain.port.outgoing.RecommendationService
import com.silenteight.bridge.core.reports.domain.port.outgoing.RegistrationService
import com.silenteight.bridge.core.reports.domain.port.outgoing.ReportsSenderService

import spock.lang.Specification
import spock.lang.Subject

class ReportsServiceSpec extends Specification {

  def reportsMapper = Mock(ReportsMapper)
  def reportSenderService = Mock(ReportsSenderService)
  def registrationService = Mock(RegistrationService)
  def recommendationService = Mock(RecommendationService)

  @Subject
  def underTest = new ReportsService(
      reportsMapper,
      reportSenderService,
      registrationService,
      recommendationService
  )

  def 'should send reports for the given analysis'() {
    given:
    def batchId = ReportFixtures.BATCH_ID
    def analysisName = ReportFixtures.ANALYSIS_NAME

    when:
    underTest.sendReports(batchId, analysisName)

    then:
    1 * registrationService.getAlertsWithMatches(ReportFixtures.BATCH_ID) >> [ReportFixtures.ALERT_ONE, ReportFixtures.ALERT_TWO]
    1 * recommendationService.getRecommendations(ReportFixtures.ANALYSIS_NAME) >> ReportFixtures.RECOMMENDATIONS_WITH_METADATA
    2 * reportsMapper.toReport(_, _, _) >>> [ReportFixtures.REPORT_ONE, ReportFixtures.REPORT_TWO]
    1 * reportSenderService.send(ReportFixtures.REPORTS)
  }

  def 'should send ERROR reports for the given analysis'() {
    given:
    def batchId = ReportFixtures.BATCH_ID
    def analysisName = ReportFixtures.ANALYSIS_NAME

    when:
    underTest.sendReports(batchId, analysisName)

    then:
    1 * registrationService.getAlertsWithMatches(ReportFixtures.BATCH_ID) >> [ReportFixtures.ALERT_ONE_WITH_ERROR_STATUS]
    1 * recommendationService.getRecommendations(ReportFixtures.ANALYSIS_NAME) >> []
    1 * reportsMapper.toErroneousReport(_, _) >>> [ReportFixtures.ERROR_REPORT_ONE]
    1 * reportSenderService.send(ReportFixtures.ERRONEOUS_REPORTS)
  }
}
