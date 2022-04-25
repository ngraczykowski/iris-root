package com.silenteight.scb.ingest.adapter.incomming.common.ingest

import spock.lang.Specification

class FilteredAlertsSpec extends Specification {

  def 'should give correct response when no recommendation exists'() {
    given:
    def alerts = Fixtures.alerts()
    def fa = new FilteredAlerts(alerts, (alert) -> false)

    expect:
    fa.alertsWithDecisions() == alerts
    fa.alertsWithoutDecisions().isEmpty()
    fa.alertsWithDecisionAndWithoutRecommendation() == alerts
    fa.alertsWithDecisionAndWithRecommendation().isEmpty()
  }

  def 'should give correct response when recommendation exists'() {
    given:
    def alerts = Fixtures.alerts()
    def fa = new FilteredAlerts(alerts, (alert) -> true)

    expect:
    fa.alertsWithDecisions() == alerts
    fa.alertsWithoutDecisions().isEmpty()
    fa.alertsWithDecisionAndWithoutRecommendation().isEmpty()
    fa.alertsWithDecisionAndWithRecommendation() == alerts
  }

}
