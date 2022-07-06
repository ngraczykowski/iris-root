package com.silenteight.bridge.core.reports.adapter.outgoing.amqp

import com.silenteight.bridge.core.reports.ReportFixtures
import com.silenteight.bridge.core.reports.infrastructure.ReportsProperties
import com.silenteight.bridge.core.reports.infrastructure.amqp.ReportsOutgoingConfigurationProperties
import com.silenteight.data.api.v2.ProductionDataIndexRequest

import org.springframework.amqp.rabbit.core.RabbitTemplate
import spock.lang.Specification
import spock.lang.Subject

class ReportsSenderServiceAdapterSpec extends Specification {

  def exchangeName = 'some exchange name'
  def routingKey = 'some routing key'

  def rabbitTemplate = Mock(RabbitTemplate)
  def reportsOutgoingConfigurationProperties = new ReportsOutgoingConfigurationProperties(
      exchangeName, routingKey)
  def reportsProperties = new ReportsProperties(
      true, 100, ReportFixtures.CUSTOMER_RECOMMENDATION_MAP)

  @Subject
  def underTest = new ReportsSenderServiceAdapter(
      rabbitTemplate,
      reportsProperties,
      reportsOutgoingConfigurationProperties
  )

  def 'should map reports and send them to rabbitmq'() {
    when:
    underTest.send(ReportFixtures.ANALYSIS_NAME, [report])

    then:
    noExceptionThrown()
    1 * rabbitTemplate.convertAndSend(
        exchangeName, routingKey, {
      with((ProductionDataIndexRequest) it) {
        it.analysisName == ReportFixtures.ANALYSIS_NAME
        it.alertsCount == 1
        if (report == ReportFixtures.REPORT_ONE) {
          it.alertsList == [ReportFixtures.WAREHOUSE_ALERT]
        }
      }
    })
    0 * _

    where:
    report << [ReportFixtures.REPORT_ONE,
               ReportFixtures.REPORT_TWO,
               ReportFixtures.REPORT_THREE,
               ReportFixtures.ERROR_REPORT_ONE,
               ReportFixtures.ERROR_REPORT_TWO]
  }
}
