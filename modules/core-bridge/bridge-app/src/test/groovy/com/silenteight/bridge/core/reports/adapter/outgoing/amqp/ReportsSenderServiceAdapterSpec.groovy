package com.silenteight.bridge.core.reports.adapter.outgoing.amqp

import com.silenteight.bridge.core.reports.ReportFixtures
import com.silenteight.bridge.core.reports.infrastructure.amqp.ReportsOutgoingConfigurationProperties
import com.silenteight.data.api.v2.ProductionDataIndexRequest

import org.springframework.amqp.rabbit.core.RabbitTemplate
import spock.lang.Specification
import spock.lang.Subject

class ReportsSenderServiceAdapterSpec extends Specification {

  def exchangeName = 'some exchange name'
  def routingKey = 'some routing key'

  def rabbitTemplate = Mock(RabbitTemplate)
  def properties = new ReportsOutgoingConfigurationProperties(exchangeName, routingKey)

  @Subject
  def underTest = new ReportsSenderServiceAdapter(rabbitTemplate, properties)

  def 'should map reports and send them to rabbitmq'() {
    when:
    underTest.send(ReportFixtures.ANALYSIS_NAME, [ReportFixtures.REPORT_ONE])

    then:
    1 * rabbitTemplate.convertAndSend(exchangeName, routingKey, {
      with((ProductionDataIndexRequest) it) {
        it.analysisName == ReportFixtures.ANALYSIS_NAME
        it.alertsCount == 1
        it.alertsList == [ReportFixtures.WAREHOUSE_ALERT]
      }
    })
  }
}
