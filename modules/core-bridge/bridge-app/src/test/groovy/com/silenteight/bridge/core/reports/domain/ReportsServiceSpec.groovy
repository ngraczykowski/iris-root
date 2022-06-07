package com.silenteight.bridge.core.reports.domain

import com.silenteight.bridge.core.reports.ReportFixtures
import com.silenteight.bridge.core.reports.domain.port.outgoing.RecommendationService
import com.silenteight.bridge.core.reports.domain.port.outgoing.RegistrationService
import com.silenteight.bridge.core.reports.domain.port.outgoing.ReportsSenderService
import com.silenteight.bridge.core.reports.infrastructure.ReportsProperties

import spock.lang.Specification
import spock.lang.Subject

import java.util.stream.Stream

class ReportsServiceSpec extends Specification {

  def reportsMapper = Mock(ReportsMapper)
  def reportSenderService = Mock(ReportsSenderService)
  def registrationService = Mock(RegistrationService)
  def recommendationService = Mock(RecommendationService)
  def reportsProperties = new ReportsProperties(true, 2, [:])

  @Subject
  def underTest = new ReportsService(
      reportsMapper,
      reportSenderService,
      registrationService,
      recommendationService,
      reportsProperties
  )

  def 'should send reports for the given analysis'() {
    given:
    def batchId = ReportFixtures.BATCH_ID
    def analysisName = ReportFixtures.ANALYSIS_NAME

    when:
    underTest.sendReports(batchId, analysisName)

    then:
    1 * registrationService.getAlertsWithMatches(ReportFixtures.BATCH_ID) >>
        [ReportFixtures.ALERT_ONE, ReportFixtures.ALERT_TWO]
    1 * recommendationService.getRecommendations(ReportFixtures.ANALYSIS_NAME) >>
        ReportFixtures.RECOMMENDATIONS_WITH_METADATA
    2 * reportsMapper.toReport(_, _, _) >>> [ReportFixtures.REPORT_ONE, ReportFixtures.REPORT_TWO]
    1 * reportSenderService.send(ReportFixtures.ANALYSIS_NAME, ReportFixtures.REPORTS)
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
    1 * reportSenderService.send(ReportFixtures.ANALYSIS_NAME, ReportFixtures.ERRONEOUS_REPORTS)
  }

  def 'should stream reports for given analysis, reports: 0'() {
    given:
    def batchId = ReportFixtures.BATCH_ID
    def analysisName = ReportFixtures.ANALYSIS_NAME

    when:
    underTest.streamReports(batchId, analysisName)

    then:
    1 * registrationService.streamAlerts(batchId) >> Stream.of()
    0 * reportSenderService.send(_, _)
    0 * _
  }

  def 'should stream reports for given analysis, reports: 2, 1 error'() {
    given:
    def batchId = ReportFixtures.BATCH_ID
    def analysisName = ReportFixtures.ANALYSIS_NAME

    when:
    underTest.streamReports(batchId, analysisName)

    then:
    1 * registrationService.streamAlerts(batchId) >> Stream.of(ReportFixtures.ALERT_ONE_WITHOUT_MATCHES, ReportFixtures.ALERT_THREE_ERROR_WITHOUT_MATCHES)

    1 * registrationService.getMatches(Set.of(Long.valueOf(ReportFixtures.ALERT_ONE_WITHOUT_MATCHES.id()), Long.valueOf(ReportFixtures.ALERT_THREE_ERROR_WITHOUT_MATCHES.id())))
        >> [ReportFixtures.MATCH_ONE_WITH_ALERT_ID, ReportFixtures.MATCH_TWO_WITH_ALERT_ID]

    1 * recommendationService.getRecommendations(analysisName, List.of(ReportFixtures.ALERT_ONE_WITHOUT_MATCHES.alertName(), ReportFixtures.ALERT_THREE_ERROR_WITHOUT_MATCHES.alertName()))
        >> [ReportFixtures.FIRST_RECOMMENDATION_WITH_METADATA]

    1 * reportsMapper.toAlertWithMatches(ReportFixtures.ALERT_ONE_WITHOUT_MATCHES, List.of(ReportFixtures.MATCH_ONE_WITH_ALERT_ID))
        >> ReportFixtures.ALERT_ONE

    1 * reportsMapper.toAlertWithMatches(ReportFixtures.ALERT_THREE_ERROR_WITHOUT_MATCHES, []) >> ReportFixtures.ALERT_THREE

    1 * reportsMapper.toReport(batchId, ReportFixtures.ALERT_ONE, ReportFixtures.FIRST_RECOMMENDATION_WITH_METADATA)
        >> ReportFixtures.REPORT_ONE

    1 * reportsMapper.toErroneousReport(batchId, ReportFixtures.ALERT_THREE)
        >> ReportFixtures.REPORT_THREE

    1 * reportSenderService.send(analysisName, List.of(ReportFixtures.REPORT_ONE, ReportFixtures.REPORT_THREE))
  }

