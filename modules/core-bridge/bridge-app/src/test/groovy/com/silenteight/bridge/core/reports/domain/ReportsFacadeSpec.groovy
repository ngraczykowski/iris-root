package com.silenteight.bridge.core.reports.domain

import com.silenteight.bridge.core.Fixtures
import com.silenteight.bridge.core.recommendation.domain.RecommendationFixtures
import com.silenteight.bridge.core.reports.domain.commands.SendReportsCommand
import com.silenteight.bridge.core.reports.infrastructure.ReportsProperties

import spock.lang.Specification
import spock.lang.Subject

class ReportsFacadeSpec extends Specification {

  def reportsService = Mock(ReportsService)

  def reportsProperties = new ReportsProperties(false, 2)

  @Subject
  def underTest = new ReportsFacade(reportsService, reportsProperties)

  def 'should send necessary batch info and recommendations to warehouse'() {
    given:
    def command = new SendReportsCommand(Fixtures.BATCH_ID, RecommendationFixtures.ANALYSIS_NAME)

    when:
    underTest.sendReports(command)

    then:
    1 * reportsService.sendReports(Fixtures.BATCH_ID, RecommendationFixtures.ANALYSIS_NAME)
  }
}