  def 'should stream reports for given analysis, reports: 3'() {
    given:
    def batchId = ReportFixtures.BATCH_ID
    def analysisName = ReportFixtures.ANALYSIS_NAME

    when:
    underTest.streamReports(batchId, analysisName)

    then:
    1 * registrationService.streamAlerts(batchId) >> Stream.of(ReportFixtures.ALERT_ONE_WITHOUT_MATCHES, ReportFixtures.ALERT_TWO_WITHOUT_MATCHES, ReportFixtures.ALERT_ONE_WITHOUT_MATCHES)

    1 * registrationService.getMatches(Set.of(Long.valueOf(ReportFixtures.ALERT_ONE_WITHOUT_MATCHES.id()), Long.valueOf(ReportFixtures.ALERT_TWO_WITHOUT_MATCHES.id())))
        >> [ReportFixtures.MATCH_ONE_WITH_ALERT_ID, ReportFixtures.MATCH_TWO_WITH_ALERT_ID]

    1 * registrationService.getMatches(Set.of(Long.valueOf(ReportFixtures.ALERT_ONE_WITHOUT_MATCHES.id())))
        >> [ReportFixtures.MATCH_ONE_WITH_ALERT_ID]

    1 * recommendationService.getRecommendations(analysisName, List.of(ReportFixtures.ALERT_ONE_WITHOUT_MATCHES.alertName(), ReportFixtures.ALERT_TWO_WITHOUT_MATCHES.alertName()))
        >> [ReportFixtures.FIRST_RECOMMENDATION_WITH_METADATA, ReportFixtures.SECOND_RECOMMENDATION_WITH_METADATA]

    1 * recommendationService.getRecommendations(analysisName, List.of(ReportFixtures.ALERT_ONE_WITHOUT_MATCHES.alertName()))
        >> []

    2 * reportsMapper.toAlertWithMatches(ReportFixtures.ALERT_ONE_WITHOUT_MATCHES, List.of(ReportFixtures.MATCH_ONE_WITH_ALERT_ID))
        >> ReportFixtures.ALERT_ONE

    1 * reportsMapper.toAlertWithMatches(ReportFixtures.ALERT_TWO_WITHOUT_MATCHES, List.of(ReportFixtures.MATCH_TWO_WITH_ALERT_ID))
        >> ReportFixtures.ALERT_TWO

    1 * reportsMapper.toReport(batchId, ReportFixtures.ALERT_ONE, ReportFixtures.FIRST_RECOMMENDATION_WITH_METADATA)
        >> ReportFixtures.REPORT_ONE

    1 * reportsMapper.toReport(batchId, ReportFixtures.ALERT_TWO, ReportFixtures.SECOND_RECOMMENDATION_WITH_METADATA)
        >> ReportFixtures.REPORT_TWO

    1 * reportsMapper.toErroneousReport(batchId, ReportFixtures.ALERT_ONE) >> ReportFixtures.ERROR_REPORT_ONE

    1 * reportSenderService.send(analysisName, List.of(ReportFixtures.REPORT_ONE, ReportFixtures.REPORT_TWO))

    1 * reportSenderService.send(analysisName, List.of(ReportFixtures.ERROR_REPORT_ONE))
  }
}
